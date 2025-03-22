// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import co.median.android.a2025_theangels_new.R;

// =======================================
// LocationFragment - Displays a map with optional manual address input
// =======================================
public class LocationFragment extends Fragment {

    // =======================================
    // VARIABLES
    // =======================================
    private TextInputEditText etManualAddress;
    private Button btnManualLocation;

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

        // Bind views
        etManualAddress = view.findViewById(R.id.etManualAddress);
        btnManualLocation = view.findViewById(R.id.btnManualLocation);

        // Static coordinates for demo (can be dynamic from server)
        double eventLat = 31.8912;
        double eventLng = 34.8115;

        // Load StaticMapFragment into container
        StaticMapFragment mapFragment = StaticMapFragment.newInstance(eventLat, eventLng);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment);
        transaction.commit();

        // Toggle manual address input visibility
        btnManualLocation.setOnClickListener(v -> {
            if (etManualAddress.getVisibility() == View.GONE) {
                etManualAddress.setVisibility(View.VISIBLE);
            } else {
                etManualAddress.setVisibility(View.GONE);
            }
        });
    }
}
