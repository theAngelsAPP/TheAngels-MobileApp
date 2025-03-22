package co.median.android.a2025_theangels_new.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.MapFragment;

public class HomeActivity extends BaseActivity {

    private LinearLayout locationPermissionContainer;
    private TextView tvLocationMessage, btnEnableLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);

        locationPermissionContainer = findViewById(R.id.location_permission_container);
        tvLocationMessage = findViewById(R.id.tv_location_message);
        btnEnableLocation = findViewById(R.id.btn_enable_location);
        checkLocationPermission();

        btnEnableLocation.setOnClickListener(v -> requestLocationPermission());
    }

    private void checkLocationPermission() {
        loadMapFragment();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            hideLocationRequestBanner();
        } else {
            showLocationRequestBanner();
        }
    }

    private void loadMapFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, new MapFragment());
        transaction.commit();
    }


    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
        }
    }


    private void showLocationRequestBanner() {
        locationPermissionContainer.setVisibility(View.VISIBLE);
        tvLocationMessage.setText("יש לאפשר גישה למיקומכם");
        btnEnableLocation.setText("לחץ כאן לאפשר הרשאות");
    }

    private void hideLocationRequestBanner() {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(500);
        fadeOut.setFillAfter(true);
        locationPermissionContainer.startAnimation(fadeOut);
        locationPermissionContainer.setVisibility(View.GONE);
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void showMap() {
        hideLocationRequestBanner();
        loadMapFragment();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1002) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMap();
            } else {
                tvLocationMessage.setText("ניתן לאפשר את המיקום בהגדרות המכשיר.");
                btnEnableLocation.setText("פתח הגדרות");
                btnEnableLocation.setOnClickListener(v -> openAppSettings());
            }
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }
}
