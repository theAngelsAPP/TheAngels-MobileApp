package co.median.android.a2025_theangels_new.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.bumptech.glide.Glide;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.models.Event;

public class EventsAdapter extends ArrayAdapter<Event> {

    private Context context;
    private ArrayList<Event> events;
    private int resource;
    private Map<String, String> eventTypeImages;


    public EventsAdapter(Context context, int resource, ArrayList<Event> events) {
        super(context, resource, events);
        this.context = context;
        this.events = events;
        this.resource = resource;
    }

    public void setEventTypeImages(Map<String, String> eventTypeImages) {
        this.eventTypeImages = eventTypeImages;
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

        TextView whatHappened = rootView.findViewById(R.id.event_case);
        TextView address = rootView.findViewById(R.id.event_address);
        TextView date = rootView.findViewById(R.id.event_date);
        TextView volunteer = rootView.findViewById(R.id.event_vol);
        TextView status = rootView.findViewById(R.id.event_status);
        ImageView picture = rootView.findViewById(R.id.event_picture);
        Button details = rootView.findViewById(R.id.details_btn);

        if (event != null) {
            whatHappened.setText(event.getEventType());
            if (eventTypeImages != null && eventTypeImages.containsKey(event.getEventType())) {
                String url = eventTypeImages.get(event.getEventType());
                Glide.with(context).load(url).placeholder(R.drawable.event_medical).into(picture);
            }

            if (event.getEventLocation() != null) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            event.getEventLocation().getLatitude(),
                            event.getEventLocation().getLongitude(),
                            1
                    );
                    if (addresses != null && !addresses.isEmpty()) {
                        String fullAddress = addresses.get(0).getAddressLine(0);
                        address.setText(fullAddress);
                    } else {
                        address.setText("כתובת לא זמינה");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    address.setText("שגיאה בהמרת מיקום לכתובת");
                }
            } else {
                address.setText("כתובת לא זמינה");
            }

            if (event.getEventTimeStarted() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                date.setText(sdf.format(event.getEventTimeStarted().toDate()));
            } else {
                date.setText("תאריך לא ידוע");
            }

            volunteer.setText(event.getEventHandleBy() != null ? event.getEventHandleBy() : "לא ידוע");
            status.setText(event.getEventStatus() != null ? event.getEventStatus() : "לא ידוע");
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
