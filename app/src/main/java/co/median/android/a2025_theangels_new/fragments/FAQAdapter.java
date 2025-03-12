package co.median.android.a2025_theangels_new.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.median.android.a2025_theangels_new.R;
import java.util.HashMap;
import java.util.List;

public class FAQAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<String> questionList;
    private final HashMap<String, String> answerMap;

    public FAQAdapter(Context context, List<String> questionList, HashMap<String, String> answerMap) {
        this.context = context;
        this.questionList = questionList;
        this.answerMap = answerMap;
    }

    @Override
    public int getGroupCount() {
        return questionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return questionList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return answerMap.get(questionList.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String question = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_faq_question, parent, false);
        }

        TextView questionText = convertView.findViewById(R.id.question_text);
        ImageView arrowIcon = convertView.findViewById(R.id.question_arrow);

        questionText.setText(question);

        // קביעת מצב החץ - רק של השאלה הנוכחית
        if (isExpanded) {
            arrowIcon.setRotation(180f);
        } else {
            arrowIcon.setRotation(0f);
        }

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String answer = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_faq_answer, parent, false);
        }

        TextView answerText = convertView.findViewById(R.id.answer_text);
        answerText.setText(answer);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
