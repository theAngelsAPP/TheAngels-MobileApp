package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import co.median.android.a2025_theangels_new.R;

public class RegistrationActivity2 extends AppCompatActivity {
    private EditText email, phone, password, confirmPassword;
    private CheckBox gunLicenseCheckbox;
    private GridLayout medicalOptions;
    private Button registerButton;
    private boolean[] medicalSelections = new boolean[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        gunLicenseCheckbox = findViewById(R.id.gunLicenseCheckbox);
        medicalOptions = findViewById(R.id.medicalOptions);
        registerButton = findViewById(R.id.registerButton);

        // טיפול בלחיצה על פרטי בריאות
        for (int i = 0; i < medicalOptions.getChildCount(); i++) {
            final TextView option = (TextView) medicalOptions.getChildAt(i);
            final int index = i;
            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (medicalSelections[index]) {
                        option.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                        medicalSelections[index] = false;
                    } else {
                        option.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        medicalSelections[index] = true;
                    }
                }
            });
        }

        // לחיצה על כפתור הרשמה
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // אפשר להוסיף כאן לוגיקה לרישום משתמש חדש
                Intent intent = new Intent(RegistrationActivity2.this, HomepageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
