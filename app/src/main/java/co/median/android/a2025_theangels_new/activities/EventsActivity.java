package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.models.Event;
import co.median.android.a2025_theangels_new.services.EventDataManager;
import co.median.android.a2025_theangels_new.services.EventTypeDataManager;
import co.median.android.a2025_theangels_new.models.EventType;

public class EventsActivity extends BaseActivity {

    private static final String TAG = "EventsActivity";

    private ListView eventsListView;
    private EventsAdapter adapter;
    private ArrayList<Event> events;
    private Map<String, String> typeImageMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showTopBar(true);
        showBottomBar(true);

        eventsListView = findViewById(R.id.events_lv);
        events = new ArrayList<>();
        adapter = new EventsAdapter(this, R.layout.event, events);
        eventsListView.setAdapter(adapter);

        loadEventTypes();
    }

    private void loadEventTypes() {
        EventTypeDataManager.getAllEventTypes(new EventTypeDataManager.EventTypeCallback() {
            @Override
            public void onEventTypesLoaded(ArrayList<EventType> types) {
                for (EventType type : types) {
                    typeImageMap.put(type.getTypeName(), type.getTypeImageURL());
                }
                adapter.setEventTypeImages(typeImageMap);
                loadEventsFromFirestore();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error loading event types", e);
                loadEventsFromFirestore();
            }
        });
    }

    private void loadEventsFromFirestore() {
        Log.d(TAG, "Fetching events from Firestore...");
        EventDataManager.getAllEvents(new EventDataManager.EventCallback() {
            @Override
            public void onEventsLoaded(ArrayList<Event> loadedEvents) {
                Log.d(TAG, "evets loaded successfully. Count: " + loadedEvents.size());
                events.clear();
                events.addAll(loadedEvents);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Adapter updated with new trainings");
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error loading events from Firestore", e);
                Toast.makeText(EventsActivity.this, "שגיאה בטעינת האירועים", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_events;
    }
}
