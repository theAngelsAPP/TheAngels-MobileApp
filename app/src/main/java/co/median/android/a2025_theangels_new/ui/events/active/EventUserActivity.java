// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.active;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import co.median.android.a2025_theangels_new.databinding.ActivityEventUserBinding;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import co.median.android.a2025_theangels_new.data.services.EventDataManager;
import co.median.android.a2025_theangels_new.util.TimerUtils;
import co.median.android.a2025_theangels_new.data.services.UserDataManager;
import java.util.Arrays;
import java.util.List;
import com.shuhart.stepview.StepView;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.map.StaticMapFragment;
import co.median.android.a2025_theangels_new.data.map.AddressHelper;
import co.median.android.a2025_theangels_new.data.models.Event;
import co.median.android.a2025_theangels_new.ui.main.BaseActivity;
import com.airbnb.lottie.LottieAnimationView;
// =======================================
// EventUserActivity - Handles the live event screen, step progression, and user feedback
// =======================================
public class EventUserActivity extends BaseActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private TextView timerTextView;
    private TextView statusTextView;
    private TextView timeTitle;
    private TextView eventAddressTitle;
    private TextView eventAddressText;
    private StepView stepView;
    private Button nextStepButton;
    private Button volview;
    private Button emergencyCallButton;
    private LinearLayout volunteerInfoLayout;
    private ImageView volunteerImage;
    private TextView volunteerName;
    private LottieAnimationView volunteerAnimation;
    private LinearLayout ratingLayout;
    private LinearLayout safetyMessageLayout;
    private RatingBar ratingBar;
    private EditText freeTextFeedback;
    private Button submitFeedbackButton;
    private FrameLayout mapContainer;
    private TextView closeReasonView;
    private TextView endTimeView;
    private View redSeparator;
    private ActivityEventUserBinding binding;

    private int currentStep = 0;
    private boolean isRunning = true;
    private int seconds = 0;
    private long eventStartMillis = -1L;
    private String previousStatus = null;
    private Handler handler = new Handler();

    private List<String> statuses;
    private ListenerRegistration eventListener;
    private String eventId;
    private String eventCloseReason;
    private com.google.firebase.Timestamp eventEnded;
    private com.google.firebase.firestore.GeoPoint eventLocation;

    // =======================================
    // onCreate - Initializes UI, step view, timer, map, and button logic
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(true);

        android.view.ViewGroup content = findViewById(co.median.android.a2025_theangels_new.R.id.activity_content);
        binding = ActivityEventUserBinding.bind(content.getChildAt(0));

        timerTextView = binding.timerTextView;
        timeTitle = binding.timeTitle;
        eventAddressTitle = binding.eventAddressTitle;
        eventAddressText = binding.eventAddressText;
        redSeparator = binding.redSeparator;
        statusTextView = binding.statusTextView;
        stepView = binding.stepView;
        nextStepButton = binding.nextStepButton;
        volview = binding.volview;
        emergencyCallButton = binding.emergencyCallButton;
        ratingLayout = binding.ratingLayout;
        safetyMessageLayout = binding.safetyMessageLayout;
        ratingBar = binding.ratingBar;
        freeTextFeedback = binding.freeTextFeedback;
        submitFeedbackButton = binding.submitFeedbackButton;
        mapContainer = binding.mapContainer;
        volunteerInfoLayout = binding.volunteerInfoLayout;
        volunteerImage = binding.volunteerImage;
        volunteerName = binding.volunteerName;
        volunteerAnimation = binding.volunteerAnimation;
        closeReasonView = binding.closeReasonTextView;
        endTimeView = binding.endTimeTextView;

        // Step statuses (translated from strings.xml)
        statuses = Arrays.asList(
                getString(R.string.status_looking_for_volunteer),
                getString(R.string.status_volunteer_on_the_way),
                getString(R.string.status_volunteer_arrived),
                getString(R.string.status_event_finished)
        );

        setupStepView();
        startTimer();
        // map will be updated once event data arrives

        // Step progression button
        nextStepButton.setOnClickListener(v -> {
            if (currentStep < statuses.size() - 1) {
                currentStep++;
                updateStep(currentStep);
            }
        });

        // Emergency call dialog
        emergencyCallButton.setOnClickListener(v -> showEmergencyCallDialog());

        // Submit feedback
        submitFeedbackButton.setOnClickListener(v -> handleFeedbackSubmission());

        // Open volunteer view
        volview.setOnClickListener(v -> {
            Intent intent = new Intent(EventUserActivity.this, EventVolActivity.class);
            startActivity(intent);
        });

        eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            eventListener = EventDataManager.listenToEvent(eventId, (snapshot, e) -> {
                if (e == null && snapshot != null && snapshot.exists()) {
                    Event event = snapshot.toObject(Event.class);
                    if (event != null) {
                        eventLocation = event.getEventLocation();
                        eventCloseReason = event.getEventCloseReason();
                        eventEnded = event.getEventTimeEnded();
                        if (eventLocation != null) {
                            updateMap(eventLocation.getLatitude(), eventLocation.getLongitude());
                            String addr = AddressHelper.getAddressFromLatLng(this, eventLocation.getLatitude(), eventLocation.getLongitude());
                            if (addr != null) eventAddressText.setText(addr);
                        }
                        if (event.getEventTimeStarted() != null && eventStartMillis == -1L) {
                            eventStartMillis = event.getEventTimeStarted().toDate().getTime();
                        }

                        String status = event.getEventStatus();
                        if (status != null) {
                            statusTextView.setText(status);
                            java.util.List<String> sts = java.util.Arrays.asList(
                                    getString(R.string.step_looking),
                                    getString(R.string.step_on_the_way),
                                    getString(R.string.step_arrived),
                                    getString(R.string.step_finished));
                            int i = sts.indexOf(status);
                            if (i >= 0) updateStep(i);

                            if ((previousStatus != null &&
                                    previousStatus.equals(getString(R.string.step_looking)) &&
                                    status.equals(getString(R.string.step_on_the_way))) ||
                                    (previousStatus == null && status.equals(getString(R.string.step_on_the_way)))) {
                                triggerVolunteerAssigned(event.getEventHandleBy());
                            }
                            previousStatus = status;
                        }
                    }
                }
            });
        }
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_user;
    }

    // =======================================
    // startTimer - Starts a real-time timer that updates every second
    // =======================================
    private void startTimer() {
        java.util.concurrent.atomic.AtomicLong counter = new java.util.concurrent.atomic.AtomicLong(seconds);
        TimerUtils.startTimer(timerTextView, handler,
                () -> eventStartMillis,
                () -> isRunning,
                counter);
    }

    // =======================================
    // setupStepView - Initializes step view with stages
    // =======================================
    private void setupStepView() {
        stepView.setSteps(Arrays.asList(
                getString(R.string.step_looking),
                getString(R.string.step_on_the_way),
                getString(R.string.step_arrived),
                getString(R.string.step_finished)
        ));
        stepView.go(0, true);
    }

    // =======================================
    // updateStep - Updates step UI and visibility logic based on current stage
    // =======================================
    public void updateStep(int step) {
        if (stepView != null) {
            stepView.go(step, true);
            statusTextView.setText(statuses.get(step));

            boolean isFinal = step == 3;

            ratingLayout.setVisibility(isFinal ? View.VISIBLE : View.GONE);
            volview.setVisibility(isFinal ? View.VISIBLE : View.GONE);
            nextStepButton.setVisibility(isFinal ? View.GONE : View.VISIBLE);
            timeTitle.setVisibility(isFinal ? View.GONE : View.VISIBLE);
            timerTextView.setVisibility(isFinal ? View.GONE : View.VISIBLE);
            redSeparator.setVisibility(isFinal ? View.GONE : View.VISIBLE);
            safetyMessageLayout.setVisibility(isFinal ? View.GONE : View.VISIBLE);
            mapContainer.setVisibility(isFinal ? View.GONE : View.VISIBLE);
            eventAddressTitle.setVisibility(isFinal ? View.GONE : View.VISIBLE);
            eventAddressText.setVisibility(isFinal ? View.GONE : View.VISIBLE);
            emergencyCallButton.setVisibility(isFinal ? View.GONE : View.VISIBLE);
            if (isFinal) {
                if (eventCloseReason != null) {
                    closeReasonView.setText(getString(R.string.close_reason_label, eventCloseReason));
                    closeReasonView.setVisibility(View.VISIBLE);
                }
                if (eventEnded != null) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
                    String time = sdf.format(eventEnded.toDate());
                    endTimeView.setText(getString(R.string.end_time_label, time));
                    endTimeView.setVisibility(View.VISIBLE);
                }
            } else {
                closeReasonView.setVisibility(View.GONE);
                endTimeView.setVisibility(View.GONE);
            }
        }
    }

    // =======================================
    // setupMap - Loads static map fragment into screen
    // =======================================
    private void updateMap(double lat, double lng) {
        StaticMapFragment mapFragment = StaticMapFragment.newInstance(lat, lng);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment);
        transaction.commit();
    }

    private void triggerVolunteerAssigned(String uid) {
        if (uid == null) return;
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(300);
            }
        }
        volunteerInfoLayout.setVisibility(View.VISIBLE);
        if (volunteerAnimation != null) {
            volunteerAnimation.playAnimation();
        }
        UserDataManager.loadBasicUserInfo(uid, info -> {
            if (info != null) {
                volunteerName.setText(info.getFirstName() + " " + info.getLastName());
                String url = info.getImageURL();
                if (url != null && !url.isEmpty()) {
                    com.bumptech.glide.Glide.with(this)
                            .load(url)
                            .placeholder(R.drawable.newuserpic)
                            .into(volunteerImage);
                }
            }
        });
    }

    // =======================================
    // showEmergencyCallDialog - Opens dialog with emergency services to call
    // =======================================
    private void showEmergencyCallDialog() {
        final String[] emergencyNumbers = {
                getString(R.string.police_option),
                getString(R.string.mda_option),
                getString(R.string.fire_option)
        };
        final String[] phoneNumbers = {"tel:100", "tel:101", "tel:102"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_emergency_service));
        builder.setItems(emergencyNumbers, (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(phoneNumbers[which]));
            startActivity(intent);
        });
        builder.show();
    }

    // =======================================
    // handleFeedbackSubmission - Handles validation and showing feedback submission summary
    // =======================================
    private void handleFeedbackSubmission() {
        float rating = ratingBar.getRating();
        String feedbackText = freeTextFeedback.getText().toString().trim();

        if (rating == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.error_title));
            builder.setMessage(getString(R.string.select_rating_warning));
            builder.setPositiveButton(getString(R.string.ok_button), null);
            builder.show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.thank_you_title));
        builder.setMessage(getString(R.string.feedback_summary, rating, feedbackText));
        builder.setPositiveButton(getString(R.string.ok_button), (d, w) -> {
            if (eventId != null) {
                java.util.Map<String, Object> updates = new java.util.HashMap<>();
                updates.put("eventRating", (int) rating);
                updates.put("eventRatingText", feedbackText);
                EventDataManager.updateEvent(eventId, updates, null,
                        err -> android.widget.Toast.makeText(this, R.string.error_title, android.widget.Toast.LENGTH_SHORT).show());
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventListener != null) {
            eventListener.remove();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }
}