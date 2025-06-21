// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.data.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import co.median.android.a2025_theangels_new.R;

/**
 * פרגמנט המציג מפה ומאתר את מיקומו הנוכחי של המשתמש.
 * אם אין הרשאה למיקום, מוצג מסך חלופי המבקש גישה.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    // =======================================
    // VARIABLES
    // =======================================
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LinearLayout mapPlaceholder;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private OnAddressChangeListener addressChangeListener;

    // Request permission result handler
    private final ActivityResultLauncher<String> locationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    enableUserLocation();
                } else {
                    showPlaceholder();
                }
            });

    // =======================================
    // Constructor with layout binding
    // =======================================
    public MapFragment() {
        super(R.layout.fragment_map);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addressChangeListener = null;
    }

    public void setAddressChangeListener(OnAddressChangeListener listener) {
        this.addressChangeListener = listener;
    }

    public interface OnAddressChangeListener {
        void onAddressChanged(String address);
    }

    /**
     * מופעל לאחר יצירת תצוגת הפרגמנט ומאתחל את המפה והרכיבים השונים.
     * כאן גם נרשמת בקשת ההרשאה למיקום במידת הצורך.
     *
     * @param view               התצוגה של הפרגמנט
     * @param savedInstanceState מצב שמור אם קיים
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mapPlaceholder = view.findViewById(R.id.map_placeholder);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    updateMapLocation(location);
                }
            }
        };

        // Request permission when the placeholder is tapped
        mapPlaceholder.setOnClickListener(v ->
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * נקרא כאשר המפה נטענה ומוכנה לשימוש.
     * כאן מוחלים עיצוב מותאם ונבדקות הרשאות מיקום.
     *
     * @param googleMap מופע המפה שהתקבל
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        applyCustomMapStyle();
        checkLocationPermission();
    }

    // =======================================
    // applyCustomMapStyle - Applies custom map styling from raw/map_style.json
    // =======================================
    private void applyCustomMapStyle() {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
            if (!success) {
                System.out.println("Error applying map style.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =======================================
    // checkLocationPermission - Checks and handles location permission
    // =======================================
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
            showPlaceholder();
        }
    }

    /**
     * מפעיל את הצגת המיקום על המפה ומתחיל בקבלת עדכוני מיקום.
     * נקרא לאחר קבלת הרשאת מיקום מהמשתמש.
     */
    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            View mapView = requireView().findViewById(R.id.map);
            mapView.setVisibility(View.VISIBLE);
            mapPlaceholder.setVisibility(View.GONE);

            mMap.setMyLocationEnabled(true);
            startLocationUpdates();
        }
    }

    /**
     * מציג שכבת חלופה במקרה שהרשאת המיקום נדחתה על ידי המשתמש.
     * בנוסף מעדכן את המאזין בכתובת כללית שאין מיקום.
     */
    private void showPlaceholder() {
        View mapView = requireView().findViewById(R.id.map);
        mapView.setVisibility(View.GONE);
        mapPlaceholder.setVisibility(View.VISIBLE);
        if (addressChangeListener != null) {
            addressChangeListener.onAddressChanged(getString(R.string.address_not_found));
        }
    }

    /**
     * מבקש מהמכשיר את מיקומו האחרון הזמין ומעדכן את המפה בהתאם.
     * במקרה של כישלון יירשם בלוג אך לא תבוצע פעולה נוספת.
     */
    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        updateMapLocation(location);
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    /**
     * מתחיל קבלת עדכוני מיקום שוטפים מהמכשיר ומעדכן מיד את המפה.
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        getUserLocation();
    }

    /**
     * מפסיק את קבלת עדכוני המיקום כאשר הפרגמנט נעצר.
     */
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * כאשר הפרגמנט מפסיק לפעול מפסיקים גם את עדכוני המיקום כדי לחסוך משאבים.
     */
    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    /**
     * מעדכן את מיקום המשתמש על המפה ומרכז את התצוגה סביבו.
     * בנוסף מוצב סמן מותאם אישית ומדווח על הכתובת למאזין.
     *
     * @param location מיקום משתמש שנמצא
     */
    private void updateMapLocation(Location location) {
        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLatLng)
                .zoom(15)
                .tilt(30)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.clear();

        mMap.addMarker(new MarkerOptions()
                .position(userLatLng)
                .title(getString(R.string.your_location))
                .icon(resizeMarker(R.drawable.custom_marker, 130, 130)));

        if (addressChangeListener != null) {
            String address = getAddressFromLocation(location);
            addressChangeListener.onAddressChanged(address);
        }
    }

    /**
     * משנה את גודל האייקון של הסמן כדי שיוצג במפה בצורה נכונה.
     *
     * @param drawableRes מזהה המשאב של האייקון
     * @param width       רוחב נדרש בפיקסלים
     * @param height      גובה נדרש בפיקסלים
     * @return אובייקט ביטמאפ לאחר שינוי גודל
     */
    private BitmapDescriptor resizeMarker(int drawableRes, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(requireContext(), drawableRes);
        if (drawable == null) return null;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * ממיר את נקודת המיקום לכתובת קריאה למשתמש.
     * במקרה ואין כתובת זמינה מוחזר טקסט כללי.
     *
     * @param location מיקום שממנו מחפשים כתובת
     * @return מחרוזת כתובת תיאורית
     */
    private String getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException ignored) {}
        return getString(R.string.address_not_found);
    }
}
