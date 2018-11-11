package com.example.yarud.yadiupi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Objects;

public class ApproveKehadiranDosenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_kehadiran_dosen);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        String idmk = Objects.requireNonNull(getIntent().getExtras()).getString("IDMK");
        Toast.makeText(this, idmk, Toast.LENGTH_SHORT).show();
    }
}
