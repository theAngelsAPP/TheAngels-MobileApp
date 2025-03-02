package co.median.android.a2025_theangels_new.activities;

import android.os.Bundle;

import co.median.android.a2025_theangels_new.R;

public class loginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity); // קישור ל-XML של המסך

        // קריאה לפונקציה מהמחלקה הראשית שמחברת את ה-HEADER וה-FOOTER
        setupHeaderAndFooter();

    }

}
