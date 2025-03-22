// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import co.median.android.a2025_theangels_new.R;

// =======================================
// ProfileActivity - Handles user profile screen and navigation to settings/help/logout
// =======================================
public class ProfileActivity extends BaseActivity {

    // =======================================
    // onCreate - Initializes the profile screen
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(true);
    }

    // =======================================
    // getLayoutResourceId - Returns layout resource
    // =======================================
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;
    }

    // =======================================
    // onMyDetailsClicked - Opens MyDetailsActivity
    // =======================================
    public void onMyDetailsClicked(View view) {
        Intent intent = new Intent(this, MyDetailsActivity.class);
        startActivity(intent);
    }

    // =======================================
    // onPrivacySettingsClicked - Opens PrivacySettingsActivity
    // =======================================
    public void onPrivacySettingsClicked(View view) {
        Intent intent = new Intent(this, PrivacySettingsActivity.class);
        startActivity(intent);
    }

    // =======================================
    // onDisplaySettingsClicked - Opens DisplaySettingsActivity
    // =======================================
    public void onDisplaySettingsClicked(View view) {
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
        startActivity(intent);
    }

    // =======================================
    // onSupportSettingsClicked - Opens SupportActivity
    // =======================================
    public void onSupportSettingsClicked(View view) {
        Intent intent = new Intent(this, SupportActivity.class);
        startActivity(intent);
    }

    // =======================================
    // onJoinVolSettingsClicked - Opens JoinVolSettingsActivity
    // =======================================
    public void onJoinVolSettingsClicked(View view) {
        Intent intent = new Intent(this, JoinVolSettingsActivity.class);
        startActivity(intent);
    }

    // =======================================
    // onShareAppClicked - Shares the app link via chooser
    // =======================================
    public void onShareAppClicked(View view) {
        String shareText = getString(R.string.share_app_text);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
    }

    // =======================================
    // onLogoutClicked - Shows confirmation dialog and logs user out if confirmed
    // =======================================
    public void onLogoutClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.logout_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.logout_no, null)
                .show();
    }
}
