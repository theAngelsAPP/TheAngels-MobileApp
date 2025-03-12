package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import co.median.android.a2025_theangels_new.R;

public class JoinVolSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(false);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_joinvol_settings;
    }
}
