package co.median.android.a2025_theangels_new.data;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * מחלקת היישום הראשית של האפליקציה.
 * כאן מתבצעת אתחול Firebase והגדרות בסיסיות בעת הפעלת האפליקציה.
 */
public class MyApplication extends Application {
    /**
     * מופעלת עם פתיחת האפליקציה ומבצעת אתחול של Firebase.
     * בנוסף מוגדרת שמירת נתונים מקומית כדי לאפשר עבודה גם ללא רשת.
     */
    @Override
    public void onCreate() {
        // Configure Flogger to use the Android backend before any
        // libraries emit log messages. This prevents the frequent
        // "Too many Flogger logs received before configuration" warnings
        // that were appearing in Logcat.
        System.setProperty(
                "flogger.backend_factory",
                "com.google.common.flogger.backend.android.AndroidBackendFactory");

        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
    }
}
