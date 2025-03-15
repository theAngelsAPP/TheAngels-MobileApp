package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import co.median.android.a2025_theangels_new.R;

public class EventsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);

        // איתור המלבן של האירוע הרפואי
        LinearLayout medicalEvent = findViewById(R.id.medical_event_card);

        // האזנה ללחיצה על האירוע הרפואי - פתיחת EventDetailsActivity
        medicalEvent.setOnClickListener(v -> {
            Intent intent = new Intent(EventsActivity.this, EventDetailsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_events;
    }
}
