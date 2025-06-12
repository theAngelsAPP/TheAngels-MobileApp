package co.median.android.a2025_theangels_new.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

import co.median.android.a2025_theangels_new.models.Event;

public class EventDataManager {

    private static final String TAG = "EventDataManager";

    public interface EventCallback {
        void onEventsLoaded(ArrayList<Event> events);
        void onError(Exception e);
    }

    public static void getAllEvents(EventCallback callback) {
        Log.d(TAG, "getAllEvents called - starting Firestore fetch");

        FirebaseFirestore.getInstance().collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Event> events = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        try {
                            Event event = doc.toObject(Event.class);
                            if (event != null) {
                                //Log.d(TAG, "Event loaded: " + event.getEventCase()); השורה הלא נכונה
                                Log.d(TAG, "Event loaded: " + event.getEventType());

                                events.add(event);
                            } else {
                                Log.w(TAG, "Event is null for document: " + doc.getId());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to convert document to Event: " + doc.getId(), e);
                        }
                    }

                    Log.d(TAG, "Total events fetched: " + events.size());
                    callback.onEventsLoaded(events);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching events from Firestore", e);
                    callback.onError(e);
                });
    }

    public static void getLastEventsCreatedByUser(String uid, int limit, EventCallback callback) {
        FirebaseFirestore.getInstance().collection("events")
                .whereEqualTo("eventCreatedBy", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Event> events = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Event event = doc.toObject(Event.class);
                        if (event != null) {
                            events.add(event);
                        }
                    }

                    Collections.sort(events, (e1, e2) -> {
                        if (e1.getEventTimeStarted() == null && e2.getEventTimeStarted() == null) {
                            return 0;
                        } else if (e1.getEventTimeStarted() == null) {
                            return 1;
                        } else if (e2.getEventTimeStarted() == null) {
                            return -1;
                        }
                        return e2.getEventTimeStarted().compareTo(e1.getEventTimeStarted());
                    });

                    if (events.size() > limit) {
                        events = new ArrayList<>(events.subList(0, limit));
                    }

                    callback.onEventsLoaded(events);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching recent events", e);
                    callback.onError(e);
                });
    }

    public interface SingleEventCallback {
        void onEventLoaded(Event event);
        void onError(Exception e);
    }

    public static void getEventByType(@NonNull String eventType, SingleEventCallback callback) {
        FirebaseFirestore.getInstance().collection("events")
                .whereEqualTo("eventType", eventType)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Event event = null;
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        event = doc.toObject(Event.class);
                        break;
                    }
                    callback.onEventLoaded(event);
                })
                .addOnFailureListener(callback::onError);
    }
}
