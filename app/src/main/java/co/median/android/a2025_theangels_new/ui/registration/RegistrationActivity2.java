// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.registration;

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

// =======================================
// RegistrationActivity2 - Second part of the registration process
// =======================================
public class RegistrationActivity2 extends AppCompatActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private EditText email, phone, password, confirmPassword;
    private CheckBox gunLicenseCheckbox;
    private ChipGroup medicalOptions;
    private Button registerButton;
    private boolean[] medicalSelections;

    // =======================================
    // onCreate - Initializes form and medical chip selection logic
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        // Bind views
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        gunLicenseCheckbox = findViewById(R.id.gunLicenseCheckbox);
        medicalOptions = findViewById(R.id.medicalOptions);
        registerButton = findViewById(R.id.registerButton);

        // Initialize selection state for each chip
        medicalSelections = new boolean[medicalOptions.getChildCount()];

        // Handle chip clicks for toggle selection
        for (int i = 0; i < medicalOptions.getChildCount(); i++) {
            final Chip chip = (Chip) medicalOptions.getChildAt(i);
            final int index = i;
            chip.setOnClickListener(v -> {
                if (medicalSelections[index]) {
                    chip.setChipBackgroundColorResource(R.color.chip_unselected); // changed from android.R
                    medicalSelections[index] = false;
                } else {
                    chip.setChipBackgroundColorResource(R.color.chip_selected); // changed from android.R
                    medicalSelections[index] = true;
                }
            });
        }

        // Navigate to HomeActivity after registration
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity2.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
