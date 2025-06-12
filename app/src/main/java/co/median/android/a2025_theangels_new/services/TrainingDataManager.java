package co.median.android.a2025_theangels_new.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import co.median.android.a2025_theangels_new.models.Training;

public class TrainingDataManager {

    private static final String TAG = "TrainingDataManager";

    public interface TrainingCallback {
        void onTrainingsLoaded(ArrayList<Training> trainings);
        void onError(Exception e);
    }

    public static void getAllTrainings(TrainingCallback callback) {
        Log.d(TAG, "getAllTrainings called - starting Firestore fetch");

        FirebaseFirestore.getInstance().collection("trainings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Training> trainings = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        try {
                            Training training = doc.toObject(Training.class);
                            if (training != null) {
                                Log.d(TAG, "Training loaded: " + training.getEduTitle());
                                trainings.add(training);
                            } else {
                                Log.w(TAG, "Training is null for document: " + doc.getId());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to convert document to Training: " + doc.getId(), e);
                        }
                    }

                    Log.d(TAG, "Total trainings fetched: " + trainings.size());
                    callback.onTrainingsLoaded(trainings);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching trainings from Firestore", e);
                    callback.onError(e);
                });
    }
}
