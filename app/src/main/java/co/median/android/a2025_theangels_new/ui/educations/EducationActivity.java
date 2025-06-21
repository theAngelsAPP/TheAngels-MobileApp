package co.median.android.a2025_theangels_new.ui.educations;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.models.Education;
import co.median.android.a2025_theangels_new.data.models.EventType;
import co.median.android.a2025_theangels_new.data.services.EducationDataManager;
import co.median.android.a2025_theangels_new.data.services.EventTypeDataManager;

public class EducationActivity extends BaseActivity {

    private static final String TAG = "EducationActivity";

    private ListView educationsListView;
    private ArrayList<Education> educations;
    private EducationAdapter adapter;
    private Map<String, String> typeImages = new HashMap<>();
    private Map<String, String> typeColors = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");

        showTopBar(true);
        showBottomBar(true);
        Log.d(TAG, "UI bars shown");

        educationsListView = findViewById(R.id.educations_list_view);
        educations = new ArrayList<>();
        adapter = new EducationAdapter(this, R.layout.education_item, educations);
        educationsListView.setAdapter(adapter);
        Log.d(TAG, "ListView and adapter initialized");

        loadEventTypes();
    }

    private void loadEventTypes() {
        EventTypeDataManager.getAllEventTypes(new EventTypeDataManager.EventTypeCallback() {
            @Override
            public void onEventTypesLoaded(ArrayList<EventType> types) {
                for (EventType type : types) {
                    typeImages.put(type.getTypeName(), type.getTypeImageURL());
                    if (type.getTypeColor() != null) {
                        typeColors.put(type.getTypeName(), type.getTypeColor());
                    }
                }
                adapter.setTypeImages(typeImages);
                adapter.setTypeColors(typeColors);
                loadEducationsFromFirestore();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error loading event types", e);
                loadEducationsFromFirestore();
            }
        });
    }

    private void loadEducationsFromFirestore() {
        Log.d(TAG, "Fetching educations from Firestore...");
        EducationDataManager.getAllEducations(new EducationDataManager.EducationCallback() {
            @Override
            public void onEducationsLoaded(ArrayList<Education> loadedEducations) {
                Log.d(TAG, "Educations loaded successfully. Count: " + loadedEducations.size());
                educations.clear();
                educations.addAll(loadedEducations);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Adapter updated with new educations");
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error loading educations from Firestore", e);
                Toast.makeText(EducationActivity.this, "שגיאה בטעינת ההדרכות", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_education;
    }
}
