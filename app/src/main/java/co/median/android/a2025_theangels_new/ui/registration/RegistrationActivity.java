package co.median.android.a2025_theangels_new.ui.registration;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import okhttp3.*;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.ui.main.MainActivity;

public class RegistrationActivity extends AppCompatActivity {

    private EditText firstName, lastName, idNumber, email, phone, password, confirmPassword;
    private Button selectBirthDateButton, selectImageButton, registerButton;
    private CheckBox weaponLicenseCheckBox;
    private ImageView profileImageView;
    private ChipGroup medicalOptionsGroup;
    private Spinner citySpinner;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedImageBitmap = null;
    private String selectedBirthDate = "";
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private final String IMGUR_CLIENT_ID = "47eaf978d864043";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        idNumber = findViewById(R.id.idNumberEditText);
        email = findViewById(R.id.emailEditText);
        phone = findViewById(R.id.phoneEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);
        selectBirthDateButton = findViewById(R.id.selectBirthDateButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        registerButton = findViewById(R.id.registerButton);
        weaponLicenseCheckBox = findViewById(R.id.weaponLicenseCheckBox);
        profileImageView = findViewById(R.id.profileImageView);
        medicalOptionsGroup = findViewById(R.id.medicalOptions);
        citySpinner = findViewById(R.id.citySpinner);
        loadCities();
        loadMedicalDetails();

        selectBirthDateButton.setOnClickListener(v -> showDatePicker());

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "בחר תמונה"), PICK_IMAGE_REQUEST);
        });

        registerButton.setOnClickListener(v -> handleRegister());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedBirthDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            selectBirthDateButton.setText("נבחר: " + selectedBirthDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handleRegister() {
        Log.d("Registration", "handleRegister called");

        if (!validateInputs()) {
            Toast.makeText(this, "נא למלא את כל הפרטים כראוי", Toast.LENGTH_SHORT).show();
            Log.e("Registration", "Input validation failed");
            return;
        }

        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        Log.d("Registration", "Starting Firebase Auth registration");

        auth.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                        Log.d("Registration", "User created with UID: " + uid);
                        uploadImageToImgur(imageUrl -> saveUserData(uid, imageUrl));
                    } else {
                        Log.e("Registration", "Firebase registration failed: " + task.getException());
                        Toast.makeText(this, "שגיאה: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void uploadImageToImgur(OnImageUploadListener listener) {
        if (selectedImageBitmap == null) {
            listener.onUploaded("");
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageBase64 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            imageBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("image", imageBase64).build();

        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> listener.onUploaded(""));
            }

            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject data = new JSONObject(json).getJSONObject("data");
                    String link = data.getString("link");
                    runOnUiThread(() -> listener.onUploaded(link));
                } catch (Exception e) {
                    runOnUiThread(() -> listener.onUploaded(""));
                }
            }
        });
    }

    private void saveUserData(String uid, String imageUrl) {
        Log.d("Registration", "Saving user data to Firestore");

        List<String> medicalSelections = new ArrayList<>();
        for (int i = 0; i < medicalOptionsGroup.getChildCount(); i++) {
            Chip chip = (Chip) medicalOptionsGroup.getChildAt(i);
            if (chip.isChecked()) medicalSelections.add(chip.getText().toString());
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("firstName", firstName.getText().toString().trim());
        userData.put("lastName", lastName.getText().toString().trim());
        userData.put("idNumber", idNumber.getText().toString().trim());
        userData.put("Email", email.getText().toString().trim());
        userData.put("Phone", phone.getText().toString().trim());
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
            java.util.Date d = sdf.parse(selectedBirthDate);
            userData.put("birthDate", d);
        } catch (Exception e) {
            userData.put("birthDate", null);
        }
        userData.put("haveGunLicense", weaponLicenseCheckBox.isChecked());
        userData.put("imageURL", imageUrl);
        userData.put("medicalDetails", medicalSelections);
        userData.put("city", citySpinner.getSelectedItem().toString());
        userData.put("role", "משתמש");

        db.collection("users").document(uid).set(userData)
                .addOnSuccessListener(unused -> {
                    Log.d("Registration", "User data saved successfully");
                    Toast.makeText(this, "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("Registration", "Failed to save user data: " + e.getMessage());
                    Toast.makeText(this, "שגיאה בשמירת הנתונים", Toast.LENGTH_SHORT).show();
                });
    }


    private boolean validateInputs() {
        boolean valid = !firstName.getText().toString().isEmpty()
                && !lastName.getText().toString().isEmpty()
                && !idNumber.getText().toString().isEmpty()
                && !email.getText().toString().isEmpty()
                && !phone.getText().toString().isEmpty()
                && !password.getText().toString().isEmpty()
                && password.getText().toString().equals(confirmPassword.getText().toString())
                && !selectedBirthDate.isEmpty();

        Log.d("Validation", "Inputs valid: " + valid);
        return valid;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(selectedImageBitmap);
                profileImageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCities() {
        db.collection("cities").get().addOnSuccessListener(querySnapshot -> {
            List<String> cities = new ArrayList<>();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                cities.add(doc.getString("name"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            citySpinner.setAdapter(adapter);
        });
    }

    private void loadMedicalDetails() {
        db.collection("medicalDetails").get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot doc : querySnapshot) {
                Chip chip = new Chip(this);
                chip.setText(doc.getString("name"));
                chip.setCheckable(true);
                medicalOptionsGroup.addView(chip);
            }
        });
    }

    interface OnImageUploadListener {
        void onUploaded(String url);
    }
}
