package com.yandi.yarud.yadiupi.forum;

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
import android.widget.TextView;
import android.widget.Toast;

import com.yandi.yarud.yadiupi.LoginActivity;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.absensi.model.User;
import com.yandi.yarud.yadiupi.forum.adapter.AdapterDataForum;
import com.yandi.yarud.yadiupi.forum.model.ModelDataForum;
import com.yandi.yarud.yadiupi.utility.controller.ApiAuthenticationClientJWT;
import com.yandi.yarud.yadiupi.utility.controller.DBHandler;
import com.yandi.yarud.yadiupi.utility.network.CheckConnection;
import com.yandi.yarud.yadiupi.utility.network.GetTokenUPI;
import com.yandi.yarud.yadiupi.utility.network.UrlUpi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DataForumActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess, displayNoData;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;

    //CONNECTION SUCCESS
    private TextView textViewDFBuatDiskusi;
    private String idmk, mk, username, password;
    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    private List<ModelDataForum> item;
    private AdapterDataForum mAdapter;
    private CardView cardViewDFBuatDiskusi;
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_forum);
        initView();
        initListener();
        tampilanToolbar();
        initRunning();
    }
    
    //BUTTON ONCLICK
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.DataForumCardViewUlangiKoneksi:
                initRunning();
                break;
            case R.id.TextViewDFBuatDiskusi:
                initInputDiskusi();
                break;
            case R.id.CardViewDFBuatDiskusi:
                initInputDiskusi();
                break;
        }
    }

    //INISIASI
    private void initView(){
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.DataForumDisplayLoading);
        displayFailed = findViewById(R.id.DataForumDisplayFailed);
        displaySuccess = findViewById(R.id.DataForumDisplaySuccess);
        displayNoData = findViewById(R.id.DataForumDisplayNoData);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.DataForumCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        cardViewDFBuatDiskusi = findViewById(R.id.CardViewDFBuatDiskusi);
        textViewDFBuatDiskusi = findViewById(R.id.TextViewDFBuatDiskusi);
        idmk = Objects.requireNonNull(getIntent().getExtras()).getString("IDMK");
        mk = Objects.requireNonNull(getIntent().getExtras()).getString("MK");
        recyclerView = findViewById(R.id.DataForumRecycleView);

    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(this);
        textViewDFBuatDiskusi.setOnClickListener(this);
        cardViewDFBuatDiskusi.setOnClickListener(this);

        //CONNECTION SUCCESS
        dbHandler = new DBHandler(this);
        item = new ArrayList<>();
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        ((LinearLayoutManager) mManager).setStackFromEnd(true);
        mAdapter = new AdapterDataForum(this,item);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(mAdapter);
    }

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    public void displayLoading(){
        displayLoading.setVisibility(View.VISIBLE);
        displayFailed.setVisibility(View.GONE);
        displaySuccess.setVisibility(View.GONE);
        displayNoData.setVisibility(View.GONE);
    }
    public void displaySuccess(){
        displayLoading.setVisibility(View.GONE);
        displayFailed.setVisibility(View.GONE);
        displaySuccess.setVisibility(View.VISIBLE);
        displayNoData.setVisibility(View.GONE);
    }
    public void displayFailed() {
        displayLoading.setVisibility(View.GONE);
        displayFailed.setVisibility(View.VISIBLE);
        displaySuccess.setVisibility(View.GONE);
        displayNoData.setVisibility(View.GONE);
    }
    public void displayNoData() {
        displayLoading.setVisibility(View.GONE);
        displayFailed.setVisibility(View.GONE);
        displaySuccess.setVisibility(View.GONE);
        displayNoData.setVisibility(View.VISIBLE);
    }

    //TOOLBAR
    private void tampilanToolbar() {
        Toolbar toolbar= findViewById(R.id.DataForumToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SpotUpi");
        toolbar.setSubtitle("Diskusi " + mk);
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
        searchView.setQueryHint("Cari Diskusi ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<ModelDataForum> newList = new ArrayList<>();
                for (ModelDataForum DataForum : item){
                    String judul = DataForum.getJudul().toLowerCase();
                    if (judul.contains(newText))
                        newList.add(DataForum);
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
                    GetTokenUPI token = new GetTokenUPI(this, "DataForum");
                    username = user.getUsername();
                    password = user.getPassword();
                    token.getToken(username, password);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            dbHandler.close();
        }
    }
    public void RunningPage(String token) {
        new UrlUpi();
        ApiAuthenticationClientJWT apiAuthenticationClientJWT = new ApiAuthenticationClientJWT(UrlUpi.URL_DataForum+idmk,token);
        AsyncTask<Void,Void,String> execute = new DataForumActivity.AmbilDataDataForum(apiAuthenticationClientJWT);
        execute.execute();
    }
    @SuppressLint("StaticFieldLeak")
    private class AmbilDataDataForum extends AsyncTask<Void, Void, String> {

        private ApiAuthenticationClientJWT apiAuthenticationClientJWT;

        AmbilDataDataForum(ApiAuthenticationClientJWT apiAuthenticationClientJWT) {
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
                JSONArray jsonArray = new JSONArray(apiAuthenticationClientJWT.getLastResponseAsJsonObject().getJSONArray("dt_forum").toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ModelDataForum model = new ModelDataForum();
                    model.setId_frm(jsonObject.getString("id_frm"));
                    model.setId_pn(jsonObject.getString("id_pn"));
                    model.setUser_id(jsonObject.getString("user_id"));
                    model.setJudul(jsonObject.getString("judul"));
                    model.setIsi(jsonObject.getString("isi"));
                    model.setInduk(jsonObject.getString("induk"));
                    model.setWaktu(jsonObject.getString("waktu"));
                    model.setNama(jsonObject.getString("nama"));
                    item.add(model);
                }
            }catch (JSONException e){
                displayNoData();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    //INIT INPUT DISKUSI
    private void initInputDiskusi() {
        Intent intentInputDiskusi = new Intent(this,InputDiskusiActivity.class);
        intentInputDiskusi.putExtra("USERID",username);
        intentInputDiskusi.putExtra("IDMK", idmk);
        intentInputDiskusi.putExtra("PASSWORD", password);
        intentInputDiskusi.putExtra("MK",mk);
        startActivity(intentInputDiskusi);
        finish();
    }
}
