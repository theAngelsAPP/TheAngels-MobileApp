package co.median.android.a2025_theangels_new.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import co.median.android.a2025_theangels_new.R;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(true);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;
    }

    public void onMyDetailsClicked(View view) {
        Intent intent = new Intent(this, MyDetailsActivity.class);
        startActivity(intent);
    }

    public void onPrivacySettingsClicked(View view) {
        Intent intent = new Intent(this, PrivacySettingsActivity.class);
        startActivity(intent);
    }

    public void onDisplaySettingsClicked(View view) {
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
        startActivity(intent);
    }

    public void onSupportSettingsClicked(View view) {
        Intent intent = new Intent(this, SupportActivity.class);
        startActivity(intent);
    }

    public void onJoinVolSettingsClicked(View view) {
        Intent intent = new Intent(this, JoinVolSettingsActivity.class);
        startActivity(intent);
    }

    public void onShareAppClicked(View view) {
        String shareText = "הורידו את האפליקציה שלנו: https://play.google.com/store/apps/dev?id=7946031944863744406&hl=en-US";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "שתף באמצעות"));
    }

    public void onLogoutClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("התנתקות")
                .setMessage("האם אתה בטוח שברצונך להתנתק?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("לא", null)
                .show();
    }
}
