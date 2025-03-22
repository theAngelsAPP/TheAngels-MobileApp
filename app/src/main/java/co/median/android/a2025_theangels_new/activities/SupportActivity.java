package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.fragments.FAQFragment;
import co.median.android.a2025_theangels_new.fragments.ContactFragment;

public class SupportActivity extends AppCompatActivity {

    private Button btnFAQ, btnContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        btnFAQ = findViewById(R.id.btn_faq);
        btnContact = findViewById(R.id.btn_contact);

        loadFragment(new FAQFragment());

        btnFAQ.setOnClickListener(v -> loadFragment(new FAQFragment()));
        btnContact.setOnClickListener(v -> loadFragment(new ContactFragment()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_container, fragment);
        transaction.commit();

    }



    public void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:theangelsyvc@gmail.com"));
        startActivity(intent);
    }

    public void openWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/theAngelsAPP"));
        startActivity(intent);
    }

    public void sendWhatsApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/972529568878"));
        startActivity(intent);
    }
}
