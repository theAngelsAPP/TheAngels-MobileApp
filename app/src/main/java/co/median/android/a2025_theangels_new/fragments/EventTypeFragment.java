package co.median.android.a2025_theangels_new.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import co.median.android.a2025_theangels_new.R;

public class EventTypeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView btnMedical = view.findViewById(R.id.btnMedical);
        TextView btnSecurity = view.findViewById(R.id.btnSecurity);
        TextView btnCar = view.findViewById(R.id.btnCar);
        TextView btnAnimals = view.findViewById(R.id.btnAnimals);

        View.OnClickListener clickListener = v -> {
            // כרגע אין פעולה, נוסיף בהמשך אם צריך
        };

        btnMedical.setOnClickListener(clickListener);
        btnSecurity.setOnClickListener(clickListener);
        btnCar.setOnClickListener(clickListener);
        btnAnimals.setOnClickListener(clickListener);
    }
}
