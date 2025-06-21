package co.median.android.a2025_theangels_new.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.models.Message;
import co.median.android.a2025_theangels_new.data.models.MessageType;

public class MessagesAdapter extends ArrayAdapter<Message> {

    public interface OnMessageClickListener {
        void onMessageClicked(Message message);
    }

    private final Context context;
    private final ArrayList<Message> messages;
    private final int resource;
    private Map<String, MessageType> typeMap;
    private OnMessageClickListener clickListener;

    public MessagesAdapter(@NonNull Context context, int resource, ArrayList<Message> messages) {
        super(context, resource, messages);
        this.context = context;
        this.resource = resource;
        this.messages = messages;
    }

    public void setTypeMap(Map<String, MessageType> typeMap) {
        this.typeMap = typeMap;
    }

    public void setOnMessageClickListener(OnMessageClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Nullable
    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Message msg = getItem(position);
        ImageView icon = convertView.findViewById(R.id.message_icon);
        TextView typeView = convertView.findViewById(R.id.message_type);
        TextView title = convertView.findViewById(R.id.message_title);
        TextView content = convertView.findViewById(R.id.message_content);

        if (msg != null) {
            MessageType type = typeMap != null ? typeMap.get(msg.getMessageType()) : null;
            if (type != null) {
                typeView.setText(type.getTypeName());
                try {
                    int color = Color.parseColor(type.getColor());
                    if (convertView.getBackground() instanceof GradientDrawable) {
                        ((GradientDrawable) convertView.getBackground()).setColor(color);
                    } else {
                        convertView.setBackgroundColor(color);
                    }
                } catch (Exception ignored) {}

                Glide.with(context.getApplicationContext())
                        .load(type.getIconURL())
                        .placeholder(R.drawable.messagebox_icon)
                        .into(icon);
            } else {
                typeView.setText(msg.getMessageType());
                icon.setImageResource(R.drawable.messagebox_icon);
            }

            title.setText(msg.getMessageTitle());
            content.setText(msg.getMessageData());
        }

        boolean hasRef = msg != null && msg.getMessageRef() != null && !msg.getMessageRef().isEmpty();
        convertView.setClickable(hasRef);

        if (hasRef && clickListener != null) {
            convertView.setOnClickListener(v -> clickListener.onMessageClicked(msg));
        } else {
            convertView.setOnClickListener(null);
        }

        return convertView;
    }
}
