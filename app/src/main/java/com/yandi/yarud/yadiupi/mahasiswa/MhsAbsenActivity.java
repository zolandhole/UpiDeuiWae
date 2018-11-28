package com.yandi.yarud.yadiupi.mahasiswa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
//import android.widget.TextView;

import android.graphics.Bitmap;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import com.google.zxing.WriterException;
import com.scottyab.aescrypt.AESCrypt;
import com.yandi.yarud.yadiupi.LoginActivity;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.mahasiswa.utility.AESEncrypt2;
import com.yandi.yarud.yadiupi.utility.controller.AESHelper;
import com.yandi.yarud.yadiupi.utility.controller.DBHandler;
import com.yandi.yarud.yadiupi.utility.network.CheckConnection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MhsAbsenActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;
    //CONNECTION SUCCESS
    private DBHandler dbHandler;
    private final static int QrWidth = 500;
    private final static int QrHeight = 500;
    private ImageView imageViewMhsAbsenQRImage;
    private String username;
    private TextView textViewMhsAbsenStatus;
    private SimpleDateFormat dtf;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhs_absen);
        initView();
        initListener();
        tampilanToolbar();
        initRunning();
    }
    //BUTTON ON CLICK
    @Override public void onClick(View view) {
        switch (view.getId()){
            case R.id.MhsAbsenCardViewUlangiKoneksi:
                initRunning();
                break;
        }
    }

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    public void displayLoading(){
        displayLoading.setVisibility(View.VISIBLE);
        displayFailed.setVisibility(View.GONE);
        displaySuccess.setVisibility(View.GONE);
    }
    public void displaySuccess(){
        displayLoading.setVisibility(View.GONE);
        displayFailed.setVisibility(View.GONE);
        displaySuccess.setVisibility(View.VISIBLE);
    }
    public void displayFailed() {
        displayLoading.setVisibility(View.GONE);
        displayFailed.setVisibility(View.VISIBLE);
        displaySuccess.setVisibility(View.GONE);
    }

    //INISIASI
    private void initView(){
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.MhsAbsenDisplayLoading);
        displayFailed = findViewById(R.id.MhsAbsenDisplayFailed);
        displaySuccess = findViewById(R.id.MhsAbsenDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.MhsAbsenCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        imageViewMhsAbsenQRImage = findViewById(R.id.imageViewMhsAbsenQRImage);
        textViewMhsAbsenStatus = findViewById(R.id.textViewMhsAbsenStatus);
    }
    @SuppressLint("SimpleDateFormat")
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(this);

        //CONNECTION SUCCESS
        username = Objects.requireNonNull(getIntent().getExtras()).getString("USERNAME");
        dbHandler = new DBHandler(this);
        dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    //TOOLBAR
    private void tampilanToolbar() {
        Toolbar toolbar= findViewById(R.id.MhsAbsenToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SpotUpi");
        toolbar.setSubtitle("Absen Mahasiswa");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainLogout:
                alert_logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void alert_logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.todoDialogLight);
        builder.setIcon(R.drawable.icon_info)
                .setTitle("Putuskan akses ke sistem?")
                .setMessage("Anda perlu memasukan kembali Username dan Password \nHalaman Login akan ditampilkan")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        keHalamanLogin();
                    }
                })
                .setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button yes = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        yes.setTextColor(Color.rgb(29,145,36));
    }
    private void keHalamanLogin() {
        dbHandler.deleteAll();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //APLIKASI BERJALAN
    @SuppressLint("SetTextI18n")
    private void initRunning() {
        displayLoading();
        if (!new CheckConnection().apakahTerkoneksiKeInternet(MhsAbsenActivity.this)){
            Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet",Toast.LENGTH_SHORT).show();
            displayFailed();
        } else {
            Calendar datetimeKalender = Calendar.getInstance();
            Date date= datetimeKalender.getTime();
            String dateformat = dtf.format(date);
            String password = "yadirudiyansah";
            try {

                String usernameEncrypted = AESCrypt.encrypt(password, username);
                String dateformatEncrypted = AESCrypt.encrypt(password, dateformat);

                imageViewMhsAbsenQRImage.setImageBitmap(TextToImageEncode(usernameEncrypted+"~"+ dateformatEncrypted));
                textViewMhsAbsenStatus.setText(usernameEncrypted+"~"+dateformatEncrypted);
                displaySuccess();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,BarcodeFormat.QR_CODE,QrWidth,QrHeight,null
            );
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y=0; y<bitMatrixHeight; y++){
            int offset = y * bitMatrixWidth;
            for (int x=0; x<bitMatrixWidth; x++){
                pixels[offset + x] = bitMatrix.get(x,y) ?
                        getResources().getColor(R.color.biru):getResources().getColor(R.color.colorAccent);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels,0,500,0,0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
