package co.median.android.a2025_theangels_new.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import co.median.android.a2025_theangels_new.R;

public class OnboardingFragment extends Fragment {
    private static final String ARG_IMAGE_RES = "image_res";

    public static OnboardingFragment newInstance(int imageRes) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RES, imageRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);
        ImageView imageView = view.findViewById(R.id.onboardingImage);
        ImageButton startButton = view.findViewById(R.id.startButton); // שינינו מ-Button ל-ImageButton

        if (getArguments() != null) {
            int imageRes = getArguments().getInt(ARG_IMAGE_RES);
            imageView.setImageResource(imageRes);

            // בדיקה אם זה המסך האחרון (onboarding_3)
            if (imageRes == R.drawable.onboarding_3) {
                startButton.setVisibility(View.VISIBLE);
                startButton.setOnClickListener(v -> {
                    SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("onboarding_complete", true).apply();

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    requireActivity().finish();
                });
            } else {
                startButton.setVisibility(View.GONE);
            }
        }

        return view;
    }

}
