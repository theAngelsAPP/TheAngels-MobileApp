package co.median.android.a2025_theangels_new.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import co.median.android.a2025_theangels_new.R;

public class WhatHappenedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_what_happened, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView btnUnconscious = view.findViewById(R.id.btnUnconscious);
        TextView btnAllergy = view.findViewById(R.id.btnAllergy);
        TextView btnChoking = view.findViewById(R.id.btnChoking);
        TextView btnOther = view.findViewById(R.id.btnOther);
        EditText etFreeText = view.findViewById(R.id.etFreeText);

        View.OnClickListener clickListener = v -> {
            // כרגע אין פעולה, נוסיף בהמשך אם צריך
        };

        btnUnconscious.setOnClickListener(clickListener);
        btnAllergy.setOnClickListener(clickListener);
        btnChoking.setOnClickListener(clickListener);
        btnOther.setOnClickListener(clickListener);
    }
}
