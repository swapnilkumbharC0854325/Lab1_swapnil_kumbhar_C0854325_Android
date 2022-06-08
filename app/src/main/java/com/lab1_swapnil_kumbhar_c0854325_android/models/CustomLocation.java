package com.lab1_swapnil_kumbhar_c0854325_android.models;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomLocation {
    private LatLng location;
    private String title;
    private final int ID;
    private Marker marker;

    public int getID() {
        return ID;
    }

    public CustomLocation(LatLng location, String title, int ID) {
        this.location = location;
        this.title = title;
        this.ID = ID;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void draw(GoogleMap mMap) {
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(location).title(title));
    }

    public boolean compareWith(LatLng latLng) {
        return location.latitude == latLng.latitude && location.longitude == latLng.longitude;
    }
}
