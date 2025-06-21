package co.median.android.a2025_theangels_new.data.models;

/**
 * Model representing an event status document stored in Firestore.
 * מודל עבור סטטוס אפשרי של אירוע.
 */
public class EventStatusInfo {
    /** שם הסטטוס כפי שמופיע במסד */
    private String statusName;
    /** צבע המייצג את הסטטוס */
    private String statusColor;

    /** בנאי ריק הנדרש לפיירבייס */
    public EventStatusInfo() {}

    /** @return שם הסטטוס */
    public String getStatusName() {
        return statusName;
    }

    /** @param statusName שם הסטטוס */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /** @return צבע הסטטוס */
    public String getStatusColor() {
        return statusColor;
    }

    /** @param statusColor צבע ההצגה */
    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }
}
