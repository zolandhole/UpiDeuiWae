package com.yandi.yarud.yadiupi.forum;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yandi.yarud.yadiupi.LoginActivity;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.utility.controller.DBHandler;
import com.yandi.yarud.yadiupi.utility.network.CheckConnection;
import com.yandi.yarud.yadiupi.utility.network.GetTokenUPI;
import com.yandi.yarud.yadiupi.utility.network.UrlUpi;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InputDiskusiActivity extends AppCompatActivity implements View.OnClickListener {

    private String idmk, username, password, mk;
    private TextInputEditText textInputIDjudul, textInputIDisi;
    private Button buttonIDkirim;
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_diskusi);
        initView();
        initListener();
        tampilanToolbar();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ButtonIDkirim:
                initRunning();
                break;
        }
    }

    private void initView() {
        idmk = Objects.requireNonNull(getIntent().getExtras()).getString("IDMK");
        username = getIntent().getExtras().getString("USERID");
        password = getIntent().getExtras().getString("PASSWORD");
        mk = getIntent().getExtras().getString("MK");
        textInputIDjudul = findViewById(R.id.TextInputIDJudul);
        textInputIDisi = findViewById(R.id.TextInputIDisi);
        buttonIDkirim = findViewById(R.id.ButtonIDkirim);
    }

    private void initListener() {
        buttonIDkirim.setOnClickListener(this);
        dbHandler = new DBHandler(this);
    }

    //TOOLBAR
    private void tampilanToolbar() {
        Toolbar toolbar= findViewById(R.id.InputDiskusiToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SpotUpi");
        toolbar.setSubtitle("Buat Diskusi Baru");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kembali();
            }
        });
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
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
    @Override public void onBackPressed() {
        kembali();
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

    private void initRunning() {
        if (!new CheckConnection().apakahTerkoneksiKeInternet(this)){
            Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet",Toast.LENGTH_SHORT).show();
        }else{
            if (TextUtils.isEmpty(textInputIDjudul.getText())){
                Toast.makeText(this, "Judul Harus dimasukan", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(textInputIDisi.getText())){
                Toast.makeText(this, "Isi Diskusi Harus dimasukan", Toast.LENGTH_SHORT).show();
            } else {
                GetTokenUPI token = new GetTokenUPI(this, "InputDiskusi");
                token.getToken(username, password);
            }
        }
    }

    public void RunningPage(final String token) {
        Map<String,String> params = new HashMap<>();
        params.put("user_id",username);
        params.put("idmk", idmk);
        params.put("judul", textInputIDjudul.getText().toString().trim());
        params.put("isi", textInputIDisi.getText().toString().trim());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUpi.URL_InputDiskusi,
                new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            kembali();
                            Toast.makeText(InputDiskusiActivity.this, "Diskusi Berhasil di Simpan", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(InputDiskusiActivity.this, "Ada Kesalahan, Silahkan Ulangi", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InputDiskusiActivity.this, "Koneksi Ke Server Gagal, Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
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

    private void kembali(){
        Intent intentDataForum = new Intent(InputDiskusiActivity.this,DataForumActivity.class);
        intentDataForum.putExtra("IDMK",idmk);
        intentDataForum.putExtra("MK",mk);
        startActivity(intentDataForum);
        finish();
    }
}
