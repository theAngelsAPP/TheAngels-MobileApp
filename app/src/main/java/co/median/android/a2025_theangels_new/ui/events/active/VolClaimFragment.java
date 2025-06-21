// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.active;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.models.Event;
import co.median.android.a2025_theangels_new.data.services.EventDataManager;

// =======================================
// VolClaimFragment - Fragment for the volunteer claim stage
// =======================================
public class VolClaimFragment extends Fragment {

    private static final String ARG_EVENT_ID = "eventId";
    private String eventId;

    public static VolClaimFragment newInstance(String eventId) {
        VolClaimFragment frag = new VolClaimFragment();
        Bundle b = new Bundle();
        b.putString(ARG_EVENT_ID, eventId);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_EVENT_ID);
        }
    }

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnClaim = view.findViewById(R.id.btnClaimEvent);
        Button btnNotInterested = view.findViewById(R.id.btnNotInterested);

        if (btnClaim != null) {
            btnClaim.setOnClickListener(v -> attemptClaimEvent());
        }
        if (btnNotInterested != null) {
            btnNotInterested.setOnClickListener(v -> requireActivity().finish());
        }
    }

    private void attemptClaimEvent() {
        if (eventId == null) return;
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (uid == null) return;

        EventDataManager.claimEvent(eventId, uid, () -> {
            AlertDialog.Builder b = new AlertDialog.Builder(requireContext());
            b.setMessage(R.string.event_claimed).setPositiveButton(R.string.ok_button, (d, w) -> d.dismiss()).show();
            if (getActivity() instanceof EventVolActivity) {
                ((EventVolActivity) getActivity()).advanceToStep(1);
            }
        }, e -> {
            AlertDialog.Builder b = new AlertDialog.Builder(requireContext());
            b.setMessage(R.string.error_title).setPositiveButton(R.string.ok_button, null).show();
        });
    }
}
