package com.example.imenez.mapsactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by imenez on 20.5.2017.
 */

public class GecmisActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String showGecmis = "http://192.168.1.1/showlokasyon2.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecmis);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        final TextView tv_gecmis = (TextView) findViewById(R.id.tv_gecmis);
        final TextView tv_tarih = (TextView) findViewById(R.id.tv_tarih);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, showGecmis, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray lokasyonlar = response.getJSONArray("koordinatlar2");
                    for(int k= 0 ; k <lokasyonlar.length(); k++){
                        JSONObject lokasyon = lokasyonlar.getJSONObject(k);

                        String adi = lokasyon.getString("adi");
                        String tarih = lokasyon.getString("tarih");

                        tv_gecmis.append(adi+"\n");
                        tv_tarih.append(tarih+"\n");



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
