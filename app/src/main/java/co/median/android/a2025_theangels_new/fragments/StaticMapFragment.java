package co.median.android.a2025_theangels_new.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import co.median.android.a2025_theangels_new.R;

public class StaticMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lng";

    public StaticMapFragment() {
        super(R.layout.fragment_static_map);
    }

    public static StaticMapFragment newInstance(double lat, double lng) {
        StaticMapFragment fragment = new StaticMapFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LAT, lat);
        args.putDouble(ARG_LNG, lng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (getArguments() != null) {
            double lat = getArguments().getDouble(ARG_LAT);
            double lng = getArguments().getDouble(ARG_LNG);
            LatLng eventLocation = new LatLng(lat, lng);

            mMap.addMarker(new MarkerOptions().position(eventLocation).title("מיקום האירוע"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15));
        }
    }
}
