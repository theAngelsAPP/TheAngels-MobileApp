package co.median.android.a2025_theangels_new.adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

import co.median.android.a2025_theangels_new.activities.OnboardingFragment;

public class OnboardingAdapter extends FragmentStateAdapter {
    private final List<Integer> images;

    public OnboardingAdapter(@NonNull FragmentActivity fragmentActivity, List<Integer> images) {
        super(fragmentActivity);
        this.images = images;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return OnboardingFragment.newInstance(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
