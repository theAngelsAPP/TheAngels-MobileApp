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
import co.median.android.a2025_theangels_new.data.services.UserDataManager;
import co.median.android.a2025_theangels_new.data.models.Event;

// =======================================
// VolStatusFragment - Displays volunteer's event progress/status
// =======================================
public class VolStatusFragment extends Fragment {

    private static final String ARG_EVENT_ID = "eventId";
    private String eventId;
    private String userPhone = "";
    private double eventLat = 0.0;
    private double eventLng = 0.0;

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
        if (eventId != null) {
            EventDataManager.getEventById(eventId, new EventDataManager.SingleEventCallback() {
                @Override
                public void onEventLoaded(Event event) {
                    if (event != null) {
                        if (event.getEventCreatedBy() != null) {
                            UserDataManager.loadUserDetails(event.getEventCreatedBy(), session -> {
                                if (session != null) {
                                    userPhone = session.getPhone();
                                }
                            });
                        }
                        if (event.getEventLocation() != null) {
                            eventLat = event.getEventLocation().getLatitude();
                            eventLng = event.getEventLocation().getLongitude();
                        }
                    }
                }

                @Override
                public void onError(Exception e) { }
            });
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
        Button btnStreetView = view.findViewById(R.id.btnStreetView);
        Button btnCancel = view.findViewById(R.id.btnCancelEvent);
        Button btnArrived = view.findViewById(R.id.btnArrived);

        if (btnCall != null) {
            btnCall.setOnClickListener(v -> callUser());
        }
        if (btnNavigate != null) {
            btnNavigate.setOnClickListener(v -> navigateToEvent());
        }
        if (btnStreetView != null) {
            btnStreetView.setOnClickListener(v -> openStreetView());
        }
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> showCancelDialog());
        }
        if (btnArrived != null) {
            btnArrived.setOnClickListener(v -> {
                updateStatus(co.median.android.a2025_theangels_new.data.models.UserEventStatus.VOLUNTEER_AT_EVENT.getDbValue());
                if (getActivity() instanceof EventVolActivity) {
                    ((EventVolActivity) getActivity()).advanceToStep(2);
                }
            });
        }
    }

    private void callUser() {
        if (getActivity() == null || userPhone.isEmpty()) return;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + userPhone));
        startActivity(intent);
    }

    private void navigateToEvent() {
        MapHelper.openNavigation(requireContext(), eventLat, eventLng);
    }

    private void openStreetView() {
        MapHelper.openStreetView(requireContext(), eventLat, eventLng);
    }

    private void showCancelDialog() {
        final android.widget.EditText input = new android.widget.EditText(requireContext());
        input.setHint(getString(R.string.close_event_other_hint));
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.close_event_dialog_title))
                .setView(input)
                .setPositiveButton(getString(R.string.close_event_confirm), (d, w) -> {
                    String reason = input.getText().toString().trim();
                    updateStatusWithReason(co.median.android.a2025_theangels_new.data.models.UserEventStatus.EVENT_FINISHED.getDbValue(), reason);
                    if (getActivity() instanceof EventVolActivity) {
                        ((EventVolActivity) getActivity()).advanceToStep(2);
                    }
                })
                .setNegativeButton(getString(R.string.close_event_cancel), null)
                .show();
    }

    private void updateStatus(String status) {
        if (eventId == null) return;
        java.util.Map<String, Object> updates = new java.util.HashMap<>();
        updates.put("eventStatus", status);
        if (status.equals(co.median.android.a2025_theangels_new.data.models.UserEventStatus.EVENT_FINISHED.getDbValue())) {
            updates.put("eventTimeEnded", com.google.firebase.firestore.FieldValue.serverTimestamp());
        }
        EventDataManager.updateEvent(eventId, updates, null,
                e -> android.widget.Toast.makeText(requireContext(), R.string.error_title, android.widget.Toast.LENGTH_SHORT).show());
    }

    private void updateStatusWithReason(String status, String reason) {
        if (eventId == null) return;
        java.util.Map<String, Object> updates = new java.util.HashMap<>();
        updates.put("eventStatus", status);
        updates.put("eventCloseReason", reason);
        updates.put("eventTimeEnded", com.google.firebase.firestore.FieldValue.serverTimestamp());
        EventDataManager.updateEvent(eventId, updates, null,
                e -> android.widget.Toast.makeText(requireContext(), R.string.error_title, android.widget.Toast.LENGTH_SHORT).show());
    }
}
