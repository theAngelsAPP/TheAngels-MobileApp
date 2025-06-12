package co.median.android.a2025_theangels_new;

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
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
    }
}
