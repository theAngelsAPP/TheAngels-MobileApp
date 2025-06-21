package co.median.android.a2025_theangels_new.data.models;

/**
 * Central enum for all event status values stored in Firestore.
 */
public enum EventStatus {
    LOOKING_FOR_VOLUNTEER("חיפוש מתנדב"),
    VOLUNTEER_ON_THE_WAY("מתנדב בדרך"),
    VOLUNTEER_AT_EVENT("מתנדב באירוע"),
    EVENT_FINISHED("האירוע הסתיים");

    private final String dbValue;

    EventStatus(String dbValue) {
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
    public static EventStatus fromDbValue(String value) {
        for (EventStatus s : values()) {
            if (s.dbValue.equals(value)) {
                return s;
            }
        }
        return null;
    }
}
