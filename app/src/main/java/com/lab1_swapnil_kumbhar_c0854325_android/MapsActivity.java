package com.lab1_swapnil_kumbhar_c0854325_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lab1_swapnil_kumbhar_c0854325_android.databinding.ActivityMapsBinding;
import com.lab1_swapnil_kumbhar_c0854325_android.models.CustomLocation;
import com.lab1_swapnil_kumbhar_c0854325_android.models.CustomPolygon;
import com.lab1_swapnil_kumbhar_c0854325_android.models.CustomPolyline;
import com.lab1_swapnil_kumbhar_c0854325_android.models.SelectedShapeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final ArrayList<CustomLocation> locations = new ArrayList();
    private final ArrayList<CustomPolyline> lines = new ArrayList<>();
    private int TAG = -1;
    private FloatingActionButton floatingActionButton;
    private Polyline selectedPolyline = null;
    private Polygon selectedPolygon = null;
    private Marker selectedMarker = null;
    private CustomPolygon polygon;
    private SelectedShapeType selectedType = SelectedShapeType.NONE;
    private Marker currentDraggingMarker = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SearchView searchView = findViewById(R.id.search_bar_text);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();

                List<Address> addressList = null;

                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    locations.add(new CustomLocation(latLng, "", TAG++));
                    mapRedraw();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(view -> {
            this.editShape();
        });
        floatingActionButton.hide();


    }

    private void editShape() {
        switch (selectedType) {
            case POLYLINE:
                this.polylineEdit();
            case MARKER:
                this.markerEdit();
            case POLYGON:
                this.polygonEdit();
            default:
        }
    }

    private void polylineEdit() {
        if (selectedPolygon != null) {
            int color = selectedPolyline.getColor();
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(MapsActivity.this, color, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    selectedPolyline.setColor(color);
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }
            });
            dialog.show();
        }
    }

    private void markerEdit() {
        if (selectedMarker == null) {
            return;
        }
        selectedMarker.hideInfoWindow();
        String previousTitle = selectedMarker.getTitle();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        final EditText input = new EditText(this);
        input.setText(previousTitle);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                if (m_Text.isEmpty()) {
                    selectedMarker.setTitle(previousTitle);
                } else {
                    selectedMarker.setTitle(m_Text);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        selectedType = SelectedShapeType.NONE;
    }

    private void polygonEdit() {
        if (selectedPolygon != null) {
            int color = selectedPolygon.getFillColor();
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(MapsActivity.this, color, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    selectedPolygon.setFillColor(color);
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }
            });
            dialog.show();
        }
    }


    private void mapRedraw() {
        for (int i = 0; i < lines.size(); i++) {
            CustomPolyline polyline = lines.get(i);
            polyline.remove();
        }
        lines.clear();
        for (int i = 0; i < locations.size(); i++) {
            CustomLocation customLocation = locations.get(i);
            customLocation.draw(mMap);
            if (i < locations.size() - 1) {
                CustomLocation nextPoint = locations.get(i + 1);
                lines.add(new CustomPolyline(customLocation, nextPoint, ++TAG));
            } else {
                lines.add(new CustomPolyline(locations.get(0), customLocation, ++TAG));
            }
        }
        for (int i = 0; i < lines.size(); i++) {
            CustomPolyline polyline = lines.get(i);
            polyline.draw(mMap);
        }
        if (locations.size() > 2) {
            polygon.draw(mMap);
        } else {
            polygon.remove();
        }
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
        LatLng northAmerica = new LatLng(42.92, -109.62);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(northAmerica, 6.0f));


        locations.add(new CustomLocation(new LatLng(46.66, -112.28), "A", TAG++));
        locations.add(new CustomLocation(new LatLng(47.46, -101.42), "B", TAG++));
        locations.add(new CustomLocation(new LatLng(39.76, -104.99), "C", TAG++));
        locations.add(new CustomLocation(new LatLng(36.12, -115.31), "D", TAG++));
        polygon = new CustomPolygon(locations);

        this.mapRedraw();

        mMap.setOnPolygonClickListener(polygon -> {
            if (this.polygon != null) {
                if (this.polygon.isDistanceMarkerShown()) {
                    this.polygon.getDistanceMarker().hideInfoWindow();
                    this.polygon.setDistanceMarkerShown(false);
                    this.selectedPolygon = null;
                } else {
                    this.polygon.getDistanceMarker().showInfoWindow();
                    this.polygon.setDistanceMarkerShown(true);
                    selectedType = SelectedShapeType.POLYGON;
                    this.selectedPolygon = polygon;
                }
                if (selectedPolygon != null) {
                    floatingActionButton.show();
                } else {
                    floatingActionButton.hide();
                }
            }
        });
        mMap.setOnPolylineClickListener(polyline -> {
            try {
                int index = (int) polyline.getTag();
                for (int i = 0; i < lines.size(); i++) {
                    CustomPolyline customPolyline = lines.get(i);
                    if (customPolyline.getTAG() == index) {
                        if (customPolyline.isDistanceMarkerShown()) {
                            customPolyline.getDistanceMarker().hideInfoWindow();
                            customPolyline.setDistanceMarkerShown(false);
                            selectedPolyline = null;
                        } else {
                            customPolyline.getDistanceMarker().showInfoWindow();
                            customPolyline.setDistanceMarkerShown(true);
                            selectedPolyline = polyline;
                            selectedType = SelectedShapeType.POLYLINE;
                        }
                        if (selectedPolyline != null) {
                            floatingActionButton.show();
                        } else {
                            floatingActionButton.hide();
                        }
                    }
                }
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }

        });

        mMap.setOnMarkerClickListener(marker -> {
            this.selectedMarker = marker;
            selectedType = SelectedShapeType.MARKER;
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
            }
            if (selectedMarker != null) {
                floatingActionButton.show();
            } else {
                floatingActionButton.hide();
            }
            return true;
        });

        mMap.setOnMapClickListener(latLng -> {
            locations.add(new CustomLocation(new LatLng(latLng.latitude, latLng.longitude), null, TAG++));
            this.mapRedraw();
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                int index = -1;
                for (int i = 0; i < locations.size(); i++) {
                    CustomLocation customLocation = locations.get(i);
                    if (customLocation.compareWith(currentDraggingMarker)) {
                        index = i;
                    }
                }
                if (index != -1) {
                    locations.get(index).remove();
                    locations.remove(index);
                    mapRedraw();
                }
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
                currentDraggingMarker = marker;
            }
        });
    }

}