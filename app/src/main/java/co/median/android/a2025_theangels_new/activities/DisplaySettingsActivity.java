// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import co.median.android.a2025_theangels_new.R;

// =======================================
// DisplaySettingsActivity - Manages dark mode and text size settings
// =======================================
public class DisplaySettingsActivity extends AppCompatActivity {

    // =======================================
    // VARIABLES
    // =======================================
    private Switch switchDarkMode;
    private SeekBar seekBarTextSize;
    private TextView textSizeStatus;
    private SharedPreferences preferences;

    // =======================================
    // onCreate - Initializes the display settings screen
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);

        preferences = getSharedPreferences("settings", MODE_PRIVATE);

        switchDarkMode = findViewById(R.id.switch_dark_mode);
        seekBarTextSize = findViewById(R.id.seekbar_text_size);
        textSizeStatus = findViewById(R.id.text_size_status);

        // Back button listener
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        // Load dark mode preference
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkMode);
        updateDarkModeStatus(isDarkMode);

        // Handle dark mode toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

            updateDarkModeStatus(isChecked);
        });

        // Load and set current text size preference
        int textSizePref = preferences.getInt("text_size", 1);
        seekBarTextSize.setProgress(textSizePref);
        updateTextSizeStatus(textSizePref);

        // Handle text size changes
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTextSizeStatus(progress);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("text_size", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    // =======================================
    // updateDarkModeStatus - Updates the dark mode status text
    // =======================================
    private void updateDarkModeStatus(boolean isDarkMode) {
        TextView darkModeStatus = findViewById(R.id.dark_mode_status);
        darkModeStatus.setText(
                getString(isDarkMode ? R.string.dark_mode_on : R.string.dark_mode_off)
        );
    }

    // =======================================
    // updateTextSizeStatus - Updates the text size status text based on progress
    // =======================================
    private void updateTextSizeStatus(int progress) {
        String textSize;
        switch (progress) {
            case 0:
                textSize = getString(R.string.text_size_very_small);
                break;
            case 1:
                textSize = getString(R.string.text_size_normal);
                break;
            case 2:
                textSize = getString(R.string.text_size_large);
                break;
            case 3:
                textSize = getString(R.string.text_size_huge);
                break;
            default:
                textSize = getString(R.string.text_size_normal);
                break;
        }
        textSizeStatus.setText(getString(R.string.text_size_label, textSize));
    }
}
