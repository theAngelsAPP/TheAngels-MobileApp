// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.active;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.map.MapHelper;
import co.median.android.a2025_theangels_new.data.services.EventDataManager;

// =======================================
// VolStatusFragment - Displays volunteer's event progress/status
// =======================================
public class VolStatusFragment extends Fragment {

    private static final String ARG_EVENT_ID = "eventId";
    private String eventId;

    public static VolStatusFragment newInstance(String eventId) {
        VolStatusFragment f = new VolStatusFragment();
        Bundle b = new Bundle();
        b.putString(ARG_EVENT_ID, eventId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_EVENT_ID);
        }
    }

    // =======================================
    // onCreateView - Inflates the layout for volunteer status UI
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vol_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCall = view.findViewById(R.id.btnCall);
        Button btnNavigate = view.findViewById(R.id.btnNavigate);
        Button btnCancel = view.findViewById(R.id.btnCancelEvent);
        Button btnArrived = view.findViewById(R.id.btnArrived);

        if (btnCall != null) {
            btnCall.setOnClickListener(v -> callUser());
        }
        if (btnNavigate != null) {
            btnNavigate.setOnClickListener(v -> navigateToEvent());
        }
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> updateStatus(getString(R.string.status_event_finished)));
        }
        if (btnArrived != null) {
            btnArrived.setOnClickListener(v -> updateStatus(getString(R.string.status_volunteer_arrived)));
        }
    }

    private void callUser() {
        if (getActivity() == null) return;
        // Phone number should be fetched from event or user, placeholder below
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
        startActivity(intent);
    }

    private void navigateToEvent() {
        // Without actual coordinates we just open google maps
        MapHelper.openNavigation(requireContext(), 0.0, 0.0);
    }

    private void updateStatus(String status) {
        if (eventId == null) return;
        EventDataManager.updateEventStatus(eventId, status, null, null);
    }
}
