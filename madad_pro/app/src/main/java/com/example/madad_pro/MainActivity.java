package com.example.madad_pro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity {

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    public String url = "https://helpnet-web.herokuapp.com/loc";

//    private String url = "http://192.168.0.5:8000/loc";

    int user_id;

     Double Lat, Lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("token_sp", MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);


    }

    @SuppressLint("MissingPermission")
    public  void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    requestNewLocationData();

                                    ((MyApplication) MainActivity.this.getApplication()).setLat(location.getLatitude());
                                    ((MyApplication) MainActivity.this.getApplication()).setLng(location.getLongitude());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
//        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            ((MyApplication) MainActivity.this.getApplication()).setLat(mLastLocation.getLatitude());
            ((MyApplication) MainActivity.this.getApplication()).setLng(mLastLocation.getLongitude());
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }


    }

    public void emergency(View view)
    {   SharedPreferences prefs = getSharedPreferences("token_sp", MODE_PRIVATE);
        String req_id = prefs.getString("req_id", "");
        String loc = prefs.getString("loc", "");

        if(! req_id.equals("")){

            Intent intent = new Intent(MainActivity.this,HelpInfo.class);
            intent.putExtra("req_id",req_id);
            intent.putExtra("loc",loc);
            intent.putExtra("flag",1);
            intent.putExtra("resume",1);


            startActivity(intent);
            MainActivity.this.finish();
        }
        else {
            Intent intent = new Intent(MainActivity.this, help2.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    public void general(View view)
    {
        Intent intent = new Intent(MainActivity.this,Nav_Main.class);
        startActivity(intent);
        MainActivity.this.finish();
    }


    public void helpOthers(View view)
    {
        SharedPreferences prefs = getSharedPreferences("token_sp", MODE_PRIVATE);
        String req_id = prefs.getString("req_id", "");
        String req_location = prefs.getString("req_location", "");
        if(! req_id.equals("")){

            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            intent.putExtra("req_id",req_id);
            intent.putExtra("req_location",req_location);
            startActivity(intent);
            MainActivity.this.finish();
        }
        else {
            sendAndRequestResponse();
        }

    }

    public void logout(View view)
    {

        SharedPreferences.Editor editor = getSharedPreferences("token_sp", MODE_PRIVATE).edit();
        editor.putString("token", "");
        editor.putInt("user_id", 0);
        editor.apply();

        ((MyApplication) MainActivity.this.getApplication()).setUser_id("");
        ((MyApplication) MainActivity.this.getApplication()).setStatus("");

        Intent intent = new Intent(MainActivity.this,Option.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void onBackPressed()
    {
        super.onBackPressed();
//        MainActivity.this.finish();
    }

    private void sendAndRequestResponse(){

        final SpotsDialog spotsDialog = new SpotsDialog(MainActivity.this);
        spotsDialog.show();

        Lat = ((MyApplication) MainActivity.this.getApplication()).getLat();
        Lng = ((MyApplication) MainActivity.this.getApplication()).getLng();

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jObject=new JSONObject();

                        try {
                            jObject = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        spotsDialog.dismiss();

                        Intent intent = new Intent(MainActivity.this,Helper2.class);
                        intent.putExtra("json",jObject.toString());
                        startActivity(intent);
                        MainActivity.this.finish();

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

                params.put("user_id","" +user_id);
                params.put("last_loc",""+Lat+":"+Lng);

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


