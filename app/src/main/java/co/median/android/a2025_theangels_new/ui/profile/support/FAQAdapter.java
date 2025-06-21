// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.ui.profile.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import co.median.android.a2025_theangels_new.R;

// =======================================
// FAQAdapter - Custom ExpandableListAdapter for showing FAQ questions and answers
// =======================================
public class FAQAdapter extends BaseExpandableListAdapter {

    // =======================================
    // VARIABLES
    // =======================================
    private final Context context;
    private final List<String> questionList;
    private final HashMap<String, String> answerMap;

    // =======================================
    // Constructor
    // =======================================
    public FAQAdapter(Context context, List<String> questionList, HashMap<String, String> answerMap) {
        this.context = context;
        this.questionList = questionList;
        this.answerMap = answerMap;
    }

    // =======================================
    // getGroupCount - Returns number of questions
    // =======================================
    @Override
    public int getGroupCount() {
        return questionList.size();
    }

    // =======================================
    // getChildrenCount - Each question has exactly one answer
    // =======================================
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    // =======================================
    // getGroup - Returns the question string
    // =======================================
    @Override
    public Object getGroup(int groupPosition) {
        return questionList.get(groupPosition);
    }

    // =======================================
    // getChild - Returns the answer string for the given question
    // =======================================
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return answerMap.get(questionList.get(groupPosition));
    }

    // =======================================
    // getGroupId - Returns group (question) ID
    // =======================================
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // =======================================
    // getChildId - Returns child (answer) ID
    // =======================================
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // =======================================
    // hasStableIds - Required method for adapter
    // =======================================
    @Override
    public boolean hasStableIds() {
        return false;
    }

    // =======================================
    // getGroupView - Displays the question row with an arrow icon
    // =======================================
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String question = (String) getGroup(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_faq_question, parent, false);
        }

        TextView questionText = convertView.findViewById(R.id.question_text);
        ImageView arrowIcon = convertView.findViewById(R.id.question_arrow);

        questionText.setText(question);

        // Rotate arrow based on expansion state
        arrowIcon.setRotation(isExpanded ? 180f : 0f);

        return convertView;
    }

    // =======================================
    // getChildView - Displays the answer row
    // =======================================
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        String answer = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_faq_answer, parent, false);
        }

        TextView answerText = convertView.findViewById(R.id.answer_text);
        answerText.setText(answer);

        return convertView;
    }

    // =======================================
    // isChildSelectable - Children (answers) are not selectable
    // =======================================
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
