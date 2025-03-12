package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import co.median.android.a2025_theangels_new.R;

public class EducationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_education;
    }
}
