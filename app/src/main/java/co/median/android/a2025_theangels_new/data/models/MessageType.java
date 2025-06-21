/**
 * מודל המתאר את סוג ההודעה במערכת.
 */
package co.median.android.a2025_theangels_new.data.models;

import com.google.firebase.firestore.DocumentId;

public class MessageType {

    /** מזהה המסמך */
    @DocumentId
    private String id;

    /** שם הסוג */
    private String typeName;
    /** צבע ברירת מחדל להצגת הודעה מסוג זה */
    private String color;
    /** אייקון המייצג את סוג ההודעה */
    private String iconURL;

    /** בנאי ריק לפיירבייס */
    public MessageType() {
        // נדרש עבור Firebase בעת המרה אוטומטית
    }

    /** @return שם הסוג */
    public String getTypeName() {
        return typeName;
    }

    /** @param typeName שם הסוג */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /** @return צבע ברירת המחדל */
    public String getColor() {
        return color;
    }

    /** @param color צבע ברירת המחדל */
    public void setColor(String color) {
        this.color = color;
    }

    /** @return כתובת האייקון */
    public String getIconURL() {
        return iconURL;
    }

    /** @param iconURL כתובת האייקון */
    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    /** @return מזהה המסמך */
    public String getId() { return id; }

    /** @param id מזהה המסמך */
    public void setId(String id) { this.id = id; }
}
