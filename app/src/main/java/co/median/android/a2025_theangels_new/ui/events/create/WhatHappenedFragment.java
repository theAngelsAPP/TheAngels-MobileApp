// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import co.median.android.a2025_theangels_new.R;

// =======================================
// WhatHappenedFragment - Handles incident type selection with optional free text
// =======================================
public class WhatHappenedFragment extends Fragment {

    // =======================================
    // VARIABLES
    // =======================================
    private MaterialButton selectedButton = null;

    // =======================================
    // onCreateView - Inflates layout for the fragment
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_what_happened, container, false);
    }

    // =======================================
    // onViewCreated - Sets up buttons and free text visibility logic
    // =======================================
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnUnconscious = view.findViewById(R.id.btnUnconscious);
        MaterialButton btnAllergy = view.findViewById(R.id.btnAllergy);
        MaterialButton btnChoking = view.findViewById(R.id.btnChoking);
        MaterialButton btnOther = view.findViewById(R.id.btnOther);
        EditText etFreeText = view.findViewById(R.id.etFreeText);

        // Default: hide free text field
        etFreeText.setVisibility(View.GONE);

        // Handle button selection
        View.OnClickListener clickListener = v -> {
            MaterialButton clickedButton = (MaterialButton) v;

            // Reset previously selected button
            if (selectedButton != null) {
                selectedButton.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.incident_option_default)
                );
            }

            // Highlight the newly selected button
            clickedButton.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.incident_option_selected)
            );
            selectedButton = clickedButton;

            // Show free text input only when "Other" is selected
            if (clickedButton.getId() == R.id.btnOther) {
                etFreeText.setVisibility(View.VISIBLE);
            } else {
                etFreeText.setVisibility(View.GONE);
            }
        };

        btnUnconscious.setOnClickListener(clickListener);
        btnAllergy.setOnClickListener(clickListener);
        btnChoking.setOnClickListener(clickListener);
        btnOther.setOnClickListener(clickListener);
    }
}
