package co.median.android.a2025_theangels_new.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.models.Training;

public class Trainingadapter extends ArrayAdapter<Training> {

    private Context context;
    private ArrayList<Training> trainingsList;

    public Trainingadapter(Context context, int resource, ArrayList<Training> trainingsList) {
        super(context, resource, trainingsList);
        this.context = context;
        this.trainingsList = trainingsList;
    }

    @Override
    public int getCount() {
        return trainingsList.size();
    }

    @Nullable
    @Override
    public Training getItem(int position) {
        return trainingsList.get(position);
    }

    @Override
    public View getView(int position, View rootView, ViewGroup parent) {
        if (rootView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.training_item, parent, false);
        }

        Training training = getItem(position);

        TextView title = rootView.findViewById(R.id.training_title);
        ImageView picture = rootView.findViewById(R.id.training_picture);

        if (training != null) {
            title.setText(training.getEduTitle());

            // Load image from URL using Glide. Fallback to placeholder if needed
            Glide.with(context)
                    .load(training.getEduImageURL())
                    .placeholder(R.drawable.training1)
                    .into(picture);

            // Open details screen when the card is clicked
            rootView.setOnClickListener(v -> {
                Intent intent = new Intent(context, EducationDetailsActivity.class);
                intent.putExtra("eduTitle", training.getEduTitle());
                intent.putExtra("eduData", training.getEduData());
                intent.putExtra("eduImageURL", training.getEduImageURL());
                context.startActivity(intent);
            });
        }

        return rootView;
    }
}
