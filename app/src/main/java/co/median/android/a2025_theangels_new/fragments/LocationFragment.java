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

public class LocationFragment extends Fragment {

    private TextInputEditText etManualAddress;
    private Button btnManualLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etManualAddress = view.findViewById(R.id.etManualAddress);
        btnManualLocation = view.findViewById(R.id.btnManualLocation);

        // נתוני מיקום לדוגמה (יכול לבוא משרת בעתיד)
        double eventLat = 31.8912;
        double eventLng = 34.8115;

        // החלפת המפה הסטטית בתוך ה-FrameLayout
        StaticMapFragment mapFragment = StaticMapFragment.newInstance(eventLat, eventLng);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment);
        transaction.commit();

        // הצגת/הסתרת שדה הזנת כתובת ידנית
        btnManualLocation.setOnClickListener(v -> {
            if (etManualAddress.getVisibility() == View.GONE) {
                etManualAddress.setVisibility(View.VISIBLE);
            } else {
                etManualAddress.setVisibility(View.GONE);
            }
        });
    }
}
