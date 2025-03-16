package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import co.median.android.a2025_theangels_new.R;

public class EducationDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_details);

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            Intent intent = new Intent(EducationDetailsActivity.this, EducationActivity.class);
            startActivity(intent);
            finish();
        });
    }
}