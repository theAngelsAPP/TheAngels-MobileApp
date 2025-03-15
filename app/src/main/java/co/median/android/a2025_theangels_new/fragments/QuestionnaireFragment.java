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
import androidx.fragment.app.Fragment;
import co.median.android.a2025_theangels_new.R;

public class QuestionnaireFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRadioGroup(view, R.id.rgSafety, true);
        setupRadioGroup(view, R.id.rgPulse, false);
        setupRadioGroup(view, R.id.rgBreathing, false);
        setupRadioGroup(view, R.id.rgBleeding, false);
    }

    private void setupRadioGroup(View view, int radioGroupId, boolean isSafetyQuestion) {
        RadioGroup radioGroup = view.findViewById(radioGroupId);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                ((RadioButton) group.getChildAt(i)).setTextColor(Color.BLACK);
                ((RadioButton) group.getChildAt(i)).setButtonTintList(null);
            }

            RadioButton selectedButton = view.findViewById(checkedId);
            if (selectedButton.getId() % 2 == 0) {
                selectedButton.setTextColor(Color.GREEN);
                selectedButton.setButtonTintList(getContext().getColorStateList(android.R.color.holo_green_dark));
            } else {
                selectedButton.setTextColor(Color.RED);
                selectedButton.setButtonTintList(getContext().getColorStateList(android.R.color.holo_red_dark));
            }

            if (isSafetyQuestion && selectedButton.getId() == R.id.rbSafetyNo) {
                Toast.makeText(getContext(), "יש להעביר את הבן אדם למקום בטוח!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
