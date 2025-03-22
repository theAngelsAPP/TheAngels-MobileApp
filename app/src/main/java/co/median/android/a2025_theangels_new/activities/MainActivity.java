// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import co.median.android.a2025_theangels_new.R;

// =======================================
// MainActivity - Handles login screen and navigation to registration or onboarding
// =======================================
public class MainActivity extends BaseActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private TextInputEditText usernameInput, passwordInput;
    private Button loginButton, registerButton;

    // =======================================
    // onCreate - Initializes the login screen and handles onboarding check
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);

        // Check if onboarding is complete
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean onboardingComplete = prefs.getBoolean("onboarding_complete", false);

        // Hide system UI for immersive mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        // Redirect to onboarding if needed
        if (!onboardingComplete) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        }

        // Load layout
        setContentView(R.layout.activity_main);

        // Bind views
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Initially disable login button
        loginButton.setEnabled(false);

        // TextWatcher to enable login only when both fields are filled
        TextWatcher textWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        usernameInput.addTextChangedListener(textWatcher);
        passwordInput.addTextChangedListener(textWatcher);

        // Login button click
        loginButton.setOnClickListener(v -> {
            if (isInputsFilled()) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        });

        // Register button click
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        });
    }

    // =======================================
    // checkInputs - Enables login button only if both fields are filled
    // =======================================
    private void checkInputs() {
        loginButton.setEnabled(isInputsFilled());
    }

    // =======================================
    // isInputsFilled - Returns true if both username and password fields are not empty
    // =======================================
    private boolean isInputsFilled() {
        return !usernameInput.getText().toString().trim().isEmpty() &&
                !passwordInput.getText().toString().trim().isEmpty();
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }
}
