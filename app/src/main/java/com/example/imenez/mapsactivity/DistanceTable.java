package com.example.imenez.mapsactivity;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by imenez on 28.3.2017.
 */

public class DistanceTable  {

    public String sonuc;
    public String org;
    public String destination;
    RequestQueue requestQueue;
    String insertmesafe = "http://192.168.1.1/insertmesafe.php";
    Context context;
    ArrayList<DistanceTable> tables;
    ArrayList<DistanceTable> liste;
    public DistanceTable(Context context, String origin, String destination, ArrayList<DistanceTable> tables) {

        this.context=context;
        sonuc="";
        org=origin;
        this.destination=destination;
        this.tables=tables;
        liste=new ArrayList<>();
        Calculate();
        //Calculate2();
    }
    public void controllastelement()
    {

            for (DistanceTable table:
                 tables) {
                if(table.sonuc.equals(""))
                    return;
            }
            distance();
            liste.clone();
        for (DistanceTable t:
             liste) {
            //Toast.makeText(context,t.org+"-"+t.destination+" "+t.sonuc,Toast.LENGTH_SHORT).show();
        }
       // Toast.makeText(context,liste.get(liste.size()-1).destination,Toast.LENGTH_LONG).show();

    }
   /* public void controllastelement2()
    {

        for (DistanceTable table:
                tables) {
            if(table.sonuc.equals(""))
                return;
        }
        liste.clone();
        for (DistanceTable t:
                liste) {
            Toast.makeText(context,t.org+"-"+t.destination+" "+t.son2,Toast.LENGTH_SHORT).show();
        }


    }*/
    public void distance() {
        ArrayList<String> orgs=new ArrayList<>();
        liste.add(tables.get(0));
        String dest=tables.get(0).destination;
        String org=tables.get(0).org;
        orgs.add(org);
        requestQueue = Volley.newRequestQueue(context);

        for (final DistanceTable t:
                tables) {
            StringRequest request = new StringRequest(Request.Method.POST, insertmesafe, new Response.Listener<String>() {
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
                    Map<String,String> parameters = new HashMap<String,String>();
                    parameters.put("baslangic",t.org);
                    parameters.put("bitis",t.destination);
                    parameters.put("mesafe",t.sonuc);

                            return parameters;
                }
            };
            requestQueue.add(request);
            //Toast.makeText(context,t.org+"-"+t.destination+" "+t.sonuc,Toast.LENGTH_SHORT).show();
        }
       /*for (int i=0;i<tables.size();i++)
       {
           boolean ilk=true;
           int maxindex = 0;
           boolean ok=false;
           for (int f=0;f<tables.size();f++)
           {


               if(dest.equals(tables.get(f).org)&&!org.equals(tables.get(f).destination))
               if(!(tables.get(i).destination.equals(tables.get(f).destination)&&tables.get(i).org.equals(tables.get(f).org)))
               if(ilk||Integer.parseInt(tables.get(f).sonuc.replace(","," ").replace(".","").split(" ")[0])<Integer.parseInt(tables.get(maxindex).sonuc.replace(","," ").replace(".","").split(" ")[0]))
               {

                   boolean orgsoke=true;
                   for (String oorgs:
                           orgs) {
                       if(oorgs.equals(tables.get(f).destination))
                       orgsoke=false;
                   }
                   if(orgsoke) {
                       ilk=false;
                       ok = true;
                       maxindex = f;
                   }
               }

           }
           if(ok) {
               ilk=true;
               org=tables.get(maxindex).org;
               dest=tables.get(maxindex).destination;
               orgs.add(org);
               liste.add(tables.get(maxindex));
               tables.remove(maxindex);
           }
       }*/

       liste.clone();


    }
    public String Calculate()
    {
        String URL="https://maps.googleapis.com/maps/api/directions/json?origin="+org+"&destination="+destination+"&sensor=false";
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(URL,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JsonData=new String(responseBody);
                try {
                    JSONObject mainobject=new JSONObject(JsonData);
                    sonuc=mainobject
                            .getJSONArray("routes")
                            .getJSONObject(0)
                            .getJSONArray("legs")
                            .getJSONObject(0)
                            .getJSONObject("distance")
                            .getString("text");
                    // .getJSONObject(0)
                    //.getString("text").toString();
                    controllastelement();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return sonuc;
    }

   /* public String Calculate2()
    {
        //String URL="https://maps.googleapis.com/maps/api/directions/json?origin="+org+"&destination="+destination+"&sensor=false";
        String URL="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+org+"&destinations="+destination+"&key=AIzaSyAc-ymia21XPKlNb3_UvXD-p1EixTluYPQ&sensor=false";
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(URL,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JsonData=new String(responseBody);
                try {
                    JSONObject mainobject=new JSONObject(JsonData);
                    son2=mainobject
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0)
                            .getJSONObject("distance")
                            .getString("text");
                    // .getJSONObject(0)
                    //.getString("text").toString();
                   controllastelement2();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return son2;


    }*/

}
