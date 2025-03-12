package co.median.android.a2025_theangels_new.fragments;

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

public class ContactFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        view.findViewById(R.id.email_section).setOnClickListener(v -> sendEmail());
        view.findViewById(R.id.website_section).setOnClickListener(v -> openWebsite());
        view.findViewById(R.id.whatsapp_section).setOnClickListener(v -> sendWhatsApp());

        return view;
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:theangelsyvc@gmail.com"));
        startActivity(intent);
    }

    private void openWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/theAngelsAPP"));
        startActivity(intent);
    }

    private void sendWhatsApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/972529568878"));
        startActivity(intent);
    }
}
