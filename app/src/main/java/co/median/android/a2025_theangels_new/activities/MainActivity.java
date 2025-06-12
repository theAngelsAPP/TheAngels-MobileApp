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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.widget.Toast;
import co.median.android.a2025_theangels_new.services.UserDataManager;

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
            String email = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "אנא הזן אימייל וסיסמה", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "כתובת האימייל אינה תקינה", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            UserDataManager.loadUserDetails(uid, session -> {
                                Toast.makeText(this, "התחברת בהצלחה", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            });
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(this, "המשתמש לא קיים", Toast.LENGTH_SHORT).show();
                            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(this, "סיסמה שגויה", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });


        // Register button click
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        });
    }

    // =======================================
    // onStart - Automatically navigates to Home if a user is already signed in
    // =======================================
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            UserDataManager.loadUserDetails(uid, session -> {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            });
        }
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
