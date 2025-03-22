package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import co.median.android.a2025_theangels_new.R;

public class EducationDetailsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(false);
        setContentView(R.layout.activity_education_details);

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            Intent intent = new Intent(EducationDetailsActivity.this, EducationActivity.class);
            startActivity(intent);
            finish();
        });
    }

    protected int getLayoutResourceId() {
        return R.layout.activity_education_details;
    }

}