package com.example.imenez.mapsactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by imenez on 16.5.2017.
 */

public class QReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        Button btn_oku= (Button) findViewById(R.id.btn_oku);
        Button btn_ekle= (Button) findViewById(R.id.btn_ekle);


        final Activity activity = this;

        btn_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Adresiniz Eklendi",Toast.LENGTH_SHORT).show();
            }
        });

        btn_oku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this,"QR iptal edildi",Toast.LENGTH_SHORT).show();
            }
            else{
                for (int i=0; i < 2; i++)
                {
                Toast.makeText(this,result.getContents(),Toast.LENGTH_SHORT).show();}
                String s = result.getContents();
                qrekle(s);

                }
        }
        else{

        super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String qrekle(String s) {

            String URL="https://maps.googleapis.com/maps/api/directions/json?origin="+s+"&destination="+s+"&sensor=false";
            AsyncHttpClient client=new AsyncHttpClient();
        final String[] sonuclatlng = new String[1];
            client.get(URL,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String JsonData=new String(responseBody);
                    try {
                        JSONObject mainobject=new JSONObject(JsonData);
                        sonuclatlng[0] =mainobject
                                .getJSONArray("routes")
                                .getJSONObject(0)
                                .getJSONObject("start_location")
                                .getString("lat");
                        // .getJSONObject(0)
                        //.getString("text").toString();
                        Toast.makeText(getApplicationContext(),"lol:"+sonuclatlng[0],Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
            return sonuclatlng[0];

    }
}
