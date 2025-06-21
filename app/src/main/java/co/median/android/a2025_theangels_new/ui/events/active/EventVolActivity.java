// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.active;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.shuhart.stepview.StepView;
import java.util.Arrays;
import java.util.List;
import android.os.Handler;


import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.map.StaticMapFragment;
import co.median.android.a2025_theangels_new.data.models.Event;
import co.median.android.a2025_theangels_new.data.services.EventDataManager;
import com.google.firebase.firestore.ListenerRegistration;
import co.median.android.a2025_theangels_new.ui.events.active.VolClaimFragment;
import co.median.android.a2025_theangels_new.ui.events.active.VolStatusFragment;
import co.median.android.a2025_theangels_new.ui.events.active.VolCloseFragment;
import co.median.android.a2025_theangels_new.ui.main.BaseActivity;
// =======================================
// EventVolActivity - Handles the volunteer flow during an active event
// =======================================
public class EventVolActivity extends BaseActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private StepView stepView;
    private TextView timerTextView;
    private Button nextStepButton;
    private boolean isRunning = true;
    private int seconds = 0;
    private Handler handler = new Handler();
    private FrameLayout mapContainer;
    private int currentStep = 0;

    private List<Fragment> stepFragments = Arrays.asList(
            new VolClaimFragment(),
            new VolStatusFragment(),
            new VolCloseFragment()
    );
    private ListenerRegistration eventListener;

    // =======================================
    // onCreate - Initializes volunteer event screen and step flow
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(true);

        // Bind views
        stepView = findViewById(R.id.step_view);
        nextStepButton = findViewById(R.id.nextStepButton);
        timerTextView = findViewById(R.id.timerTextView);
        mapContainer = findViewById(R.id.map_container);

        startTimer();
        setupStepView();
        setupMap();
        loadStepFragment(0);

        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            eventListener = EventDataManager.listenToEvent(eventId, (snapshot, e) -> {
                if (e == null && snapshot != null && snapshot.exists()) {
                    Event event = snapshot.toObject(Event.class);
                    if (event != null && event.getEventStatus() != null) {
                        int index = statuses.indexOf(event.getEventStatus());
                        if (index >= 0) updateStep(index);
                    }
                }
            });
        }

        // Step progression
        nextStepButton.setOnClickListener(v -> {
            if (currentStep < stepFragments.size() - 1) {
                currentStep++;
                updateStep(currentStep);
            }
        });
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_vol;
    }

    // =======================================
    // setupMap - Initializes static map fragment with event location
    // =======================================
    private void setupMap() {
        double eventLat = 31.8912;
        double eventLng = 34.8115;

        StaticMapFragment mapFragment = StaticMapFragment.newInstance(eventLat, eventLng);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment);
        transaction.commit();
    }

    // =======================================
    // setupStepView - Sets up step titles in StepView
    // =======================================
    private void setupStepView() {
        stepView.setSteps(Arrays.asList(
                getString(R.string.step_vol_claim),
                getString(R.string.step_vol_status),
                getString(R.string.step_vol_close)
        ));
        stepView.go(0, true);
    }

    // =======================================
    // updateStep - Updates StepView and replaces fragment according to current step
    // =======================================
    private void updateStep(int step) {
        if (stepView != null) {
            stepView.go(step, true);
            loadStepFragment(step);
        }
    }

    // =======================================
    // loadStepFragment - Loads fragment that corresponds to the current step
    // =======================================
    private void loadStepFragment(int step) {
        Fragment fragment = stepFragments.get(step);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    // =======================================
    // startTimer - Starts real-time timer for event duration
    // =======================================
    private void startTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = seconds / 60;
                int secs = seconds % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, secs);
                timerTextView.setText(timeFormatted);

                if (isRunning) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventListener != null) {
            eventListener.remove();
        }
    }
}