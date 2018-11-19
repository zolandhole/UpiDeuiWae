package com.yandi.yarud.yadiupi;

import android.annotation.SuppressLint;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.yandi.yarud.yadiupi.adapter.AdapterApproveKehadiranDosen;
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

public class ApproveKehadiranDosenActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed, displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;

    //CONNECTION SUCCESS
    private String idmk, username, password;
    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    private List<ModelRisalahMK> item;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_kehadiran_dosen);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        initView();
        initListener();
        initRunning();
    }
    //BUTTON ON CLICK
    @Override
    public void onClick(View view) {

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
        displayLoading = findViewById(R.id.ApproveKDDisplayLoading);
        displayFailed = findViewById(R.id.ApproveKDDisplayFailed);
        displaySuccess = findViewById(R.id.ApproveKDDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.ApproveKDCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        recyclerView = findViewById(R.id.ApproveKDRecycleView);
        idmk = Objects.requireNonNull(getIntent().getExtras()).getString("IDMK");
    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(this);

        //CONNECTION SUCCESS
        dbHandler = new DBHandler(this);
        item = new ArrayList<>();
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mManager);
        mAdapter = new AdapterApproveKehadiranDosen(this,item);
        recyclerView.setAdapter(mAdapter);
    }

    //APLIKASI BERJALAN
    public void initRunning() {
        displayLoading();
        //CEK KONEKSI INTERNET
        if (!new CheckConnection().apakahTerkoneksiKeInternet(this)){
            Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet",Toast.LENGTH_SHORT).show();
            displayFailed();
        }else{
            try {
                List<User> userdb = dbHandler.getAllUser();
                for(User user : userdb){
                    GetTokenUPI token = new GetTokenUPI(this, "ApproveKehadiranDosen");
                    username = user.getUsername();
                    password = user.getPassword();
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
        ApiAuthenticationClientJWT apiAuthenticationClientJWT = new ApiAuthenticationClientJWT(
                UrlUpi.URL_RisalahMK + idmk,token
        );
        AsyncTask<Void,Void,String> execute = new ApproveKehadiranDosenActivity.AmbilData(apiAuthenticationClientJWT);
        execute.execute();
    }
    @SuppressLint("StaticFieldLeak")
    private class AmbilData extends AsyncTask<Void, Void, String> {
        private ApiAuthenticationClientJWT apiAuthenticationClientJWT;
        AmbilData(ApiAuthenticationClientJWT apiAuthenticationClientJWT) {
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
                JSONArray jsonArray = new JSONArray(
                        apiAuthenticationClientJWT.getLastResponseAsJsonObject().getJSONArray(
                                "dt_risalah"
                        ).toString()
                );
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    String TGLFINISH = data.getString("tgl_finish");
                    if (TGLFINISH.contains("-")){
                        String TGL = data.getString("tgl");
                        if (TGL.contains("-")){
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
                            model.setTgl(data.getString("tgl"));
                            model.setFinish(data.getString("finish"));
                            model.setTgl_finish(data.getString("tgl_finish"));
                            item.add(model);
                        }
                    }
                }
            }catch (JSONException e){
                Toast.makeText(ApproveKehadiranDosenActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                finish();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    //APPROVE DOSEN
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}
