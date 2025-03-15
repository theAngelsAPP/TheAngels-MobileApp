package co.median.android.a2025_theangels_new.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import co.median.android.a2025_theangels_new.R;

public class WhatHappenedFragment extends Fragment {

    private MaterialButton selectedButton = null;
    private static final int SELECTED_COLOR = 0xFFE9C46A; // צבע נבחר
    private static final int DEFAULT_COLOR = 0xFFDCDCDC; // צבע ברירת מחדל

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_what_happened, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnUnconscious = view.findViewById(R.id.btnUnconscious);
        MaterialButton btnAllergy = view.findViewById(R.id.btnAllergy);
        MaterialButton btnChoking = view.findViewById(R.id.btnChoking);
        MaterialButton btnOther = view.findViewById(R.id.btnOther);
        EditText etFreeText = view.findViewById(R.id.etFreeText);

        // ברירת מחדל - הסתרת שדה הטקסט
        etFreeText.setVisibility(View.GONE);

        View.OnClickListener clickListener = v -> {
            MaterialButton clickedButton = (MaterialButton) v;

            // אם יש כבר כפתור שנבחר → מחזירים אותו לצבע הרגיל
            if (selectedButton != null) {
                selectedButton.setBackgroundColor(DEFAULT_COLOR);
            }

            // מסמנים את הכפתור הנוכחי שנבחר
            clickedButton.setBackgroundColor(SELECTED_COLOR);
            selectedButton = clickedButton;

            // הצגת שדה טקסט רק אם "אחר" נבחר
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
