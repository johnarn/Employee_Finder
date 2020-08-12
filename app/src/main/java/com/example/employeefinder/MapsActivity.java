package com.example.employeefinder;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DbController dbController;
    private HashMap<String, Boolean> employees_names;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dbController = new DbController(this);

        employees_names = (HashMap<String, Boolean>) getIntent().getSerializableExtra("HashMapOfEmployeesNames");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder gc = new Geocoder(this);
        try {
            HashMap<LatLng, Boolean> addresses = new HashMap<>();
            for (Map.Entry<String, Boolean> employee_name : employees_names.entrySet()) {
                Map.Entry pair = (Map.Entry) employee_name;
                String home_address = dbController.getHomeAddress(employee_name.getKey());
                List<Address> home_addresses = gc.getFromLocationName(home_address, 5);
                LatLng address = new LatLng(home_addresses.get(0).getLatitude(), home_addresses.get(0).getLongitude());
                MarkerOptions marker = new MarkerOptions().position(address).title(employee_name.getKey());
                if (employee_name.getValue()) {
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    mMap.addMarker(marker);
                    float zoomLevel = 12.0f; //This goes up to 21
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, zoomLevel));
                    addresses.put(address, true);
                } else {
                    mMap.addMarker(marker);
                    addresses.put(address, false);
                }

                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
            drawPrimaryLinePath(addresses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void drawPrimaryLinePath(HashMap<LatLng, Boolean> addressHashMap) {
        if (mMap == null) {
            return;
        }
        if (addressHashMap.size() < 2) {
            return;
        }
        PolylineOptions options = new PolylineOptions();
        LatLng main_address = null;
        for (Map.Entry<LatLng, Boolean> address : addressHashMap.entrySet()) {
            if (address.getValue()) {
                main_address = address.getKey();
                break;
            }
        }

        for (Map.Entry<LatLng, Boolean> address : addressHashMap.entrySet()) {
            options.add(main_address);
            options.color(Color.parseColor("#CC0000FF"));
            options.width(5);
            options.visible(true);
            options.add(address.getKey());
            mMap.addPolyline(options);
            options = new PolylineOptions();
        }
    }
}




