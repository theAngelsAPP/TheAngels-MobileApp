// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.profile.settings;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import com.google.android.gms.common.api.Status;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import java.util.ArrayList;
import java.util.List;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.data.map.AutocompleteHelper;
import co.median.android.a2025_theangels_new.ui.main.BaseActivity;

// =======================================
// JoinVolSettingsActivity - Displays settings screen for new volunteer registration
// =======================================
public class JoinVolSettingsActivity extends BaseActivity {

    /** קוד זיהוי לבחירת עיר דרך האוטוקומפליט */
    private static final int CITY_AUTOCOMPLETE_REQUEST = 1001;

    /** שדה החיפוש לבחירת ערים */
    private EditText inputCity;

    /** קבוצת הצ'יפים המציגה את הערים שנבחרו */
    private ChipGroup chipGroupCities;

    /** רשימת הערים שנבחרו בפועל */
    private final List<String> selectedCities = new ArrayList<>();

    // =======================================
    // onCreate - Initializes the screen and sets back button listener
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(false);

        // איתחול רכיבי בחירת הערים
        inputCity = findViewById(R.id.input_city);
        chipGroupCities = findViewById(R.id.chip_group_cities);

        // לחיצה על שדה החיפוש תפתח את ממשק האוטוקומפליט של גוגל
        if (inputCity != null) {
            inputCity.setOnClickListener(v ->
                    AutocompleteHelper.openCityAutocomplete(this, CITY_AUTOCOMPLETE_REQUEST));
        }

        // לחצן החזרה במסך
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource for this screen
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_joinvol_settings;
    }

    /**
     * מטפל בתוצאות בחירת העיר מהאוטוקומפליט ומוסיף צ'יפ לרשימה
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CITY_AUTOCOMPLETE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                Place place = AutocompleteHelper.getPlaceFromResult(data);
                if (place != null) {
                    String city = place.getName();
                    if (!selectedCities.contains(city)) {
                        selectedCities.add(city);
                        addCityChip(city);
                    } else {
                        Toast.makeText(this, R.string.city_already_selected, Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR && data != null) {
                Status status = AutocompleteHelper.getErrorStatus(data);
                Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * יוצר צ'יפ חדש עם אפשרות להסרה ומוסיף אותו לקבוצה
     */
    private void addCityChip(String city) {
        Chip chip = new Chip(this);
        chip.setText(city);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            chipGroupCities.removeView(chip);
            selectedCities.remove(city);
        });
        chipGroupCities.addView(chip);
    }
}
