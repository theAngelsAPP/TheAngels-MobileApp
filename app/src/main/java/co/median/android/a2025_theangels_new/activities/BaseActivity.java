package co.median.android.a2025_theangels_new.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import co.median.android.a2025_theangels_new.R;

public abstract class BaseActivity extends AppCompatActivity {

    private View topBar;
    private BottomAppBar bottomAppBar;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setTransparentStatusBar();
        hideSystemUI();

        ViewGroup contentFrame = findViewById(R.id.activity_content);
        getLayoutInflater().inflate(getLayoutResourceId(), contentFrame, true);

        topBar = findViewById(R.id.top_bar);
        if (topBar != null) {
            topBar.setAlpha(0f);
            topBar.animate().alpha(1f).setDuration(600).start();
        }

        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fabEmergency = findViewById(R.id.fab_emergency);

        if (fabEmergency != null) {
            fabEmergency.setOnClickListener(v -> {
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.2f, 1.0f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.2f, 1.0f);
                scaleX.setInterpolator(new OvershootInterpolator());
                scaleY.setInterpolator(new OvershootInterpolator());
                scaleX.setDuration(150);
                scaleY.setDuration(150);
                scaleX.start();
                scaleY.start();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    if (vibrator != null) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                }

                startActivityWithAnimation(NewEventActivity.class);
            });
        }

        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivityWithAnimation(HomeActivity.class);
                } else if (itemId == R.id.nav_education) {
                    startActivityWithAnimation(EducationActivity.class);
                } else if (itemId == R.id.nav_events) {
                    startActivityWithAnimation(EventsActivity.class);
                } else if (itemId == R.id.nav_profile) {
                    startActivityWithAnimation(ProfileActivity.class);
                }

                return true;
            });

            highlightCurrentTab();
        }
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            getWindow().getInsetsController().setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
    }

    private void setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
        }
    }

    private void startActivityWithAnimation(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void highlightCurrentTab() {
        if (bottomNavigationView != null) {
            int currentItemId = getCurrentMenuItemId();
            bottomNavigationView.getMenu().findItem(currentItemId).setChecked(true);
        }
    }

    private int getCurrentMenuItemId() {
        if (this instanceof HomeActivity) {
            return R.id.nav_home;
        } else if (this instanceof EducationActivity) {
            return R.id.nav_education;
        } else if (this instanceof EventsActivity) {
            return R.id.nav_events;
        } else if (this instanceof ProfileActivity) {
            return R.id.nav_profile;
        }
        return R.id.nav_home;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        highlightCurrentTab();
    }

    protected void showTopBar(boolean show) {
        if (topBar != null) {
            topBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    protected void showBottomBar(boolean show) {
        if (bottomAppBar != null) {
            bottomAppBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (fabEmergency != null) {
            fabEmergency.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    protected abstract int getLayoutResourceId();
}
