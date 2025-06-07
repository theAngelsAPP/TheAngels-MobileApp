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
}
