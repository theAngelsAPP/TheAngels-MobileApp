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

public class DisplaySettingsActivity extends AppCompatActivity {

    private Switch switchDarkMode;
    private SeekBar seekBarTextSize;
    private TextView textSizeStatus;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);

        preferences = getSharedPreferences("settings", MODE_PRIVATE);

        switchDarkMode = findViewById(R.id.switch_dark_mode);
        seekBarTextSize = findViewById(R.id.seekbar_text_size);
        textSizeStatus = findViewById(R.id.text_size_status);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        boolean isDarkMode = preferences.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkMode);
        updateDarkModeStatus(isDarkMode);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            updateDarkModeStatus(isChecked);
        });

        int textSizePref = preferences.getInt("text_size", 1);
        seekBarTextSize.setProgress(textSizePref);
        updateTextSizeStatus(textSizePref);

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

    private void updateDarkModeStatus(boolean isDarkMode) {
        TextView darkModeStatus = findViewById(R.id.dark_mode_status);
        darkModeStatus.setText(isDarkMode ? "מצב כהה פעיל" : "מצב כהה כבוי");
    }

    private void updateTextSizeStatus(int progress) {
        String textSize;
        switch (progress) {
            case 0: textSize = "קטן מאוד"; break;
            case 1: textSize = "רגיל"; break;
            case 2: textSize = "גדול"; break;
            case 3: textSize = "ענק"; break;
            default: textSize = "רגיל"; break;
        }
        textSizeStatus.setText("גודל טקסט: " + textSize);
    }
}
