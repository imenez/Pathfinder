package com.example.imenez.mapsactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by imenez on 21.5.2017.
 */

public class DurumActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String id;
    String insertDurum = "http://192.168.1.1/insertDurum.php";
    String showDurum = "http://192.168.1.1/showlokasyon.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_durum);

        Button bt_1= (Button) findViewById(R.id.bt_1);
        final EditText et_1 = (EditText) findViewById(R.id.et_1);
        final TextView tv_1 = (TextView) findViewById(R.id.tv_1);
        final TextView tv_adres = (TextView) findViewById(R.id.tv_adres);

        Bundle gt=getIntent().getExtras();
        String str=gt.getString("aciklama");
        tv_1.setText(str);

         requestQueue = Volley.newRequestQueue(getApplicationContext());

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, showDurum, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray durums = response.getJSONArray("koordinatlar2");
                    JSONObject durum = durums.getJSONObject(0);
                    String aciklama = durum.getString("aciklama");
                    String adres = durum.getString("adi");
                    id = durum.getString("Id");
                    tv_1.append(aciklama+"\n");


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

        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, insertDurum, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters = new HashMap<String, String>();
                        parameters.put("Id",id);
                        parameters.put("aciklama", et_1.getText().toString());

                        return parameters;
                    }
                };
                requestQueue.add(request);
                Toast.makeText(getApplicationContext(),"Paket Durumunuz Güncellendi",Toast.LENGTH_SHORT).show();
            }
        });




    }
}
