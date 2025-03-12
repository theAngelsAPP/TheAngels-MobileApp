package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import co.median.android.a2025_theangels_new.R;

public class EventVolActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(false);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_vol;
    }
}
