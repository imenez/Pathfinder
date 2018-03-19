package com.example.imenez.mapsactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

/**
 * Created by imenez on 10.5.2017.
 */

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    StringRequest stringRequest;
    private static final String TAG = MainActivity.class.getName();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url00 = "http://192.168.1.1/truncate.php";
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                stringRequest = new StringRequest(Request.Method.GET, url00, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG,"Response:"+response.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"Erro:"+error.toString());

                    }
                });
                requestQueue.add(stringRequest);
                Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);
            }
        });

        Button button4= (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),QReaderActivity.class);
                startActivity(i);
            }
        });


        Button btn_gecmis = (Button) findViewById(R.id.btn_gecmis);
        btn_gecmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), GecmisActivity.class);
                startActivity(i);


            }
        });
    }
}
