package co.median.android.a2025_theangels_new.maps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * פונקציות עזר להצגת מפות וניהול סמנים.
 */
public class MapHelper {

    /**
     * מוסיפה סמן חדש למפה במיקום המבוקש.
     *
     * @param map   מופע המפה עליו יוצג הסמן
     * @param pos   מיקום הסמן
     * @param title כותרת לטקסט הכלי
     * @return הסמן שנוצר
     */
    public static Marker addMarker(GoogleMap map, LatLng pos, String title) {
        MarkerOptions options = new MarkerOptions().position(pos).title(title);
        return map.addMarker(options);
    }

    /**
     * מעדכנת את מיקום המצלמה ומקרבת לזום נתון.
     *
     * @param map  המפה לשינוי תצוגה
     * @param pos  מיקום מרכזי
     * @param zoom רמת הזום המבוקשת
     */
    public static void moveCamera(GoogleMap map, LatLng pos, float zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
    }

    /**
     * מציגה על המפה את מיקום המשתמש והמתנדב בזמן אמת.
     *
     * @param map          מפה קיימת להצגה
     * @param userMarker   סמן קיים של המשתמש (יכול להיות null)
     * @param volunteerMarker סמן קיים של המתנדב (יכול להיות null)
     * @param userPos      מיקום נוכחי של המשתמש
     * @param volunteerPos מיקום נוכחי של המתנדב
     * @return מערך עם הסמנים המעודכנים [user, volunteer]
     * הערה: במודל Event יש לשמור את eventLocation מסוג GeoPoint ואת כתובת האירוע
     *       במידה ותרצו להשתמש בהמשך לניווט.
     */
    public static Marker[] updateLiveMarkers(GoogleMap map,
                                             Marker userMarker,
                                             Marker volunteerMarker,
                                             LatLng userPos,
                                             LatLng volunteerPos) {
        if (userMarker == null) {
            userMarker = addMarker(map, userPos, "User");
        } else {
            userMarker.setPosition(userPos);
        }

        if (volunteerMarker == null) {
            volunteerMarker = addMarker(map, volunteerPos, "Volunteer");
        } else {
            volunteerMarker.setPosition(volunteerPos);
        }
        return new Marker[]{userMarker, volunteerMarker};
    }

    /**
     * יוצר Intent לפתיחת אפליקציית ניווט חיצונית למיקום המבוקש.
     *
     * @param context הקשר ממנו מופעל
     * @param lat     קו רוחב היעד
     * @param lng     קו אורך היעד
     */
    public static void openNavigation(Context context, double lat, double lng) {
        Uri uri = Uri.parse("google.navigation:q=" + lat + "," + lng + "&mode=w");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }
}
