package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.util.Arrays;
import com.shuhart.stepview.StepView;
import androidx.appcompat.app.AppCompatActivity;
import co.median.android.a2025_theangels_new.R;

public class EventUserActivity extends BaseActivity {

    private TextView timerTextView;
    private StepView stepView;
    private int seconds = 0; // משתנה שמחזיק את הזמן שעבר
    private boolean isRunning = true;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(true);

        timerTextView = findViewById(R.id.timerTextView);
        stepView = findViewById(R.id.step_view);

        setupStepView(); // הגדרת השלבים
        startTimer(); // התחלת הטיימר
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

                handler.postDelayed(this, 1000); // עדכון כל שנייה
            }
        });
    }

    private void setupStepView() {
        stepView.setSteps(Arrays.asList("חיפוש מתנדב", "מתנדב בדרך", "מתנדב באירוע", "אירוע הסתיים"));
        stepView.go(0, true); // התחלת הסטטוס הראשון (חיפוש מתנדב)
    }

    public void updateStep(int step) {
        if (stepView != null) {
            stepView.go(step, true); // עדכון השלב הנוכחי
        }
    }
}
