package co.median.android.a2025_theangels_new.data.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.map.MapStyleHelper;

/**
 * פרגמנט הבית המציג מפה סטטית עם סימון מיקום המשתמש הנוכחי.
 * אם אין הרשאת מיקום, מוצג מסר ברור להפעלת השירות.
 */
public class HomeMapFragment extends Fragment implements OnMapReadyCallback {

    // =======================================
    // משתנים פנימיים
    // =======================================
    private GoogleMap mMap;
    private LocationService locationService;
    private Marker userMarker;
    private LinearLayout locationBox;

    private OnAddressChangeListener addressChangeListener;

    // משגר בקשת ההרשאה
    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showMap();
                } else {
                    showPermissionBox();
                }
            });

    /**
     * בנאי ברירת מחדל עם קישור לקובץ הפריסה.
     */
    public HomeMapFragment() {
        super(R.layout.fragment_home_map);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addressChangeListener = null;
    }

    /** ממשק לקבלת כתובת מהמפה */
    public interface OnAddressChangeListener {
        void onAddressChanged(String address);
    }

    /** מאפשר להצמיד מאזין לשינוי הכתובת */
    public void setAddressChangeListener(OnAddressChangeListener listener) {
        this.addressChangeListener = listener;
    }

    /**
     * אתחול רכיבי התצוגה והפעלת המפה הפנימית.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationService = new LocationService(requireContext());
        locationBox = view.findViewById(R.id.location_box);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // בקשת הרשאה בעת לחיצה על הקופסה
        locationBox.setOnClickListener(v ->
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    /**
     * הפעלת עיצוב המפה ובדיקת הרשאות כאשר המפה מוכנה.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        applyCustomStyle();
        checkPermission();
    }

    /**
     * בדיקת הרשאת מיקום וכיבוי/הפעלת התצוגה בהתאם.
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            showMap();
        } else {
            showPermissionBox();
        }
    }

    /**
     * מציג את המפה ומתחיל בקבלת המיקום העדכני.
     */
    private void showMap() {
        View mapView = requireView().findViewById(R.id.map);
        mapView.setVisibility(View.VISIBLE);
        locationBox.setVisibility(View.GONE);
        startLocationUpdates();
    }

    /**
     * מציג למשתמש את הקופסה המבקשת להפעיל שירותי מיקום.
     */
    private void showPermissionBox() {
        View mapView = requireView().findViewById(R.id.map);
        mapView.setVisibility(View.GONE);
        locationBox.setVisibility(View.VISIBLE);
        if (addressChangeListener != null) {
            addressChangeListener.onAddressChanged(getString(R.string.address_not_found));
        }
    }

    /**
     * התחלת האזנה לעדכוני מיקום מהמכשיר.
     */
    private void startLocationUpdates() {
        locationService.getCurrentLocation(new LocationService.SimpleLocationListener() {
            @Override
            public void onLocation(Location location) {
                updateUserLocation(location);
            }

            @Override
            public void onError(Exception e) {
                // במידה ויש שגיאה, פשוט לא נעדכן
            }
        });

        locationService.startLocationUpdates(new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                Location location = result.getLastLocation();
                if (location != null) {
                    updateUserLocation(location);
                }
            }
        });
    }

    /**
     * הפסקת האזנה לעדכוני המיקום בעת עצירת הפרגמנט.
     */
    private void stopLocationUpdates() {
        locationService.stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    /**
     * עדכון מיקום המשתמש על המפה ודיווח הכתובת אם קיימת.
     */
    private void updateUserLocation(Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        if (userMarker == null) {
            userMarker = MapHelper.addMarker(mMap, pos, getString(R.string.your_location));
        } else {
            userMarker.setPosition(pos);
        }
        MapHelper.moveCamera(mMap, pos, 15f);

        if (addressChangeListener != null) {
            String address = AddressHelper.getAddressFromLatLng(requireContext(), pos.latitude, pos.longitude);
            if (address == null) address = getString(R.string.address_not_found);
            addressChangeListener.onAddressChanged(address);
        }
    }

    /**
     * החלת סגנון מותאם ממקור המשאבים.
     */
    private void applyCustomStyle() {
        MapStyleHelper.applyStyle(mMap, requireContext(), R.raw.map_style);
    }
}
