package com.lab1_swapnil_kumbhar_c0854325_android;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lab1_swapnil_kumbhar_c0854325_android.databinding.ActivityMapsBinding;
import com.lab1_swapnil_kumbhar_c0854325_android.models.CustomLocation;
import com.lab1_swapnil_kumbhar_c0854325_android.models.CustomPolygon;
import com.lab1_swapnil_kumbhar_c0854325_android.models.CustomPolyline;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final ArrayList<CustomLocation> locations = new ArrayList();
    private ArrayList<CustomPolyline> lines = new ArrayList<>();
    private int TAG = -1;
    private FloatingActionButton floatingActionButton;
    private Polyline selectedPolyline = null;
    private CustomPolygon polygon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.hide();


        locations.add(new CustomLocation(new LatLng(46.66,-112.28), "A"));
        locations.add(new CustomLocation(new LatLng(47.46,-101.42), "B"));
        locations.add(new CustomLocation(new LatLng(39.76,-104.99), "C"));
        locations.add(new CustomLocation(new LatLng(36.12,-115.31), "D"));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng northAmerica = new LatLng(42.92, -109.62);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(northAmerica, 6.0f));

        for (CustomLocation location: locations) {
            mMap.addMarker(new MarkerOptions().position(location.getLocation()).title(location.getTitle()));
        }

        polygon = new CustomPolygon(locations, mMap);

        lines.add(new CustomPolyline(locations.get(0), locations.get(1), ++TAG, mMap));
        lines.add(new CustomPolyline(locations.get(1), locations.get(2), ++TAG, mMap));
        lines.add(new CustomPolyline(locations.get(2), locations.get(3), ++TAG, mMap));
        lines.add(new CustomPolyline(locations.get(3), locations.get(0), ++TAG, mMap));
        mMap.setOnPolygonClickListener(polygon -> {
            if (this.polygon != null) {
                if (this.polygon.isDistanceMarkerShown()) {
                    this.polygon.getDistanceMarker().hideInfoWindow();
                    this.polygon.setDistanceMarkerShown(false);
                } else {
                    this.polygon.getDistanceMarker().showInfoWindow();
                    this.polygon.setDistanceMarkerShown(true);
                }
            }
        });
        mMap.setOnPolylineClickListener(polyline -> {
            try {
                int index = (int) polyline.getTag();
                CustomPolyline m = lines.get(index);
                if (m.isDistanceMarkerShown()) {
                    m.getDistanceMarker().hideInfoWindow();
                    m.setDistanceMarkerShown(false);
                    selectedPolyline = null;
                } else {
                    m.getDistanceMarker().showInfoWindow();
                    m.setDistanceMarkerShown(true);
                    selectedPolyline = polyline;
                }
                if (selectedPolyline != null) {
                    floatingActionButton.show();
                } else {
                    floatingActionButton.hide();
                }
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }

        });
    }

}