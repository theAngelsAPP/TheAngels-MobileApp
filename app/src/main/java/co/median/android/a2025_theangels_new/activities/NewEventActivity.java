package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.EventTypeFragment;
import co.median.android.a2025_theangels_new.fragments.WhatHappenedFragment;
import co.median.android.a2025_theangels_new.fragments.QuestionnaireFragment;
import co.median.android.a2025_theangels_new.fragments.LocationFragment;
import co.median.android.a2025_theangels_new.fragments.SummaryFragment;

public class NewEventActivity extends AppCompatActivity {

    private int currentStep = 0;
    private Fragment[] steps = new Fragment[]{
            new EventTypeFragment(),
            new WhatHappenedFragment(),
            new QuestionnaireFragment(),
            new LocationFragment(),
            new SummaryFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        ImageView ivClose = findViewById(R.id.ivClose);
        Button btnNext = findViewById(R.id.btnNext);

        if (savedInstanceState == null) {
            replaceFragment(steps[currentStep]);
        }

        btnNext.setOnClickListener(v -> {
            if (currentStep < steps.length - 1) {
                currentStep++;
                replaceFragment(steps[currentStep]);
            }
        });

        ivClose.setOnClickListener(v -> finish());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
