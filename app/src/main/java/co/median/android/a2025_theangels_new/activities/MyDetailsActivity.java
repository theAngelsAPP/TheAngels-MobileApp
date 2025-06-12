// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Calendar;
import java.util.Map;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.models.UserSession;
import co.median.android.a2025_theangels_new.services.UserDataManager;

// =======================================
// MyDetailsActivity - Handles user personal details, including date picker and save logic
// =======================================
public class MyDetailsActivity extends AppCompatActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private TextInputEditText etFirstName, etLastName, etBirthDate, etIdNumber, etCity, etEmail, etPhone;

    // =======================================
    // onCreate - Initializes the personal details screen and interactions
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etBirthDate = findViewById(R.id.et_birth_date);
        etIdNumber = findViewById(R.id.et_id_number);
        etCity = findViewById(R.id.et_city);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);

        UserSession session = UserSession.getInstance();
        if (session.getFirstName() != null) etFirstName.setText(session.getFirstName());
        if (session.getLastName() != null) etLastName.setText(session.getLastName());
        if (session.getBirthDate() != null) etBirthDate.setText(session.getBirthDate());
        if (session.getIdNumber() != null) etIdNumber.setText(session.getIdNumber());
        if (session.getCity() != null) etCity.setText(session.getCity());
        if (session.getEmail() != null) etEmail.setText(session.getEmail());
        if (session.getPhone() != null) etPhone.setText(session.getPhone());

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        // Save button (closes activity with animation)
        findViewById(R.id.btn_save_changes).setOnClickListener(v -> {
            if (!validateInputs()) return;

            Map<String, Object> updates = new HashMap<>();
            updates.put("firstName", etFirstName.getText().toString().trim());
            updates.put("lastName", etLastName.getText().toString().trim());
            updates.put("birthDate", etBirthDate.getText().toString().trim());
            updates.put("city", etCity.getText().toString().trim());
            updates.put("Email", etEmail.getText().toString().trim());
            updates.put("Phone", etPhone.getText().toString().trim());

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            UserDataManager.updateUserDetails(uid, updates, success -> {
                if (success) {
                    Toast.makeText(this, getString(R.string.details_saved), Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
                } else {
                    Toast.makeText(this, getString(R.string.error_saving_details), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Show date picker when clicking birthdate field
        etBirthDate.setOnClickListener(v -> showDatePicker());

        // Show toast when trying to edit ID field
        etIdNumber.setOnClickListener(v ->
                Toast.makeText(this, getString(R.string.id_field_locked), Toast.LENGTH_SHORT).show()
        );

        // Hide keyboard when tapping outside inputs
        findViewById(R.id.root_layout).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                }
            }
            return false;
        });
    }

    // =======================================
    // showDatePicker - Opens a calendar picker and sets selected date in field
    // =======================================
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etBirthDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private boolean validateInputs() {
        if (etFirstName.getText().toString().trim().isEmpty() ||
                etLastName.getText().toString().trim().isEmpty() ||
                etBirthDate.getText().toString().trim().isEmpty() ||
                etCity.getText().toString().trim().isEmpty() ||
                etEmail.getText().toString().trim().isEmpty() ||
                etPhone.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
