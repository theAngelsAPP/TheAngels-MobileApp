package co.median.android.a2025_theangels_new.data.models;

/**
 * Enum for volunteer-side statuses with mapping to user status.
 */
public enum VolunteerEventStatus {
    CLAIM("שיוך אירוע", UserEventStatus.LOOKING_FOR_VOLUNTEER),
    ARRIVAL_UPDATE("עדכון הגעה", UserEventStatus.VOLUNTEER_ON_THE_WAY),
    CLOSE("סגירת אירוע", UserEventStatus.VOLUNTEER_AT_EVENT);

    private final String dbValue;
    private final UserEventStatus userStatus;

    VolunteerEventStatus(String dbValue, UserEventStatus userStatus) {
        this.dbValue = dbValue;
        this.userStatus = userStatus;
    }

    public String getDbValue() {
        return dbValue;
    }

    public UserEventStatus getUserStatus() {
        return userStatus;
    }

    public static VolunteerEventStatus fromDbValue(String value) {
        for (VolunteerEventStatus s : values()) {
            if (s.dbValue.equals(value)) {
                return s;
            }
        }
        return null;
    }
}
