package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import co.median.android.a2025_theangels_new.R;
import nl.dionsegijn.konfetti.xml.KonfettiView;
import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;
import nl.dionsegijn.konfetti.xml.image.ImageUtil;

public class OnboardingFragment extends Fragment {
    private static final String ARG_IMAGE_RES = "image_res";
    private KonfettiView konfettiView = null;
    private boolean showConfetti = false;

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
        konfettiView = view.findViewById(R.id.konfettiView);
        ImageView imageView = view.findViewById(R.id.onboardingImage);

        if (getArguments() != null) {
            int imageRes = getArguments().getInt(ARG_IMAGE_RES);
            imageView.setImageResource(imageRes);
        }

        if (showConfetti) {
            startConfetti();
        } else {
            konfettiView.setVisibility(View.GONE);
        }

        return view;
    }


    private void startConfetti() {
        EmitterConfig emitterConfig = new Emitter(1, TimeUnit.SECONDS).perSecond(200);
        Party party = new PartyFactory(emitterConfig)
                .spread(360)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 30f)
                .position(0.5, 0.0)
                .timeToLive(5000L)
                .build();

        konfettiView.start(party);
    }

    public void setShowConfetti(boolean show) {
        this.showConfetti = show;
        if (konfettiView == null) return; // ודא שה-View קיים

        konfettiView.post(() -> { // גורם לו לפעול מיידית בתור Thread UI
            if (showConfetti) {
                konfettiView.setVisibility(View.VISIBLE);
                startConfetti();
            } else {
                konfettiView.setVisibility(View.GONE);
            }
        });
    }


}
