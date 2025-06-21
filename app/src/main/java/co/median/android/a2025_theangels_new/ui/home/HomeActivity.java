// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.home;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import co.median.android.a2025_theangels_new.data.services.UserDataManager;
import java.util.Locale;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import co.median.android.a2025_theangels_new.data.services.MessageDataManager;
import co.median.android.a2025_theangels_new.data.services.EducationDataManager;
import co.median.android.a2025_theangels_new.data.models.Message;
import co.median.android.a2025_theangels_new.data.models.MessageType;
import co.median.android.a2025_theangels_new.ui.home.MessagesAdapter.OnMessageClickListener;
import co.median.android.a2025_theangels_new.data.models.Education;
import co.median.android.a2025_theangels_new.ui.educations.EducationDetailsActivity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import co.median.android.a2025_theangels_new.data.services.EventDataManager;
import co.median.android.a2025_theangels_new.data.services.EventTypeDataManager;
import co.median.android.a2025_theangels_new.data.models.EventType;
import com.google.firebase.firestore.ListenerRegistration;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.map.HomeMapFragment;
import co.median.android.a2025_theangels_new.data.models.UserSession;
import co.median.android.a2025_theangels_new.data.models.Event;
import co.median.android.a2025_theangels_new.ui.events.list.RecentEventsAdapter;
import co.median.android.a2025_theangels_new.ui.main.BaseActivity;
import co.median.android.a2025_theangels_new.ui.home.OpenEventsAdapter;

// =======================================
// HomeActivity - Displays the home screen and handles location permission logic
// =======================================
public class HomeActivity extends BaseActivity implements HomeMapFragment.OnAddressChangeListener {

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
    private LinearLayout messagesContainer;
    private MessagesAdapter messagesAdapter;
    private ArrayList<Message> messages = new ArrayList<>();
    private LinearLayout openEventsWidget;
    private LinearLayout openEventsContainer;
    private OpenEventsAdapter openEventsAdapter;
    private ArrayList<Event> openEvents = new ArrayList<>();
    private ArrayList<String> openEventIds = new ArrayList<>();
    private ListenerRegistration openEventsListener;

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
        messagesContainer = findViewById(R.id.messages_container);
        messagesAdapter = new MessagesAdapter(this, R.layout.message_item, messages);
        openEventsWidget = findViewById(R.id.vol_active_events_widget);
        openEventsContainer = findViewById(R.id.open_events_container);
        openEventsAdapter = new OpenEventsAdapter(this, R.layout.item_open_event, openEvents, openEventIds);

        UserSession session = UserSession.getInstance();
        String fullName = session.getFirstName() + " " + session.getLastName();
        tvGreeting.setText("שלום, " + fullName);
        String url = session.getImageURL();
        if (url != null && !url.isEmpty()) {
            Glide.with(this).load(url).placeholder(R.drawable.newuserpic).into(imgProfile);
        }

        if ("מתנדב".equals(session.getRole())) {
            volDashboard.setVisibility(View.VISIBLE);
            openEventsWidget.setVisibility(View.VISIBLE);
            loadVolunteerDashboardData();
            startOpenEventsListener();
        } else {
            volDashboard.setVisibility(View.GONE);
            openEventsWidget.setVisibility(View.GONE);
        }

        loadEventTypes();
        loadMessages();


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
        HomeMapFragment fragment = new HomeMapFragment();
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

    private void loadMessages() {
        messagesAdapter.setOnMessageClickListener(this::handleMessageClick);
        MessageDataManager.getMessagesWithTypes(new MessageDataManager.MessagesCallback() {
            @Override
            public void onMessagesLoaded(ArrayList<Message> msgs, Map<String, MessageType> types) {
                messages.clear();
                messages.addAll(msgs);
                messagesAdapter.setTypeMap(types);
                displayMessages();
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void displayMessages() {
        if (messagesContainer == null) return;
        messagesContainer.removeAllViews();
        if (messages.isEmpty()) {
            messagesContainer.setVisibility(View.GONE);
            return;
        }
        messagesContainer.setVisibility(View.VISIBLE);
        for (int i = 0; i < messages.size(); i++) {
            View item = messagesAdapter.getView(i, null, messagesContainer);
            messagesContainer.addView(item);
        }
    }

    private void startOpenEventsListener() {
        if (openEventsListener != null) openEventsListener.remove();
        openEventsListener = EventDataManager.listenToActiveEvents((ids, events) -> {
            openEventIds.clear();
            openEvents.clear();
            openEventIds.addAll(ids);
            openEvents.addAll(events);
            displayOpenEvents();
        });
    }

    private void displayOpenEvents() {
        if (openEventsContainer == null) return;
        openEventsContainer.removeAllViews();
        if (openEvents.isEmpty()) {
            openEventsWidget.setVisibility(View.GONE);
            openEventsAdapter.stopTimers();
            return;
        }
        openEventsWidget.setVisibility(View.VISIBLE);
        openEventsAdapter.startTimers();
        for (int i = 0; i < openEvents.size(); i++) {
            View item = openEventsAdapter.getView(i, null, openEventsContainer);
            openEventsContainer.addView(item);
        }
    }

    private void handleMessageClick(Message message) {
        String ref = message.getMessageRef();
        if (ref == null || ref.isEmpty()) {
            return;
        }

        EducationDataManager.getEducationById(ref, new EducationDataManager.SingleEducationCallback() {
            @Override
            public void onEducationLoaded(Education education) {
                if (education != null) {
                    Intent intent = new Intent(HomeActivity.this, EducationDetailsActivity.class);
                    intent.putExtra("id", ref);
                    intent.putExtra("eduTitle", education.getEduTitle());
                    intent.putExtra("eduData", education.getEduData());
                    intent.putExtra("eduImageURL", education.getEduImageURL());
                    intent.putExtra("eduType", education.getEduType());
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, R.string.education_not_found, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(HomeActivity.this, R.string.education_not_found, Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (openEventsListener != null) {
            openEventsListener.remove();
        }
        openEventsAdapter.stopTimers();
    }
}