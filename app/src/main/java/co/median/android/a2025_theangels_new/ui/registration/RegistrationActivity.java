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
import co.median.android.a2025_theangels_new.databinding.ActivityRegistrationBinding;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import co.median.android.a2025_theangels_new.data.map.AutocompleteHelper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import okhttp3.*;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.ui.main.MainActivity;
import co.median.android.a2025_theangels_new.data.services.UserDataManager;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private EditText firstName, lastName, idNumber, email, phone, password, confirmPassword;
    private Button selectBirthDateButton, selectImageButton, registerButton;
    private CheckBox weaponLicenseCheckBox;
    private ImageView profileImageView;
    private ChipGroup medicalOptionsGroup;
    private EditText cityEditText; // שדה בחירת עיר מגורים
    private String selectedCity = ""; // שם העיר שנבחרה

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CITY_AUTOCOMPLETE_REQUEST = 2;
    private Bitmap selectedImageBitmap = null;
    private String selectedBirthDate = "";
    private FirebaseAuth auth;

    private final String IMGUR_CLIENT_ID = "47eaf978d864043";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        firstName = binding.firstNameEditText;
        lastName = binding.lastNameEditText;
        idNumber = binding.idNumberEditText;
        email = binding.emailEditText;
        phone = binding.phoneEditText;
        password = binding.passwordEditText;
        confirmPassword = binding.confirmPasswordEditText;
        selectBirthDateButton = binding.selectBirthDateButton;
        selectImageButton = binding.selectImageButton;
        registerButton = binding.registerButton;
        weaponLicenseCheckBox = binding.weaponLicenseCheckBox;
        profileImageView = binding.profileImageView;
        medicalOptionsGroup = binding.medicalOptions;
        cityEditText = binding.cityEditText;
        // פותח את ממשק החיפוש של גוגל כאשר המשתמש לוחץ על שדה העיר
        cityEditText.setOnClickListener(v ->
                AutocompleteHelper.openCityAutocomplete(this, CITY_AUTOCOMPLETE_REQUEST));

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
            Toast.makeText(this, getString(R.string.fill_details_correctly), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, getString(R.string.error_generic, task.getException().getMessage()), Toast.LENGTH_SHORT).show();
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
        userData.put("city", selectedCity);
        userData.put("role", "משתמש");

        UserDataManager.createUser(uid, userData,
                () -> {
                    Log.d("Registration", "User data saved successfully");
                    Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                },
                e -> {
                    Log.e("Registration", "Failed to save user data: " + e.getMessage());
                    Toast.makeText(this, getString(R.string.error_saving_details), Toast.LENGTH_SHORT).show();
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

        if (valid && selectedCity.isEmpty()) {
            Toast.makeText(this, R.string.select_city_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        Log.d("Validation", "Inputs valid: " + (valid && !selectedCity.isEmpty()));
        return valid && !selectedCity.isEmpty();
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
        } else if (requestCode == CITY_AUTOCOMPLETE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                Place place = AutocompleteHelper.getPlaceFromResult(data);
                selectedCity = place.getName();
                cityEditText.setText(selectedCity);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR && data != null) {
                Status status = AutocompleteHelper.getErrorStatus(data);
                Toast.makeText(this, getString(R.string.error_select_city, status.getStatusMessage()), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadMedicalDetails() {
        UserDataManager.loadMedicalDetails(list -> {
            for (String name : list) {
                Chip chip = new Chip(this);
                chip.setText(name);
                chip.setCheckable(true);
                medicalOptionsGroup.addView(chip);
            }
        });
    }

    interface OnImageUploadListener {
        void onUploaded(String url);
    }
}
