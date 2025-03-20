package co.median.android.a2025_theangels_new.activities;

import androidx.annotation.NonNull;
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
    private List<Integer> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        viewPager = findViewById(R.id.viewPager);
        viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        startButton = findViewById(R.id.startButton);
        images = Arrays.asList(
                R.drawable.onboarding_1,
                R.drawable.onboarding_2,
                R.drawable.onboarding_3
        );

        OnboardingAdapter adapter = new OnboardingAdapter(this, images);
        viewPager.setAdapter(adapter);


        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float alpha = 0f;
                if (position >= -1 && position <= 1) {
                    alpha = 1 - Math.abs(position);
                }
                page.setAlpha(alpha);
                page.setTranslationX(-position * page.getWidth());
                page.setTranslationZ(-Math.abs(position));
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == images.size() - 1) {
                    startButton.setVisibility(View.VISIBLE);
                } else {
                    startButton.setVisibility(View.GONE);
                }
            }
        });

        startButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("onboarding_complete", true).apply();

            startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
            finish();
        });

        startButton.setVisibility(View.GONE);
    }
}
