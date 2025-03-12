package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.core.content.ContextCompat;
import co.median.android.a2025_theangels_new.R;

public class EventsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(true);
        showBottomBar(true);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_events;
    }

}
