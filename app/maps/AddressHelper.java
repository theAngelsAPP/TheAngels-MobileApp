package co.median.android.a2025_theangels_new.maps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * מחלקת עזר לביצוע המרות בין קואורדינטות לכתובת ולהפך.
 */
public class AddressHelper {

    /**
     * ממירה קואורדינטות לכתובת מלאה בעברית באמצעות Geocoder המקומי.
     *
     * @param context הקשר ממנו נקרא השירות
     * @param lat     קו רוחב
     * @param lng     קו אורך
     * @return מחרוזת כתובת אם נמצאה או null אחרת
     */
    public static String getAddressFromLatLng(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, new Locale("he"));
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * מקבל מחרוזת כתובת ומחזיר את נקודת המיקום המשוערת שלה.
     *
     * @param context הקשר ממנו מופעלת הקריאה
     * @param address כתובת חופשית בעברית
     * @return אובייקט LatLng של המיקום או null אם לא נמצא
     */
    public static LatLng getLatLngFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, new Locale("he"));
        try {
            List<Address> list = geocoder.getFromLocationName(address, 1);
            if (list != null && !list.isEmpty()) {
                Address addr = list.get(0);
                return new LatLng(addr.getLatitude(), addr.getLongitude());
            }
        } catch (IOException ignored) {
        }
        return null;
    }
}
