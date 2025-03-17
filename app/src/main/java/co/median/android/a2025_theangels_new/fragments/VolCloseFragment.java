package co.median.android.a2025_theangels_new.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.activities.HomeActivity;

public class VolCloseFragment extends Fragment {

    private Button btnCloseEvent;
    private String selectedReason = null;
    private String[] closeReasons = {"פינוי לבית חולים", "מטופל מסרב טיפול", "אירוע שווא", "קיבל טיפול עזרה ראשונה"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vol_close, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCloseEvent = view.findViewById(R.id.btnCloseEvent);

        btnCloseEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseEventDialog();
            }
        });
    }

    private void showCloseEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("בחר את סיבת סגירת האירוע");

        builder.setSingleChoiceItems(closeReasons, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedReason = closeReasons[which];
            }
        });

        builder.setPositiveButton("סיום", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedReason != null) {
                    navigateToHome();
                }
            }
        });

        builder.setNegativeButton("ביטול", null);
        builder.show();
    }

    private void navigateToHome() {
        Intent intent = new Intent(requireActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
