package com.example.madad_pro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;


public class help2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double Lat=0.0;
    private Double Lng=0.0;
    LatLng userLocation;
    LatLng midLatLng;
    SupportMapFragment mapFragment;
    AutocompleteSupportFragment autocompleteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Places.initialize(getApplicationContext(),"AIzaSyAZtuMxAlgBL3RGJ43EU5reKUBHnk4Z1Xo");
//        PlacesClient placesClient = Places.createClient(this);

        Lat = ((MyApplication) help2.this.getApplication()).getLat();
        Lng = ((MyApplication) help2.this.getApplication()).getLng();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help2);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button request = findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });





    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.clear();
        userLocation = new LatLng(Lat, Lng);
        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.addMarker(new MarkerOptions().position(userLocation).title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            Place.Field.ID, Place.Field.NAME,
            @Override
            public void onPlaceSelected(Place place) {

                userLocation =place.getLatLng();
                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(userLocation).title("You").draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));


            }

            @Override
            public void onError(Status status) {

                Log.i("Error", "An error occurred: " + status);
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //get latlng at the center by calling
                 midLatLng = mMap.getCameraPosition().target;
                Log.d("midln",midLatLng.toString());
                ((MyApplication) help2.this.getApplication()).setPinnedlat(midLatLng.latitude);
                ((MyApplication) help2.this.getApplication()).setPinnedlng(midLatLng.longitude);
            }
        });



    }


    public void openDialog()
    {
        dialog_handler exampleDialog = new dialog_handler();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(help2.this,MainActivity.class);
        startActivity(i);
        help2.this.finish();
    }
}

