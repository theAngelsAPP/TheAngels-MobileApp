// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.profile.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.median.android.a2025_theangels_new.R;

// =======================================
// ContactFragment - Allows the user to contact support via Email, Website, or WhatsApp
// =======================================
public class ContactFragment extends Fragment {

    // =======================================
    // onCreateView - Inflates layout and sets click listeners for contact options
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        view.findViewById(R.id.email_section).setOnClickListener(v -> sendEmail());
        view.findViewById(R.id.website_section).setOnClickListener(v -> openWebsite());
        view.findViewById(R.id.whatsapp_section).setOnClickListener(v -> sendWhatsApp());

        return view;
    }

    // =======================================
    // sendEmail - Opens email app to send message
    // =======================================
    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:" + getString(R.string.support_email)));
        startActivity(intent);
    }

    // =======================================
    // openWebsite - Opens the project's GitHub page
    // =======================================
    private void openWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.support_website_url)));
        startActivity(intent);
    }

    // =======================================
    // sendWhatsApp - Opens WhatsApp chat with support
    // =======================================
    private void sendWhatsApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.support_whatsapp_url)));
        startActivity(intent);
    }
}
