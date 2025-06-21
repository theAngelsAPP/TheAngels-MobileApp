// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.active;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import java.util.Arrays;
import java.util.List;
import com.shuhart.stepview.StepView;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.map.StaticMapFragment;
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
    private LinearLayout ratingLayout;
    private LinearLayout safetyMessageLayout;
    private RatingBar ratingBar;
    private EditText freeTextFeedback;
    private Button submitFeedbackButton;
    private FrameLayout mapContainer;
    private View redSeparator;

    private int currentStep = 0;
    private boolean isRunning = true;
    private int seconds = 0;
    private Handler handler = new Handler();

    private List<String> statuses;

    // =======================================
    // onCreate - Initializes UI, step view, timer, map, and button logic
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(true);

        // Bind views
        timerTextView = findViewById(R.id.timerTextView);
        timeTitle = findViewById(R.id.timeTitle);
        eventAddressTitle = findViewById(R.id.eventAddressTitle);
        eventAddressText = findViewById(R.id.eventAddressText);
        redSeparator = findViewById(R.id.redSeparator);
        statusTextView = findViewById(R.id.statusTextView);
        stepView = findViewById(R.id.step_view);
        nextStepButton = findViewById(R.id.nextStepButton);
        volview = findViewById(R.id.volview);
        emergencyCallButton = findViewById(R.id.emergencyCallButton);
        ratingLayout = findViewById(R.id.ratingLayout);
        safetyMessageLayout = findViewById(R.id.safetyMessageLayout);
        ratingBar = findViewById(R.id.ratingBar);
        freeTextFeedback = findViewById(R.id.freeTextFeedback);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);
        mapContainer = findViewById(R.id.map_container);

        // Step statuses (translated from strings.xml)
        statuses = Arrays.asList(
                getString(R.string.status_looking_for_volunteer),
                getString(R.string.status_volunteer_on_the_way),
                getString(R.string.status_volunteer_arrived),
                getString(R.string.status_event_finished)
        );

        setupStepView();
        startTimer();
        setupMap();

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
        }
    }

    // =======================================
    // setupMap - Loads static map fragment into screen
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
        builder.setPositiveButton(getString(R.string.ok_button), null);
        builder.show();
    }
}