package co.median.android.a2025_theangels_new.activities;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;

import java.util.ArrayList;

import co.median.android.a2025_theangels_new.R;

public class Trainingadapter extends ArrayAdapter<Training> {

    private Context context;
    private ArrayList<Training> trainings_list;

    public Trainingadapter(Context context, int resource, ArrayList<Training> trainings_list) {
        super(context, resource);
        this.context = context;
        this.trainings_list = trainings_list;
    }

    @Override
    public int getCount() {
        return trainings_list.size();
    }

    @Nullable
    @Override
    public Training getItem(int position) {
        return trainings_list.get(position);
    }


    @Override
    public View getView(int position, View rootView, ViewGroup parent) {

        if (rootView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.training_item, null, false);
        }


        Training training = getItem(position);


        TextView title = rootView.findViewById(R.id.training_title);
        ImageView picture = rootView.findViewById(R.id.training_picture);
        Button Button = rootView.findViewById(R.id.training_button);


        // Set the book details in the views
        if (training != null) {
            title.setText(training.getTitle());
            picture.setImageResource(training.getTrainingID());

        }
        Button.setOnClickListener(v -> {
            Intent intent=new Intent(context,EducationDetailsActivity.class);
            context.startActivity(intent);
        });

        return rootView;
    }

}
