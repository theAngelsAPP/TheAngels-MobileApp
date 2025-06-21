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
import co.median.android.a2025_theangels_new.data.map.AddressHelper;
import com.google.firebase.firestore.GeoPoint;
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
    private long eventStartMillis = -1L;
    private Handler handler = new Handler();
    private FrameLayout mapContainer;
    private int currentStep = 0;

    private String eventId;
    private GeoPoint eventLocation;

    private List<Fragment> stepFragments;

    private void initFragments() {
        stepFragments = Arrays.asList(
                VolClaimFragment.newInstance(eventId),
                VolStatusFragment.newInstance(eventId),
                VolCloseFragment.newInstance(eventId)
        );
    }
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

        eventId = getIntent().getStringExtra("eventId");

        startTimer();
        setupStepView();
        // map will be updated once event data arrives

        initFragments();
        loadStepFragment(0);

        if (eventId != null) {
            eventListener = EventDataManager.listenToEvent(eventId, (snapshot, e) -> {
                if (e == null && snapshot != null && snapshot.exists()) {
                    Event event = snapshot.toObject(Event.class);
                    if (event != null) {
                        eventLocation = event.getEventLocation();
                        if (eventLocation != null) {
                            updateMap(eventLocation.getLatitude(), eventLocation.getLongitude());
                            String addr = AddressHelper.getAddressFromLatLng(this, eventLocation.getLatitude(), eventLocation.getLongitude());
                            if (addr != null) {
                                TextView tv = findViewById(R.id.eventAddressText);
                                if (tv != null) tv.setText(addr);
                            }
                        }
                        if (event.getEventTimeStarted() != null && eventStartMillis == -1L) {
                            eventStartMillis = event.getEventTimeStarted().toDate().getTime();
                        }
                        if (event.getEventStatus() != null) {
                            java.util.List<String> statuses = java.util.Arrays.asList(
                                    getString(R.string.step_looking),
                                    getString(R.string.step_on_the_way),
                                    getString(R.string.step_arrived),
                                    getString(R.string.step_finished)
                            );
                            int idx = statuses.indexOf(event.getEventStatus());
                            if (idx >= 0 && idx < 3) updateStep(idx);
                        }
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
    private void updateMap(double lat, double lng) {
        StaticMapFragment mapFragment = StaticMapFragment.newInstance(lat, lng);
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
    public void updateStep(int step) {
        if (stepView != null) {
            stepView.go(step, true);
            loadStepFragment(step);
        }
    }

    public void advanceToStep(int step) {
        currentStep = step;
        updateStep(step);
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
                long elapsed;
                if (eventStartMillis > 0) {
                    elapsed = (System.currentTimeMillis() - eventStartMillis) / 1000;
                } else {
                    elapsed = seconds;
                    if (isRunning) seconds++;
                }
                int minutes = (int) (elapsed / 60);
                int secs = (int) (elapsed % 60);
                String timeFormatted = String.format("%02d:%02d", minutes, secs);
                timerTextView.setText(timeFormatted);

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