package co.median.android.a2025_theangels_new.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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
                .orderBy("eventTimeStarted", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Event> events = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Event event = doc.toObject(Event.class);
                        if (event != null) {
                            events.add(event);
                        }
                    }
                    callback.onEventsLoaded(events);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching recent events", e);
                    callback.onError(e);
                });
    }
}
