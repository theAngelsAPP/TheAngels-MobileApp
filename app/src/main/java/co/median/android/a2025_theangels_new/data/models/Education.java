/**
 * מודל המייצג פריט הדרכה כפי שהוא מאוחסן בפיירבייס.
 */
package co.median.android.a2025_theangels_new.data.models;

import com.google.firebase.firestore.DocumentId;

public class Education {

    /** מזהה המסמך */
    @DocumentId
    private String id;

    /** כותרת ההדרכה */
    private String eduTitle;
    /** תוכן ההדרכה */
    private String eduData;
    /** כתובת התמונה של ההדרכה */
    private String eduImageURL;
    /** סוג ההדרכה (לדוגמה: עזרה ראשונה) */
    private String eduType;

    /**
     * בנאי ריק הנדרש עבור Firebase בעת המרה אוטומטית.
     */
    public Education() {
        // נדרש עבור Firebase
    }

    /**
     * בנאי מלא ליצירת אובייקט הדרכה.
     *
     * @param eduTitle   כותרת ההדרכה
     * @param eduData    תוכן ההדרכה
     * @param eduImageURL כתובת התמונה המצורפת
     * @param eduType    סוג ההדרכה
     */
    public Education(String eduTitle, String eduData, String eduImageURL, String eduType) {
        this.eduTitle = eduTitle;
        this.eduData = eduData;
        this.eduImageURL = eduImageURL;
        this.eduType = eduType;
    }

    /**
     * @return כותרת ההדרכה
     */
    public String getEduTitle() {
        return eduTitle;
    }

    /**
     * @param eduTitle כותרת ההדרכה
     */
    public void setEduTitle(String eduTitle) {
        this.eduTitle = eduTitle;
    }

    /**
     * @return תוכן ההדרכה
     */
    public String getEduData() {
        return eduData;
    }

    /**
     * @param eduData תוכן ההדרכה
     */
    public void setEduData(String eduData) {
        this.eduData = eduData;
    }

    /**
     * @return כתובת התמונה של ההדרכה
     */
    public String getEduImageURL() {
        return eduImageURL;
    }

    /**
     * @param eduImageURL כתובת התמונה
     */
    public void setEduImageURL(String eduImageURL) {
        this.eduImageURL = eduImageURL;
    }

    /**
     * @return סוג ההדרכה
     */
    public String getEduType() {
        return eduType;
    }

    /**
     * @param eduType סוג ההדרכה
     */
    public void setEduType(String eduType) {
        this.eduType = eduType;
    }

    /** @return מזהה המסמך */
    public String getId() { return id; }

    /** @param id מזהה המסמך */
    public void setId(String id) { this.id = id; }
}
