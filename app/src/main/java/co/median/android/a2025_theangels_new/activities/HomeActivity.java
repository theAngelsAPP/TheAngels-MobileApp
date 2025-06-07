// =======================================
// IMPORTS
// =======================================
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.MapFragment;
import co.median.android.a2025_theangels_new.models.UserSession;

// =======================================
// HomeActivity - Displays the home screen and handles location permission logic
// =======================================
public class HomeActivity extends BaseActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private LinearLayout locationPermissionContainer;
    private TextView tvLocationMessage, btnEnableLocation;
    private ImageView imgProfile;
    private TextView tvGreeting;

    // =======================================
    // onCreate - Initializes UI and checks for location permission
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);

        locationPermissionContainer = findViewById(R.id.location_permission_container);
        tvLocationMessage = findViewById(R.id.tv_location_message);
        btnEnableLocation = findViewById(R.id.btn_enable_location);
        imgProfile = findViewById(R.id.img_profile);
        tvGreeting = findViewById(R.id.tv_greeting);

        UserSession session = UserSession.getInstance();
        String fullName = session.getFirstName() + " " + session.getLastName();
        tvGreeting.setText("שלום, " + fullName);
        String url = session.getImageURL();
        if (url != null && !url.isEmpty()) {
            Glide.with(this).load(url).placeholder(R.drawable.newuserpic).into(imgProfile);
        }

        checkLocationPermission();

        btnEnableLocation.setOnClickListener(v -> requestLocationPermission());
    }

    // =======================================
    // checkLocationPermission - Checks permission and displays map or request UI accordingly
    // =======================================
    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            hideLocationRequestBanner();
            if (getSupportFragmentManager().findFragmentById(R.id.map_container) == null) {
                loadMapFragment();
            }
        } else {
            showLocationRequestBanner();
        }
    }

    // =======================================
    // loadMapFragment - Loads the map fragment into the container
    // =======================================
    private void loadMapFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, new MapFragment());
        transaction.commit();
    }

    // =======================================
    // requestLocationPermission - Requests location permission from the user
    // =======================================
    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
        }
    }

    // =======================================
    // showLocationRequestBanner - Shows banner asking for location permission
    // =======================================
    private void showLocationRequestBanner() {
        locationPermissionContainer.setVisibility(View.VISIBLE);
        tvLocationMessage.setText(getString(R.string.location_permission_message));
        btnEnableLocation.setText(getString(R.string.enable_permission_button));
    }

    // =======================================
    // hideLocationRequestBanner - Hides the location permission banner with animation
    // =======================================
    private void hideLocationRequestBanner() {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(500);
        fadeOut.setFillAfter(true);
        locationPermissionContainer.startAnimation(fadeOut);
        locationPermissionContainer.setVisibility(View.GONE);
    }

    // =======================================
    // openAppSettings - Opens the device settings for the current app
    // =======================================
    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    // =======================================
    // showMap - Hides permission banner and loads the map fragment
    // =======================================
    private void showMap() {
        hideLocationRequestBanner();
        loadMapFragment();
    }

    // =======================================
    // onRequestPermissionsResult - Handles result of location permission request
    // =======================================
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1002) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMap();
            } else {
                tvLocationMessage.setText(getString(R.string.location_permission_settings_message));
                btnEnableLocation.setText(getString(R.string.open_settings_button));
                btnEnableLocation.setOnClickListener(v -> openAppSettings());
            }
        }
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }
}