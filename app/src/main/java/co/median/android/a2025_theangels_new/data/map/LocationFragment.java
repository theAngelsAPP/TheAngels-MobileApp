// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.data.map;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

import co.median.android.a2025_theangels_new.R;

// =======================================
// LocationFragment - Displays a map with optional manual address input
// =======================================
public class LocationFragment extends Fragment implements OnMapReadyCallback {

    // =======================================
    // VARIABLES
    // =======================================
    private GoogleMap mMap;
    private LocationService locationService;
    private Marker locationMarker;
    private LinearLayout locationBox;
    private TextView tvAddress;
    private TextInputEditText etManualAddress;
    private Button btnManualLocation;
    private boolean manualMode = false;
    private LatLng manualLatLng;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showMap();
                } else {
                    showPermissionBox();
                }
            });

    // =======================================
    // onCreateView - Inflates the layout for the fragment
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    // =======================================
    // onViewCreated - Initializes map and manual location logic
    // =======================================
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etManualAddress = view.findViewById(R.id.etManualAddress);
        btnManualLocation = view.findViewById(R.id.btnManualLocation);
        locationBox = view.findViewById(R.id.location_box);
        tvAddress = view.findViewById(R.id.tv_current_address);
        locationService = new LocationService(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnManualLocation.setOnClickListener(v -> {
            if (etManualAddress.getVisibility() == View.GONE) {
                etManualAddress.setVisibility(View.VISIBLE);
            } else {
                etManualAddress.setVisibility(View.GONE);
            }
        });

        etManualAddress.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleManualAddress();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        applyCustomStyle();
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            showMap();
        } else {
            showPermissionBox();
        }
    }

    private void showMap() {
        View mapView = requireView().findViewById(R.id.map);
        mapView.setVisibility(View.VISIBLE);
        locationBox.setVisibility(View.GONE);
        if (!manualMode) {
            startLocationUpdates();
        }
    }

    private void showPermissionBox() {
        View mapView = requireView().findViewById(R.id.map);
        mapView.setVisibility(View.GONE);
        locationBox.setVisibility(View.VISIBLE);
        tvAddress.setText(getString(R.string.address_not_found));
    }

    private void startLocationUpdates() {
        locationService.getCurrentLocation(new LocationService.SimpleLocationListener() {
            @Override
            public void onLocation(android.location.Location location) {
                if (!manualMode) {
                    updateLiveLocation(location);
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });

        locationService.startLocationUpdates(new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                android.location.Location location = result.getLastLocation();
                if (location != null && !manualMode) {
                    updateLiveLocation(location);
                }
            }
        });
    }

    private void stopLocationUpdates() {
        locationService.stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void updateLiveLocation(android.location.Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        if (locationMarker == null) {
            locationMarker = MapHelper.addMarker(mMap, pos, getString(R.string.your_location));
        } else {
            locationMarker.setPosition(pos);
        }
        MapHelper.moveCamera(mMap, pos, 15f);

        String address = AddressHelper.getAddressFromLatLng(requireContext(), pos.latitude, pos.longitude);
        if (address == null) address = getString(R.string.address_not_found);
        tvAddress.setText(address);
    }

    private void updateManualLocation(LatLng pos, String address) {
        manualLatLng = pos;
        manualMode = true;
        stopLocationUpdates();
        if (locationMarker == null) {
            locationMarker = MapHelper.addMarker(mMap, pos, getString(R.string.your_location));
        } else {
            locationMarker.setPosition(pos);
        }
        MapHelper.moveCamera(mMap, pos, 15f);
        tvAddress.setText(address);
    }

    private void handleManualAddress() {
        String input = etManualAddress.getText() != null ? etManualAddress.getText().toString().trim() : "";
        if (input.isEmpty()) return;

        android.location.Geocoder geocoder = new android.location.Geocoder(requireContext(), new Locale("he"));
        try {
            java.util.List<android.location.Address> list = geocoder.getFromLocationName(input, 1);
            if (list != null && !list.isEmpty()) {
                android.location.Address addr = list.get(0);
                if ("IL".equalsIgnoreCase(addr.getCountryCode())) {
                    LatLng pos = new LatLng(addr.getLatitude(), addr.getLongitude());
                    updateManualLocation(pos, addr.getAddressLine(0));
                    etManualAddress.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), R.string.location_updated, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(requireContext(), R.string.invalid_israel_address, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(requireContext(), R.string.address_not_found, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.address_lookup_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void applyCustomStyle() {
        try {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
        } catch (Exception ignored) {
        }
    }
}
