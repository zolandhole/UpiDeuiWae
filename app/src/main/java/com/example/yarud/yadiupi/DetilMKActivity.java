package com.example.yarud.yadiupi;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
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

import com.example.yarud.yadiupi.controller.ApiAuthenticationClientJWT;
import com.example.yarud.yadiupi.controller.DBHandler;
import com.example.yarud.yadiupi.network.UrlUpi;
import com.example.yarud.yadiupi.model.User;
import com.example.yarud.yadiupi.network.CheckConnection;
import com.example.yarud.yadiupi.network.GetTokenUPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class DetilMKActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess;
    
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;
    
    //CONNECTION SUCCESS
    private TextView textViewIDMK, textViewKODEKLS, textViewNAMAKELAS, textViewKODEMK, textViewKODEDSN, textViewNAMADSN, textViewNAMAMK, textViewSKS, textViewNAMAPST, textViewTHN, textViewSMT, textViewNAMAHR, textViewJAM1, textViewJAM2, textViewKODERUANG, textViewNAMARUANG;
    private DBHandler dbHandler;
    private Button buttonRisalahMK;
    private String idmk;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_mk);
        initView();
        initListener();
        tampilanToolbar();
        initRunning();
    }
    //BUTTON ON CLICK
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.DetilMKCardViewUlangiKoneksi:
                initRunning();
                break;
            case R.id.buttonRisalahMK:
                Toast.makeText(this, "Ke Risalah MK", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //INISIASI
    private void initView(){
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.DetilMKDisplayLoading);
        displayFailed = findViewById(R.id.DetilMKDisplayFailed);
        displaySuccess = findViewById(R.id.DetilMKDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.DetilMKCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        textViewIDMK = findViewById(R.id.TextViewDetailIDMK);
        textViewKODEKLS = findViewById(R.id.TextViewDetailKODEKLS);
        textViewNAMAKELAS = findViewById(R.id.TextViewNAMAKLS);
        textViewKODEMK = findViewById(R.id.TextViewDetilKODEMK);
        textViewKODEDSN = findViewById(R.id.TextViewDetilKODEDSN);
        textViewKODERUANG = findViewById(R.id.TextViewDetailKODERUANG);
        textViewNAMADSN = findViewById(R.id.TextViewDetailNAMADSN);
        textViewNAMAMK = findViewById(R.id.TextViewDetailNAMAMK);
        textViewSKS = findViewById(R.id.TextViewDetailSKS);
        textViewNAMAPST = findViewById(R.id.TextViewDetailNAMAPST);
        textViewTHN = findViewById(R.id.TextViewDetailTHN);
        textViewSMT = findViewById(R.id.TextViewDetailSMT);
        textViewNAMAHR = findViewById(R.id.TextViewDetailNAMAHR);
        textViewJAM1 = findViewById(R.id.TextViewDetailJAM1);
        textViewJAM2 = findViewById(R.id.TextViewDetailJAM2);
        textViewNAMARUANG = findViewById(R.id.TextViewDetailNAMARUANG);
        buttonRisalahMK = findViewById(R.id.buttonRisalahMK);

        idmk = Objects.requireNonNull(getIntent().getExtras()).getString("IDMK");
    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(this);

        //CONNECTION SUCCESS
        buttonRisalahMK.setOnClickListener(this);
        dbHandler = new DBHandler(this);
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
        Toolbar toolbar= findViewById(R.id.DetilMKToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SpotUpi");
        toolbar.setSubtitle("Detil Matakuliah");
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
    private void initRunning() {
        displayLoading();
        //CEK KONEKSI INTERNET
        if (!new CheckConnection().apakahTerkoneksiKeInternet(this)){
            Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet",Toast.LENGTH_SHORT).show();
            displayFailed();
        }else{
            try {
                List<User> userdb = dbHandler.getAllUser();
                for(User user : userdb){
                    GetTokenUPI token = new GetTokenUPI(this, "DetilMK");
                    token.getToken(user.getUsername(), user.getPassword());
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            dbHandler.close();
        }
    }
    public void RunningPage(String token) {
        new UrlUpi();
        ApiAuthenticationClientJWT apiAuthenticationClientJWT = new ApiAuthenticationClientJWT(UrlUpi.URL_DetilMK+idmk,token);
        AsyncTask<Void,Void,String> execute = new DetilMKActivity.AmbilDataDetilMK(apiAuthenticationClientJWT);
        execute.execute();
    }
    @SuppressLint("StaticFieldLeak")
    private class AmbilDataDetilMK extends AsyncTask<Void, Void, String> {

        private ApiAuthenticationClientJWT apiAuthenticationClientJWT;

        AmbilDataDetilMK(ApiAuthenticationClientJWT apiAuthenticationClientJWT) {
            this.apiAuthenticationClientJWT = apiAuthenticationClientJWT;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                apiAuthenticationClientJWT.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            displaySuccess();
            try {
                JSONArray jsonArray = new JSONArray(apiAuthenticationClientJWT.getLastResponseAsJsonObject().getJSONArray("dt_mk").toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    textViewIDMK.setText(data.getString("IDMK"));
                    textViewSMT.setText(data.getString("SMT"));
                    textViewNAMAKELAS.setText(data.getString("NAMAKELAS"));
                    textViewNAMADSN.setText(data.getString("NAMADSN"));
                    textViewNAMAMK.setText(data.getString("NAMAMK"));
                    textViewSKS.setText(data.getString("SKS"));
                    textViewNAMAPST.setText(data.getString("NAMAPST"));
                    textViewTHN.setText(data.getString("THN"));
                    textViewNAMAHR.setText(data.getString("NAMAHR"));
                    textViewJAM1.setText(data.getString("JAM1"));
                    textViewJAM2.setText(data.getString("JAM2"));
                    textViewNAMARUANG.setText(data.getString("NAMARUANG"));
                    textViewKODEKLS.setText(data.getString("KODEKLS"));
                    textViewKODEMK.setText(data.getString("KODEMK"));
                    textViewKODEDSN.setText(data.getString("KODEDSN"));
                    textViewKODERUANG.setText(data.getString("KODERUANG"));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
