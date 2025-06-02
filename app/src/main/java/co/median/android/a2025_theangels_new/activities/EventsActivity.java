// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.time.LocalDate;
import java.util.ArrayList;

import co.median.android.a2025_theangels_new.R;

// =======================================
// EventsActivity - Displays the events screen and handles navigation to event details
// =======================================
public class EventsActivity extends BaseActivity {

    private ListView events_list;
    private ArrayList<Event> events;

    private EventsAdapter adapter;

    // =======================================
    // onCreate - Initializes the events screen and sets click listener for event card
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);

        events_list = findViewById(R.id.events_lv);
        events = new ArrayList<>();

        events.add(new Event(R.drawable.event_medical,"חוסר הכרה","נחל צבי 55", LocalDate.of(2025, 6, 2),"אמיר כהן","נסגר"));
        events.add(new Event(R.drawable.event_medical,"חוסר הכרה","נחל צבי 55", LocalDate.of(2025, 6, 2),"אמיר כהן","נסגר"));
        events.add(new Event(R.drawable.event_medical,"חוסר הכרה","נחל צבי 55", LocalDate.of(2025, 6, 2),"אמיר כהן","נסגר"));
        events.add(new Event(R.drawable.event_medical,"חוסר הכרה","נחל צבי 55", LocalDate.of(2025, 6, 2),"אמיר כהן","נסגר"));


        adapter = new EventsAdapter(this, R.layout.event, events);
        events_list.setAdapter(adapter);



    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_events;
    }

}
