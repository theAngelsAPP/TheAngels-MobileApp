/**
 * מודל הודעה המוצגת באפליקציה.
 */
package co.median.android.a2025_theangels_new.data.models;

import com.google.firebase.firestore.DocumentId;

public class Message {

    /** מזהה המסמך */
    @DocumentId
    private String id;

    /** כותרת ההודעה */
    private String messageTitle;
    /** תוכן ההודעה */
    private String messageData;
    /** סוג ההודעה (לפי טבלת סוגים) */
    private String messageType;
    /** הפניה אופציונלית למסמך אחר */
    private String messageRef; // שדה אופציונלי – יכול להיות null

    /** בנאי ריק הנדרש לפיירבייס */
    public Message() {
        // דרוש עבור Firebase בעת המרה אוטומטית
    }

    /** @return כותרת ההודעה */
    public String getMessageTitle() {
        return messageTitle;
    }

    /** @param messageTitle כותרת ההודעה */
    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    /** @return תוכן ההודעה */
    public String getMessageData() {
        return messageData;
    }

    /** @param messageData תוכן ההודעה */
    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }

    /** @return סוג ההודעה */
    public String getMessageType() {
        return messageType;
    }

    /** @param messageType סוג ההודעה */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /** @return מזהה הפניה למסמך אחר */
    public String getMessageRef() {
        return messageRef;
    }

    /** @param messageRef מזהה הפניה */
    public void setMessageRef(String messageRef) {
        this.messageRef = messageRef;
    }

    /** @return מזהה המסמך */
    public String getId() { return id; }

    /** @param id מזהה המסמך */
    public void setId(String id) { this.id = id; }
}
