package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import co.median.android.a2025_theangels_new.R;

public class MainActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private ImageButton loginButton;
    private TextView loginButtonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // בדיקה אם המשתמש סיים את ה-Onboarding
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean onboardingComplete = prefs.getBoolean("onboarding_complete", false);

        if (!onboardingComplete) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return; // יציאה מה-`onCreate` כדי למנוע טעינת מסך ההתחברות
        }

        // אם ה-Onboarding הושלם, ממשיכים עם מסך ההתחברות
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        loginButtonText = findViewById(R.id.loginButtonText);

        // מאזין לשינויים בטקסט
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // מאזינים לשדות שם משתמש וסיסמה
        usernameInput.addTextChangedListener(textWatcher);
        passwordInput.addTextChangedListener(textWatcher);

        // לחיצה על כפתור התחברות
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputsFilled()) {
                    Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // לחיצה על כפתור הרשמה
        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkInputs() {
        if (isInputsFilled()) {
            loginButton.setImageResource(R.drawable.loginbutton_filled);
            loginButtonText.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            loginButton.setImageResource(R.drawable.loginbutton);
            loginButtonText.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    private boolean isInputsFilled() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        return !username.isEmpty() && !password.isEmpty();
    }
}
