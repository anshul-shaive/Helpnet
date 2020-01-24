package com.example.madad_pro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.widget.Toast.makeText;

public class Frag_GetHelp extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double Lat=0.0;
    private Double Lng=0.0;
    SupportMapFragment mapFragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Lat = ((MyApplication) Frag_GetHelp.this.getActivity().getApplication()).getLat();
        Lng = ((MyApplication) Frag_GetHelp.this.getActivity().getApplication()).getLng();

         manager = getFragmentManager();
         transaction = manager.beginTransaction();
         mapFragment = new SupportMapFragment();
        transaction.add(R.id.general_map, mapFragment);
        transaction.commit();

        mapFragment.getMapAsync(this);

        return inflater.inflate(R.layout.fraggethelp,container,false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Lat = ((MyApplication) Frag_GetHelp.this.getActivity().getApplication()).getLat();
        Lng = ((MyApplication) Frag_GetHelp.this.getActivity().getApplication()).getLng();
        mMap = googleMap;
        Toast.makeText(getActivity(), "Map Is ready"+Lat+Lng, Toast.LENGTH_SHORT).show();
        mMap.clear();
        LatLng userLocation = new LatLng(Lat, Lng);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.addMarker(new MarkerOptions().position(userLocation).title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

    }

    public void openDialog()
    {
        dialog_handler_general dialog = new dialog_handler_general();
        dialog.show(getFragmentManager(), "dialog");
    }


}

