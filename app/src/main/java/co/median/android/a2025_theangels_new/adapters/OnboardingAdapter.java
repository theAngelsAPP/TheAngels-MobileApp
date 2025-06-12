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

/**
 * מתאם המציג את מסכי ההדרכה הראשוניים באפליקציה.
 * לכל עמוד מוצגת תמונה אחרת מתוך רשימת התמונות שהתקבלה.
 */
public class OnboardingAdapter extends FragmentStateAdapter {

    // =======================================
    // VARIABLES
    // =======================================
    private final List<Integer> images;

    /**
     * יוצר את המתאם עם הפעילות והרשימה של התמונות להצגה.
     *
     * @param fragmentActivity הפעילות שמחזיקה את ViewPager2
     * @param images           רשימת מזהי תמונה שמוצגים בעמודי ההדרכה
     */
    public OnboardingAdapter(@NonNull FragmentActivity fragmentActivity, List<Integer> images) {
        super(fragmentActivity);
        this.images = images;
    }

    /**
     * יוצר עמוד חדש של ההדרכה בהתאם למיקום המבוקש.
     *
     * @param position המיקום הנוכחי בעמודים
     * @return פרגמנט שמציג את התמונה המתאימה
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return OnboardingFragment.newInstance(images.get(position));
    }

    /**
     * מחזיר את מספר עמודי ההדרכה שקיימים.
     *
     * @return כמות העמודים הכוללת
     */
    @Override
    public int getItemCount() {
        return images.size();
    }
}