// =======================================
// IMPORTS
// =======================================
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
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import co.median.android.a2025_theangels_new.models.UserSession;
// =======================================
// BaseActivity - Abstract base class for all activities
// Handles layout setup, top/bottom bars, and shared UI logic
// =======================================

public abstract class BaseActivity extends AppCompatActivity {

    // =======================================
    // VARIABLES
    // =======================================

    private View topBar;
    private BottomAppBar bottomAppBar;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabEmergency;
    private ImageView imgProfile;
    private TextView tvGreeting;

    // =======================================
    // onCreate - Called when the activity is first created
    // Sets up layout, UI components, animations, and listeners
    // =======================================

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
            imgProfile = topBar.findViewById(R.id.img_profile);
            tvGreeting = topBar.findViewById(R.id.tv_greeting);
            updateUserInfo();
        }

        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fabEmergency = findViewById(R.id.fab_emergency);

        // Setup FAB click with animation and vibration
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

        // Setup bottom navigation listener
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

    // =======================================
    // hideSystemUI - Hides status/navigation bars for fullscreen experience
    // =======================================
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

    // =======================================
    // setTransparentStatusBar - Makes the status bar transparent
    // =======================================
    private void setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
        }
    }

    // =======================================
    // startActivityWithAnimation - Starts an activity with fade animation and finishes current
    // =======================================
    private void startActivityWithAnimation(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
        overridePendingTransition(0, 0); // Disable transition animations
        finish();
    }

    // =======================================
    // highlightCurrentTab - Highlights the current active navigation tab
    // =======================================
    private void highlightCurrentTab() {
        if (bottomNavigationView != null) {
            int currentItemId = getCurrentMenuItemId();
            bottomNavigationView.getMenu().findItem(currentItemId).setChecked(true);
        }
    }

    // =======================================
    // getCurrentMenuItemId - Determines the current navigation item based on the activity
    // =======================================
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

    // =======================================
    // onResume - Called when the activity is resumed
    // Ensures UI remains fullscreen and highlights correct tab
    // =======================================
    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        highlightCurrentTab();
        updateUserInfo();

    }

    // =======================================
    // showTopBar - Shows or hides the top bar
    // =======================================
    protected void showTopBar(boolean show) {
        if (topBar != null) {
            topBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    // =======================================
    // showBottomBar - Shows or hides the bottom bar and FAB
    // =======================================
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

    // =======================================
    // updateUserInfo - Updates the greeting and profile picture from UserSession
    // =======================================
    protected void updateUserInfo() {
        if (imgProfile == null || tvGreeting == null) return;

        UserSession session = UserSession.getInstance();
        String first = session.getFirstName();
        String last = session.getLastName();
        if (first != null) {
            String name = first + (last != null ? " " + last : "");
            tvGreeting.setText("שלום, " + name);
        }

        String url = session.getImageURL();
        if (url != null && !url.isEmpty()) {
            Glide.with(this).load(url).placeholder(R.drawable.newuserpic).into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.newuserpic);
        }
    }


    // =======================================
    // getLayoutResourceId - Abstract method to define the layout resource in subclasses
    // =======================================
    protected abstract int getLayoutResourceId();
}