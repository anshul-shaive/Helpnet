package com.example.madad_pro;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import static android.widget.Toast.makeText;

public class Frag_GetHelp extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double Lat=0.0;
    private Double Lng=0.0;
    SupportMapFragment mapFragment;
    FragmentManager manager;
    FragmentTransaction transaction;
    LatLng userLocation;
    LatLng midLatLng;
    AutocompleteSupportFragment autocompleteFragment;
    Context context;
    View v;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v =inflater.inflate(R.layout.fraggethelp,container,false);
        context= Frag_GetHelp.this.getActivity();

        Lat = ((MyApplication) Frag_GetHelp.this.getActivity().getApplication()).getLat();
        Lng = ((MyApplication) Frag_GetHelp.this.getActivity().getApplication()).getLng();
        Places.initialize(Frag_GetHelp.this.getActivity(),"AIzaSyAZtuMxAlgBL3RGJ43EU5reKUBHnk4Z1Xo");

        mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.general_map);
        mapFragment.getMapAsync(this);

        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.general_autocomplete_fragment);


        Button request = v.findViewById(R.id.general_request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.clear();
        userLocation = new LatLng(Lat, Lng);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {

                userLocation =place.getLatLng();
                mMap.clear();
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
                ((MyApplication) Frag_GetHelp.this.getActivity().getApplication()).setPinnedlat(midLatLng.latitude);
                ((MyApplication) Frag_GetHelp.this.getActivity().getApplication()).setPinnedlng(midLatLng.longitude);
            }
        });

    }

    public void openDialog()
    {
        dialog_handler_general dialog = new dialog_handler_general();
        dialog.show(getChildFragmentManager(), "dialog");
    }


}

