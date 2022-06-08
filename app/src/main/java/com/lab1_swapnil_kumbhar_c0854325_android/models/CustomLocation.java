package com.lab1_swapnil_kumbhar_c0854325_android.models;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomLocation {
    private LatLng location;
    private String title;
    private final int ID;

    public int getID() {
        return ID;
    }

    public CustomLocation(LatLng location, String title, int ID, GoogleMap mMap) {
        this.location = location;
        this.title = title;
        this.ID = ID;
        mMap.addMarker(new MarkerOptions().position(location).title(title));
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
}
