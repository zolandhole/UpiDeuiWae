package com.yandi.yarud.yadiupi.absensi.QRChoice;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.utility.controller.AESUtils;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class NoGoogleQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private MediaPlayer mp, nmp;
    private AlertDialog alert1;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_google_qr);
        mp = MediaPlayer.create(this, R.raw.success);
        nmp = MediaPlayer.create(this, R.raw.unsuccess);
        mScannerView = new ZXingScannerView(NoGoogleQRActivity.this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(NoGoogleQRActivity.this);
        mScannerView.startCamera(1);
        builder = new AlertDialog.Builder(NoGoogleQRActivity.this);
        alert1 = builder.create();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert1.isShowing()) {
                    alert1.dismiss();
                }
                finish();
            }
        };
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 15 * (60 * 1000));
    }

    @Override
    public void handleResult(final com.google.zxing.Result result) {
        String hasilScanEncripted = result.getText();
        try {
            String hasilDecripted = AESUtils.decrypt(hasilScanEncripted);
            Toast.makeText(NoGoogleQRActivity.this, hasilDecripted, Toast.LENGTH_LONG).show();
            String parseHasil[] = hasilDecripted.split(" ",3);
            final String namaHasil = parseHasil[1];
            final String nimHasil = parseHasil[0];
            final String waktuHasil = parseHasil[2];

            final String nimDB = "INTAN NURFAEDAH, 1600862";
            if (nimDB.toLowerCase().contains(nimHasil.toLowerCase())){
                mp.start();
                builder.setTitle("Selamat Belajar");
                builder.setMessage(nimHasil+ " - " +namaHasil+ " anda absen pada " +waktuHasil);
            } else {
                nmp.start();
                builder.setTitle("Ups");
                builder.setMessage("Anda tidak terdaftar pertemuan ini");
            }
            alert1 = builder.create();
            alert1.show();
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alert1.dismiss();
                    mScannerView.resumeCameraPreview(NoGoogleQRActivity.this);
                }
            },2000);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NoGoogleQRActivity.this, "Ada Kesalahan silahkan ulangi kembali", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        if (alert1.isShowing()) {
            alert1.dismiss();
        }
        super.onPause();
        mScannerView.stopCamera();
    }
}
