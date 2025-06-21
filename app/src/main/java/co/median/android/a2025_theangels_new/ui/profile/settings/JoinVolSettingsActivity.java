// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.profile.settings;

import android.os.Bundle;

import co.median.android.a2025_theangels_new.R;

// =======================================
// JoinVolSettingsActivity - Displays settings screen for new volunteer registration
// =======================================
public class JoinVolSettingsActivity extends BaseActivity {

    // =======================================
    // onCreate - Initializes the screen and sets back button listener
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(false);

        // Handle back button click
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_joinvol_settings;
    }
}
