package com.example.yarud.yadiupi;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yarud.yadiupi.adapter.AdapterPemilihanKM;
import com.example.yarud.yadiupi.controller.DBHandler;
import com.example.yarud.yadiupi.model.ModelPresensi;
import com.example.yarud.yadiupi.model.User;
import com.example.yarud.yadiupi.network.CheckConnection;
import com.example.yarud.yadiupi.network.GetTokenUPI;
import com.example.yarud.yadiupi.network.UrlUpi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PemilihanKMActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed;
    private NestedScrollView displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;

    //CONNECTION SUCCESS
    private String namakelas, idmk, idrs, usernameDB, passwordDB;
    private TextView textViewPemilihanKMKelas;
    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    private List<ModelPresensi> item;
    private AdapterPemilihanKM mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemilihan_km);
        namakelas = getIntent().getExtras().getString("NAMAKELAS");
        initView();
        initListener();
        tampilanToolbar();
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
        displayLoading = findViewById(R.id.PemilihanKMDisplayLoading);
        displayFailed = findViewById(R.id.PemilihanKMDisplayFailed);
        displaySuccess = findViewById(R.id.PemilihanKMDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.PemilihanKMCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        textViewPemilihanKMKelas = findViewById(R.id.TextViewPemilihanKMKelas);
        idmk = Objects.requireNonNull(getIntent().getExtras()).getString("IDMK");
        idrs = getIntent().getExtras().getString("IDRS");
        namakelas = getIntent().getExtras().getString("NAMAKELAS");
        recyclerView = findViewById(R.id.PemilihanKMRecycleView);
    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(this);

        //CONNECTION SUCCESS
        textViewPemilihanKMKelas.setText(namakelas);
        dbHandler = new DBHandler(this);
        item = new ArrayList<>();
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new AdapterPemilihanKM(this,item);
        recyclerView.setAdapter(mAdapter);
    }

    //TOOLBAR
    private void tampilanToolbar() {
        Toolbar toolbar= findViewById(R.id.PemilihanKMToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SpotUpi");
        toolbar.setSubtitle("PemilihanKM");
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

    //INIT RUNNING
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
                    GetTokenUPI token = new GetTokenUPI(this, "PemilihanKM");
                    usernameDB = user.getUsername();
                    passwordDB = user.getPassword();
                    token.getToken(usernameDB, passwordDB);
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
            dbHandler.close();
        }
    }
    public void RunningPage(final String token) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlUpi.URL_Presensi+idrs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        displaySuccess();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("dt_presensi");
                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject model = jsonArray.getJSONObject(i);
                                ModelPresensi presensi = new ModelPresensi();
                                presensi.setNim(model.getString("nim"));
                                presensi.setNama(model.getString("nama"));
                                presensi.setStatus(model.getString("status"));
                                presensi.setKeterangan(model.getString("ket"));
                                presensi.setIdrs(model.getString("id_rs"));
                                item.add(presensi);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders(){
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //SET KM
    public String getUsernameDB(){
        return usernameDB;
    }
    public String getPasswordDB(){
        return passwordDB;
    }
    public String getIdrs(){
        return idrs;
    }
    public String getIdmk(){
        return idmk;
    }
}
