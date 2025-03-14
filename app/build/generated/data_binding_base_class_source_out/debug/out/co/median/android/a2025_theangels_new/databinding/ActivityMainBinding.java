// Generated by view binder compiler. Do not edit!
package co.median.android.a2025_theangels_new.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import co.median.android.a2025_theangels_new.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView loginBackground;

  @NonNull
  public final ImageButton loginButton;

  @NonNull
  public final TextView loginButtonText;

  @NonNull
  public final ImageView loginText;

  @NonNull
  public final EditText passwordInput;

  @NonNull
  public final TextView passwordLabel;

  @NonNull
  public final ImageButton registerButton;

  @NonNull
  public final TextView registerButtonText;

  @NonNull
  public final EditText usernameInput;

  @NonNull
  public final TextView usernameLabel;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView loginBackground, @NonNull ImageButton loginButton,
      @NonNull TextView loginButtonText, @NonNull ImageView loginText,
      @NonNull EditText passwordInput, @NonNull TextView passwordLabel,
      @NonNull ImageButton registerButton, @NonNull TextView registerButtonText,
      @NonNull EditText usernameInput, @NonNull TextView usernameLabel) {
    this.rootView = rootView;
    this.loginBackground = loginBackground;
    this.loginButton = loginButton;
    this.loginButtonText = loginButtonText;
    this.loginText = loginText;
    this.passwordInput = passwordInput;
    this.passwordLabel = passwordLabel;
    this.registerButton = registerButton;
    this.registerButtonText = registerButtonText;
    this.usernameInput = usernameInput;
    this.usernameLabel = usernameLabel;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.loginBackground;
      ImageView loginBackground = ViewBindings.findChildViewById(rootView, id);
      if (loginBackground == null) {
        break missingId;
      }

      id = R.id.loginButton;
      ImageButton loginButton = ViewBindings.findChildViewById(rootView, id);
      if (loginButton == null) {
        break missingId;
      }

      id = R.id.loginButtonText;
      TextView loginButtonText = ViewBindings.findChildViewById(rootView, id);
      if (loginButtonText == null) {
        break missingId;
      }

      id = R.id.loginText;
      ImageView loginText = ViewBindings.findChildViewById(rootView, id);
      if (loginText == null) {
        break missingId;
      }

      id = R.id.passwordInput;
      EditText passwordInput = ViewBindings.findChildViewById(rootView, id);
      if (passwordInput == null) {
        break missingId;
      }

      id = R.id.passwordLabel;
      TextView passwordLabel = ViewBindings.findChildViewById(rootView, id);
      if (passwordLabel == null) {
        break missingId;
      }

      id = R.id.registerButton;
      ImageButton registerButton = ViewBindings.findChildViewById(rootView, id);
      if (registerButton == null) {
        break missingId;
      }

      id = R.id.registerButtonText;
      TextView registerButtonText = ViewBindings.findChildViewById(rootView, id);
      if (registerButtonText == null) {
        break missingId;
      }

      id = R.id.usernameInput;
      EditText usernameInput = ViewBindings.findChildViewById(rootView, id);
      if (usernameInput == null) {
        break missingId;
      }

      id = R.id.usernameLabel;
      TextView usernameLabel = ViewBindings.findChildViewById(rootView, id);
      if (usernameLabel == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, loginBackground, loginButton,
          loginButtonText, loginText, passwordInput, passwordLabel, registerButton,
          registerButtonText, usernameInput, usernameLabel);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
