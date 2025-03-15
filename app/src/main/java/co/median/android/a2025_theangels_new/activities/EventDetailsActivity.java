package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.StaticMapFragment;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        double eventLat = 31.8912;
        double eventLng = 34.8115;

        StaticMapFragment mapFragment = StaticMapFragment.newInstance(eventLat, eventLng);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment);
        transaction.commit();

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}
