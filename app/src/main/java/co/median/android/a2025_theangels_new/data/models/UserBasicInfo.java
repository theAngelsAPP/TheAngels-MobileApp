/**
 * מידע בסיסי של משתמש לתצוגה מהירה.
 */
package co.median.android.a2025_theangels_new.data.models;

public class UserBasicInfo {
    /** שם פרטי */
    private String firstName;
    /** שם משפחה */
    private String lastName;
    /** קישור לתמונת פרופיל */
    private String imageURL;

    /** בנאי ריק נדרש לפיירבייס */
    public UserBasicInfo() {}

    /**
     * בנאי מלא לאתחול הנתונים.
     */
    public UserBasicInfo(String firstName, String lastName, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
    }

    /** @return שם פרטי */
    public String getFirstName() {
        return firstName;
    }

    /** @param firstName שם פרטי */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** @return שם משפחה */
    public String getLastName() {
        return lastName;
    }

    /** @param lastName שם משפחה */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** @return כתובת התמונה */
    public String getImageURL() {
        return imageURL;
    }

    /** @param imageURL כתובת התמונה */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
