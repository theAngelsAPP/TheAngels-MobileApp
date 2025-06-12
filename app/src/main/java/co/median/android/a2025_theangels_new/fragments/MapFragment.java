// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import co.median.android.a2025_theangels_new.R;

// =======================================
// MapFragment - Displays Google Map with current user location
// =======================================
public class MapFragment extends Fragment implements OnMapReadyCallback {

    // =======================================
    // VARIABLES
    // =======================================
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LinearLayout mapPlaceholder;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    // Request permission result handler
    private final ActivityResultLauncher<String> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    enableUserLocation();
                } else {
                    showPlaceholder();
                }
            });

    // =======================================
    // Constructor with layout binding
    // =======================================
    public MapFragment() {
        super(R.layout.fragment_map);
    }

    // =======================================
    // onViewCreated - Initializes map and views
    // =======================================
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mapPlaceholder = view.findViewById(R.id.map_placeholder);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    updateMapLocation(location);
                }
            }
        };

        // Request permission when the placeholder is tapped
        mapPlaceholder.setOnClickListener(v ->
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // =======================================
    // onMapReady - Called when map is ready to use
    // =======================================
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        applyCustomMapStyle();
        checkLocationPermission();
    }

    // =======================================
    // applyCustomMapStyle - Applies custom map styling from raw/map_style.json
    // =======================================
    private void applyCustomMapStyle() {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
            if (!success) {
                System.out.println("Error applying map style.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =======================================
    // checkLocationPermission - Checks and handles location permission
    // =======================================
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
            showPlaceholder();
        }
    }

    // =======================================
    // enableUserLocation - Enables MyLocation and requests user position
    // =======================================
    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            View mapView = requireView().findViewById(R.id.map);
            mapView.setVisibility(View.VISIBLE);
            mapPlaceholder.setVisibility(View.GONE);

            mMap.setMyLocationEnabled(true);
            startLocationUpdates();
        }
    }

    // =======================================
    // showPlaceholder - Shows fallback UI when location permission is denied
    // =======================================
    private void showPlaceholder() {
        View mapView = requireView().findViewById(R.id.map);
        mapView.setVisibility(View.GONE);
        mapPlaceholder.setVisibility(View.VISIBLE);
    }

    // =======================================
    // getUserLocation - Requests the user's current location
    // =======================================
    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        updateMapLocation(location);
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        getUserLocation();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    // =======================================
    // updateMapLocation - Centers camera and adds marker at user location
    // =======================================
    private void updateMapLocation(Location location) {
        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLatLng)
                .zoom(15)
                .tilt(30)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.clear();

        mMap.addMarker(new MarkerOptions()
                .position(userLatLng)
                .title(getString(R.string.your_location))
                .icon(resizeMarker(R.drawable.custom_marker, 130, 130)));
    }

    // =======================================
    // resizeMarker - Resizes custom marker drawable
    // =======================================
    private BitmapDescriptor resizeMarker(int drawableRes, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(requireContext(), drawableRes);
        if (drawable == null) return null;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
