// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.events.list;

import android.os.Bundle;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.map.StaticMapFragment;
import co.median.android.a2025_theangels_new.data.models.Event;
import co.median.android.a2025_theangels_new.data.models.UserBasicInfo;
import co.median.android.a2025_theangels_new.data.services.EventDataManager;
import co.median.android.a2025_theangels_new.data.services.UserDataManager;
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
        String handleBy = getIntent().getStringExtra("eventHandleBy");
        int rating = getIntent().getIntExtra("eventRating", 0);
        double eventLat = getIntent().getDoubleExtra("lat", 0);
        double eventLng = getIntent().getDoubleExtra("lng", 0);
        String imageUrl = getIntent().getStringExtra("typeImageURL");

        TextView tvType = findViewById(R.id.event_type_text);
        Button btnNavigate = findViewById(R.id.btnNavigate);
        TextView tvVolunteer = findViewById(R.id.volunteer_name);
        ImageView volunteerImage = findViewById(R.id.volunteer_image);
        RatingBar ratingBar = findViewById(R.id.ratingBarDetails);
        ImageView ivType = findViewById(R.id.event_type_image);
        LinearLayout findingsContainer = findViewById(R.id.findings_container);
        TextView durationText = findViewById(R.id.duration_text);
        TextView summaryStart = findViewById(R.id.summary_start_time);
        TextView summaryVolunteer = findViewById(R.id.summary_volunteer);
        TextView summaryEnd = findViewById(R.id.summary_end_time);
        TextView closeReasonText = findViewById(R.id.close_reason_text);

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

        EventDataManager.getEventByType(eventType, new EventDataManager.SingleEventCallback() {
            @Override
            public void onEventLoaded(Event event) {
                if (event == null) return;

                Map<String, Object> form = event.getEventForm();
                if (form != null && findingsContainer != null) {
                    findingsContainer.removeAllViews();
                    for (Map.Entry<String, Object> entry : form.entrySet()) {
                        boolean value = entry.getValue() instanceof Boolean && (Boolean) entry.getValue();
                        TextView tv = new TextView(EventDetailsActivity.this);
                        tv.setText((value ? "\u2714 " : "\u2716 ") + entry.getKey());
                        int color = getResources().getColor(value ? android.R.color.holo_green_dark : android.R.color.holo_red_dark);
                        tv.setTextColor(color);
                        findingsContainer.addView(tv);
                    }
                }

                if (event.getEventTimeStarted() != null && event.getEventTimeEnded() != null && durationText != null) {
                    long diff = event.getEventTimeEnded().toDate().getTime() - event.getEventTimeStarted().toDate().getTime();
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                    durationText.setText("\u23F3 האירוע התרחש במשך " + minutes + " דקות");

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    if (summaryStart != null)
                        summaryStart.setText(sdf.format(event.getEventTimeStarted().toDate()) + " - האירוע נפתח על ידי המשתמש.");
                    if (summaryEnd != null)
                        summaryEnd.setText(sdf.format(event.getEventTimeEnded().toDate()) + " - האירוע הסתיים.");
                }

                if (closeReasonText != null && event.getEventCloseReason() != null) {
                    closeReasonText.setText(event.getEventCloseReason());
                }

                ratingBar.setRating(event.getEventRating());

                String volunteerUid = event.getEventHandleBy();
                if (volunteerUid != null && !volunteerUid.isEmpty()) {
                    UserDataManager.loadBasicUserInfo(volunteerUid, info -> {
                        if (info != null) {
                            String name = info.getFirstName() + " " + info.getLastName();
                            if (tvVolunteer != null) tvVolunteer.setText(name);
                            if (summaryVolunteer != null) summaryVolunteer.setText("המתנדב " + name + " שויך לאירוע");
                            if (volunteerImage != null && info.getImageURL() != null && !info.getImageURL().isEmpty()) {
                                Glide.with(EventDetailsActivity.this)
                                        .load(info.getImageURL())
                                        .placeholder(R.drawable.newuserpic)
                                        .circleCrop()
                                        .into(volunteerImage);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                // handle error if needed
            }
        });

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
