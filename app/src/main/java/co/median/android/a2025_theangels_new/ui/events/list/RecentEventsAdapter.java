package co.median.android.a2025_theangels_new.ui.events.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.models.Event;

public class RecentEventsAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final ArrayList<Event> events;
    private final int resource;
    private Map<String, String> eventTypeImages;
    private Map<String, String> eventStatusColors;

    public RecentEventsAdapter(Context context, int resource, ArrayList<Event> events) {
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

        ImageView icon = rootView.findViewById(R.id.event_icon);
        View statusDot = rootView.findViewById(R.id.status_dot);
        TextView typeName = rootView.findViewById(R.id.event_type_name);
        TextView statusLabel = rootView.findViewById(R.id.event_status);
        TextView dateText = rootView.findViewById(R.id.event_date);

        if (event != null) {
            typeName.setText(event.getEventType());
            statusLabel.setText(event.getEventStatus());

            if (event.getEventTimeStarted() != null) {
                Date d = event.getEventTimeStarted().toDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                dateText.setText(sdf.format(d));
            } else {
                dateText.setText("");
            }

            if (eventTypeImages != null && eventTypeImages.containsKey(event.getEventType())) {
                Glide.with(context.getApplicationContext())
                        .load(eventTypeImages.get(event.getEventType()))
                        .placeholder(R.drawable.medicevent)
                        .into(icon);
            }

            if (eventStatusColors != null && eventStatusColors.containsKey(event.getEventStatus())) {
                try {
                    int color = Color.parseColor(eventStatusColors.get(event.getEventStatus()));
                    statusDot.setBackgroundColor(color);
                } catch (Exception ignored) {}
            }
        }

        String finished = context.getString(R.string.status_event_finished);
        if (!finished.equals(event.getEventStatus())) {
            rootView.setBackgroundResource(R.drawable.active_event_background);
            rootView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.blink));
            rootView.setOnClickListener(v -> {
                Intent intent = new Intent(context, co.median.android.a2025_theangels_new.ui.events.active.EventUserActivity.class);
                intent.putExtra("eventId", event.getId());
                context.startActivity(intent);
            });
        } else {
            rootView.setBackgroundResource(R.drawable.event_row_background);
            rootView.clearAnimation();
            rootView.setOnClickListener(v -> {
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
        }

        return rootView;
    }
}
