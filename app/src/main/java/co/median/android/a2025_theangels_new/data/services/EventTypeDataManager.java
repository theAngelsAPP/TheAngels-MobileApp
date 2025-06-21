package co.median.android.a2025_theangels_new.data.services;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import co.median.android.a2025_theangels_new.data.models.EventType;

/**
 * מנהל שליפת סוגי אירועים ממסד הנתונים של Firebase.
 */
public class EventTypeDataManager {

    private static final String TAG = "EventTypeDataManager";

    /**
     * ממשק החוזר מידע לאחר השליפה או שגיאה במקרה של כישלון.
     */
    public interface EventTypeCallback {
        /**
         * נקרא כאשר הרשימה נטענה בהצלחה.
         * @param types רשימת סוגי האירועים שהתקבלה
         */
        void onEventTypesLoaded(ArrayList<EventType> types);

        /**
         * נקרא כאשר התרחשה שגיאה בשליפה.
         * @param e הפרט על השגיאה
         */
        void onError(Exception e);
    }

    /**
     * טוען את כל סוגי האירועים ממסד הנתונים של Firebase.
     *
     * @param callback ממשק לקבלת התוצאות או שגיאה
     */
    public static void getAllEventTypes(EventTypeCallback callback) {
        Log.d(TAG, "getAllEventTypes called - starting Firestore fetch");
        FirebaseFirestore.getInstance().collection("eventsType")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<EventType> types = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        try {
                            EventType type = doc.toObject(EventType.class);
                            if (type != null) {
                                Log.d(TAG, "EventType loaded: " + type.getTypeName());
                                types.add(type);
                            } else {
                                Log.w(TAG, "EventType is null for document: " + doc.getId());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to convert document to EventType: " + doc.getId(), e);
                        }
                    }
                    Log.d(TAG, "Total event types fetched: " + types.size());
                    callback.onEventTypesLoaded(types);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching event types from Firestore", e);
                    callback.onError(e);
                });
    }
}
