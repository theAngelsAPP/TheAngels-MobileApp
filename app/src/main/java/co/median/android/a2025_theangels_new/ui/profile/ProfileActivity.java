// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import co.median.android.a2025_theangels_new.data.models.UserSession;
import com.google.firebase.auth.FirebaseAuth;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.ui.main.BaseActivity;
import co.median.android.a2025_theangels_new.ui.main.MainActivity;
import co.median.android.a2025_theangels_new.ui.profile.settings.DisplaySettingsActivity;
import co.median.android.a2025_theangels_new.ui.profile.settings.JoinVolSettingsActivity;
import co.median.android.a2025_theangels_new.ui.profile.settings.PrivacySettingsActivity;
import co.median.android.a2025_theangels_new.ui.profile.support.SupportActivity;

// =======================================
// ProfileActivity - Handles user profile screen and navigation to settings/help/logout
// =======================================
public class ProfileActivity extends BaseActivity {

    private ImageView imgProfile;
    private TextView tvUsername;
    private TextView tvUserRole;
    private View btnJoinVolunteers;


    // =======================================
    // onCreate - Initializes the profile screen
    // =======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        showBottomBar(true);
        imgProfile = findViewById(R.id.img_profile_large);
        tvUsername = findViewById(R.id.tv_username);
        tvUserRole = findViewById(R.id.tv_user_role);
        btnJoinVolunteers = findViewById(R.id.btn_join_volunteers);

        populateUserDetails();
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
    // populateUserDetails - Fills UI with logged in user information
    // =======================================
    private void populateUserDetails() {
        UserSession session = UserSession.getInstance();

        String fullName = session.getFirstName() + " " + session.getLastName();
        tvUsername.setText(fullName);

        String role = session.getRole();
        if (role != null && !role.isEmpty()) {
            tvUserRole.setText(role);
        }

        if ("מתנדב".equals(role)) {
            btnJoinVolunteers.setVisibility(View.GONE);
        } else {
            btnJoinVolunteers.setVisibility(View.VISIBLE);
        }

        String url = session.getImageURL();
        if (url != null && !url.isEmpty()) {
            Glide.with(this).load(url).placeholder(R.drawable.newuserpic).into(imgProfile);
        }
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
                        FirebaseAuth.getInstance().signOut();
                        UserSession.getInstance().clear();
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.logout_no, null)
                .show();
    }
}
