// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import co.median.android.a2025_theangels_new.R;

// =======================================
// MyDetailsActivity - Handles user personal details, including date picker and save logic
// =======================================
public class MyDetailsActivity extends AppCompatActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private EditText etBirthDate, etIdNumber;

    // =======================================
    // onCreate - Initializes the personal details screen and interactions
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);

        etBirthDate = findViewById(R.id.et_birth_date);
        etIdNumber = findViewById(R.id.et_id_number);

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        // Save button (closes activity with animation)
        findViewById(R.id.btn_save_changes).setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
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
}
