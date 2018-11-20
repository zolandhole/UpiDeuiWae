package com.yandi.yarud.yadiupi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yandi.yarud.yadiupi.absensi.controller.ApiAuthenticationClientJWT;
import com.yandi.yarud.yadiupi.absensi.controller.DBHandler;
import com.yandi.yarud.yadiupi.absensi.network.UrlUpi;
import com.yandi.yarud.yadiupi.absensi.model.User;
import com.yandi.yarud.yadiupi.absensi.network.CheckConnection;
import com.yandi.yarud.yadiupi.absensi.network.GetTokenUPI;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
    private ConstraintLayout displayLoading,displayFailed,displaySuccess;
    //CONNECTION FAILED
    private CardView cardViewUlangiKoneksi;
    
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private Intent intenMain;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
        initRunning();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LoginButton:
                validationForm();
                break;
        }
    }
    @Override
    protected void onPause() {

        // hide the keyboard in order to avoid getTextBeforeCursor on inactive InputConnection
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(editTextPassword.getWindowToken(),0);

        super.onPause();
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

    //INITVIEW
    private void initView() {
        //KEMUNGKINAN YANG TERJADI PADA SAAT PAGE DI LOAD
        displayLoading = findViewById(R.id.LoginDisplayLoading);
        displayFailed = findViewById(R.id.LoginDisplayFailed);
        displaySuccess = findViewById(R.id.LoginDisplaySuccess);

        //CONNECTION FAILED
        cardViewUlangiKoneksi = findViewById(R.id.LoginCardViewUlangiKoneksi);
        
        editTextUsername = findViewById(R.id.LoginEditTextUsername);
        editTextPassword = findViewById(R.id.LoginEditTextPassword);
        buttonLogin = findViewById(R.id.LoginButton);
    }
    //INITLISTENER
    private void initListener() {
        intenMain = new Intent(LoginActivity.this,MainActivity.class);
        db = new DBHandler(LoginActivity.this);
        buttonLogin.setOnClickListener(LoginActivity.this);
    }

    //INIT RUNNING
    private void initRunning() {
        if (!new CheckConnection().apakahTerkoneksiKeInternet(LoginActivity.this)){
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }
    private void validationForm() {
        if (TextUtils.isEmpty(editTextUsername.getText())){
            editTextUsername.setError("Username harus dimasukan");
        } else if (TextUtils.isEmpty(editTextPassword.getText())){
            editTextPassword.setError("Password harus dimasukan");
        } else {
            displayLoading();
            GetTokenUPI token = new GetTokenUPI(LoginActivity.this, "Login");
            synchronized (LoginActivity.this){
                token.getToken(
                        editTextUsername.getText().toString().trim(),
                        editTextPassword.getText().toString().trim()
                );
            }
        }
    }
    public void RunningPage(String token) {
        new UrlUpi();
        ApiAuthenticationClientJWT apiAuthenticationClientJWT = new ApiAuthenticationClientJWT(UrlUpi.URL_USER,token);
        AsyncTask<Void,Void,String> execute = new AmbilData(apiAuthenticationClientJWT);
        execute.execute();
    }
    @SuppressLint("StaticFieldLeak")
    public class AmbilData extends AsyncTask<Void, Void, String> {

        private ApiAuthenticationClientJWT apiAuthenticationClientJWT;

        AmbilData(ApiAuthenticationClientJWT apiAuthenticationClientJWT) {
            this.apiAuthenticationClientJWT = apiAuthenticationClientJWT;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                JSONObject jsonObject = new JSONObject((apiAuthenticationClientJWT.getLastResponseAsJsonObject()).getJSONObject("dt_user").toString());
                    User user = new User(db.getUser(1));
                    if (user.getUsername().equals("")){
                        db.addUser(
                                new User(1,
                                        editTextUsername.getText().toString(),
                                        editTextPassword.getText().toString(),
                                        jsonObject.getString("STATUS"),
                                        jsonObject.getString("KODEDOSEN"),
                                        jsonObject.getString("GELARNAMA")
                                )
                        );
                    } else {
                        db.updateUser(
                                new User(1,
                                        editTextUsername.getText().toString(),
                                        editTextPassword.getText().toString(),
                                        jsonObject.getString("STATUS"),
                                        jsonObject.getString("KODEDOSEN"),
                                        jsonObject.getString("GELARNAMA")
                                )
                        );
                    }
                db.close();
                startActivity(intenMain);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
