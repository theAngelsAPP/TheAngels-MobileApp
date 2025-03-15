package co.median.android.a2025_theangels_new.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import co.median.android.a2025_theangels_new.R;

public class EventTypeFragment extends Fragment {

    private MaterialButton btnMedical, btnSecurity, btnCar, btnAnimals;
    private MaterialButton selectedButton = null;

    private final int DEFAULT_COLOR = 0xFFDCDCDC; // אפור דיפולט
    private final int MEDICAL_COLOR = 0xFFE95E50; // אדום רפואי
    private final int SECURITY_COLOR = 0xFFFAC723; // צהוב ביטחוני
    private final int CAR_COLOR = 0xFF0CB2AF; // טורקיז רכב
    private final int ANIMALS_COLOR = 0xFFA1C65D; // ירוק בעלי חיים

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_type, container, false);

        btnMedical = view.findViewById(R.id.btnMedical);
        btnSecurity = view.findViewById(R.id.btnSecurity);
        btnCar = view.findViewById(R.id.btnCar);
        btnAnimals = view.findViewById(R.id.btnAnimals);

        setupButtonClickListener(btnMedical, MEDICAL_COLOR);
        setupButtonClickListener(btnSecurity, SECURITY_COLOR);
        setupButtonClickListener(btnCar, CAR_COLOR);
        setupButtonClickListener(btnAnimals, ANIMALS_COLOR);

        return view;
    }

    private void setupButtonClickListener(MaterialButton button, int selectedColor) {
        button.setOnClickListener(v -> {
            if (selectedButton != null) {
                selectedButton.setBackgroundColor(DEFAULT_COLOR);
            }
            button.setBackgroundColor(selectedColor);
            selectedButton = button;
        });
    }
}
