package com.example.yarud.yadiupi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yarud.yadiupi.controller.DBHandler;
import com.example.yarud.yadiupi.model.User;
import com.example.yarud.yadiupi.network.CheckConnection;

import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;
    //CONNECTION SUCCESS
    private TextView textViewGelarNama;
    private CardView cardViewPenugasan, cardViewKontrakMK, cardViewForum;
        private DBHandler dbHandler;
        private String username, gelarnama, kodedosen, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        tampilanToolbar();
        initRunning();

    }
    //BUTTON ON CLICK
    @Override public void onClick(View view) {
        switch (view.getId()){
            case R.id.MainCardViewUlangiKoneksi:
                initRunning();
                break;
            case R.id.MainCardViewDosenPenugasan:
                initPenugasan();
                break;
            case R.id.MainCardViewMahasiswaKontrakMK:
                Toast.makeText(getApplicationContext(),"Urusan Mahasiswa",Toast.LENGTH_LONG).show();
                break;
            case R.id.MainCardViewForum:
                Toast.makeText(getApplicationContext(),"Urusan Forum Bersama",Toast.LENGTH_LONG).show();
                break;

        }
    }

    //EXIT APLIKASI
    @Override public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.todoDialogLight);
        builder.setIcon(R.drawable.icon_info)
                .setTitle("Keluar dari Aplikasi?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
        AlertDialog alert1 = builder.create();
        alert1.show();
        Button yes = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
        yes.setTextColor(Color.rgb(29,145,36));
    }

    //INISIASI
    private void initView(){
        TextView textView2 = findViewById(R.id.textView2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView2.setAutoSizeTextTypeUniformWithConfiguration(
                    1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
        }
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.MainDisplayLoading);
        displayFailed = findViewById(R.id.MainDisplayFailed);
        displaySuccess = findViewById(R.id.MainDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.MainCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        textViewGelarNama = findViewById(R.id.MainTvGelarNama);
        cardViewPenugasan = findViewById(R.id.MainCardViewDosenPenugasan);
        cardViewKontrakMK = findViewById(R.id.MainCardViewMahasiswaKontrakMK);
        cardViewForum = findViewById(R.id.MainCardViewForum);

    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(MainActivity.this);

        //CONNECTION SUCCESS
        dbHandler = new DBHandler(MainActivity.this);
        cardViewPenugasan.setOnClickListener(MainActivity.this);
        cardViewKontrakMK.setOnClickListener(MainActivity.this);
        cardViewForum.setOnClickListener(MainActivity.this);
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

    //TOOLBAR
    private void tampilanToolbar() {
        Toolbar mainToolbar = findViewById(R.id.MainToolbar);
        setSupportActionBar(mainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.todoDialogLight);
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //APLIKASI BERJALAN
    private void initRunning() {
        displayLoading();
        //CEK KONEKSI INTERNET
        if (!new CheckConnection().apakahTerkoneksiKeInternet(MainActivity.this)){
            Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet",Toast.LENGTH_SHORT).show();
            displayFailed();
        }else{
            //APAKAH USER ADA PADA DATABASE
            ambilUserDiDatabase();
            if (username.equals("")){
                keHalamanLogin();
            }else{
                switch (status) {
                    case "Mahasiswa":
                        cardViewKontrakMK.setVisibility(View.VISIBLE);
                        cardViewPenugasan.setVisibility(View.GONE);
                        break;
                    case "Dosen":
                        cardViewKontrakMK.setVisibility(View.GONE);
                        cardViewPenugasan.setVisibility(View.VISIBLE);
                        break;
                    default:
                        keHalamanLogin();
                        break;
                }
                textViewGelarNama.setText(gelarnama);
                textViewGelarNama.setAllCaps(false);
                displaySuccess();
            }
        }
    }
    private void ambilUserDiDatabase() {
        try {
            List<User> listUser = dbHandler.getAllUser();
            for (User user : listUser){
                username = user.getUsername();
                gelarnama = user.getKodedosen();
                kodedosen = user.getGelarnama();
                status = user.getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbHandler.close();
    }

    //INIT PENUGASAN
    private void initPenugasan() {
        Intent intentPenugasan = new Intent(MainActivity.this,PenugasanActivity.class);
        intentPenugasan.putExtra("KODEDOSEN",kodedosen);
        startActivity(intentPenugasan);
    }
}
