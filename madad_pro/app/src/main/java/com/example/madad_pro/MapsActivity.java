package com.example.madad_pro;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = "so47492459";
    Double Lat,Lng;
    String req_location;
    String req_lat;
    String req_lng;
    String[] reqloc;
    private static final int overview = 0;
    DirectionsResult res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Lat = ((MyApplication) MapsActivity.this.getApplication()).getLat();
        Lng = ((MyApplication) MapsActivity.this.getApplication()).getLng();

        req_location=getIntent().getStringExtra("req_location");
        reqloc=req_location.split(":");
        req_lat=reqloc[0];
        req_lng=reqloc[1];
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        Button request = findViewById(R.id.Navigate);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNavigationView(req_lat,req_lng);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng userLoc = new LatLng(Lat, Lng);
        LatLng reqLoc = new LatLng(Double.parseDouble(req_lat), Double.parseDouble(req_lng));

        mMap.addMarker(new MarkerOptions().position(userLoc).title("You"));

        ////////////////////////////////////
        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();

        String origLat=Lat.toString();
        String origLng=Lng.toString();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAZtuMxAlgBL3RGJ43EU5reKUBHnk4Z1Xo")
                .build();
//        DirectionsApiRequest req = DirectionsApi.getDirections(context, "41.385064,2.173403", "40.416775,-3.70379");
        DirectionsApiRequest req = DirectionsApi.getDirections(context, origLat+","+origLng,req_lat+","+req_lng);

        try {
            res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
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
        } catch(Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }



        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.addMarker(new MarkerOptions().position(reqLoc).title("Help required here!")).setSnippet(getEndLocationTitle(res));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(reqLoc, 14));



        ////////////////////////////////////////
        mMap.moveCamera(CameraUpdateFactory.newLatLng(reqLoc));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
        mMap.animateCamera(zoom);
    }
    private String getEndLocationTitle(DirectionsResult results){
        return  "Time :"+ results.routes[overview].legs[overview].duration.humanReadable + " Distance :" + results.routes[overview].legs[overview].distance.humanReadable;
    }

    public void loadNavigationView(String lat,String lng){
        Uri navigation = Uri.parse("google.navigation:q="+lat+","+lng+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        navigationIntent.setPackage("com.google.android.apps.maps");
        startActivity(navigationIntent);
    }
}
