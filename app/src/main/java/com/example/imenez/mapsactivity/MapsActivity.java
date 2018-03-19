package com.example.imenez.mapsactivity;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpConnection;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getName();
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    
    String insertUrl = "http://192.168.1.8/insertlokasyon.php";
    String showUrl = "http://192.168.1.8/showlokasyon.php";
    double lati,lon;
    DistanceTable dt;
    public String sonuc2="";//"morzFsxjfEUbDaAUeDmB_Bw@[]cAuA_@q@Yw@CF]BK?IOgJgPa@e@KEKA?A?C?ECICEGEkB_EkAkBu@cAcBiBaCgBmB{@u@QiASq@K_@C{BBqEVo@Ey@OkA_@_Am@cFwDuDcCaBy@kFuAyEcB_Ac@eBiAu@_@sB}A?GCIEIOII?yP}N_LoJkAaAiFsEy@s@_@UCGMMEYoAeE[o@{@qCOW]^SXKDcAf@u@Vm@NFb@Jr@";
    public String src,des,id,x,y,aciklama;
    LatLng basla= new LatLng(41.2237539,32.6645834);
    LatLng bitir =new LatLng(41.2514274,32.6842289);
    Polyline line;
    List<LatLng> liste;
    List<LatLng> list1;

    ArrayList<String> Destinations=null;
    ArrayList<LatLng> list=new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button ekle= (Button) findViewById(R.id.bekle);
        final EditText etadres = (EditText) findViewById(R.id.etadres);


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Destinations=new ArrayList<>();
        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Destinations.add(etadres.getText().toString().trim());
                //Adreslistesi destinationsda db ye atıcaz
                Calculate();
                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters = new HashMap<String, String>();
                        parameters.put("adi",etadres.getText().toString());
                        parameters.put("lati", String.valueOf(lati));
                        parameters.put("lon", String.valueOf(lon));
                        return parameters;

                    }
                };
                requestQueue.add(request);
                Toast.makeText(getApplicationContext(),"Adresiniz Eklendi",Toast.LENGTH_SHORT).show();
            }
        });

        ZoomControls zoom = (ZoomControls) findViewById(R.id.zoom);
        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });



        Button bttip = (Button) findViewById(R.id.btip);

        bttip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /* list.add(new LatLng(40.4221888,29.7198587));
                    list.add(new LatLng(40.4249712,29.7245057));
                    list.add(new LatLng(40.4263624,29.7221527));*/
                Calculate2();

            }
        });
    }
    public String Calculate2()
    {
        mMap.clear();
        String url0 = "http://192.168.1.8/son.php";
        requestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, url0, new Response.Listener<String>() {
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray lokasyonlar = response.getJSONArray("sondurum");
                    for(int i = 0 ; i <= lokasyonlar.length(); i++){
                        JSONObject lokasyon = lokasyonlar.getJSONObject(i);
                        JSONObject lokasyonn = lokasyonlar.getJSONObject(i+1);
                        src = lokasyon.getString("adi");
                        des = lokasyonn.getString("adi");
                        id= lokasyon.getString("Id");
                        aciklama= lokasyon.getString("aciklama");
                         y = lokasyon.getString("lati");
                         x = lokasyon.getString("lon");
                        double d= Double.parseDouble(y);
                        double e= Double.parseDouble(x);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(d,e)).title(id));
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Intent intent = new Intent(getApplicationContext(),DurumActivity.class);
                                intent.putExtra("aciklama", aciklama);
                                startActivity(intent);
                                return false;
                            }
                        });
                        String tarih = lokasyon.getString("tarih");

                        //Toast.makeText(getApplicationContext(),"lo:"+src+"lol:"+des,Toast.LENGTH_SHORT).show();
                        String URL="https://maps.googleapis.com/maps/api/directions/json?origin="+src+"&destination="+des+"&sensor=false";
                        AsyncHttpClient client=new AsyncHttpClient();
                        client.get(URL,new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String JsonData=new String(responseBody);
                                try {
                                    JSONObject mainobject=new JSONObject(JsonData);
                                    sonuc2=mainobject
                                            .getJSONArray("routes")
                                            .getJSONObject(0)
                                            .getJSONObject("overview_polyline")
                                            .getString("points");

                                    // .getJSONObject(0)
                                    //.getString("text").toString();
                                    liste = decodePoly(sonuc2);
                                    drawPolyLineOnMap(liste);



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });



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


        return sonuc2;
    }
    public void drawPolyLineOnMap(List<LatLng> liste) {
        int y = 0;
        //Toast.makeText(getApplicationContext(),"lol:"+sonuc2,Toast.LENGTH_SHORT).show();
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int z = 0; z < liste.size(); z++) {
            LatLng point = new LatLng(liste.get(z).latitude, liste.get(z).longitude);
            options.add(point);
            y=z;

        }
       LatLng mark2 = new LatLng(liste.get(y).latitude, liste.get(y).longitude);
        mMap.addMarker(new MarkerOptions().position(mark2).title("son"));
       /*LatLng mark = new LatLng(liste.get(0).latitude, liste.get(0).longitude);
        mMap.addMarker(new MarkerOptions().position(mark).title(id));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getApplicationContext(),DurumActivity.class);
                startActivity(intent);
                return false;
            }
        });*/
        line = mMap.addPolyline(options);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : liste) {
            builder.include(latLng);
        }
        final LatLngBounds bounds = builder.build();
        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cu);


        /*PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.addAll(list);
        polyOptions.color(Color.RED);
        polyOptions.width(5);
        polyOptions.geodesic(true);
        mMap.clear();
        mMap.addPolyline(polyOptions);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
        final LatLngBounds bounds = builder.build();
        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cu);*/
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    public ArrayList<DistanceTable> tables=new ArrayList<>();
    public void Calculate()
    {
        for (String org:
             Destinations) {

            for (String dest:
                 Destinations) {
                boolean exist=false;
                if(!org.trim().equals(dest.trim())) {
                    for (DistanceTable table :
                            tables) {

                        if (table.destination.equals(dest) && table.org.equals(org))
                            exist = true;
                    }
                    if(!exist)
                    tables.add(new DistanceTable(getApplicationContext(), org, dest,tables));
                }
            }
        }
tables.clone();


    }
    public void Ara(View view) {

        EditText etlokasyon = (EditText) findViewById(R.id.etadres);
        Button btara = (Button) findViewById(R.id.bara);
        String lokasyon = etlokasyon.getText().toString();
        List<Address> addressList = null;


        if (lokasyon != null || !lokasyon.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(lokasyon, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            lokasyon = address.getLocality();

            String addresss = addressList.get(0).getAddressLine(0);
            String city = addressList.get(0).getLocality();
            String state = addressList.get(0).getAdminArea();
            String country = addressList.get(0).getCountryName();
            String postalCode = addressList.get(0).getPostalCode();
            String knownName = addressList.get(0).getFeatureName();

            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            lati = latLng.latitude;
            lon = latLng.longitude;
            mMap.addMarker(new MarkerOptions().position(latLng).title(knownName+ " "+ addresss +" " + city+" " + state+" " + country+" " + postalCode ));


            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            SphericalUtil.computeDistanceBetween(latLng,latLng);


        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else{
        mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        if (requestCode == 0){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
            }
        }
    }

}
