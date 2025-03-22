// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.StaticMapFragment;

// =======================================
// EventDetailsActivity - Displays event details screen including a static map
// =======================================
public class EventDetailsActivity extends BaseActivity {

    // =======================================
    // onCreate - Initializes the event details screen and loads the static map
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        showTopBar(false);
        showBottomBar(false);

        // Coordinates for the event location
        double eventLat = 31.8912;
        double eventLng = 34.8115;

        // Load StaticMapFragment with coordinates
        StaticMapFragment mapFragment = StaticMapFragment.newInstance(eventLat, eventLng);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment);
        transaction.commit();

        // Back button listener
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_details;
    }
}
