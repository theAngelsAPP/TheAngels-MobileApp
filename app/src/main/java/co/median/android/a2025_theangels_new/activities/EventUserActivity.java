package co.median.android.a2025_theangels_new.activities;

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
import co.median.android.a2025_theangels_new.fragments.StaticMapFragment;

public class EventUserActivity extends BaseActivity {

    private TextView timerTextView;
    private TextView statusTextView;
    private TextView timeTitle;
    private TextView eventAddressTitle;
    private TextView eventAddressText;
    private StepView stepView;
    private Button nextStepButton;
    private Button emergencyCallButton;
    private LinearLayout ratingLayout;
    private LinearLayout safetyMessageLayout;
    private RatingBar ratingBar;
    private EditText freeTextFeedback;
    private Button submitFeedbackButton;
    private FrameLayout mapContainer;
    private View redSeparator;

    private int currentStep = 0;
    private int seconds = 0;
    private boolean isRunning = true;
    private Handler handler = new Handler();
    private List<String> statuses = Arrays.asList(
            "מחפשים אחר מתנדב זמין בסביבתך",
            "המתנדב נמצא בדרך אליך",
            "המתנדב הגיע לאירוע",
            "האירוע הסתיים"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(true);

        timerTextView = findViewById(R.id.timerTextView);
        timeTitle = findViewById(R.id.timeTitle);
        eventAddressTitle = findViewById(R.id.eventAddressTitle);
        eventAddressText = findViewById(R.id.eventAddressText);
        redSeparator = findViewById(R.id.redSeparator);
        statusTextView = findViewById(R.id.statusTextView);
        stepView = findViewById(R.id.step_view);
        nextStepButton = findViewById(R.id.nextStepButton);
        emergencyCallButton = findViewById(R.id.emergencyCallButton);
        ratingLayout = findViewById(R.id.ratingLayout);
        safetyMessageLayout = findViewById(R.id.safetyMessageLayout);
        ratingBar = findViewById(R.id.ratingBar);
        freeTextFeedback = findViewById(R.id.freeTextFeedback);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);
        mapContainer = findViewById(R.id.map_container);

        setupStepView();
        startTimer();
        setupMap();

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep < statuses.size() - 1) {
                    currentStep++;
                    updateStep(currentStep);
                }
            }
        });

        emergencyCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmergencyCallDialog();
            }
        });

        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFeedbackSubmission();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_user;
    }

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

    private void setupStepView() {
        stepView.setSteps(Arrays.asList("חיפוש מתנדב", "מתנדב בדרך", "מתנדב באירוע", "אירוע הסתיים"));
        stepView.go(0, true);
    }

    public void updateStep(int step) {
        if (stepView != null) {
            stepView.go(step, true);
            statusTextView.setText(statuses.get(step));

            if (step == 3) {
                ratingLayout.setVisibility(View.VISIBLE);
                nextStepButton.setVisibility(View.GONE);
                timeTitle.setVisibility(View.GONE);
                timerTextView.setVisibility(View.GONE);
                redSeparator.setVisibility(View.GONE);
                safetyMessageLayout.setVisibility(View.GONE);
                mapContainer.setVisibility(View.GONE);
                eventAddressTitle.setVisibility(View.GONE);
                eventAddressText.setVisibility(View.GONE);
                emergencyCallButton.setVisibility(View.GONE);
            } else {
                ratingLayout.setVisibility(View.GONE);
                nextStepButton.setVisibility(View.VISIBLE);
                timeTitle.setVisibility(View.VISIBLE);
                timerTextView.setVisibility(View.VISIBLE);
                redSeparator.setVisibility(View.VISIBLE);
                safetyMessageLayout.setVisibility(View.VISIBLE);
                mapContainer.setVisibility(View.VISIBLE);
                eventAddressTitle.setVisibility(View.VISIBLE);
                eventAddressText.setVisibility(View.VISIBLE);
                emergencyCallButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupMap() {
        double eventLat = 31.8912;
        double eventLng = 34.8115;

        StaticMapFragment mapFragment = StaticMapFragment.newInstance(eventLat, eventLng);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment);
        transaction.commit();
    }

    private void showEmergencyCallDialog() {
        final String[] emergencyNumbers = {"100 - משטרה", "101 - מגן דוד אדום", "102 - מכבי אש"};
        final String[] phoneNumbers = {"tel:100", "tel:101", "tel:102"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר שירות חירום להתקשרות");
        builder.setItems(emergencyNumbers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(phoneNumbers[which]));
                startActivity(intent);
            }
        });
        builder.show();
    }

    private void handleFeedbackSubmission() {
        float rating = ratingBar.getRating();
        String feedbackText = freeTextFeedback.getText().toString().trim();

        if (rating == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("שגיאה");
            builder.setMessage("נא לבחור דירוג לפני השליחה.");
            builder.setPositiveButton("אישור", null);
            builder.show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("תודה על הדירוג!");
        builder.setMessage("הדירוג שלך: " + rating + "\nהמשוב שלך:\n" + feedbackText);
        builder.setPositiveButton("אישור", null);
        builder.show();
    }
}
