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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yarud.yadiupi.adapter.AdapterPenugasan;
import com.example.yarud.yadiupi.controller.ApiAuthenticationClientJWT;
import com.example.yarud.yadiupi.controller.DBHandler;
import com.example.yarud.yadiupi.model.ModelPenugasan;
import com.example.yarud.yadiupi.network.UrlUpi;
import com.example.yarud.yadiupi.model.User;
import com.example.yarud.yadiupi.network.CheckConnection;
import com.example.yarud.yadiupi.network.GetTokenUPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PenugasanActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;

    //CONNECTION SUCCESS
    private String kodedosen="";
    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    private List<ModelPenugasan> item;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penugasan);

        initView();
        initListener();
        tampilanToolbar();
        initRunning();
    }
    //BUTTON ON CLICK
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.PenugasanCardViewUlangiKoneksi:
                initRunning();
                break;
        }
    }

    //INISIASI
    private void initView(){
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.PenugasanDisplayLoading);
        displayFailed = findViewById(R.id.PenugasanDisplayFailed);
        displaySuccess = findViewById(R.id.PenugasanDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.PenugasanCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        kodedosen = Objects.requireNonNull(getIntent().getExtras()).getString("KODEDOSEN");
        recyclerView = findViewById(R.id.PenugasanRecycleView);
    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(this);

        //CONNECTION SUCCESS
        dbHandler = new DBHandler(this);
        item = new ArrayList<>();
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mManager);
        mAdapter = new AdapterPenugasan(this,item);
        recyclerView.setAdapter(mAdapter);
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
        Toolbar toolbar= findViewById(R.id.PenugasanToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SpotUpi");
        toolbar.setSubtitle("Penugasan");
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
                    GetTokenUPI token = new GetTokenUPI(this, "Penugasan");
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
        ApiAuthenticationClientJWT apiAuthenticationClientJWT = new ApiAuthenticationClientJWT(UrlUpi.URL_PENUGASAN+kodedosen,token);
        AsyncTask<Void,Void,String> execute = new AmbilDataPenugasan(apiAuthenticationClientJWT);
        execute.execute();
    }
    @SuppressLint("StaticFieldLeak")
    private class AmbilDataPenugasan extends AsyncTask<Void, Void, String> {

        private ApiAuthenticationClientJWT apiAuthenticationClientJWT;

        AmbilDataPenugasan(ApiAuthenticationClientJWT apiAuthenticationClientJWT) {
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
                JSONArray jsonArray = new JSONArray(apiAuthenticationClientJWT.getLastResponseAsJsonObject().getJSONArray("dt_penugasan").toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ModelPenugasan model = new ModelPenugasan();
                    model.setIdMK(jsonObject.getString("IDMK"));
                    model.setNamaMK(jsonObject.getString("NAMAMK"));
                    model.setSks(jsonObject.getString("SKS"));
                    model.setNamaKelas(jsonObject.getString("NAMAKELAS"));
                    model.setSemester(jsonObject.getString("SMTAJAR"));
                    model.setTahunajar(jsonObject.getString("THNAJAR"));
                    model.setNamaPST(jsonObject.getString("NAMAPST"));
                    model.setJenjang(jsonObject.getString("JENJANG"));
                    model.setNamadsn(jsonObject.getString("NAMADSN"));
                    if(jsonObject.getString("NAMADSN2").equals("null")){
                        model.setNamadsn2("");
                    }else{
                        model.setNamadsn2(jsonObject.getString("NAMADSN2"));
                    }
                    if(jsonObject.getString("NAMADSN3").equals("null")){
                        model.setNamadsn3("");
                    }else{
                        model.setNamadsn3(jsonObject.getString("NAMADSN3"));
                    }
                    item.add(model);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
