package com.miniproject.collegeapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class user_location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String lon,lat,mes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        lon=intent.getStringExtra("longitude");
        lat=intent.getStringExtra("latitude");
        mes=intent.getStringExtra("mes");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        mMap.addMarker(new MarkerOptions().position(location).title(mes));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16F));
    }
}