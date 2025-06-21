package co.median.android.a2025_theangels_new.data.models;

/**
 * Enum representing the possible user-facing statuses for an event.
 * The value stored in Firestore is the Hebrew name.
 */
public enum UserEventStatus {
    LOOKING_FOR_VOLUNTEER("חיפוש מתנדב"),
    VOLUNTEER_ON_THE_WAY("מתנדב בדרך"),
    VOLUNTEER_AT_EVENT("מתנדב באירוע"),
    EVENT_FINISHED("האירוע הסתיים");

    private final String dbValue;

    UserEventStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    /**
     * @return value stored in Firestore
     */
    public String getDbValue() {
        return dbValue;
    }

    /**
     * Returns the enum constant for the given Firestore value.
     */
    public static UserEventStatus fromDbValue(String value) {
        for (UserEventStatus s : values()) {
            if (s.dbValue.equals(value)) {
                return s;
            }
        }
        if ("אירוע הסתיים".equals(value)) {
            return EVENT_FINISHED;
        }
        return null;
    }
}
