// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

import co.median.android.a2025_theangels_new.activities.OnboardingFragment;

// =======================================
// OnboardingAdapter - Adapter for onboarding ViewPager2
// Creates fragments for each onboarding image
// =======================================
public class OnboardingAdapter extends FragmentStateAdapter {

    // =======================================
    // VARIABLES
    // =======================================
    private final List<Integer> images;

    // =======================================
    // Constructor - Receives activity and list of drawable resources
    // =======================================
    public OnboardingAdapter(@NonNull FragmentActivity fragmentActivity, List<Integer> images) {
        super(fragmentActivity);
        this.images = images;
    }

    // =======================================
    // createFragment - Returns a new OnboardingFragment with the image for the current position
    // =======================================
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return OnboardingFragment.newInstance(images.get(position));
    }

    // =======================================
    // getItemCount - Returns number of onboarding pages
    // =======================================
    @Override
    public int getItemCount() {
        return images.size();
    }
}