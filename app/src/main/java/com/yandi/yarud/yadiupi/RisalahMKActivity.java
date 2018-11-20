package com.yandi.yarud.yadiupi;

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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yandi.yarud.yadiupi.adapter.AdapterRisalahMK;
import com.yandi.yarud.yadiupi.controller.ApiAuthenticationClientJWT;
import com.yandi.yarud.yadiupi.controller.DBHandler;
import com.yandi.yarud.yadiupi.model.ModelRisalahMK;
import com.yandi.yarud.yadiupi.model.User;
import com.yandi.yarud.yadiupi.network.CheckConnection;
import com.yandi.yarud.yadiupi.network.GetTokenUPI;
import com.yandi.yarud.yadiupi.network.UrlUpi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RisalahMKActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;

    //CONNECTION SUCCESS
    private String idmk="";
    private String kodekls="";
    private String namakelas="";
    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    private List<ModelRisalahMK> item;
    private AdapterRisalahMK mAdapter;
    private String statusKM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risalah_mk);
        initView();
        initListener();
        tampilanToolbar();
        initRunning();
    }
    //BUTTON ON CLICK
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RisalahMKCardViewUlangiKoneksi:
                initRunning();
                break;
        }
    }

    //INISIASI
    private void initView(){
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.RisalahMKDisplayLoading);
        displayFailed = findViewById(R.id.RisalahMKDisplayFailed);
        displaySuccess = findViewById(R.id.RisalahMKDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.RisalahMKCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        idmk = Objects.requireNonNull(getIntent().getExtras()).getString("IDMK");
        kodekls = getIntent().getExtras().getString("KODEKLS");
        namakelas = getIntent().getExtras().getString("NAMAKELAS");
        recyclerView = findViewById(R.id.RisalahMKRecycleView);
    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(this);

        //CONNECTION SUCCESS
        dbHandler = new DBHandler(this);
        item = new ArrayList<>();
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mManager);
        mAdapter = new AdapterRisalahMK(this,item, kodekls, namakelas);
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
        Toolbar toolbar= findViewById(R.id.RisalahMKToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SpotUpi");
        toolbar.setSubtitle("Risalah Matakuliah");
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
        SearchView searchView  = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Cari Topik Risalah ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<ModelRisalahMK> newList = new ArrayList<>();
                for (ModelRisalahMK risalahMK : item){
                    String topik = risalahMK.getTopik().toLowerCase();
                    if (topik.contains(newText))
                        newList.add(risalahMK);
                }
                mAdapter.setFilter(newList);
                return true;
            }
        });
        return true;
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
                    GetTokenUPI token = new GetTokenUPI(this, "RisalahMK");
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
        ApiAuthenticationClientJWT apiAuthenticationClientJWT = new ApiAuthenticationClientJWT(UrlUpi.URL_RisalahMK+idmk,token);
        AsyncTask<Void,Void,String> execute = new RisalahMKActivity.AmbilDataRisalahMK(apiAuthenticationClientJWT);
        execute.execute();
    }
    @SuppressLint("StaticFieldLeak") private class AmbilDataRisalahMK extends AsyncTask<Void, Void, String> {

        private ApiAuthenticationClientJWT apiAuthenticationClientJWT;

        AmbilDataRisalahMK(ApiAuthenticationClientJWT apiAuthenticationClientJWT) {
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
                JSONArray abcd = new JSONArray(apiAuthenticationClientJWT.getLastResponseAsJsonObject().getJSONArray("dt_risalah").toString());
                for (int i = 0; i < abcd.length(); i++) {
                    JSONObject data = abcd.getJSONObject(i);
                    ModelRisalahMK model = new ModelRisalahMK();
                    model.setIdrs(data.getString("id_rs"));
                    model.setIdpn(data.getString("id_pn"));
                    model.setPertemuan(data.getString("pertemuan"));
                    model.setTopik(data.getString("topik"));
                    model.setSubtopik(data.getString("subtopik"));
                    model.setSesuai(data.getString("sesuai"));
                    model.setWaktu(data.getString("waktu"));
                    model.setUserid(data.getString("user_id"));
                    model.setApprove(data.getString("approve"));
                    item.add(model);
                }
                displaySuccess();
            }catch (JSONException e){
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    //PENGECEKAN KM
    public String getStatusKM(){
        return statusKM;
    }
    public void LoadDataKM(String token) {
        new UrlUpi();
        ApiAuthenticationClientJWT apiAuthenticationClientJWT = new ApiAuthenticationClientJWT(UrlUpi.URL_RisalahMK_Cek_KM+idmk,token);
        AsyncTask<Void,Void,String> execute = new RisalahMKActivity.CheckKM(apiAuthenticationClientJWT);
        execute.execute();
    }
    @SuppressLint("StaticFieldLeak") private class CheckKM extends AsyncTask<Void, Void, String> {
        private ApiAuthenticationClientJWT apiAuthenticationClientJWT;
        CheckKM(ApiAuthenticationClientJWT apiAuthenticationClientJWT) {
            this.apiAuthenticationClientJWT = apiAuthenticationClientJWT;
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                apiAuthenticationClientJWT.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
                super.onPostExecute(result);
                displaySuccess();
                try {
                    if(apiAuthenticationClientJWT.getLastResponseAsJsonObject().getString("km").equals("1")){
                        statusKM="1";
                    }else{
                        statusKM="0";
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
        }
    }

    //KE PEMILIHAN KM
    public void konfirmasiSetKM(final Intent intentpresensi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.todoDialogLight);
        builder.setIcon(R.drawable.icon_info)
                .setTitle("KM belum di Set")
                .setMessage("Apakah anda akan memilih KM saat ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(intentpresensi);
                    }
                })
                .setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alert1 = builder.create();
        alert1.show();
        Button yes = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
        yes.setTextColor(Color.rgb(29,145,36));
    }
}
