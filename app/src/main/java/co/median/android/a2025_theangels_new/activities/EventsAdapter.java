//package co.median.android.a2025_theangels_new.activities;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import java.util.ArrayList;
//
//import co.median.android.a2025_theangels_new.R;
//import co.median.android.a2025_theangels_new.models.Event;
//
//public class EventsAdapter extends ArrayAdapter<Event> {
//
//    private Context context;
//    private ArrayList<Event> events;
//
//    public EventsAdapter(Context context,int resource,ArrayList<Event> events){
//        super(context,resource);
//        this.context=context;
//        this.events=events;
//    }
//
//    @Override
//    public int getCount() {
//        return events.size();
//    }
//
//    @Nullable
//    @Override
//    public Event getItem(int position) {
//        return events.get(position);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View rootView, @NonNull ViewGroup parent) {
//        if (rootView == null) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            rootView = inflater.inflate(R.layout.training_item, null, false);
//        }
//
//        Event event = getItem(position);
//
//
//        ImageView picture=rootView.findViewById(R.id.event_picture);
//        TextView what_happend = rootView.findViewById(R.id.event_case);
//        TextView address = rootView.findViewById(R.id.event_address);
//        TextView date = rootView.findViewById(R.id.event_date);
//        TextView vol= rootView.findViewById(R.id.event_vol);
//        TextView status = rootView.findViewById(R.id.event_status);
//        Button details =rootView.findViewById(R.id.details_btn);
//
//        if(event != null){
//            picture.setImageResource(event.getEventID());
//            what_happend.setText(event.getCase());
//            address.setText(event.getAddress());
//            date.setText(event.getDate().toString());
//            vol.setText(event.getVol_name());
//            status.setText(event.getStatus());
//        }
//
//        // Handle click on the medical event card
//        details.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EventDetailsActivity.class);
//            context.startActivity(intent);
//        });
//
//        return rootView;
//    }
//}
