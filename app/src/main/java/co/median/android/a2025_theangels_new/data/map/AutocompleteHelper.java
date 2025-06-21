package co.median.android.a2025_theangels_new.data.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.gms.common.api.Status;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import co.median.android.a2025_theangels_new.R;

/**
 * כלי עזר להפעלת ממשק ההשלמה האוטומטית של Google Places
 * עבור ערים וכתובות.
 */
public class AutocompleteHelper {

    /**
     * אתחול ספריית Places במידת הצורך.
     * יש לקרוא לפונקציה זו לפני כל שימוש ב-Places API.
     *
     * @param context הקשר ממנו נקרא השירות
     */
    public static void initPlaces(Context context) {
        if (!Places.isInitialized()) {
            Places.initialize(context.getApplicationContext(),
                    context.getString(R.string.google_places_key), new Locale("he"));
        }
    }

    /**
     * פותחת מסך השלמה אוטומטית המאפשר בחירת עיר אמיתית מתוך רשימת הערים.
     * התוצאה תתקבל ב-onActivityResult של האקטיביטי הקורא.
     *
     * @param activity    האקטיביטי שמפעיל את המסך
     * @param requestCode קוד הבקשה שישמש לזיהוי התוצאה
     */
    public static void openCityAutocomplete(Activity activity, int requestCode) {
        initPlaces(activity);
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setTypeFilter(TypeFilter.CITIES)
                .setCountries(Arrays.asList("IL"))
                .build(activity);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * פותחת מסך השלמה אוטומטית לבחירת כתובת מלאה בישראל.
     * התוצאה מכילה את מיקום הנקודה והכתובת המלאה בעברית.
     *
     * @param activity    האקטיביטי שמפעיל את המסך
     * @param requestCode קוד הזיהוי לתוצאה
     */
    public static void openAddressAutocomplete(Activity activity, int requestCode) {
        initPlaces(activity);
        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountries(Arrays.asList("IL"))
                .build(activity);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * שולפת את ה-Place שנבחר מתוך Intent התוצאה של האוטוקומפליט.
     *
     * @param data Intent שהוחזר ב-onActivityResult
     * @return האובייקט שנבחר או null במקרה של כישלון
     */
    public static Place getPlaceFromResult(Intent data) {
        return Autocomplete.getPlaceFromIntent(data);
    }

    /**
     * שולפת סטטוס שגיאה במקרה והמשתמש סגר את המסך ללא בחירה או אירעה תקלה.
     *
     * @param data Intent שהוחזר ב-onActivityResult
     * @return סטטוס המתאר את השגיאה
     */
    public static Status getErrorStatus(Intent data) {
        return Autocomplete.getStatusFromIntent(data);
    }
}
