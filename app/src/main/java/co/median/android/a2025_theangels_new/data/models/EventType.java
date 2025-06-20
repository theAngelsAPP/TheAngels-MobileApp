/**
 * מודל המתאר סוג אירוע אפשרי במערכת.
 */
package co.median.android.a2025_theangels_new.data.models;

import java.util.List;
import com.google.firebase.firestore.DocumentId;

public class EventType {

    /** מזהה המסמך */
    @DocumentId
    private String id;

    /** שם הסוג */
    private String typeName;
    /** כתובת אייקון/תמונה לסוג */
    private String typeImageURL;
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


    /** @return שאלות ברירת המחדל לסוג */
    public List<String> getQuestions() {
        return questions;
    }

    /** @param questions שאלות ברירת המחדל */
    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    /** @return מזהה המסמך */
    public String getId() { return id; }

    /** @param id מזהה המסמך */
    public void setId(String id) { this.id = id; }
}
