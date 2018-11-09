package com.example.yarud.yadiupi.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yarud.yadiupi.LoginActivity;
import com.example.yarud.yadiupi.model.UrlUpi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetTokenUPI {
    public String token;
    String bagian;
    Context context;

    public GetTokenUPI(Context context, String bagian){
        this.context = context;
        this.bagian = bagian;
    }

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void getContextLogin() {
        LoginActivity loginActivity = (LoginActivity) context;
        loginActivity.RunningPage(token);
    }
}
