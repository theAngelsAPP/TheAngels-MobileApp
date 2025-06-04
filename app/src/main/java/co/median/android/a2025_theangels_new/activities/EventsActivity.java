//// =======================================
//// IMPORTS
//// =======================================
//package co.median.android.a2025_theangels_new.activities;
//
//import android.os.Bundle;
//import android.widget.ListView;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import co.median.android.a2025_theangels_new.R;
//import co.median.android.a2025_theangels_new.models.Event;
//
//// =======================================
//// EventsActivity - Displays the events screen and handles navigation to event details
//// =======================================
//public class EventsActivity extends BaseActivity {
//
//    private ListView events_list;
//    private ArrayList<Event> events;
//
//    private EventsAdapter adapter;
//
//    // =======================================
//    // onCreate - Initializes the events screen and sets click listener for event card
//    // =======================================
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        showTopBar(true);
//        showBottomBar(true);
//
//        events_list = findViewById(R.id.events_lv);
//        events = new ArrayList<>();
//
//
//
//        adapter = new EventsAdapter(this, R.layout.event, events);
//        events_list.setAdapter(adapter);
//
//
//
//    }
//
//    // =======================================
//    // getLayoutResourceId - Returns layout resource for this screen
//    // =======================================
//    @Override
//    protected int getLayoutResourceId() {
//        return R.layout.activity_events;
//    }
//
//}
