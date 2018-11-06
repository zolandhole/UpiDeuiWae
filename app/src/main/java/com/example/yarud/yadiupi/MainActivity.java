package com.example.yarud.yadiupi;

import android.content.DialogInterface;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //TOOLBAR
    private Toolbar mainToolbar;
    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;
    //CONNECTION SUCCESS
    private TextView textViewGelarNama;
        private CardView cardViewPenugasan, cardViewKontrakMK, cardViewForum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        tampilanToolbar();
        initRunning();

    }
    @Override public void onClick(View view) {
        switch (view.getId()){
            case R.id.MainCardViewUlangiKoneksi:
                Toast.makeText(getApplicationContext(),"Lamun Koneksi Gagal",Toast.LENGTH_LONG).show();
                break;
            case R.id.MainCardViewDosenPenugasan:
                Toast.makeText(getApplicationContext(),"Urusan Dosen",Toast.LENGTH_LONG).show();
                break;
            case R.id.MainCardViewMahasiswaKontrakMK:
                Toast.makeText(getApplicationContext(),"Urusan Mahasiswa",Toast.LENGTH_LONG).show();
                break;
            case R.id.MainCardViewForum:
                Toast.makeText(getApplicationContext(),"Urusan Forum Bersama",Toast.LENGTH_LONG).show();
                break;

        }
    }

    //INISIASI
    private void initView(){
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.MainDisplayLoading);
        displayFailed = findViewById(R.id.MainDisplayFailed);
        displaySuccess = findViewById(R.id.MainDisplaySuccess);

        //TOOLBAR
        mainToolbar = findViewById(R.id.MainToolbar);

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
        cardViewPenugasan.setOnClickListener(MainActivity.this);
        cardViewKontrakMK.setOnClickListener(MainActivity.this);
        cardViewForum.setOnClickListener(MainActivity.this);
    }

    //APLIKASI BERJALAN
    private void initRunning() {
        Toast.makeText(getApplicationContext(),"Aplikasi Berjalan",Toast.LENGTH_LONG).show();
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
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("");
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
                        keLogin();
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
    private void keLogin() {
        Toast.makeText(getApplicationContext(),"Nanti Hapus dulu database, baru buka Menu Login",Toast.LENGTH_LONG).show();
    }
}
