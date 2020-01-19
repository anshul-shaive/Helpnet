package com.example.madad_pro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;


public class HelpInfo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double Lat=0.0;
    private Double Lng=0.0;
    private String req_id,loc;
    private String url = "http://192.168.0.5:8000/helpinfo";
    private String res;

    SpotsDialog spotsDialog;
    private Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help_info);
        req_id=getIntent().getStringExtra("req_id");
        loc=getIntent().getStringExtra("loc");

        Lat=Double.parseDouble(loc.split(":")[0]);
        Lng=Double.parseDouble(loc.split(":")[1]);
        spotsDialog = new SpotsDialog(HelpInfo.this);
        sendAndRequestResponse();

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 5000);

    }

    private void TimerMethod()
    {
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            sendAndRequestResponse();
        }
    };



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.clear();

        if(! res.equals("")) {
            res= res.substring(2, res.length()-2);
            ArrayList uloc = new ArrayList<String>(Arrays.asList(res.split(",")));
            ArrayList uid = new ArrayList<String>();
            ArrayList loc = new ArrayList<String>();

            for (int i = 0; i < uloc.size(); i++) {
                String u = uloc.get(i).toString().split("L")[0];
                String l = uloc.get(i).toString().split("L")[1];
                uid.add(u);
                loc.add(l);
            }
            for (int i = 0; i < uid.size(); i++) {

                String iloc = loc.get(i).toString();
                Double Lat = Double.parseDouble(iloc.split(":")[0]);
                Double Lng = Double.parseDouble(iloc.split(":")[1]);

                LatLng userLocation = new LatLng(Lat, Lng);
                mMap.addMarker(new MarkerOptions().position(userLocation).title(uid.get(i).toString()));
            }
        }
        LatLng userLocation = new LatLng(Lat, Lng);
        mMap.addMarker(new MarkerOptions().position(userLocation).title("You"));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

        spotsDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(HelpInfo.this,help2.class);
        startActivity(i);
        HelpInfo.this.finish();
    }


    private void sendAndRequestResponse(){

        RequestQueue queue = Volley.newRequestQueue(HelpInfo.this);
        spotsDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("res",response);
                        res=response;
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map3);
                        mapFragment.getMapAsync(HelpInfo.this::onMapReady);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }

        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("req_id","" +req_id);
                return params;
            }

        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        queue.add(postRequest);

    }
}


