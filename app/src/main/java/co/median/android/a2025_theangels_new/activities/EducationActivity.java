package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import co.median.android.a2025_theangels_new.R;

public class EducationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);

        setupClickListener(); // קריאה לפונקציה שמוסיפה מאזין ללחיצה
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_education;
    }

    // פונקציה שמאזינה ללחיצה על הריבוע ומעבירה למסך EducationDetailsActivity
    private void setupClickListener() {
        findViewById(R.id.education_card).setOnClickListener(v -> {
            Intent intent = new Intent(EducationActivity.this, EducationDetailsActivity.class);
            startActivity(intent);
        });
    }
}
