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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Nav_Main extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActionBarDrawerToggle mDrawerToggle;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    private String url = "https://helpnet-web.herokuapp.com/loc";
    ViewDragHelper viewDragHelper;

    int user_id;

    Double Lat, Lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav__main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_history_black_24dp);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Checking the saved data from shared prefrences
        SharedPreferences prefs = getSharedPreferences("token_sp", MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                    || super.onSupportNavigateUp();
//        }

        if(id == R.id.logout)
        {
            SharedPreferences.Editor editor = getSharedPreferences("token_sp", MODE_PRIVATE).edit();
            editor.putString("token", "");
            editor.putInt("user_id", 0);
            editor.apply();

            ((MyApplication) Nav_Main.this.getApplication()).setUser_id("");
            ((MyApplication) Nav_Main.this.getApplication()).setStatus("");

            Intent intent = new Intent(Nav_Main.this,Option.class);
            startActivity(intent);
            Nav_Main.this.finish();
        }
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
                                    ((MyApplication) Nav_Main.this.getApplication()).setLat(location.getLatitude());
                                    ((MyApplication) Nav_Main.this.getApplication()).setLng(location.getLongitude());
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
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

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

            ((MyApplication) Nav_Main.this.getApplication()).setLat(mLastLocation.getLatitude());
            ((MyApplication) Nav_Main.this.getApplication()).setLng(mLastLocation.getLongitude());
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

    public void logout(View view)
    {

        SharedPreferences.Editor editor = getSharedPreferences("token_sp", MODE_PRIVATE).edit();
        editor.putString("token", "");
        editor.putInt("user_id", 0);
        editor.apply();

        ((MyApplication) Nav_Main.this.getApplication()).setUser_id("");
        ((MyApplication) Nav_Main.this.getApplication()).setStatus("");

        Intent intent = new Intent(Nav_Main.this,Option.class);
        startActivity(intent);
        Nav_Main.this.finish();
    }


    public void onDrawerSlide(View drawerView, float slideOffset){
        if(slideOffset > .5){
            mDrawerToggle.onDrawerOpened(drawerView);
        } else {
            mDrawerToggle.onDrawerClosed(drawerView);
        }
    }



    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Nav_Main.this,MainActivity.class));
        Nav_Main.this.finish();
    }

//    private void sendAndRequestResponse(){
//
//        final SpotsDialog spotsDialog = new SpotsDialog(Nav_Main.this);
//        spotsDialog.show();
//
//        Lat = ((MyApplication) Nav_Main.this.getApplication()).getLat();
//        Lng = ((MyApplication) Nav_Main.this.getApplication()).getLng();
//
//        RequestQueue queue = Volley.newRequestQueue(Nav_Main.this);
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        JSONObject jObject=new JSONObject();
//
//                        try {
//                            jObject = new JSONObject(response);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        spotsDialog.dismiss();
//
//                        Intent intent = new Intent(Nav_Main.this,General.class);
//                        intent.putExtra("json",jObject.toString());
//                        startActivity(intent);
//                        Nav_Main.this.finish();
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("Error.Response", String.valueOf(error));
//                    }
//                }
//
//        )





//
//        {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("user_id","" +user_id);
//                params.put("last_loc",""+Lat+":"+Lng);
//
//                return params;
//            }
//
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
//        );
//
//        queue.add(postRequest);
//
//    }

    public void emergency(View view)
    {
        Intent intent = new Intent(Nav_Main.this,help2.class);
        startActivity(intent);
        Nav_Main.this.finish();
    }

//    public void helpOthers(View view)
//    {
//        sendAndRequestResponse();
//
//    }
    public  void general(View view)
    {

        startActivity(new Intent(Nav_Main.this,General.class));
        Nav_Main.this.finish();
    }


}
