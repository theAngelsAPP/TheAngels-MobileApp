package co.median.android.a2025_theangels_new.activities;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;

import co.median.android.a2025_theangels_new.R;

public class TestActivity extends BaseActivity  {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test); // קישור ל-XML של המסך

        // קריאה לפונקציה מהמחלקה הראשית שמחברת את ה-HEADER וה-FOOTER
        setupHeaderAndFooter();

    }
}