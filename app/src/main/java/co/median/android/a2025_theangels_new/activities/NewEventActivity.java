package co.median.android.a2025_theangels_new.activities;

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
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.EventTypeFragment;
import co.median.android.a2025_theangels_new.fragments.WhatHappenedFragment;
import co.median.android.a2025_theangels_new.fragments.QuestionnaireFragment;
import co.median.android.a2025_theangels_new.fragments.LocationFragment;
import co.median.android.a2025_theangels_new.fragments.SummaryFragment;
import com.shuhart.stepview.StepView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import java.util.Arrays;

public class NewEventActivity extends AppCompatActivity {

    private int currentStep = 0;
    private StepView stepView;
    private TextView tvStepTitle, tvStepDescription;
    private Button btnNext;
    private Vibrator vibrator;

    private Fragment[] steps = new Fragment[]{
            new EventTypeFragment(),
            new WhatHappenedFragment(),
            new QuestionnaireFragment(),
            new LocationFragment(),
            new SummaryFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        stepView = findViewById(R.id.step_view);
        ImageView ivClose = findViewById(R.id.ivClose);
        btnNext = findViewById(R.id.btnNext);
        tvStepTitle = findViewById(R.id.tvStepTitle);
        tvStepDescription = findViewById(R.id.tvStepDescription);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // הגדרת שמות השלבים
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

        btnNext.setOnClickListener(v -> {
            if (currentStep < steps.length - 1) {
                currentStep++;
                replaceFragment(steps[currentStep]);
                stepView.go(currentStep, true);
                updateStepInfo();
                triggerVibration();
                animateStepCircle();

                // עדכון טקסט הכפתור כאשר מגיעים לשלב האחרון
                if (currentStep == steps.length - 1) {
                    btnNext.setText(R.string.call_for_help); // קריאה לעזרה
                } else {
                    btnNext.setText(R.string.next_step); // המשך
                }
            } else {
                // מעבר למסך הפעילות לאחר סיום התהליך
                startActivity(new Intent(NewEventActivity.this, EventUserActivity.class));
                finish(); // לסגור את המסך הנוכחי כדי למנוע חזרה אחורה
            }
        });

        ivClose.setOnClickListener(v -> finish());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // אנימציות מעבר חלקות
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    private void updateStepInfo() {
        // אנימציה לבלוק ההסבר
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

    private void triggerVibration() {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
    }

    private void animateStepCircle() {
        stepView.getState()
                .selectedCircleColor(getResources().getColor(R.color.circle1))
                .commit();

        stepView.postDelayed(() -> stepView.getState()
                .selectedCircleColor(getResources().getColor(R.color.circle2))
                .commit(), 500);

        stepView.postDelayed(() -> stepView.getState()
                .selectedCircleColor(getResources().getColor(R.color.circle1))
                .commit(), 1000);

        stepView.postDelayed(() -> stepView.getState()
                .selectedCircleColor(getResources().getColor(R.color.circle2))
                .commit(), 1500);

        stepView.postDelayed(() -> stepView.getState()
                .selectedCircleColor(getResources().getColor(R.color.circle1))
                .commit(), 2000);

        stepView.postDelayed(() -> stepView.getState()
                .selectedCircleColor(getResources().getColor(R.color.circle2))
                .commit(), 2500);

        stepView.postDelayed(() -> stepView.getState()
                .selectedCircleColor(getResources().getColor(R.color.circle1))
                .commit(), 3000);
    }
}
