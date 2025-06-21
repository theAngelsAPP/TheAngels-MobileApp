// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.profile.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.ui.profile.support.FAQFragment;
import co.median.android.a2025_theangels_new.ui.profile.support.ContactFragment;

// =======================================
// SupportActivity - Displays FAQ and Contact Us tabs with fragment switching
// =======================================
public class SupportActivity extends AppCompatActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private Button btnFAQ, btnContact;

    // =======================================
    // onCreate - Initializes support screen with tabs
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        // Bind tab buttons
        btnFAQ = findViewById(R.id.btn_faq);
        btnContact = findViewById(R.id.btn_contact);

        // Load default fragment
        loadFragment(new FAQFragment());

        // Switch tabs on button click
        btnFAQ.setOnClickListener(v -> loadFragment(new FAQFragment()));
        btnContact.setOnClickListener(v -> loadFragment(new ContactFragment()));
    }

    // =======================================
    // loadFragment - Replaces the content container with the given fragment
    // =======================================
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_container, fragment);
        transaction.commit();
    }

    // =======================================
    // sendEmail - Opens user's email app to send a message
    // =======================================
    public void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + getString(R.string.support_email)));
        startActivity(intent);
    }

    // =======================================
    // openWebsite - Opens the project's GitHub page
    // =======================================
    public void openWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.support_website_url)));
        startActivity(intent);
    }

    // =======================================
    // sendWhatsApp - Opens WhatsApp conversation to support number
    // =======================================
    public void sendWhatsApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.support_whatsapp_url)));
        startActivity(intent);
    }
}