package co.median.android.a2025_theangels_new.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.viewpager2.widget.ViewPager2;
import java.util.List;
import java.util.Arrays;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.adapters.OnboardingAdapter;


public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        startButton = findViewById(R.id.startButton);


        List<Integer> images = Arrays.asList(
                R.drawable.onboarding_1,
                R.drawable.onboarding_2,
                R.drawable.onboarding_3
        );

        OnboardingAdapter adapter = new OnboardingAdapter(this, images);
        viewPager.setAdapter(adapter);

        startButton.setOnClickListener(v -> {

            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("onboarding_complete", true).apply();

            startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
            finish();
        });
    }
}