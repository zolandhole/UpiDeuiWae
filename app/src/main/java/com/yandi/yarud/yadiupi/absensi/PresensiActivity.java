package com.yandi.yarud.yadiupi.absensi;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yandi.yarud.yadiupi.LoginActivity;
import com.yandi.yarud.yadiupi.MainActivity;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.absensi.adapter.AdapterPresensi;
import com.yandi.yarud.yadiupi.absensi.controller.ApiAuthenticationClientJWT;
import com.yandi.yarud.yadiupi.absensi.controller.DBHandler;
import com.yandi.yarud.yadiupi.absensi.model.ModelPresensi;
import com.yandi.yarud.yadiupi.absensi.model.User;
import com.yandi.yarud.yadiupi.absensi.network.CheckConnection;
import com.yandi.yarud.yadiupi.absensi.network.GetTokenUPI;
import com.yandi.yarud.yadiupi.absensi.network.UrlUpi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PresensiActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed;
    private NestedScrollView displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;

    //CONNECTION SUCCESS
    private String namakelas="", idrs="", usernameDB="", passwordDB="";
    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    private List<ModelPresensi> item;
    private AdapterPresensi mAdapter;
    private TextView textViewPresensiKelas;
    private Button buttonFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presensi);
        initView();
        initListener();
        tampilanToolbar();
        initRunning();
    }
    //BUTTON ON CLICK
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.PresensiCardViewUlangiKoneksi:
                initRunning();
                break;
            case R.id.buttonPresensiselesai:
                finishAbsensi();
                break;
        }
    }

    //INISIASI
    private void initView(){
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.PresensiDisplayLoading);
        displayFailed = findViewById(R.id.PresensiDisplayFailed);
        displaySuccess = findViewById(R.id.PresensiDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.PresensiCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        buttonFinish = findViewById(R.id.buttonPresensiselesai);
        textViewPresensiKelas = findViewById(R.id.TextViewPresensiKelas);
        idrs = Objects.requireNonNull(getIntent().getExtras()).getString("IDRS");
        namakelas = getIntent().getExtras().getString("NAMAKELAS");
        recyclerView = findViewById(R.id.PresensiRecycleView);
    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(this);

        //CONNECTION SUCCESS
        buttonFinish.setOnClickListener(this);
        textViewPresensiKelas.setText(namakelas);
        dbHandler = new DBHandler(this);
        item = new ArrayList<>();
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new AdapterPresensi(this,item);
        recyclerView.setAdapter(mAdapter);
    }

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    public void displayLoading(){
        buttonFinish.setVisibility(View.GONE);
        displayLoading.setVisibility(View.VISIBLE);
        displayFailed.setVisibility(View.GONE);
        displaySuccess.setVisibility(View.GONE);
    }
    public void displaySuccess(){
        buttonFinish.setVisibility(View.VISIBLE);
        displayLoading.setVisibility(View.GONE);
        displayFailed.setVisibility(View.GONE);
        displaySuccess.setVisibility(View.VISIBLE);
    }
    public void displayFailed() {
        buttonFinish.setVisibility(View.GONE);
        displayLoading.setVisibility(View.GONE);
        displayFailed.setVisibility(View.VISIBLE);
        displaySuccess.setVisibility(View.GONE);
    }

    //TOOLBAR
    private void tampilanToolbar() {
        Toolbar toolbar= findViewById(R.id.PresensiToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SpotUpi");
        toolbar.setSubtitle("Presensi");
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
        searchItem.setVisible(false);
        SearchView searchView  = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Cari Mahasiswa ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<ModelPresensi> newList = new ArrayList<>();
                for (ModelPresensi presensi: item){
                    String mahasiswa = presensi.getNama().toLowerCase();
                    if (mahasiswa.contains(newText))
                        newList.add(presensi);
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
    
    //INIT RUNNING
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
                    GetTokenUPI token = new GetTokenUPI(this, "Presensi");
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
    public void RunningPage(String token) {
        new UrlUpi();
        ApiAuthenticationClientJWT apiAuthenticationClientJWT = new ApiAuthenticationClientJWT(UrlUpi.URL_Presensi+idrs,token);
        AsyncTask<Void,Void,String> execute = new PresensiActivity.AmbilDataPresensi(apiAuthenticationClientJWT);
        execute.execute();
    }
    @SuppressLint("StaticFieldLeak") private class AmbilDataPresensi extends AsyncTask<Void, Void, String> {
        private ApiAuthenticationClientJWT apiAuthenticationClientJWT;
        AmbilDataPresensi(ApiAuthenticationClientJWT apiAuthenticationClientJWT) {
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
                JSONArray jsonArray = new JSONArray(apiAuthenticationClientJWT.getLastResponseAsJsonObject().getJSONArray("dt_presensi").toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    ModelPresensi model = new ModelPresensi();
                    model.setNim(data.getString("nim"));
                    model.setNama(data.getString("nama"));
                    model.setStatus(data.getString("status"));
                    model.setKeterangan(data.getString("ket"));
                    model.setIdrs(data.getString("id_rs"));
                    item.add(model);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    //UPDATE KEHADIRAN
    public String getUsernameDB(){
        return usernameDB;
    }
    public String getPasswordDB(){
        return passwordDB;
    }

    //FINISH ABSENSI
    private void finishAbsensi() {
        displayLoading();
        //CEK KONEKSI INTERNET
        if (!new CheckConnection().apakahTerkoneksiKeInternet(this)){
            Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet",Toast.LENGTH_SHORT).show();
            displayFailed();
        }else{
            try {
                List<User> userdb = dbHandler.getAllUser();
                for(User user : userdb){
                    GetTokenUPI token = new GetTokenUPI(this, "FinishAbsen");
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
    public void ProsesFinishAbsen(final String token) {
        Map<String,String> params = new HashMap<>();
        params.put("id_rs", idrs);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,UrlUpi.URL_FinishAbsen,
                new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        displaySuccess();
                        try {
                            Toast.makeText(PresensiActivity.this, "Presensi Berhasil disimpan", Toast.LENGTH_SHORT).show();
                            keMainActivity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders(){
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private void keMainActivity() {
        Intent intentMain = new Intent(this,MainActivity.class);
        startActivity(intentMain);
        finish();
    }
}
