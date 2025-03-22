// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;

import co.median.android.a2025_theangels_new.R;

// =======================================
// EducationDetailsActivity - Shows detailed education content and handles back navigation
// =======================================
public class EducationDetailsActivity extends BaseActivity {

    // =======================================
    // onCreate - Initializes the education details screen and sets back button logic
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(false);
        setContentView(R.layout.activity_education_details);

        // Handle back button click
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            Intent intent = new Intent(EducationDetailsActivity.this, EducationActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_education_details;
    }
}
