// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import co.median.android.a2025_theangels_new.R;

// =======================================
// PrivacySettingsActivity - Displays and manages privacy-related permissions
// =======================================
public class PrivacySettingsActivity extends AppCompatActivity {

    // =======================================
    // CONSTANTS
    // =======================================
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int CALL_PERMISSION_REQUEST_CODE = 1002;
    private static final int HEALTH_PERMISSION_REQUEST_CODE = 1003;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1004;

    // =======================================
    // onCreate - Initializes UI and each permission section
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        setupPermissionSection(
                R.id.location_permission_section,
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_PERMISSION_REQUEST_CODE,
                getString(R.string.location_permission_enabled),
                getString(R.string.location_permission_disabled),
                getString(R.string.location_permission_description),
                R.drawable.ic_location_on,
                R.drawable.ic_location_off
        );

        setupPermissionSection(
                R.id.call_permission_section,
                Manifest.permission.CALL_PHONE,
                CALL_PERMISSION_REQUEST_CODE,
                getString(R.string.call_permission_enabled),
                getString(R.string.call_permission_disabled),
                getString(R.string.call_permission_description),
                R.drawable.ic_phone_on,
                R.drawable.ic_phone_off
        );

        setupPermissionSection(
                R.id.health_permission_section,
                Manifest.permission.BODY_SENSORS,
                HEALTH_PERMISSION_REQUEST_CODE,
                getString(R.string.health_permission_enabled),
                getString(R.string.health_permission_disabled),
                getString(R.string.health_permission_description),
                R.drawable.ic_health_on,
                R.drawable.ic_health_off
        );

        setupPermissionSection(
                R.id.notification_permission_section,
                Manifest.permission.POST_NOTIFICATIONS,
                NOTIFICATION_PERMISSION_REQUEST_CODE,
                getString(R.string.notifications_enabled),
                getString(R.string.notifications_disabled),
                getString(R.string.notifications_description),
                R.drawable.ic_notifications_on,
                R.drawable.ic_notifications_off
        );
    }

    // =======================================
    // setupPermissionSection - Displays UI state and handles click for a permission block
    // =======================================
    private void setupPermissionSection(int sectionId, String permission, int requestCode,
                                        String enabledText, String disabledText, String descriptionText,
                                        int enabledIcon, int disabledIcon) {
        TextView status = findViewById(sectionId).findViewById(R.id.permission_status);
        ImageView icon = findViewById(sectionId).findViewById(R.id.permission_icon);
        TextView description = findViewById(sectionId).findViewById(R.id.permission_description);
        Button button = findViewById(sectionId).findViewById(R.id.btn_manage_permission);

        boolean isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;

        status.setText(isGranted ? enabledText : disabledText);
        icon.setImageResource(isGranted ? enabledIcon : disabledIcon);
        description.setText(descriptionText);
        button.setText(isGranted ? getString(R.string.manage_permission) : getString(R.string.allow_permission));

        button.setOnClickListener(v -> {
            if (!isGranted) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                openAppSettings();
            }
        });
    }

    // =======================================
    // openAppSettings - Opens the device's app-specific settings page
    // =======================================
    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    // =======================================
    // onRequestPermissionsResult - Called after permission dialog response; refresh UI
    // =======================================
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        recreate();
    }
}
