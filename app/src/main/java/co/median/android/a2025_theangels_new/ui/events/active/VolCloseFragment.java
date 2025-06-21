// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.active;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.ui.home.HomeActivity;
import co.median.android.a2025_theangels_new.data.services.EventDataManager;
import com.google.firebase.firestore.FieldValue;

// =======================================
// VolCloseFragment - Allows the volunteer to close the event with a reason
// =======================================
public class VolCloseFragment extends Fragment {

    // =======================================
    // VARIABLES
    // =======================================
    private static final String ARG_EVENT_ID = "eventId";
    private Button btnCloseEvent;
    private String selectedReason = null;
    private String[] closeReasons;
    private String eventId;

    public static VolCloseFragment newInstance(String eventId) {
        VolCloseFragment f = new VolCloseFragment();
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
    // onCreateView - Inflates layout for closing event UI
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vol_close, container, false);
    }

    // =======================================
    // onViewCreated - Initializes button and listeners
    // =======================================
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCloseEvent = view.findViewById(R.id.btnCloseEvent);

        // Load reasons from string-array resource
        closeReasons = getResources().getStringArray(R.array.close_event_reasons);

        btnCloseEvent.setOnClickListener(v -> showCloseEventDialog());
    }

    // =======================================
    // showCloseEventDialog - Opens dialog to select reason and confirms closing
    // =======================================
    private void showCloseEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.close_event_dialog_title));

        builder.setSingleChoiceItems(closeReasons, -1, (dialog, which) -> {
            selectedReason = closeReasons[which];
        });

        builder.setPositiveButton(getString(R.string.close_event_confirm), (dialog, which) -> {
            if (selectedReason != null) {
                if (eventId != null) {
                    java.util.Map<String, Object> updates = new java.util.HashMap<>();
                    updates.put("eventCloseReason", selectedReason);
                    updates.put("eventStatus", getString(R.string.status_event_finished));
                    updates.put("eventTimeEnded", FieldValue.serverTimestamp());
                    EventDataManager.updateEvent(eventId, updates, this::navigateToHome, null);
                } else {
                    navigateToHome();
                }
            }
        });

        builder.setNegativeButton(getString(R.string.close_event_cancel), null);
        builder.show();
    }

    // =======================================
    // navigateToHome - Navigates back to the HomeActivity and clears stack
    // =======================================
    private void navigateToHome() {
        Intent intent = new Intent(requireActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
