// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import co.median.android.a2025_theangels_new.R;

// =======================================
// EventsActivity - Displays the events screen and handles navigation to event details
// =======================================
public class EventsActivity extends BaseActivity {

    // =======================================
    // onCreate - Initializes the events screen and sets click listener for event card
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);

        // Find the medical event card layout
        LinearLayout medicalEvent = findViewById(R.id.medical_event_card);

        // Handle click on the medical event card
        medicalEvent.setOnClickListener(v -> {
            Intent intent = new Intent(EventsActivity.this, EventDetailsActivity.class);
            startActivity(intent);
        });
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_events;
    }
}
