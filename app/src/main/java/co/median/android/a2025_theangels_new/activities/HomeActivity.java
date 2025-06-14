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
import com.google.firebase.auth.FirebaseAuth;
import co.median.android.a2025_theangels_new.services.UserDataManager;
import java.util.Locale;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import co.median.android.a2025_theangels_new.services.EventDataManager;
import co.median.android.a2025_theangels_new.services.EventTypeDataManager;
import co.median.android.a2025_theangels_new.services.EventStatusDataManager;
import co.median.android.a2025_theangels_new.models.EventType;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.MapFragment;
import co.median.android.a2025_theangels_new.models.UserSession;
import co.median.android.a2025_theangels_new.models.Event;
import co.median.android.a2025_theangels_new.activities.RecentEventsAdapter;

// =======================================
// HomeActivity - Displays the home screen and handles location permission logic
// =======================================
public class HomeActivity extends BaseActivity implements MapFragment.OnAddressChangeListener {

    // =======================================
    // VARIABLES
    // =======================================
    private LinearLayout locationPermissionContainer;
    private TextView tvLocationMessage, btnEnableLocation;
    private ImageView imgProfile;
    private TextView tvGreeting;
    private LinearLayout volDashboard;
    private TextView tvEventsCount, tvAvgRating;
    private FrameLayout mapContainer;
    private TextView tvCurrentAddress;
    private LinearLayout recentEventsContainer;
    private RecentEventsAdapter recentEventsAdapter;
    private ArrayList<Event> recentEvents = new ArrayList<>();
    private Map<String, String> typeImageMap = new HashMap<>();
    private Map<String, String> statusColorMap = new HashMap<>();

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
        volDashboard = findViewById(R.id.volDashboard);
        tvEventsCount = findViewById(R.id.tv_events_count);
        tvAvgRating = findViewById(R.id.tv_avg_rating);
        mapContainer = findViewById(R.id.map_container);
        tvCurrentAddress = findViewById(R.id.tv_current_address);
        recentEventsContainer = findViewById(R.id.recent_events_container);
        recentEventsAdapter = new RecentEventsAdapter(this, R.layout.item_recent_event, recentEvents);

        UserSession session = UserSession.getInstance();
        String fullName = session.getFirstName() + " " + session.getLastName();
        tvGreeting.setText("שלום, " + fullName);
        String url = session.getImageURL();
        if (url != null && !url.isEmpty()) {
            Glide.with(this).load(url).placeholder(R.drawable.newuserpic).into(imgProfile);
        }

        if ("מתנדב".equals(session.getRole())) {
            volDashboard.setVisibility(View.VISIBLE);
            loadVolunteerDashboardData();
        } else {
            volDashboard.setVisibility(View.GONE);
        }

        loadEventTypes();


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
            mapContainer.setVisibility(View.VISIBLE);
            if (getSupportFragmentManager().findFragmentById(R.id.map_container) == null) {
                loadMapFragment();
            }
        } else {
            mapContainer.setVisibility(View.GONE);
            showLocationRequestBanner();
        }
    }

    // =======================================
    // loadMapFragment - Loads the map fragment into the container
    // =======================================
    private void loadMapFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MapFragment fragment = new MapFragment();
        fragment.setAddressChangeListener(this);
        transaction.replace(R.id.map_container, fragment);
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
        mapContainer.setVisibility(View.VISIBLE);
        loadMapFragment();
    }

    private void loadVolunteerDashboardData() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserDataManager.getHandledEventsCount(uid, count ->
                tvEventsCount.setText(String.valueOf(count)));
        UserDataManager.getHandledEventsAverageRating(uid, avg ->
                tvAvgRating.setText(String.format(Locale.getDefault(), "%.1f", avg)));
    }

    private void loadEventTypes() {
        EventTypeDataManager.getAllEventTypes(new EventTypeDataManager.EventTypeCallback() {
            @Override
            public void onEventTypesLoaded(ArrayList<EventType> types) {
                for (EventType type : types) {
                    typeImageMap.put(type.getTypeName(), type.getTypeImageURL());
                }
                recentEventsAdapter.setEventTypeImages(typeImageMap);
                loadEventStatuses();
            }

            @Override
            public void onError(Exception e) {
                loadEventStatuses();
            }
        });
    }

    private void loadEventStatuses() {
        EventStatusDataManager.getAllEventStatuses(new EventStatusDataManager.EventStatusCallback() {
            @Override
            public void onStatusesLoaded(Map<String, String> statusMap) {
                statusColorMap.putAll(statusMap);
                recentEventsAdapter.setEventStatusColors(statusColorMap);
                loadRecentEvents();
            }

            @Override
            public void onError(Exception e) {
                loadRecentEvents();
            }
        });
    }

    private void loadRecentEvents() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        EventDataManager.getLastEventsCreatedByUser(uid, 3, new EventDataManager.EventCallback() {
            @Override
            public void onEventsLoaded(ArrayList<Event> events) {
                recentEvents.clear();
                recentEvents.addAll(events);
                displayRecentEvents();
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void displayRecentEvents() {
        recentEventsContainer.removeAllViews();
        for (int i = 0; i < recentEvents.size(); i++) {
            View item = recentEventsAdapter.getView(i, null, recentEventsContainer);
            recentEventsContainer.addView(item);
        }
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

    @Override
    public void onAddressChanged(String address) {
        if (tvCurrentAddress != null) {
            tvCurrentAddress.setText(address);
        }
    }
}