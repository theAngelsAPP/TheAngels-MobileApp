package co.median.android.a2025_theangels_new.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.map.AddressHelper;
import co.median.android.a2025_theangels_new.data.models.Event;
import co.median.android.a2025_theangels_new.ui.events.active.EventVolActivity;

public class OpenEventsAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final ArrayList<Event> events;
    private final ArrayList<String> ids;
    private final int resource;
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            notifyDataSetChanged();
            timerHandler.postDelayed(this, 1000);
        }
    };

    public void startTimers() {
        timerHandler.post(timerRunnable);
    }

    public void stopTimers() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public OpenEventsAdapter(Context context, int resource, ArrayList<Event> events, ArrayList<String> ids) {
        super(context, resource, events);
        this.context = context;
        this.events = events;
        this.ids = ids;
        this.resource = resource;
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        Event event = getItem(position);
        TextView type = convertView.findViewById(R.id.open_event_type);
        TextView address = convertView.findViewById(R.id.open_event_address);
        TextView time = convertView.findViewById(R.id.open_event_time);
        TextView timer = convertView.findViewById(R.id.open_event_timer);

        if (event != null) {
            type.setText(event.getEventType());
            if (event.getEventLocation() != null) {
                String addr = AddressHelper.getAddressFromLatLng(context,
                        event.getEventLocation().getLatitude(),
                        event.getEventLocation().getLongitude());
                if (addr != null) address.setText(addr);
            }
            if (event.getEventTimeStarted() != null) {
                Date d = event.getEventTimeStarted().toDate();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                time.setText(sdf.format(d));

                long diff = (System.currentTimeMillis() - d.getTime()) / 1000;
                String t = String.format(Locale.getDefault(), "%02d:%02d", diff / 60, diff % 60);
                timer.setText(t);
            }
        }

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventVolActivity.class);
            intent.putExtra("eventId", ids.get(position));
            context.startActivity(intent);
        });
        return convertView;
    }
}
