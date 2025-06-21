/**
 * מודל המתאר סוג אירוע אפשרי במערכת.
 */
package co.median.android.a2025_theangels_new.data.models;

import java.util.List;

public class EventType {

    /** שם הסוג */
    private String typeName;
    /** כתובת אייקון/תמונה לסוג */
    private String typeImageURL;
    /** צבע ברירת מחדל להצגה */
    private String typeColor;
    /** רשימת שאלות הקשורות לסוג האירוע */
    private List<String> questions;

    /** בנאי ריק הנדרש לפיירבייס */
    public EventType() {
        // דרוש עבור Firebase בעת המרה אוטומטית
    }

    /** @return שם הסוג */
    public String getTypeName() {
        return typeName;
    }

    /** @param typeName שם הסוג */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /** @return כתובת האייקון */
    public String getTypeImageURL() {
        return typeImageURL;
    }

    /** @param typeImageURL כתובת האייקון */
    public void setTypeImageURL(String typeImageURL) {
        this.typeImageURL = typeImageURL;
    }

    /** @return צבע הסוג */
    public String getTypeColor() {
        return typeColor;
    }

    /** @param typeColor צבע הסוג */
    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    /** @return שאלות ברירת המחדל לסוג */
    public List<String> getQuestions() {
        return questions;
    }

    /** @param questions שאלות ברירת המחדל */
    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
