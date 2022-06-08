package com.lab1_swapnil_kumbhar_c0854325_android.models;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;

public class CustomPolyline {
    public Marker getDistanceMarker() {
        return distanceMarker;
    }

    private Marker distanceMarker;
    private Polyline polyline;
    private boolean isDistanceMarkerShown;
    private CustomLocation point1;
    private CustomLocation point2;
    private int TAG;
    private double distance;

    public int getTAG() {
        return TAG;
    }

    public boolean isDistanceMarkerShown() {
        return isDistanceMarkerShown;
    }

    public void setDistanceMarkerShown(boolean distanceMarkerShown) {
        isDistanceMarkerShown = distanceMarkerShown;
    }

    public CustomPolyline(CustomLocation point1, CustomLocation point2, int TAG, GoogleMap mMap) {
        this.point1 = point1;
        this.point2 = point2;
        this.TAG = TAG;
        this.polyline = mMap.addPolyline(createPolylineOptions(this.point1, this.point2));
        double lat = (this.point1.getLocation().latitude + this.point2.getLocation().latitude) / 2;
        double lng = (this.point1.getLocation().longitude + this.point2.getLocation().longitude) / 2;
        LatLng midPoint = new LatLng(lat, lng);
        DecimalFormat df = new DecimalFormat("0.00");
        float[] results = new float[1];
        Location.distanceBetween(point1.getLocation().latitude, point1.getLocation().longitude, point2.getLocation().latitude, point2.getLocation().longitude, results);
        this.distance = results[0];
        BitmapDescriptor invisibleMarker =
                BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
        this.distanceMarker = mMap.addMarker(new MarkerOptions().position(midPoint)
                .title(this.point1.getTitle() + " -> " + this.point2.getTitle())
                .snippet( df.format(distance / 1000) + " KM")
                .alpha(0f)
                .icon(invisibleMarker)
                .anchor(0f, 0f));
        polyline.setTag(this.TAG);
    }

    public Polyline getPolyline() {
        return polyline;
    }


    private PolylineOptions createPolylineOptions(CustomLocation a, CustomLocation b) {
        final PolylineOptions options = new PolylineOptions();
        options.add(a.getLocation());
        options.add(b.getLocation());
        options.color(0xffff0000);
        options.width(3);
        options.clickable(true);
        return options;
    }
}
