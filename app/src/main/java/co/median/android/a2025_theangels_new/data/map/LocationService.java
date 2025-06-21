package co.median.android.a2025_theangels_new.data.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 * שירות לקבלת מיקום מהמכשיר וניהול עדכוני מיקום חיים.
 */
public class LocationService {

    private final FusedLocationProviderClient fusedClient;
    private LocationRequest locationRequest;
    private LocationCallback internalCallback;

    public interface SimpleLocationListener {
        void onLocation(Location location);
        void onError(Exception e);
    }

    /**
     * בנאי המקבל הקשר ומשתמש ב-FusedLocationProviderClient לקבלת מיקום.
     *
     * @param context הקשר ממנו מופעל השירות
     */
    public LocationService(Context context) {
        fusedClient = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * מביא את המיקום העכשווי של המשתמש בבקשה אחת.
     * אם אין מיקום אחרון זמין יבוצע ניסיון לקבלת עדכון בודד.
     *
     * @param listener מאזין לקבלת התוצאה או שגיאה
     */
    @SuppressLint("MissingPermission")
    public void getCurrentLocation(SimpleLocationListener listener) {
        fusedClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        listener.onLocation(location);
                    } else {
                        startSingleUpdate(listener);
                    }
                })
                .addOnFailureListener(listener::onError);
    }

    @SuppressLint("MissingPermission")
    private void startSingleUpdate(SimpleLocationListener listener) {
        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                fusedClient.removeLocationUpdates(this);
                if (result != null && !result.getLocations().isEmpty()) {
                    listener.onLocation(result.getLastLocation());
                } else {
                    listener.onError(new Exception("Location unavailable"));
                }
            }
        };
        fusedClient.requestLocationUpdates(locationRequest, callback, null);
    }

    /**
     * מתחיל האזנה לעדכוני מיקום רציפים.
     *
     * @param callback קריאה חוזרת שתופעל בכל עדכון מיקום
     */
    @SuppressLint("MissingPermission")
    public void startLocationUpdates(LocationCallback callback) {
        internalCallback = callback;
        fusedClient.requestLocationUpdates(locationRequest, callback, null);
    }

    /**
     * מפסיק את האזנה לעדכוני המיקום.
     */
    public void stopLocationUpdates() {
        if (internalCallback != null) {
            fusedClient.removeLocationUpdates(internalCallback);
        }
    }
}
