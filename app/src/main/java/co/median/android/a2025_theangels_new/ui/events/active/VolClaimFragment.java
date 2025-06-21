// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.active;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.median.android.a2025_theangels_new.R;

// =======================================
// VolClaimFragment - Fragment for the volunteer claim stage
// =======================================
public class VolClaimFragment extends Fragment {

    // =======================================
    // onCreateView - Inflates the layout for volunteer claim UI
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vol_claim, container, false);
    }
}
