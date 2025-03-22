// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import co.median.android.a2025_theangels_new.R;

// =======================================
// QuestionnaireFragment - Handles questionnaire logic for incident state
// =======================================
public class QuestionnaireFragment extends Fragment {

    // =======================================
    // onCreateView - Inflates the questionnaire layout
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false);
    }

    // =======================================
    // onViewCreated - Initializes radio groups and listeners
    // =======================================
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRadioGroup(view, R.id.rgSafety, true);
        setupRadioGroup(view, R.id.rgPulse, false);
        setupRadioGroup(view, R.id.rgBreathing, false);
        setupRadioGroup(view, R.id.rgBleeding, false);
    }

    // =======================================
    // setupRadioGroup - Sets up radio button behavior and conditional styling
    // =======================================
    private void setupRadioGroup(View view, int radioGroupId, boolean isSafetyQuestion) {
        RadioGroup radioGroup = view.findViewById(radioGroupId);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton rb = (RadioButton) group.getChildAt(i);
                rb.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                rb.setButtonTintList(null);
            }

            RadioButton selectedButton = view.findViewById(checkedId);

            if (selectedButton.getId() % 2 == 0) {
                selectedButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.questionnaire_red));
                selectedButton.setButtonTintList(ContextCompat.getColorStateList(requireContext(), R.color.questionnaire_red));
            } else {
                selectedButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.questionnaire_green));
                selectedButton.setButtonTintList(ContextCompat.getColorStateList(requireContext(), R.color.questionnaire_green));
            }

            if (isSafetyQuestion && selectedButton.getId() == R.id.rbSafetyNo) {
                Toast.makeText(getContext(), getString(R.string.safety_warning), Toast.LENGTH_LONG).show();
            }
        });
    }
}
