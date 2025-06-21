// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.profile.support;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.median.android.a2025_theangels_new.R;

// =======================================
// FAQFragment - Displays a collapsible list of questions and answers (FAQ)
// =======================================
public class FAQFragment extends Fragment {

    // =======================================
    // VARIABLES
    // =======================================
    private ExpandableListView faqListView;
    private FAQAdapter faqAdapter;
    private List<String> questionList;
    private HashMap<String, String> answerMap;

    // =======================================
    // onCreateView - Inflates FAQ layout and initializes list
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        faqListView = view.findViewById(R.id.faq_list);

        // Load data and set adapter
        setupFAQData();
        faqAdapter = new FAQAdapter(requireContext(), questionList, answerMap);
        faqListView.setAdapter(faqAdapter);

        // Collapse all others when one is expanded
        faqListView.setOnGroupExpandListener(groupPosition -> {
            for (int i = 0; i < faqAdapter.getGroupCount(); i++) {
                if (i != groupPosition) {
                    faqListView.collapseGroup(i);
                }
            }
            faqListView.smoothScrollToPosition(groupPosition);
        });

        return view;
    }

    // =======================================
    // setupFAQData - Loads question and answer data from resources
    // =======================================
    private void setupFAQData() {
        String[] questions = getResources().getStringArray(R.array.faq_questions);
        String[] answers = getResources().getStringArray(R.array.faq_answers);

        questionList = new ArrayList<>();
        answerMap = new HashMap<>();

        for (int i = 0; i < questions.length; i++) {
            questionList.add(questions[i]);
            answerMap.put(questions[i], answers[i]);
        }
    }
}
