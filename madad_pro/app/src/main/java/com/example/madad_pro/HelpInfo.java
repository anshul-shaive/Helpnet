package com.example.madad_pro;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;


public class HelpInfo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double Lat=0.0;
    private Double Lng=0.0;
    private String req_id,loc;
    private String url = "https://helpnet-web.herokuapp.com/helpinfo";
//    private String url = "http://192.168.0.5:8000/helpinfo";
    private String res;
    DirectionsResult directionsResult;
    int flag=0;
    String status="active";
    SupportMapFragment mapFragment;


    SpotsDialog spotsDialog;
    private Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help_info);
        req_id=getIntent().getStringExtra("req_id");
        loc=getIntent().getStringExtra("loc");
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);

        Lat=Double.parseDouble(loc.split(":")[0]);
        Lng=Double.parseDouble(loc.split(":")[1]);
        spotsDialog = new SpotsDialog(HelpInfo.this);
        sendAndRequestResponse();

        Button Cancel = findViewById(R.id.cancel_help);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onhelpcancel();
            }
        });

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
            sendAndRequestResponse2();
        }
    };



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.clear();

        if(! res.equals("")) {
            res= res.substring(1, res.length()-1);
            ArrayList uloc = new ArrayList<String>(Arrays.asList(res.split(",")));
            ArrayList uid = new ArrayList<String>();
            ArrayList loc = new ArrayList<String>();

            for (int i = 0; i < uloc.size(); i++) {
                if(i==0) {
                    String uu = uloc.get(i).toString();
                    uu = uu.substring(1, uu.length() - 1);
                    String u = uu.split("L")[0];
                    String l = uu.split("L")[1];
                    uid.add(u);
                    loc.add(l);
                }
                else {
                    String uu = uloc.get(i).toString();
                    uu = uu.substring(2, uu.length() - 1);
                    String u = uu.split("L")[0];
                    String l = uu.split("L")[1];
                    uid.add(u);
                    loc.add(l);

                }


            }
            for (int i = 0; i < uid.size(); i++) {

                String iloc = loc.get(i).toString();
                Double Lathelper = Double.parseDouble(iloc.split(":")[0]);
                Double Lnghelper = Double.parseDouble(iloc.split(":")[1]);

                LatLng userLocation = new LatLng(Lathelper, Lnghelper);
                Marker myMarker=mMap.addMarker(new MarkerOptions().position(userLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.helper))
                        .title(uid.get(i).toString()));



///////////////////////////////////////////////////////
                if(flag !=0) {
                    //Define list to get all latlng for the route
                    List<LatLng> path = new ArrayList();

                    String origLat = Lat.toString();
                    String origLng = Lng.toString();

                    //Execute Directions API request
                    GeoApiContext context = new GeoApiContext.Builder()
                            .apiKey("AIzaSyAZtuMxAlgBL3RGJ43EU5reKUBHnk4Z1Xo")
                            .build();
//        DirectionsApiRequest req = DirectionsApi.getDirections(context, "41.385064,2.173403", "40.416775,-3.70379");
                    DirectionsApiRequest req = DirectionsApi.getDirections(context, origLat + "," + origLng, Lathelper + "," + Lnghelper);

                    try {
                        directionsResult = req.await();

                        //Loop through legs and steps to get encoded polylines of each step
                        if (directionsResult.routes != null && directionsResult.routes.length > 0) {
                            DirectionsRoute route = directionsResult.routes[0];

                            if (route.legs != null) {
                                for (int l = 0; l < route.legs.length; l++) {
                                    DirectionsLeg leg = route.legs[l];
                                    if (leg.steps != null) {
                                        for (int j = 0; j < leg.steps.length; j++) {
                                            DirectionsStep step = leg.steps[j];
                                            if (step.steps != null && step.steps.length > 0) {
                                                for (int k = 0; k < step.steps.length; k++) {
                                                    DirectionsStep step1 = step.steps[k];
                                                    EncodedPolyline points1 = step1.polyline;
                                                    if (points1 != null) {
                                                        //Decode polyline and add points to list of route coordinates
                                                        List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                        for (com.google.maps.model.LatLng coord1 : coords1) {
                                                            path.add(new LatLng(coord1.lat, coord1.lng));
                                                        }
                                                    }
                                                }
                                            } else {
                                                EncodedPolyline points = step.polyline;
                                                if (points != null) {
                                                    //Decode polyline and add points to list of route coordinates
                                                    List<com.google.maps.model.LatLng> coords = points.decodePath();
                                                    for (com.google.maps.model.LatLng coord : coords) {
                                                        path.add(new LatLng(coord.lat, coord.lng));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.e("road_err", ex.getLocalizedMessage());
                    }

                    //Draw the polyline
                    if (path.size() > 0) {
                        PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                        mMap.addPolyline(opts);
                    }


                    mMap.getUiSettings().setZoomControlsEnabled(true);
//                    mMap.addMarker(new MarkerOptions().position(reqLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.requester)).title("Help required here!")).setSnippet(getEndLocationTitle(res));
////        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(reqLoc, 14));
//
//
//                    ////////////////////////////////////////
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(reqLoc));
//                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
//                    mMap.animateCamera(zoom);
                }
//////////////////////////////////////////////////////////////


            }

            LatLng userLocation = new LatLng(Lat, Lng);
            mMap.addMarker(new MarkerOptions().position(userLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.requester))
                    .title("You"));
            mMap.getUiSettings().setZoomControlsEnabled(true);


        }

        else {
            LatLng userLocation = new LatLng(Lat, Lng);
            mMap.addMarker(new MarkerOptions().position(userLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.requester))
                    .title("You"));
            mMap.getUiSettings().setZoomControlsEnabled(true);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

            spotsDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(HelpInfo.this, MainActivity.class);
        startActivity(intent);
        HelpInfo.this.finish();
    }


    private void sendAndRequestResponse(){

        RequestQueue queue = Volley.newRequestQueue(HelpInfo.this);
        spotsDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("cancelled")){
                            spotsDialog.dismiss();
                            Toast.makeText(HelpInfo.this, "Request Cancelled" , Toast.LENGTH_LONG).show();
                            Intent intent;
                            intent = new Intent(HelpInfo.this, MainActivity.class);
                            startActivity(intent);
                            HelpInfo.this.finish();
                        }
                        else {
//                        Log.d("res",response);
                            res = response;

                            mapFragment.getMapAsync(HelpInfo.this::onMapReady);
                        }
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
                params.put("status","" +status);

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


    private void sendAndRequestResponse2(){

        RequestQueue queue = Volley.newRequestQueue(HelpInfo.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(status.equals("active")) {
                            Log.d("res", response);
                            res = response;
                            flag = 1;

                            mapFragment.getMapAsync(HelpInfo.this::onMapReady);
                        }
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
                params.put("status","active");
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

    public void onhelpcancel(){
        status="cancelled";
        new AlertDialog.Builder(HelpInfo.this)
                .setTitle("Are You Sure you want to cancel?")
                .setMessage("You'll be fined for this")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendAndRequestResponse();
                    }
                })

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        status="active";
                    }
                })
                .setIcon(R.drawable.risk)
                .show();
    }
}


