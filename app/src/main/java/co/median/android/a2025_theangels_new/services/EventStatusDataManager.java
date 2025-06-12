package co.median.android.a2025_theangels_new.services;

import android.util.Log;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EventStatusDataManager {
    private static final String TAG = "EventStatusDataManager";

    public interface EventStatusCallback {
        void onStatusesLoaded(Map<String, String> statusMap);
        void onError(Exception e);
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
