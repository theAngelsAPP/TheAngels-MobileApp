package co.median.android.a2025_theangels_new.services;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.Map;

import co.median.android.a2025_theangels_new.models.UserSession;

public class UserDataManager {
    private static final String TAG = "UserDataManager";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * מחזיר את רשימת האירועים שיצר המשתמש המחובר
     */
    public static void getEventsCreatedByUser(String uid, Consumer<List<Map<String, Object>>> callback) {
        db.collection("events")
                .whereEqualTo("eventCreatedBy", uid)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Map<String, Object>> events = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        events.add(doc.getData());
                    }
                    callback.accept(events);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "שגיאה בשליפת אירועים שנוצרו על ידי המשתמש", e);
                    callback.accept(new ArrayList<>());
                });
    }

    /**
     * מחזיר את רשימת האירועים שבהם המשתמש טיפל כמתנדב
     */
    public static void getEventsHandledByUser(String uid, Consumer<List<Map<String, Object>>> callback) {
        db.collection("events")
                .whereEqualTo("eventHandleBy", uid)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Map<String, Object>> events = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        events.add(doc.getData());
                    }
                    callback.accept(events);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "שגיאה בשליפת אירועים שטופלו על ידי המשתמש", e);
                    callback.accept(new ArrayList<>());
                });
    }


    /**
     * טוען את נתוני המשתמש מהמסד ושומר אותם ב-UserSession
     */
    public static void loadUserDetails(String uid, Consumer<UserSession> callback) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(document -> {
                    if (document != null && document.exists()) {
                        String email = document.getString("Email");
                        String phone = document.getString("Phone");
                        String birthDate = document.getString("birthDate");
                        String city = document.getString("city");
                        String firstName = document.getString("firstName");
                        Boolean gun = document.getBoolean("haveGunLicense");
                        String idNumber = document.getString("idNumber");
                        String imageURL = document.getString("imageURL");
                        String lastName = document.getString("lastName");
                        List<String> medicalDetails = (List<String>) document.get("medicalDetails");
                        String role = document.getString("role");

                        UserSession.getInstance().initialize(
                                email, phone, birthDate, city,
                                firstName, gun != null && gun,
                                idNumber, imageURL, lastName,
                                medicalDetails, role);
                        callback.accept(UserSession.getInstance());
                    } else {
                        callback.accept(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "שגיאה בשליפת נתוני משתמש", e);
                    callback.accept(null);
                });
    }

    public static void getHandledEventsCount(String uid, Consumer<Integer> callback) {
        db.collection("events")
                .whereEqualTo("eventHandleBy", uid)
                .get()
                .addOnSuccessListener(querySnapshot -> callback.accept(querySnapshot.size()))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "שגיאה בספירת אירועים", e);
                    callback.accept(0);
                });
    }

    public static void getHandledEventsAverageRating(String uid, Consumer<Double> callback) {
        db.collection("events")
                .whereEqualTo("eventHandleBy", uid)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    double sum = 0;
                    int count = 0;
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Long rating = doc.getLong("eventRating");
                        if (rating != null) {
                            sum += rating;
                            count++;
                        }
                    }
                    callback.accept(count > 0 ? sum / count : 0.0);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "שגיאה בחישוב דירוג", e);
                    callback.accept(0.0);
                });
    }

    public static void updateUserDetails(String uid, Map<String, Object> updates, Consumer<Boolean> callback) {
        db.collection("users").document(uid).update(updates)
                .addOnSuccessListener(unused ->
                        loadUserDetails(uid, session -> callback.accept(true)))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "\u05e9\u05d2\u05d9\u05d0\u05d4 \u05d1\u05e2\u05d3\u05db\u05d5\u05df \u05e4\u05e8\u05d8\u05d9 \u05d4\u05de\u05e9\u05ea\u05de\u05e9", e);
                    callback.accept(false);
                });
    }

    public static void loadBasicUserInfo(String uid, Consumer<co.median.android.a2025_theangels_new.models.UserBasicInfo> callback) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(document -> {
                    if (document != null && document.exists()) {
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        String imageURL = document.getString("imageURL");
                        callback.accept(new co.median.android.a2025_theangels_new.models.UserBasicInfo(firstName, lastName, imageURL));
                    } else {
                        callback.accept(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "שגיאה בשליפת נתוני משתמש בסיסיים", e);
                    callback.accept(null);
                });
    }

}
