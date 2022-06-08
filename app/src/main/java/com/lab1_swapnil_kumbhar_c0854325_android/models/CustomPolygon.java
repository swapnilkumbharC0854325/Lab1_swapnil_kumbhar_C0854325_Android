package com.lab1_swapnil_kumbhar_c0854325_android.models;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomPolygon {
    private Marker distanceMarker;
    private boolean isDistanceMarkerShown = false;
    private final ArrayList<CustomLocation> points;
    private double distance;
    private Polygon polygon;

    public Marker getDistanceMarker() {
        return distanceMarker;
    }

    public void setDistanceMarker(Marker distanceMarker) {
        this.distanceMarker = distanceMarker;
    }

    public boolean isDistanceMarkerShown() {
        return isDistanceMarkerShown;
    }

    public void setDistanceMarkerShown(boolean distanceMarkerShown) {
        isDistanceMarkerShown = distanceMarkerShown;
    }

    public CustomPolygon(ArrayList<CustomLocation> points) {
        this.points = points;
    }

    public void draw(GoogleMap mMap) {
        if (polygon != null) {
            polygon.remove();
        }
        polygon = mMap.addPolygon(createPolygonOptions(mMap));
    }

    public void remove() {
        if (polygon != null) {
            polygon.remove();
        }
    }

    private PolygonOptions createPolygonOptions(GoogleMap mMap) {
        DecimalFormat df = new DecimalFormat("0.00");
        final PolygonOptions options = new PolygonOptions();
        for (int i = 0; i < points.size(); i++) {
            CustomLocation customLocation = points.get(i);
            options.add(customLocation.getLocation());
            if (i < points.size() - 1) {
                CustomLocation nextPoint = points.get(i+1);
                float[] results = new float[1];
                Location.distanceBetween(customLocation.getLocation().latitude, customLocation.getLocation().longitude, nextPoint.getLocation().latitude, nextPoint.getLocation().longitude, results);
                this.distance += results[0];
            }
        }
        options.fillColor(0x5900ff00);
        options.strokeWidth(0);
        options.clickable(true);
        BitmapDescriptor invisibleMarker =
                BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
        this.distanceMarker = mMap.addMarker(new MarkerOptions().position(getCentroid(points))
                .title("Distance")
                .snippet( df.format(distance / 1000) + " KM")
                .alpha(0f)
                .icon(invisibleMarker)
                .anchor(0f, 0f));
        return options;
    }

    private LatLng getCentroid(ArrayList<CustomLocation> points) {
        double[] centroid = { 0.0, 0.0 };
        for (int i = 0; i < points.size(); i++) {
            centroid[0] += points.get(i).getLocation().latitude;
            centroid[1] += points.get(i).getLocation().longitude;
        }
        int totalPoints = points.size();
        centroid[0] = centroid[0] / totalPoints;
        centroid[1] = centroid[1] / totalPoints;

        return new LatLng(centroid[0], centroid[1]);
    }
}
