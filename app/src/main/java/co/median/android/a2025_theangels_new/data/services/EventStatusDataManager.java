/**
 * שירות לשליפת סטטוסי אירועים מהמסד.
 */
package co.median.android.a2025_theangels_new.data.services;

import android.util.Log;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

    /** תגית לוג */
public class EventStatusDataManager {
    private static final String TAG = "EventStatusDataManager";

    /**
     * קריאה חוזרת לאחר שליפת סטטוסים.
     */
    public interface EventStatusCallback {
        void onStatusesLoaded(Map<String, String> statusMap);
        void onError(Exception e);
    /**
     * טוען את כל הסטטוסים מקולקציית eventStatus.
     */
    }

    public static void getAllEventStatuses(EventStatusCallback callback) {
        FirebaseFirestore.getInstance().collection("eventStatus")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Map<String, String> map = new HashMap<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String name = doc.getString("statusName");
                        String color = doc.getString("statusColor");
                        if (name != null && color != null) {
                            map.put(name, color);
                        }
                    }
                    callback.onStatusesLoaded(map);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching event statuses", e);
                    callback.onError(e);
                });
    }
}
