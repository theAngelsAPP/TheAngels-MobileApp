package co.median.android.a2025_theangels_new.data.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import co.median.android.a2025_theangels_new.data.models.Education;

public class EducationDataManager {

    private static final String TAG = "EducationDataManager";

    public interface EducationCallback {
        void onEducationsLoaded(ArrayList<Education> educations);
        void onError(Exception e);
    }

    public static void getAllEducations(EducationCallback callback) {
        Log.d(TAG, "getAllEducations called - starting Firestore fetch");

        // Fetch education documents from Firestore. Collection name is
        // "education" as defined in the DB.
        FirebaseFirestore.getInstance().collection("education")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Education> educations = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        try {
                            Education training = doc.toObject(Education.class);
                            if (training != null) {
                                Log.d(TAG, "Education loaded: " + training.getEduTitle());
                                educations.add(training);
                            } else {
                                Log.w(TAG, "Education is null for document: " + doc.getId());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to convert document to Education: " + doc.getId(), e);
                        }
                    }

                    Log.d(TAG, "Total educations fetched: " + educations.size());
                    callback.onEducationsLoaded(educations);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching educations from Firestore", e);
                    callback.onError(e);
                });
    }
}
