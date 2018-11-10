package com.example.yarud.yadiupi.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yarud.yadiupi.DetilMKActivity;
import com.example.yarud.yadiupi.LoginActivity;
import com.example.yarud.yadiupi.PemilihanKMActivity;
import com.example.yarud.yadiupi.PenugasanActivity;
import com.example.yarud.yadiupi.PresensiActivity;
import com.example.yarud.yadiupi.RisalahMKActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetTokenUPI {
    private String token;
    private String bagian;
    private Context context;

    public GetTokenUPI(Context context, String bagian){
        this.context = context;
        this.bagian = bagian;
    }

    //AMBIL TOKEN UNTUK GET
    public void getToken (final String username, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUpi.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            token = jsonObject.getString("token");
                            switch (bagian){
                                case "Login":
                                    getContextLogin();
                                    break;
                                case "Penugasan":
                                    getContextPenugasan();
                                    break;
                                case "DetilMK":
                                    getContextDetilMK();
                                    break;
                                case "RisalahMK":
                                    getContextRisalahMK();
                                    break;
                                case "PemilihanKM":
                                    getContextPemilihanKM();
                                    break;
                                case "Presensi":
                                    getContextPresensi();
                                    break;
                                case "FinishAbsen":
                                    getContextFinishAbsen();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Username/Password anda salah", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //CONTEXT GET
    private void getContextLogin() {
        LoginActivity loginActivity = (LoginActivity) context;
        loginActivity.RunningPage(token);
    }
    private void getContextPenugasan() {
        PenugasanActivity penugasanActivity = (PenugasanActivity) context;
        penugasanActivity.RunningPage(token);
    }
    private void getContextDetilMK() {
        DetilMKActivity detilMKActivity = (DetilMKActivity) context;
        detilMKActivity.RunningPage(token);
    }
    private void getContextRisalahMK() {
        RisalahMKActivity risalahMKActivity = (RisalahMKActivity) context;
        risalahMKActivity.RunningPage(token);
        risalahMKActivity.LoadDataKM(token);
    }
    private void getContextPemilihanKM() {
        PemilihanKMActivity pemilihanKMActivity = (PemilihanKMActivity) context;
        pemilihanKMActivity.RunningPage(token);
    }
    private void getContextPresensi() {
        PresensiActivity presensiActivity = (PresensiActivity) context;
        presensiActivity.RunningPage(token);
    }
    private void getContextFinishAbsen() {
        PresensiActivity presensiActivity = (PresensiActivity) context;
        presensiActivity.ProsesFinishAbsen(token);
    }

    //SET ABSENSI DI PRESENSI
    public void getTokenAbsensi(final String usernameDB, final String passwordDB, final String idrs, final String nim, final String status, final String ket) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUpi.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            token = jsonObject.getString("token");
                            PostAbsen(token,idrs,nim,status,ket);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(status.equals("1")) {
                            Toast.makeText(context, "Username/Password anda salah", Toast.LENGTH_LONG).show();
                        }else{

                            Toast.makeText(context, "Koneksi ke server terputus", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("username", usernameDB);
                params.put("password", passwordDB);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void PostAbsen(final String token, String idrs, String nim, final String status, String ket) {
        Map<String,String> params = new HashMap<>();
        params.put("id_rs", idrs);
        params.put("nim", nim);
        params.put("status", status);
        params.put("ket", ket);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,UrlUpi.URL_InputPresensi,
                new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressDialog.dismiss();
                        try {
                            Log.w("JSONJWT",response.toString());
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    //SET KM
    public void setKM(final String usernameDB, final String passwordDB, final String idmk, final String nim, final String idrs) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,UrlUpi.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            token = jsonObject.getString("token");
                            postSetKM(token,idmk,nim,idrs);
                        } catch (JSONException e) {
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
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("username", usernameDB);
                params.put("password", passwordDB);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void postSetKM(final String token, String idmk, String nim, final String idrs) {
        Map<String,String> params = new HashMap<>();
        params.put("id_pn",idmk);
        params.put("nim", nim);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,UrlUpi.URL_SetKM,
                new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Intent intent =new Intent(context,PresensiActivity.class);
                            PemilihanKMActivity pemilihan = (PemilihanKMActivity)context;
                            intent.putExtra("IDRS",idrs);
                            pemilihan.startActivity(intent);
                            pemilihan.finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
