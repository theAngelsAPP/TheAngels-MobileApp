// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.events.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import co.median.android.a2025_theangels_new.R;

// =======================================
// EventTypeFragment - Allows user to select the type of event from predefined categories
// =======================================
public class EventTypeFragment extends Fragment {

    // =======================================
    // VARIABLES
    // =======================================
    private MaterialButton btnMedical, btnSecurity, btnCar, btnAnimals;
    private MaterialButton selectedButton = null;

    // =======================================
    // onCreateView - Inflates layout and sets up event type buttons
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_type, container, false);

        // Bind buttons
        btnMedical = view.findViewById(R.id.btnMedical);
        btnSecurity = view.findViewById(R.id.btnSecurity);
        btnCar = view.findViewById(R.id.btnCar);
        btnAnimals = view.findViewById(R.id.btnAnimals);

        // Set listeners for each button with respective color
        setupButtonClickListener(btnMedical, R.color.medical_event_color);
        setupButtonClickListener(btnSecurity, R.color.security_event_color);
        setupButtonClickListener(btnCar, R.color.car_event_color);
        setupButtonClickListener(btnAnimals, R.color.animal_event_color);

        return view;
    }

    // =======================================
    // setupButtonClickListener - Handles color change and selection logic
    // =======================================
    private void setupButtonClickListener(MaterialButton button, int selectedColorRes) {
        button.setOnClickListener(v -> {
            if (selectedButton != null) {
                selectedButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.event_default_color));
            }
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), selectedColorRes));
            selectedButton = button;
        });
    }
}
