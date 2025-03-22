// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import co.median.android.a2025_theangels_new.R;

// =======================================
// EducationActivity - Displays the education screen and navigates to details
// =======================================
public class EducationActivity extends BaseActivity {

    // =======================================
    // onCreate - Initializes the education screen and sets UI visibility
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);
        setupClickListener();
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_education;
    }

    // =======================================
    // setupClickListener - Handles click on the education card to open details screen
    // =======================================
    private void setupClickListener() {
        findViewById(R.id.education_card).setOnClickListener(v -> {
            Intent intent = new Intent(EducationActivity.this, EducationDetailsActivity.class);
            startActivity(intent);
        });
    }
}
