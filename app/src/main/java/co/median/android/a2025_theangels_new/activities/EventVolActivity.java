package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.shuhart.stepview.StepView;
import java.util.Arrays;
import java.util.List;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.StaticMapFragment;
import co.median.android.a2025_theangels_new.fragments.VolClaimFragment;
import co.median.android.a2025_theangels_new.fragments.VolStatusFragment;
import co.median.android.a2025_theangels_new.fragments.VolCloseFragment;

public class EventVolActivity extends BaseActivity {

    private StepView stepView;
    private Button nextStepButton;
    private FrameLayout mapContainer;
    private int currentStep = 0;
    private List<Fragment> stepFragments = Arrays.asList(
            new VolClaimFragment(),
            new VolStatusFragment(),
            new VolCloseFragment()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(true);

        stepView = findViewById(R.id.step_view);
        nextStepButton = findViewById(R.id.nextStepButton);

        setupStepView();
        mapContainer = findViewById(R.id.map_container);
        setupMap();
        loadStepFragment(0);

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep < stepFragments.size() - 1) {
                    currentStep++;
                    updateStep(currentStep);
                }
            }
        });
    }

    private void setupMap() {
        double eventLat = 31.8912;
        double eventLng = 34.8115;

        StaticMapFragment mapFragment = StaticMapFragment.newInstance(eventLat, eventLng);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment);
        transaction.commit();
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_vol;
    }

    private void setupStepView() {
        stepView.setSteps(Arrays.asList("שיוך אירוע", "עדכון הגעה", "סגירת אירוע"));
        stepView.go(0, true);
    }

    private void updateStep(int step) {
        if (stepView != null) {
            stepView.go(step, true);
            loadStepFragment(step);
        }
    }

    private void loadStepFragment(int step) {
        Fragment fragment = stepFragments.get(step);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
