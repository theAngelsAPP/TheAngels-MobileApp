// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.Calendar;
import co.median.android.a2025_theangels_new.R;

// =======================================
// RegistrationActivity - Handles user input for registration step 1
// =======================================
public class RegistrationActivity extends AppCompatActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private EditText firstName, lastName, idNumber, city;
    private Button birthDateButton, uploadImageButton, continueButton;
    private ImageView profileImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String selectedDate;

    // =======================================
    // onCreate - Initializes the registration form and handles UI interactions
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Bind views
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        idNumber = findViewById(R.id.idNumber);
        city = findViewById(R.id.city);
        birthDateButton = findViewById(R.id.birthDateButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        profileImageView = findViewById(R.id.profileImageView);
        continueButton = findViewById(R.id.continueButton);

        // Handle birth date selection
        birthDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RegistrationActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        birthDateButton.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Handle image upload
        uploadImageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), PICK_IMAGE_REQUEST);
        });

        // Continue to next registration step
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity2.class);
            startActivity(intent);
        });
    }

    // =======================================
    // onActivityResult - Handles selected image and shows it in the ImageView
    // =======================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
                profileImageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}