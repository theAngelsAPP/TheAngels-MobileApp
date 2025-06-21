package co.median.android.a2025_theangels_new.data.models;

public class MessageType {

    private String typeName;
    private String color;
    private String iconURL;

    public MessageType() {
        // נדרש עבור Firebase בעת המרה אוטומטית
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }
}
