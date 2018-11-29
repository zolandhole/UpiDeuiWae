package com.yandi.yarud.yadiupi;

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
import android.widget.TextView;
import android.widget.Toast;

import com.yandi.yarud.yadiupi.mahasiswa.MahasiswaKontrakActivity;
import com.yandi.yarud.yadiupi.absensi.PenugasanActivity;
import com.yandi.yarud.yadiupi.forum.ForumActivity;
import com.yandi.yarud.yadiupi.forum.ForumMhsActivity;
import com.yandi.yarud.yadiupi.mahasiswa.MhsAbsenActivity;
import com.yandi.yarud.yadiupi.utility.controller.DBHandler;
import com.yandi.yarud.yadiupi.absensi.model.User;
import com.yandi.yarud.yadiupi.utility.network.CheckConnection;

import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;
    //CONNECTION SUCCESS
    private TextView textViewGelarNama;
    private CardView cardViewPenugasan, cardViewKontrakMK, cardViewForum, cardViewMhsAbsen;
    private DBHandler dbHandler;
    private String username, gelarnama, kodedosen, status;
    private ConstraintLayout mhsAbsenConstraint;

    @Override protected void onCreate(Bundle savedInstanceState) {
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
                initMahasiswaKontrak();
                break;
            case R.id.MainCardViewForum:
                initForum();
                break;
            case R.id.MainCardViewMhsAbsen:
                initMhsAbsen();
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
//        TextView textView2 = findViewById(R.id.textView2);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            textView2.setAutoSizeTextTypeUniformWithConfiguration(
//                    1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
//        }
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
        cardViewMhsAbsen = findViewById(R.id.MainCardViewMhsAbsen);
        mhsAbsenConstraint = findViewById(R.id.MhsAbsenConstraint);

    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(MainActivity.this);

        //CONNECTION SUCCESS
        dbHandler = new DBHandler(MainActivity.this);
        cardViewPenugasan.setOnClickListener(MainActivity.this);
        cardViewKontrakMK.setOnClickListener(MainActivity.this);
        cardViewForum.setOnClickListener(MainActivity.this);
        cardViewMhsAbsen.setOnClickListener(MainActivity.this);
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
        MenuItem hideItem = menu.findItem(R.id.action_search);
        hideItem.setVisible(false);
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
        ambilUserDiDatabase();
        if (username == null){
            keHalamanLogin();
        } else if (textViewGelarNama.equals("Gelar Nama")){
            keHalamanLogin();
        }
        //CEK KONEKSI INTERNET
        else if (!new CheckConnection().apakahTerkoneksiKeInternet(MainActivity.this)){
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
                        mhsAbsenConstraint.setVisibility(View.VISIBLE);
                        break;
                    case "Dosen":
                        cardViewKontrakMK.setVisibility(View.GONE);
                        cardViewPenugasan.setVisibility(View.VISIBLE);
                        mhsAbsenConstraint.setVisibility(View.GONE);
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

    //INIT MAHASISWA KONTRAK
    private void initMahasiswaKontrak() {
        Intent intentMhsKontrak = new Intent(MainActivity.this,MahasiswaKontrakActivity.class);
        startActivity(intentMhsKontrak);
    }

    //INIT FORUM
    private void initForum() {
        if (status.equals("Dosen")){
            Intent intentForum = new Intent(MainActivity.this, ForumActivity.class);
            intentForum.putExtra("KODEDOSEN",kodedosen);
            intentForum.putExtra("STATUS", status);
            startActivity(intentForum);
        } else if (status.equals("Mahasiswa")){
            Intent intentForumMhs = new Intent(MainActivity.this, ForumMhsActivity.class);
            intentForumMhs.putExtra("KODEDOSEN",kodedosen);
            intentForumMhs.putExtra("STATUS", status);
            startActivity(intentForumMhs);
        }
    }

    //INIT ABSEN
    private void initMhsAbsen() {
        Intent intenMhsAbsen = new Intent(MainActivity.this, MhsAbsenActivity.class);
        intenMhsAbsen.putExtra("USERNAME", username);
        startActivity(intenMhsAbsen);
        displayLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displaySuccess();
    }
}
