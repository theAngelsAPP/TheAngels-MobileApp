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

public class PrivacySettingsActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int CALL_PERMISSION_REQUEST_CODE = 1002;
    private static final int HEALTH_PERMISSION_REQUEST_CODE = 1003;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        setupPermissionSection(
                R.id.location_permission_section,
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_PERMISSION_REQUEST_CODE,
                "שיתוף מיקומך בזמן אמת פעיל",
                "שיתוף מיקום בזמן אמת לא פעיל",
                "שיתוף מיקומך מאפשר לנו לעזור בעת הצורך",
                R.drawable.ic_location_on,
                R.drawable.ic_location_off
        );

        setupPermissionSection(
                R.id.call_permission_section,
                Manifest.permission.CALL_PHONE,
                CALL_PERMISSION_REQUEST_CODE,
                "אפשרות לבצע שיחות חירום פעילה",
                "שיחות חירום אינן זמינות",
                "אפשר לבצע שיחות לגורמי ביטחון במקרה הצורך",
                R.drawable.ic_phone_on,
                R.drawable.ic_phone_off
        );

        setupPermissionSection(
                R.id.health_permission_section,
                Manifest.permission.BODY_SENSORS,
                HEALTH_PERMISSION_REQUEST_CODE,
                "גישה למידע רפואי מאופשרת",
                "גישה למידע רפואי חסומה",
                "מאפשר לנו לסנכרן עם אפליקציות כושר ובריאות",
                R.drawable.ic_health_on,
                R.drawable.ic_health_off
        );

        setupPermissionSection(
                R.id.notification_permission_section,
                Manifest.permission.POST_NOTIFICATIONS,
                NOTIFICATION_PERMISSION_REQUEST_CODE,
                "התראות האפליקציה מופעלות",
                "התראות האפליקציה חסומות",
                "אם תאפשר הרשאות התראות, תוכל לקבל עדכונים חשובים בזמן אמת",
                R.drawable.ic_notifications_on,
                R.drawable.ic_notifications_off
        );
    }

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
        button.setText(isGranted ? "נהל הרשאות" : "אפשר הרשאה");

        button.setOnClickListener(v -> {
            if (!isGranted) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                openAppSettings();
            }
        });
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        recreate();
    }
}
