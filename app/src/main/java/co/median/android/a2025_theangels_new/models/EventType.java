package co.median.android.a2025_theangels_new.models;

import java.util.List;

public class EventType {

    private String typeName;
    private String typeImageURL;
    private String typeColor;
    private List<String> questions;

    public EventType() {
        // דרוש עבור Firebase בעת המרה אוטומטית
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeImageURL() {
        return typeImageURL;
    }

    public void setTypeImageURL(String typeImageURL) {
        this.typeImageURL = typeImageURL;
    }

    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
