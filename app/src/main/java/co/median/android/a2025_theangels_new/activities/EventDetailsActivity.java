// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.StaticMapFragment;
import com.bumptech.glide.Glide;

// =======================================
// EventDetailsActivity - Displays event details screen including a static map
// =======================================
public class EventDetailsActivity extends BaseActivity {

    // =======================================
    // onCreate - Initializes the event details screen and loads the static map
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        showTopBar(false);
        showBottomBar(false);

        String eventType = getIntent().getStringExtra("eventType");
        String eventStatus = getIntent().getStringExtra("eventStatus");
        String handleBy = getIntent().getStringExtra("eventHandleBy");
        long timeStartedSeconds = getIntent().getLongExtra("eventTimeStarted", 0);
        int rating = getIntent().getIntExtra("eventRating", 0);
        double eventLat = getIntent().getDoubleExtra("lat", 0);
        double eventLng = getIntent().getDoubleExtra("lng", 0);
        String imageUrl = getIntent().getStringExtra("typeImageURL");

        TextView tvType = findViewById(R.id.event_type_text);
        Button btnNavigate = findViewById(R.id.btnNavigate);
        TextView tvVolunteer = findViewById(R.id.volunteer_name);
        RatingBar ratingBar = findViewById(R.id.ratingBarDetails);
        ImageView ivType = findViewById(R.id.event_type_image);

        if (tvType != null && eventType != null) {
            tvType.setText(eventType);
        }
        if (tvVolunteer != null && handleBy != null) {
            tvVolunteer.setText(handleBy);
        }
        if (imageUrl != null && ivType != null) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.event_medical).into(ivType);
        }
        if (ratingBar != null) {
            ratingBar.setRating(rating);
        }

        if (eventLat != 0 && eventLng != 0) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(eventLat, eventLng, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    String fullAddress = addresses.get(0).getAddressLine(0);
                    if (btnNavigate != null) btnNavigate.setText(fullAddress);
                }
            } catch (IOException ignored) {}
        }

        // Load StaticMapFragment with coordinates
        StaticMapFragment mapFragment = StaticMapFragment.newInstance(eventLat, eventLng);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment);
        transaction.commit();

        // Back button listener
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_details;
    }
}
