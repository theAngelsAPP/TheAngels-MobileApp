/**
 * שירות לשליפת ועדכון נתוני משתמשים במסד Firebase.
 */
package co.median.android.a2025_theangels_new.data.services;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.Map;

import co.median.android.a2025_theangels_new.data.models.UserSession;

public class UserDataManager {
    /** תגית לוג */
    private static final String TAG = "UserDataManager";
    /** מופע Firestore לשימוש פנימי */
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
                        java.util.Date birthDate = document.getDate("birthDate");
                        String city = document.getString("city");
                        String firstName = document.getString("firstName");
                        Boolean gun = document.getBoolean("haveGunLicense");
                        String idNumber = document.getString("idNumber");
                        String imageURL = document.getString("imageURL");
                        String lastName = document.getString("lastName");
                        List<String> medicalDetails = (List<String>) document.get("medicalDetails");
                        String role = document.getString("role");
                        java.util.List<String> volAvailable = (java.util.List<String>) document.get("volAvailable");
                        java.util.List<String> volCities = (java.util.List<String>) document.get("volCities");
                        Boolean volDriver = document.getBoolean("volHaveDriverLicense");
                        String volVerification = document.getString("volVerification");
                        java.util.List<String> volSpecialty = (java.util.List<String>) document.get("volSpecialty");

                        UserSession.getInstance().initialize(
                                email, phone, birthDate, city,
                                firstName, gun != null && gun,
                                idNumber, imageURL, lastName,
                                medicalDetails, role,
                                volAvailable, volCities,
                                volDriver, volVerification,
                                volSpecialty);
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

    /**
     * מחזיר את מספר האירועים שהמשתמש טיפל בהם
     */
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

    /**
     * מחשב את ממוצע הדירוג שקיבל המשתמש עבור האירועים שטיפל בהם
     */
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

    /**
     * מעדכן את פרטי המשתמש במסד הנתונים
     */
    public static void updateUserDetails(String uid, Map<String, Object> updates, Consumer<Boolean> callback) {
        db.collection("users").document(uid).update(updates)
                .addOnSuccessListener(unused ->
                        loadUserDetails(uid, session -> callback.accept(true)))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "שגיאה בעדכון פרטי המשתמש", e);
                    callback.accept(false);
                });
    }

    /**
     * טוען מידע בסיסי בלבד על משתמש
     */
    public static void loadBasicUserInfo(String uid, Consumer<co.median.android.a2025_theangels_new.data.models.UserSession> callback) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(document -> {
                    if (document != null && document.exists()) {
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        String imageURL = document.getString("imageURL");
                        callback.accept(new co.median.android.a2025_theangels_new.data.models.UserSession(firstName, lastName, imageURL));
                    } else {
                        callback.accept(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "שגיאה בשליפת נתוני משתמש בסיסיים", e);
                    callback.accept(null);
                });
    }

    /**
     * יוצר משתמש חדש במסד הנתונים.
     */
    public static void createUser(String uid, Map<String, Object> data, Runnable onSuccess, Consumer<Exception> onError) {
        db.collection("users").document(uid).set(data)
                .addOnSuccessListener(unused -> { if (onSuccess != null) onSuccess.run(); })
                .addOnFailureListener(e -> { if (onError != null) onError.accept(e); });
    }

    /**
     * טוען את רשימת אפשרויות המצב הרפואי הזמינות.
     */
    public static void loadMedicalDetails(Consumer<java.util.List<String>> callback) {
        db.collection("medicalDetails").get()
                .addOnSuccessListener(querySnapshot -> {
                    java.util.List<String> list = new java.util.ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String name = doc.getString("name");
                        if (name != null) list.add(name);
                    }
                    callback.accept(list);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "שגיאה בטעינת פרטי רפואה", e);
                    callback.accept(new java.util.ArrayList<>());
                });
    }
}
