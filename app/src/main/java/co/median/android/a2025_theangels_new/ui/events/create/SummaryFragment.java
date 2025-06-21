// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
        NewEventViewModel viewModel = new ViewModelProvider(requireActivity()).get(NewEventViewModel.class);

        android.widget.TextView tvType = view.findViewById(R.id.tvSummaryType);
        android.widget.TextView tvWhat = view.findViewById(R.id.tvSummaryWhat);
        android.widget.TextView tvLocation = view.findViewById(R.id.tvSummaryLocation);
        android.widget.LinearLayout llForm = view.findViewById(R.id.llSummaryForm);

        if (tvType != null && viewModel.getEventType() != null) {
            tvType.setText(getString(R.string.event_type_label, viewModel.getEventType()));
        }
        if (tvWhat != null && viewModel.getEventQuestionChoice() != null) {
            tvWhat.setText(getString(R.string.what_happened_label, viewModel.getEventQuestionChoice()));
        }
        if (tvLocation != null && viewModel.getEventLocation() != null) {
            tvLocation.setText(getString(R.string.location_label,
                    viewModel.getEventLocation().getLatitude(),
                    viewModel.getEventLocation().getLongitude()));
        }

        if (llForm != null) {
            llForm.removeViews(1, Math.max(0, llForm.getChildCount() - 1));
            for (java.util.Map.Entry<String, Boolean> entry : viewModel.getEventForm().entrySet()) {
                android.widget.LinearLayout row = new android.widget.LinearLayout(requireContext());
                row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
                android.widget.TextView q = new android.widget.TextView(requireContext());
                q.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                q.setText(entry.getKey());
                android.widget.TextView a = new android.widget.TextView(requireContext());
                a.setText(entry.getValue() ? getString(R.string.yes) : getString(R.string.no));
                int color = entry.getValue() ? android.graphics.Color.parseColor("#2E7D32") : android.graphics.Color.parseColor("#D32F2F");
                a.setTextColor(color);
                row.addView(q);
                row.addView(a);
                llForm.addView(row);
            }
        }
    }
}
