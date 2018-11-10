package com.example.yarud.yadiupi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class PemilihanKMActivity extends AppCompatActivity {

    private String kodekls="", namakelas="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemilihan_km);
        kodekls = getIntent().getExtras().getString("KODEKLS");
        namakelas = getIntent().getExtras().getString("NAMAKELAS");
        Toast.makeText(this, kodekls+" "+namakelas, Toast.LENGTH_SHORT).show();
    }
}
