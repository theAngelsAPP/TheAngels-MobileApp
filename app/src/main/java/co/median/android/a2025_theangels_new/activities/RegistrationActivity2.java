package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import co.median.android.a2025_theangels_new.R;

public class RegistrationActivity2 extends AppCompatActivity {
    private EditText email, phone, password, confirmPassword;
    private CheckBox gunLicenseCheckbox;
    private ChipGroup medicalOptions;
    private Button registerButton;
    private boolean[] medicalSelections;

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

        medicalSelections = new boolean[medicalOptions.getChildCount()];

        for (int i = 0; i < medicalOptions.getChildCount(); i++) {
            final Chip chip = (Chip) medicalOptions.getChildAt(i);
            final int index = i;
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (medicalSelections[index]) {
                        chip.setChipBackgroundColorResource(android.R.color.darker_gray);
                        medicalSelections[index] = false;
                    } else {
                        chip.setChipBackgroundColorResource(android.R.color.holo_blue_light);
                        medicalSelections[index] = true;
                    }
                }
            });
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity2.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
