// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.events.create;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.ui.events.active.EventUserActivity;
import co.median.android.a2025_theangels_new.ui.events.create.EventTypeFragment;
import co.median.android.a2025_theangels_new.ui.events.create.WhatHappenedFragment;
import co.median.android.a2025_theangels_new.ui.events.create.QuestionnaireFragment;
import co.median.android.a2025_theangels_new.data.map.LocationFragment;
import co.median.android.a2025_theangels_new.ui.events.create.SummaryFragment;
import com.shuhart.stepview.StepView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import java.util.Arrays;
import co.median.android.a2025_theangels_new.ui.main.BaseActivity;

// =======================================
// NewEventActivity - Multi-step form for creating a new event
// =======================================
public class NewEventActivity extends BaseActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private int currentStep = 0;
    private StepView stepView;
    private TextView tvStepTitle, tvStepDescription;
    private Button btnNext;
    private Vibrator vibrator;
    private NewEventViewModel viewModel;

    private Fragment[] steps = new Fragment[]{
            new EventTypeFragment(),
            new WhatHappenedFragment(),
            new QuestionnaireFragment(),
            new LocationFragment(),
            new SummaryFragment()
    };

    // =======================================
    // onCreate - Initializes step flow UI and navigation logic
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        showTopBar(false);
        showBottomBar(false);

        // Bind views
        stepView = findViewById(R.id.step_view);
        ImageView ivClose = findViewById(R.id.ivClose);
        btnNext = findViewById(R.id.btnNext);
        tvStepTitle = findViewById(R.id.tvStepTitle);
        tvStepDescription = findViewById(R.id.tvStepDescription);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        viewModel = new ViewModelProvider(this).get(NewEventViewModel.class);

        // Set step titles
        stepView.setSteps(Arrays.asList(
                getString(R.string.step_title_event_type),
                getString(R.string.step_title_what_happened),
                getString(R.string.step_title_questionnaire),
                getString(R.string.step_title_location),
                getString(R.string.step_title_summary)
        ));

        if (savedInstanceState == null) {
            replaceFragment(steps[currentStep]);
            stepView.go(currentStep, true);
            updateStepInfo();
        }

        // Handle next step
        btnNext.setOnClickListener(v -> {
            if (currentStep < steps.length - 1) {
                currentStep++;
                replaceFragment(steps[currentStep]);
                stepView.go(currentStep, true);
                updateStepInfo();
                triggerVibration();
                animateStepCircle();

                // Update button text on last step
                if (currentStep == steps.length - 1) {
                    btnNext.setText(R.string.call_for_help);
                } else {
                    btnNext.setText(R.string.next_step);
                }
            } else {
                btnNext.setEnabled(false);
                String uid = FirebaseAuth.getInstance().getCurrentUser() != null ?
                        FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
                if (uid != null) {
                    viewModel.createEvent(uid, eventId -> {
                        Intent intent = new Intent(NewEventActivity.this, EventUserActivity.class);
                        intent.putExtra("eventId", eventId);
                        startActivity(intent);
                        finish();
                    }, e -> btnNext.setEnabled(true));
                }
            }
        });

        // Close screen
        ivClose.setOnClickListener(v -> finish());
    }

    // =======================================
    // replaceFragment - Replaces current fragment with step fragment
    // =======================================
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    // =======================================
    // updateStepInfo - Updates step title and description based on current step
    // =======================================
    private void updateStepInfo() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        tvStepTitle.startAnimation(fadeIn);
        tvStepDescription.startAnimation(fadeIn);

        switch (currentStep) {
            case 0:
                tvStepTitle.setText(R.string.step_title_event_type);
                tvStepDescription.setText(R.string.step_desc_event_type);
                break;
            case 1:
                tvStepTitle.setText(R.string.step_title_what_happened);
                tvStepDescription.setText(R.string.step_desc_what_happened);
                break;
            case 2:
                tvStepTitle.setText(R.string.step_title_questionnaire);
                tvStepDescription.setText(R.string.step_desc_questionnaire);
                break;
            case 3:
                tvStepTitle.setText(R.string.step_title_location);
                tvStepDescription.setText(R.string.step_desc_location);
                break;
            case 4:
                tvStepTitle.setText(R.string.step_title_summary);
                tvStepDescription.setText(R.string.step_desc_summary);
                break;
        }
    }

    // =======================================
    // triggerVibration - Short vibration feedback on step transition
    // =======================================
    private void triggerVibration() {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
    }

    // =======================================
    // animateStepCircle - Custom step indicator animation by alternating colors
    // =======================================
    private void animateStepCircle() {
        int[] delayTimes = {0, 500, 1000, 1500, 2000, 2500, 3000};
        int color1 = getColor(R.color.circle1);
        int color2 = getColor(R.color.circle2);

        for (int i = 0; i < delayTimes.length; i++) {
            int finalColor = (i % 2 == 0) ? color1 : color2;
            int delay = delayTimes[i];
            stepView.postDelayed(() -> {
                stepView.getState().selectedCircleColor(finalColor).commit();
            }, delay);
        }
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_new_event;
    }
}