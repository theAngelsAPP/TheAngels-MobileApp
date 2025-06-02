// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import co.median.android.a2025_theangels_new.R;

// =======================================
// EducationActivity - Displays the education screen and navigates to details
// =======================================
public class EducationActivity extends BaseActivity {


    private ListView trainings_list;
    private ArrayList<Training> trainings;

    private Trainingadapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_education);

        showTopBar(true); // ואז אפשר להשתמש ברכיבים שבו
        showBottomBar(true);

        trainings_list = findViewById(R.id.trainings_list_view);
        trainings = new ArrayList<>();

        trainings.add(new Training("how to turn on a defibrilator",R.drawable.training1));
        trainings.add(new Training("how to turn on a defibrilator",R.drawable.training2));
        trainings.add(new Training("how to turn on a defibrilator",R.drawable.training3));
        trainings.add(new Training("how to turn on a defibrilator",R.drawable.training4));
        trainings.add(new Training("how to turn on a defibrilator",R.drawable.training5));
        trainings.add(new Training("how to turn on a defibrilator",R.drawable.training5));
        trainings.add(new Training("how to turn on a defibrilator",R.drawable.training5));
        trainings.add(new Training("how to turn on a defibrilator",R.drawable.training5));

        adapter = new Trainingadapter(this, R.layout.training_item, trainings);
        trainings_list.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_education;
    }




    // =======================================
    // setupClickListener - Handles click on the education card to open details screen
    // =======================================
    private void setupClickListener() {
        findViewById(R.id.training_button).setOnClickListener(v -> {
            Intent intent = new Intent(EducationActivity.this, EducationDetailsActivity.class);
            startActivity(intent);
        });
    }
}
