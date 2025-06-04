package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.models.Training;
import co.median.android.a2025_theangels_new.services.TrainingDataManager;

public class EducationActivity extends BaseActivity {

    private static final String TAG = "EducationActivity";

    private ListView trainingsListView;
    private ArrayList<Training> trainings;
    private Trainingadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_education);

        showTopBar(true);
        showBottomBar(true);
        Log.d(TAG, "UI bars shown");

        trainingsListView = findViewById(R.id.trainings_list_view);
        trainings = new ArrayList<>();
        adapter = new Trainingadapter(this, R.layout.training_item, trainings);
        trainingsListView.setAdapter(adapter);
        Log.d(TAG, "ListView and adapter initialized");

        loadTrainingsFromFirestore();
    }

    private void loadTrainingsFromFirestore() {
        Log.d(TAG, "Fetching trainings from Firestore...");
        TrainingDataManager.getAllTrainings(new TrainingDataManager.TrainingCallback() {
            @Override
            public void onTrainingsLoaded(ArrayList<Training> loadedTrainings) {
                Log.d(TAG, "Trainings loaded successfully. Count: " + loadedTrainings.size());
                trainings.clear();
                trainings.addAll(loadedTrainings);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Adapter updated with new trainings");
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error loading trainings from Firestore", e);
                Toast.makeText(EducationActivity.this, "שגיאה בטעינת ההדרכות", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_education;
    }
}
