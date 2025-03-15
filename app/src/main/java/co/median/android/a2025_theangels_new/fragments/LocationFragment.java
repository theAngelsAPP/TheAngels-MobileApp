package co.median.android.a2025_theangels_new.fragments;

import android.location.Geocoder;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import co.median.android.a2025_theangels_new.R;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextInputEditText etManualAddress;
    private Button btnManualLocation;
    private final LatLng DEFAULT_LOCATION = new LatLng(32.0853, 34.7818); // ת"א כברירת מחדל

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etManualAddress = view.findViewById(R.id.etManualAddress);
        btnManualLocation = view.findViewById(R.id.btnManualLocation);

        // אתחול המפה מיד
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_container);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // הצגת/הסתרת שדה הזנת כתובת ידנית
        btnManualLocation.setOnClickListener(v -> {
            if (etManualAddress.getVisibility() == View.GONE) {
                etManualAddress.setVisibility(View.VISIBLE);
            } else {
                etManualAddress.setVisibility(View.GONE);
            }
        });

        // עדכון המפה לפי כתובת ידנית
        etManualAddress.setOnEditorActionListener((v, actionId, event) -> {
            String address = etManualAddress.getText().toString();
            updateMapLocationFromAddress(address);
            return false;
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true); // הוספת שליטה בזום
        mMap.getUiSettings().setCompassEnabled(true); // הוספת מצפן

        // הצגת מיקום ברירת מחדל גם אם אין הרשאות
        updateMapMarker(DEFAULT_LOCATION, "מיקום ברירת מחדל - תל אביב");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 14));
    }

    private void updateMapLocationFromAddress(String addressStr) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(addressStr, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                updateMapMarker(latLng, address.getAddressLine(0));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateMapMarker(LatLng latLng, String title) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(title));
    }
}
