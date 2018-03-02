package com.ruchita.personaldirectory.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ruchita.personaldirectory.R;
import com.ruchita.personaldirectory.models.User;


/**
 * Created by ruchita on 2/3/18.
 */

public class MapScreen extends AppCompatActivity implements OnMapReadyCallback {

    private String name;
    private String placeName;
    private String phone;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        Bundle bun = getIntent().getExtras();

        if (bun != null) {
            User user = (User) bun.getSerializable("user_data");
            latitude = user.getLat();
            longitude = user.getLang();
            placeName = user.getAddress();
            name = user.getName();
            phone = user.getPhone();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latlng = new LatLng(latitude, longitude);
        Marker marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                .title("Name : "+ name+ " Phone : "+phone ));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,12f));

        marker.showInfoWindow();
    }
}
