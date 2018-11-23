package com.yandi.yarud.yadiupi.utility.network;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yandi.yarud.yadiupi.absensi.ApproveKehadiranDosenActivity;
import com.yandi.yarud.yadiupi.absensi.DetilMKActivity;
import com.yandi.yarud.yadiupi.LoginActivity;
import com.yandi.yarud.yadiupi.absensi.MahasiswaKontrakActivity;
import com.yandi.yarud.yadiupi.absensi.PemilihanKMActivity;
import com.yandi.yarud.yadiupi.absensi.PenugasanActivity;
import com.yandi.yarud.yadiupi.absensi.PresensiActivity;
import com.yandi.yarud.yadiupi.absensi.RisalahMKActivity;
import com.yandi.yarud.yadiupi.forum.DataForumActivity;
import com.yandi.yarud.yadiupi.forum.DiskusiActivity;
import com.yandi.yarud.yadiupi.forum.ForumActivity;
import com.yandi.yarud.yadiupi.forum.InputDiskusiActivity;

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
                                    LoginActivity loginActivity = (LoginActivity) context;
                                    loginActivity.RunningPage(token);
                                    break;
                                case "Penugasan":
                                    PenugasanActivity penugasanActivity = (PenugasanActivity) context;
                                    penugasanActivity.RunningPage(token);
                                    break;
                                case "DetilMK":
                                    DetilMKActivity detilMKActivity = (DetilMKActivity) context;
                                    detilMKActivity.RunningPage(token);
                                    break;
                                case "RisalahMK":
                                    RisalahMKActivity risalahMKActivity = (RisalahMKActivity) context;
                                    risalahMKActivity.RunningPage(token);
                                    risalahMKActivity.LoadDataKM(token);
                                    break;
                                case "PemilihanKM":
                                    PemilihanKMActivity pemilihanKMActivity = (PemilihanKMActivity) context;
                                    pemilihanKMActivity.RunningPage(token);
                                    break;
                                case "Presensi":
                                    PresensiActivity presensiActivity = (PresensiActivity) context;
                                    presensiActivity.RunningPage(token);
                                    break;
                                case "FinishAbsen":
                                    presensiActivity = (PresensiActivity) context;
                                    presensiActivity.ProsesFinishAbsen(token);
                                    break;
                                case "MahasiswaKontrak":
                                    MahasiswaKontrakActivity mahasiswaKontrakActivity = (MahasiswaKontrakActivity) context;
                                    mahasiswaKontrakActivity.RunningPage(token);
                                    break;
                                case "ApproveKehadiranDosen":
                                    ApproveKehadiranDosenActivity approveKehadiranDosenActivity = (ApproveKehadiranDosenActivity) context;
                                    approveKehadiranDosenActivity.RunningPage(token);
                                    break;
                                case "Forum":
                                    ForumActivity forumActivity = (ForumActivity) context;
                                    forumActivity.RunningPage(token);
                                    break;
                                case "ForumMahasiswa":
                                    forumActivity = (ForumActivity) context;
                                    forumActivity.RunningPageMahasiswa(token);
                                    break;
                                case "DataForum":
                                    DataForumActivity dataForumActivity = (DataForumActivity) context;
                                    dataForumActivity.RunningPage(token);
                                    break;
                                case "InputDiskusi":
                                    InputDiskusiActivity inputDiskusiActivity = (InputDiskusiActivity) context;
                                    inputDiskusiActivity.RunningPage(token);
                                    break;
                                case "Diskusi":
                                    DiskusiActivity diskusiActivity = (DiskusiActivity) context;
                                    diskusiActivity.RunningPage(token);
                                    break;
                                case "PostKomentar":
                                    diskusiActivity = (DiskusiActivity) context;
                                    diskusiActivity.PostKomentar(token);
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
                        switch (bagian) {
                            case "Login": {
                                LoginActivity activity = (LoginActivity) context;
                                activity.displaySuccess();
                                Toast.makeText(context, "Username Password anda salah / Tidak ada Koneksi Internet", Toast.LENGTH_LONG).show();
                                break;
                            }
                            case "Penugasan": {
                                PenugasanActivity activity = (PenugasanActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "DetilMK": {
                                DetilMKActivity activity = (DetilMKActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "RisalahMK": {
                                RisalahMKActivity activity = (RisalahMKActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "PemilihanKM": {
                                PemilihanKMActivity activity = (PemilihanKMActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "Presensi": {
                                PresensiActivity activity = (PresensiActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "FinishAbsen": {
                                PresensiActivity activity = (PresensiActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "MahasiswaKontrak": {
                                MahasiswaKontrakActivity activity = (MahasiswaKontrakActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "ApproveKehadiranDosen": {
                                ApproveKehadiranDosenActivity activity = (ApproveKehadiranDosenActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "Forum":{
                                ForumActivity activity = (ForumActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "ForumMahasiswa":{
                                ForumActivity activity = (ForumActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "DataForum": {
                                DataForumActivity activity = (DataForumActivity) context;
                                activity.displayFailed();
                                break;
                            }
                            case "InputDiskusi": {
                                Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case "Diskusi": {
                                DiskusiActivity activity = (DiskusiActivity) context;
                                activity.displayFailed();
                                break;
                            }
                        }
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

    //SET ABSENSI DI PRESENSI
    public void getTokenAbsensi(final String usernameDB, final String passwordDB, final String idrs, final String nim, final String status, final String ket, final String nama) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUpi.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            token = jsonObject.getString("token");
                            PostAbsen(token,idrs,nim,status,ket,nama);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PresensiActivity presensiActivity = (PresensiActivity) context;
                        presensiActivity.displayFailed();
                        Toast.makeText(context, "Tidak Ada Koneksi Internet, Silahkan Coba lagi", Toast.LENGTH_SHORT).show();
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
    private void PostAbsen(final String token, String idrs, String nim, final String status, String ket, final String nama) {
        Map<String,String> params = new HashMap<>();
        params.put("id_rs", idrs);
        params.put("nim", nim);
        params.put("status", status);
        params.put("ket", ket);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,UrlUpi.URL_InputPresensi,
                new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PresensiActivity presensiActivity = (PresensiActivity) context;
                        presensiActivity.displaySuccess();
                        try {
                            Toast.makeText(context,nama + " berhasil di Update",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PresensiActivity presensiActivity = (PresensiActivity) context;
                        presensiActivity.displayFailed();
                        Toast.makeText(context, "Tidak Ada Koneksi Internet, Silahkan Coba lagi", Toast.LENGTH_SHORT).show();
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

    //APPROVE DOSEN
    public void approveKM(final String username, final String password, final String idrs){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,UrlUpi.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            token = jsonObject.getString("token");
                            postApproveKM(token,idrs);
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
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void postApproveKM(final String token, final String idrs) {
        Map<String,String> params = new HashMap<>();
        params.put("idrs",idrs);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,UrlUpi.URL_ApproveKehadiranDosen,
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent intent =new Intent(context,MahasiswaKontrakActivity.class);
                    ApproveKehadiranDosenActivity approve = (ApproveKehadiranDosenActivity) context;
                    approve.startActivity(intent);
                    approve.finish();
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
