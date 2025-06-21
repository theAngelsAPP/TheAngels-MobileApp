// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.educations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import co.median.android.a2025_theangels_new.R;

// =======================================
// EducationDetailsActivity - Shows detailed education content and handles back navigation
// =======================================
public class EducationDetailsActivity extends BaseActivity {

    // =======================================
    // onCreate - Initializes the education details screen and sets back button logic
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(false);

        // Retrieve data passed from the list screen
        Intent intent = getIntent();
        String title = intent.getStringExtra("eduTitle");
        String data = intent.getStringExtra("eduData");
        String imageUrl = intent.getStringExtra("eduImageURL");

        TextView tvTitle = findViewById(R.id.title);
        TextView tvContent = findViewById(R.id.education_content);
        ImageView ivHeader = findViewById(R.id.header_image);

        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        if (tvContent != null) {
            tvContent.setText(data);
        }
        if (ivHeader != null) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.training1).into(ivHeader);
        }

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            finish();
        });
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_education_details;
    }
}
