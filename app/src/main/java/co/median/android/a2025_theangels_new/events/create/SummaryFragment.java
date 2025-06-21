// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.events.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.median.android.a2025_theangels_new.R;

// =======================================
// SummaryFragment - Final summary step in event creation
// =======================================
public class SummaryFragment extends Fragment {

    // =======================================
    // onCreateView - Inflates the summary fragment layout
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    // =======================================
    // onViewCreated - Called after the view is created
    // Use this to populate the summary from ViewModel or arguments if needed
    // =======================================
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Load summary data here if needed in the future
    }
}
