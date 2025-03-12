package co.median.android.a2025_theangels_new.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

        RadioGroup rgPulse = view.findViewById(R.id.rgPulse);
        RadioGroup rgBreathing = view.findViewById(R.id.rgBreathing);
        RadioGroup rgSafety = view.findViewById(R.id.rgSafety);
        RadioGroup rgBleeding = view.findViewById(R.id.rgBleeding);

        View.OnClickListener listener = v -> {
            RadioButton selectedButton = (RadioButton) v;
            // כאן אפשר להוסיף לוגיקה לטיפול בתשובות
        };

        rgPulse.setOnCheckedChangeListener((group, checkedId) -> listener.onClick(view.findViewById(checkedId)));
        rgBreathing.setOnCheckedChangeListener((group, checkedId) -> listener.onClick(view.findViewById(checkedId)));
        rgSafety.setOnCheckedChangeListener((group, checkedId) -> listener.onClick(view.findViewById(checkedId)));
        rgBleeding.setOnCheckedChangeListener((group, checkedId) -> listener.onClick(view.findViewById(checkedId)));
    }
}
