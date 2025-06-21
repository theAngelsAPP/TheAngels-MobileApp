package co.median.android.a2025_theangels_new.data.models;

public class Message {

    private String messageTitle;
    private String messageData;
    private String messageType;
    private String messageRef; // שדה אופציונלי – יכול להיות null

    public Message() {
        // דרוש עבור Firebase בעת המרה אוטומטית
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageData() {
        return messageData;
    }

    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageRef() {
        return messageRef;
    }

    public void setMessageRef(String messageRef) {
        this.messageRef = messageRef;
    }
}
