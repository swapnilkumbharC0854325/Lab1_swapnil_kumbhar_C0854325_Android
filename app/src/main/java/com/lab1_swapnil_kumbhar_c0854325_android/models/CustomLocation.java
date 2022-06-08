package com.lab1_swapnil_kumbhar_c0854325_android.models;

import com.google.android.gms.maps.model.LatLng;

public class CustomLocation {
    private LatLng location;
    private String title;

    public CustomLocation(LatLng location, String title) {
        this.location = location;
        this.title = title;
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
