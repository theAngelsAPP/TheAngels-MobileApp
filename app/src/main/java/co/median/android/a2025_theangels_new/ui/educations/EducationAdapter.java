package co.median.android.a2025_theangels_new.ui.educations;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.models.Education;

public class EducationAdapter extends ArrayAdapter<Education> {

    private Context context;
    private ArrayList<Education> educationsList;
    private java.util.Map<String, String> typeImages;
    private java.util.Map<String, String> typeColors;

    public EducationAdapter(Context context, int resource, ArrayList<Education> educationsList) {
        super(context, resource, educationsList);
        this.context = context;
        this.educationsList = educationsList;
    }

    public void setTypeImages(java.util.Map<String, String> typeImages) {
        this.typeImages = typeImages;
    }

    public void setTypeColors(java.util.Map<String, String> typeColors) {
        this.typeColors = typeColors;
    }

    @Override
    public int getCount() {
        return educationsList.size();
    }

    @Nullable
    @Override
    public Education getItem(int position) {
        return educationsList.get(position);
    }

    @Override
    public View getView(int position, View rootView, ViewGroup parent) {
        if (rootView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.education_item, parent, false);
        }

        Education education = getItem(position);

        TextView title = rootView.findViewById(R.id.training_title);
        TextView typeLabel = rootView.findViewById(R.id.training_type_label);
        ImageView picture = rootView.findViewById(R.id.training_picture);

        if (education != null) {
            title.setText(education.getEduTitle());
            typeLabel.setText(education.getEduType());

            if (typeColors != null && typeColors.containsKey(education.getEduType())) {
                try {
                    int color = Color.parseColor(typeColors.get(education.getEduType()));
                    typeLabel.setBackgroundColor(color);
                } catch (Exception ignored) {}
            }

            // Load image from URL using Glide. Fallback to placeholder if needed
            Glide.with(context)
                    .load(education.getEduImageURL())
                    .placeholder(R.drawable.training1)
                    .into(picture);

            // Open details screen when the card is clicked
            rootView.setOnClickListener(v -> {
                Intent intent = new Intent(context, EducationDetailsActivity.class);
                intent.putExtra("eduTitle", education.getEduTitle());
                intent.putExtra("eduData", education.getEduData());
                intent.putExtra("eduImageURL", education.getEduImageURL());
                context.startActivity(intent);
            });
        }

        return rootView;
    }
}
