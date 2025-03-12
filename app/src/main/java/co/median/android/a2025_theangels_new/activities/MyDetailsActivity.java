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
import co.median.android.a2025_theangels_new.R;
import java.util.Calendar;

public class MyDetailsActivity extends AppCompatActivity {

    private EditText etBirthDate, etIdNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);

        etBirthDate = findViewById(R.id.et_birth_date);
        etIdNumber = findViewById(R.id.et_id_number);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.btn_save_changes).setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
        });

        etBirthDate.setOnClickListener(v -> showDatePicker());

        etIdNumber.setOnClickListener(v ->
                Toast.makeText(this, "שדה זה לא ניתן לעריכה", Toast.LENGTH_SHORT).show()
        );

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
