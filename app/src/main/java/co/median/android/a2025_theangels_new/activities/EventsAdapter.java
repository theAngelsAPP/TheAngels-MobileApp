package co.median.android.a2025_theangels_new.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import com.bumptech.glide.Glide;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.models.Event;
import co.median.android.a2025_theangels_new.models.UserBasicInfo;
import co.median.android.a2025_theangels_new.services.UserDataManager;

public class EventsAdapter extends ArrayAdapter<Event> {

    private Context context;
    private ArrayList<Event> events;
    private int resource;
    private Map<String, String> eventTypeImages;
    private Map<String, String> eventStatusColors;
    private Map<String, UserBasicInfo> volunteerCache = new HashMap<>();


    public EventsAdapter(Context context, int resource, ArrayList<Event> events) {
        super(context, resource, events);
        this.context = context;
        this.events = events;
        this.resource = resource;
    }

    public void setEventTypeImages(Map<String, String> eventTypeImages) {
        this.eventTypeImages = eventTypeImages;
    }

    public void setEventStatusColors(Map<String, String> eventStatusColors) {
        this.eventStatusColors = eventStatusColors;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Nullable
    @Override
    public Event getItem(int position) {
        return events.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View rootView, @NonNull ViewGroup parent) {
        if (rootView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(resource, parent, false);
        }

        Event event = getItem(position);

        TextView whatHappened = rootView.findViewById(R.id.event_title);
        TextView date = rootView.findViewById(R.id.event_date);
        TextView statusLabel = rootView.findViewById(R.id.event_status);
        RatingBar ratingBar = rootView.findViewById(R.id.event_rating);
        ImageView volunteerImage = rootView.findViewById(R.id.creator_image);
        TextView volunteerName = rootView.findViewById(R.id.creator_name);
        Button details = rootView.findViewById(R.id.details_button);

        if (event != null) {
            whatHappened.setText("אירוע " + event.getEventType());

            if (event.getEventTimeStarted() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.ENGLISH);
                date.setText(sdf.format(event.getEventTimeStarted().toDate()));
            } else {
                date.setText("תאריך לא ידוע");
            }

            String statusText = event.getEventStatus() != null ? event.getEventStatus() : "לא ידוע";
            statusLabel.setText(statusText);
            if (eventStatusColors != null && eventStatusColors.containsKey(statusText)) {
                try {
                    int color = android.graphics.Color.parseColor(eventStatusColors.get(statusText));
                    statusLabel.setBackgroundColor(color);
                } catch (Exception ignored) {}
            }

            ratingBar.setRating(event.getEventRating());

            String uid = event.getEventHandleBy();
            if (uid != null && !uid.isEmpty()) {
                if (volunteerCache.containsKey(uid)) {
                    UserBasicInfo info = volunteerCache.get(uid);
                    if (info != null) {
                        volunteerName.setText(info.getFirstName() + " " + info.getLastName());
                        if (info.getImageURL() != null && !info.getImageURL().isEmpty()) {
                            Glide.with(context)
                                    .load(info.getImageURL())
                                    .placeholder(R.drawable.newuserpic)
                                    .circleCrop()
                                    .into(volunteerImage);
                        }
                    }
                } else {
                    UserDataManager.loadBasicUserInfo(uid, info -> {
                        if (info != null) {
                            volunteerCache.put(uid, info);
                            volunteerName.setText(info.getFirstName() + " " + info.getLastName());
                            if (info.getImageURL() != null && !info.getImageURL().isEmpty()) {
                                Glide.with(context)
                                        .load(info.getImageURL())
                                        .placeholder(R.drawable.newuserpic)
                                        .circleCrop()
                                        .into(volunteerImage);
                            }
                        }
                    });
                }
            }
        }

        details.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetailsActivity.class);
            intent.putExtra("eventType", event.getEventType());
            intent.putExtra("eventStatus", event.getEventStatus());
            intent.putExtra("eventHandleBy", event.getEventHandleBy());
            if (event.getEventTimeStarted() != null) {
                intent.putExtra("eventTimeStarted", event.getEventTimeStarted().getSeconds());
            }
            intent.putExtra("eventRating", event.getEventRating());
            if (event.getEventLocation() != null) {
                intent.putExtra("lat", event.getEventLocation().getLatitude());
                intent.putExtra("lng", event.getEventLocation().getLongitude());
            }
            if (eventTypeImages != null && eventTypeImages.containsKey(event.getEventType())) {
                intent.putExtra("typeImageURL", eventTypeImages.get(event.getEventType()));
            }
            context.startActivity(intent);
        });

        return rootView;
    }
}
