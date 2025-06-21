package co.median.android.a2025_theangels_new.data.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

import co.median.android.a2025_theangels_new.data.models.Event;

/**
 * אחראי לשליפת אירועים ממסד הנתונים של Firebase.
 */
public class EventDataManager {

    /** תגית לוג */
    private static final String TAG = "EventDataManager";

    /**
     * ממשק לקבלת רשימת אירועים או הודעת שגיאה.
     */
    public interface EventCallback {
        /**
         * נקרא עם סיום השליפה המוצלחת.
         * @param events רשימת האירועים שהתקבלה
         */
        void onEventsLoaded(ArrayList<Event> events);

        /**
         * נקרא במקרה של שגיאה.
         * @param e פירוט השגיאה
         */
        void onError(Exception e);
    }

    /**
     * טוען את כל האירועים הקיימים במסד הנתונים.
     *
     * @param callback יוזם התוצאה לקבלה
     */
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

    /**
     * מביא את האירועים האחרונים שנוצרו על ידי משתמש מסוים.
     *
     * @param uid      מזהה המשתמש המבוקש
     * @param limit    כמות האירועים המקסימלית להחזרה
     * @param callback מחזיר התוצאה
     */
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

    /**
     * ממשק לקבלת אירוע בודד או הודעת שגיאה.
     */
    public interface SingleEventCallback {
        /**
         * אירוע שנמצא נשלח לכאן.
         * @param event האירוע שהתקבל או null אם לא נמצא
         */
        void onEventLoaded(Event event);

        /**
         * קריאה במקרה של תקלה בשליפה.
         * @param e פירוט השגיאה
         */
        void onError(Exception e);
    }

    /**
     * מחפש אירוע ראשון התואם לסוג מסוים.
     *
     * @param eventType סוג האירוע הרצוי
     * @param callback  יקבל את האירוע או שגיאה
     */
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

    /** Callback returning a string value on success. */
    public interface StringCallback {
        void onSuccess(String value);
    }

    /** Callback returning an error on failure. */
    public interface ErrorCallback {
        void onError(Exception e);
    }

    /**
     * Creates a new event document in Firestore.
     *
     * @param data      event fields to store
     * @param onSuccess called with the created document id
     * @param onError   called when an error occurs
     */
    public static void createNewEvent(@NonNull java.util.Map<String, Object> data,
                                      StringCallback onSuccess,
                                      ErrorCallback onError) {
        FirebaseFirestore.getInstance().collection("events")
                .add(data)
                .addOnSuccessListener(docRef -> {
                    if (onSuccess != null) onSuccess.onSuccess(docRef.getId());
                })
                .addOnFailureListener(e -> {
                    if (onError != null) onError.onError(e);
                });
    }

    /**
     * Starts listening for real-time updates of the given event.
     *
     * @param eventId  ID of the event document
     * @param listener Firestore snapshot listener
     * @return ListenerRegistration to remove the listener
     */
    public static com.google.firebase.firestore.ListenerRegistration listenToEvent(
            @NonNull String eventId,
            com.google.firebase.firestore.EventListener<DocumentSnapshot> listener) {
        return FirebaseFirestore.getInstance()
                .collection("events")
                .document(eventId)
                .addSnapshotListener(listener);
    }
}
