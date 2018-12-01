package com.yandi.yarud.yadiupi.forum;

import android.content.Intent;
import android.database.SQLException;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.absensi.model.User;
import com.yandi.yarud.yadiupi.utility.controller.DBHandler;
import com.yandi.yarud.yadiupi.utility.network.CheckConnection;
import com.yandi.yarud.yadiupi.utility.network.GetTokenUPI;
import com.yandi.yarud.yadiupi.utility.network.UrlUpi;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InputDiskusiActivity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {
    private TextView textViewHasilJudul;
    private String getJudul;
    private String username, getIsi;
    private String idmk;
    private String mk;
    private ImageButton input_btn_prev;
    private GestureDetectorCompat detector;
    private Button input_wd_btn_selesai;
    private EditText editTextDiskusiIsi;
    private ConstraintLayout displayLoading, displaySuccess, displayFailed;
    private DBHandler dbHandler;
    private CardView cardViewUlangiKoneksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_diskusi);
        getJudul = Objects.requireNonNull(getIntent().getExtras()).getString("JUDUL");
        idmk = getIntent().getExtras().getString("IDMK");
        mk = getIntent().getExtras().getString("MK");
        getIsi = getIntent().getExtras().getString("ISI");
        initView();
        initListerner();
    }

    private void initListerner() {
        textViewHasilJudul.setText(getJudul);
        editTextDiskusiIsi.setText(getIsi);

        input_btn_prev.setOnClickListener(this);
        detector = new GestureDetectorCompat(this,this);
        input_wd_btn_selesai.setOnClickListener(this);
        dbHandler = new DBHandler(this);
        cardViewUlangiKoneksi.setOnClickListener(this);
    }

    private void initView() {
        textViewHasilJudul = findViewById(R.id.textViewHasilJudul);
        editTextDiskusiIsi = findViewById(R.id.editTextDiskusiIsi);
        input_btn_prev = findViewById(R.id.input_btn_prev);
        input_wd_btn_selesai = findViewById(R.id.input_wd_btn_selesai);
        displayLoading = findViewById(R.id.inputDiskusiDisplayLoading);
        displayFailed = findViewById(R.id.inputDiskusiDisplayFailed);
        displaySuccess = findViewById(R.id.inputDiskusiDisplaySuccess);
        cardViewUlangiKoneksi = findViewById(R.id.inputDiskusiCardViewUlangiKoneksi);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.input_btn_prev:
                keWD();
                break;
            case R.id.input_wd_btn_selesai:
                prosesHalaman();
                break;
            case R.id.inputDiskusiCardViewUlangiKoneksi:
                prosesHalaman();
                break;
        }
    }

    private void prosesHalaman() {
        if (TextUtils.isEmpty(editTextDiskusiIsi.getText())){
            Toast.makeText(this, "Isi Diskusi harus dimasukan", Toast.LENGTH_SHORT).show();
        } else {
            displayLoading();
            //CEK KONEKSI INTERNET
            if (!new CheckConnection().apakahTerkoneksiKeInternet(this)){
                Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet",Toast.LENGTH_SHORT).show();
                displayFailed();
            }else{
                try {
                    List<User> userdb = dbHandler.getAllUser();
                    for(User user : userdb){
                        GetTokenUPI token = new GetTokenUPI(this, "InputDiskusi");
                        username = user.getUsername();
                        String password = user.getPassword();
                        token.getToken(username, password);
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                dbHandler.close();
            }
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        keWD();
        return true;
    }

    private void keWD() {
        Intent intentWD = new Intent(InputDiskusiActivity.this, InputWDActivity.class);
        intentWD.putExtra("JUDUL", getJudul);
        intentWD.putExtra("ISI", editTextDiskusiIsi.getText().toString().trim());
        intentWD.putExtra("IDMK", idmk);
        intentWD.putExtra("MK", mk);
        startActivity(intentWD);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
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

    public void RunningPage(final String token) {
        getIsi = editTextDiskusiIsi.getText().toString().trim();
        Map<String,String> params = new HashMap<>();
        params.put("user_id", username);
        params.put("idmk", idmk);
        params.put("judul", getJudul);
        params.put("isi", getIsi);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,UrlUpi.URL_InputDiskusi,
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                displaySuccess();
                try {
                    keDataForum();
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

    private void keDataForum() {
        Intent intentDataForum = new Intent(InputDiskusiActivity.this, DataForumActivity.class);
        intentDataForum.putExtra("IDMK", idmk);
        intentDataForum.putExtra("MK",mk);
        startActivity(intentDataForum);
        finish();
    }
}
