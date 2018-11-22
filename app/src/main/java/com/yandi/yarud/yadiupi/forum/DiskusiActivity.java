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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yandi.yarud.yadiupi.LoginActivity;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.absensi.model.User;
import com.yandi.yarud.yadiupi.forum.adapter.AdapterDiskusi;
import com.yandi.yarud.yadiupi.forum.model.ModelDiskusi;
import com.yandi.yarud.yadiupi.utility.controller.ApiAuthenticationClientJWT;
import com.yandi.yarud.yadiupi.utility.controller.DBHandler;
import com.yandi.yarud.yadiupi.utility.network.CheckConnection;
import com.yandi.yarud.yadiupi.utility.network.GetTokenUPI;
import com.yandi.yarud.yadiupi.utility.network.UrlUpi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DiskusiActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;

    //CONNECTION SUCCESS
    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    private List<ModelDiskusi> item;
    private AdapterDiskusi mAdapter;
    private String idmk, id_frm, user_id;
    private Button buttonDiskusiKomentar;
    private EditText editTextDiskusiIsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diskusi);

        initView();
        initListener();
        tampilanToolbar();
        initRunning();
    }
    
    //BUTTON ON CLICK
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.DiskusiActCardViewUlangiKoneksi:
                initRunning();
                break;
            case R.id.ButtonDiskusiActKomentar:
                kirimKomentar();
                break;
        }
    }

    //INISIASI
    private void initView(){
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.DiskusiActDisplayLoading);
        displayFailed = findViewById(R.id.DiskusiActDisplayFailed);
        displaySuccess = findViewById(R.id.DiskusiActDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.DiskusiActCardViewUlangiKoneksi);

        //CONNECTION SUCCESS
        TextView textViewDiskusiActnama = findViewById(R.id.TextViewDiskusiActnama);
        TextView textViewDiskusiActisi = findViewById(R.id.TextViewDiskusiActisi);
        TextView textViewDiskusiActjudul = findViewById(R.id.TextViewDiskusiActjudul);
        TextView textViewDiskusiActwaktu = findViewById(R.id.TextViewDiskusiActwaktu);
        String judul = Objects.requireNonNull(getIntent().getExtras()).getString("JUDUL");
        String nama = Objects.requireNonNull(getIntent().getExtras()).getString("NAMA");
        String waktu = Objects.requireNonNull(getIntent().getExtras()).getString("WAKTU");
        String isi = Objects.requireNonNull(getIntent().getExtras()).getString("ISI");
        idmk = Objects.requireNonNull(getIntent().getExtras()).getString("IDMK");
        id_frm = Objects.requireNonNull(getIntent().getExtras()).getString("IDFRM");
        textViewDiskusiActnama.setText(nama);
        textViewDiskusiActisi.setText(isi);
        textViewDiskusiActjudul.setText(judul);
        textViewDiskusiActwaktu.setText(waktu);
        buttonDiskusiKomentar = findViewById(R.id.ButtonDiskusiActKomentar);
        recyclerView = findViewById(R.id.DiskusiActRecycleView);
        editTextDiskusiIsi = findViewById(R.id.EditTextDiskusiIsi);
    }
    private void initListener(){
        //CONNECTION FAILED
        cardViewUlangiKoneksi.setOnClickListener(this);

        //CONNECTION SUCCESS
        buttonDiskusiKomentar.setOnClickListener(this);
        dbHandler = new DBHandler(this);
        item = new ArrayList<>();
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        ((LinearLayoutManager) mManager).setStackFromEnd(true);
        mAdapter = new AdapterDiskusi(this,item);
        recyclerView.setLayoutManager(mManager);
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
        Toolbar toolbar= findViewById(R.id.DiskusiActToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SpotUpi");
        toolbar.setSubtitle("Diskusi");
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
        searchView.setQueryHint("Cari Matakuliah ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<ModelDiskusi> newList = new ArrayList<>();
                for (ModelDiskusi Diskusi : item){
                    String mataKuliah = Diskusi.getNama().toLowerCase();
                    if (mataKuliah.contains(newText))
                        newList.add(Diskusi);
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
                    GetTokenUPI token = new GetTokenUPI(this, "Diskusi");
                    user_id = user.getUsername();
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
        ApiAuthenticationClientJWT apiAuthenticationClientJWT = new ApiAuthenticationClientJWT(UrlUpi.URL_Diskusi+id_frm,token);
        AsyncTask<Void,Void,String> execute = new DiskusiActivity.AmbilDataDiskusi(apiAuthenticationClientJWT);
        execute.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class AmbilDataDiskusi extends AsyncTask<Void, Void, String> {

        private ApiAuthenticationClientJWT apiAuthenticationClientJWT;

        AmbilDataDiskusi(ApiAuthenticationClientJWT apiAuthenticationClientJWT) {
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
                    ModelDiskusi model = new ModelDiskusi();
                    model.setId_frm(jsonObject.getString("id_frm"));
                    model.setId_pn(jsonObject.getString("id_pn"));
                    model.setUser_id(jsonObject.getString("user_id"));
                    model.setJudul(jsonObject.getString("judul"));
                    model.setIsi(jsonObject.getString("isi"));
                    model.setIndu(jsonObject.getString("induk"));
                    model.setWaktu(jsonObject.getString("waktu"));
                    model.setNama(jsonObject.getString("nama"));
                    item.add(model);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    //POST KOMENTAR
    private void kirimKomentar() {
        if (TextUtils.isEmpty(editTextDiskusiIsi.getText())){
            editTextDiskusiIsi.setError("Komentar harus dimasukan");
        } else {
            try {
                List<User> userdb = dbHandler.getAllUser();
                for(User user : userdb){
                    GetTokenUPI token = new GetTokenUPI(this, "PostKomentar");
                    token.getToken(user.getUsername(), user.getPassword());
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            dbHandler.close();
        }
    }
    public void PostKomentar(final String token) {
        Map<String,String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("idmk", idmk);
        params.put("isi", editTextDiskusiIsi.getText().toString().trim());
        params.put("induk", id_frm);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,UrlUpi.URL_ReplyDiskusi,
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                } catch (Exception e) {
                    Toast.makeText(DiskusiActivity.this, "Ada Kesalahan Silahkan ulangi", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DiskusiActivity.this, "Tidak terhubung ke server silahkan coba lagi", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(DiskusiActivity.this);
        requestQueue.add(request);
    }
}
