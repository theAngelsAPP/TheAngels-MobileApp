/**
 * מודל המייצג אירוע במערכת ומכיל את כל מאפייני האירוע כפי שנשמרו בפיירבייס.
 */
package co.median.android.a2025_theangels_new.data.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class Event {

    /** מזהה מסמך האירוע */
    private String id;

    /** סיבת סגירת האירוע */
    private String eventCloseReason;
    /** מזהה המשתמש שיצר את האירוע */
    private String eventCreatedBy;
    /** נתונים נוספים של האירוע בטופס דינמי */
    private Map<String, Object> eventForm;
    /** מזהה המתנדב שטיפל באירוע */
    private String eventHandleBy;
    /** מיקום האירוע במפה */
    private GeoPoint eventLocation;
    /** תשובת השאלון שנבחרה לאחר האירוע */
    private String eventQuestionChoice;
    /** דירוג שהמתנדב קיבל לאחר הטיפול */
    private int eventRating;
    /** טקסט חופשי עם משוב על הטיפול */
    private String eventRatingText;
    /** סטטוס נוכחי של האירוע */
    private String eventStatus;
    /** זמן התחלת האירוע */
    private Timestamp eventTimeStarted;
    /** זמן סיום האירוע */
    private Timestamp eventTimeEnded;
    /** סוג האירוע (לפי טבלת סוגים) */
    private String eventType;

    /**
     * בנאי ריק הנדרש לשליפה מפיירבייס.
     */
    public Event() {
        // דרוש לפיירבייס בעת שליפה
    }

    /**
     * בנאי מלא ליצירת אובייקט אירוע.
     */
    public Event(String eventCloseReason,
                 String eventCreatedBy,
                 Map<String, Object> eventForm,
                 String eventHandleBy,
                 GeoPoint eventLocation,
                 String eventQuestionChoice,
                 int eventRating,
                 String eventRatingText,
                 String eventStatus,
                 Timestamp eventTimeStarted,
                 Timestamp eventTimeEnded,
                 String eventType) {
        this.eventCloseReason = eventCloseReason
        ;this.eventCreatedBy = eventCreatedBy;
        this.eventForm = eventForm;
        this.eventHandleBy = eventHandleBy;
        this.eventLocation = eventLocation;
        this.eventQuestionChoice = eventQuestionChoice;
        this.eventRating = eventRating;
        this.eventRatingText = eventRatingText;
        this.eventStatus = eventStatus;
        this.eventTimeStarted = eventTimeStarted;
        this.eventTimeEnded = eventTimeEnded;
        this.eventType = eventType;
    }

    /** @return סיבת סגירת האירוע */
    public String getEventCloseReason() {
        return eventCloseReason;
    }

    /** @param eventCloseReason סיבת הסגירה */
    public void setEventCloseReason(String eventCloseReason) {
        this.eventCloseReason = eventCloseReason;
    }

    /** @return מזהה יוצר האירוע */
    public String getEventCreatedBy() {
        return eventCreatedBy;
    }

    /** @param eventCreatedBy מזהה יוצר האירוע */
    public void setEventCreatedBy(String eventCreatedBy) {
        this.eventCreatedBy = eventCreatedBy;
    }

    /** @return נתוני הטופס של האירוע */
    public Map<String, Object> getEventForm() {
        return eventForm;
    }

    /** @param eventForm נתוני הטופס */
    public void setEventForm(Map<String, Object> eventForm) {
        this.eventForm = eventForm;
    }

    /** @return מזהה המתנדב המטפל */
    public String getEventHandleBy() {
        return eventHandleBy;
    }

    /** @param eventHandleBy מזהה המטפל */
    public void setEventHandleBy(String eventHandleBy) {
        this.eventHandleBy = eventHandleBy;
    }

    /** @return מיקום האירוע */
    public GeoPoint getEventLocation() {
        return eventLocation;
    }

    /** @param eventLocation מיקום האירוע */
    public void setEventLocation(GeoPoint eventLocation) {
        this.eventLocation = eventLocation;
    }

    /** @return בחירת השאלה לאחר האירוע */
    public String getEventQuestionChoice() {
        return eventQuestionChoice;
    }

    /** @param eventQuestionChoice בחירת השאלה */
    public void setEventQuestionChoice(String eventQuestionChoice) {
        this.eventQuestionChoice = eventQuestionChoice;
    }

    /** @return דירוג האירוע */
    public int getEventRating() {
        return eventRating;
    }

    /** @param eventRating דירוג האירוע */
    public void setEventRating(int eventRating) {
        this.eventRating = eventRating;
    }

    /** @return טקסט הדירוג */
    public String getEventRatingText() {
        return eventRatingText;
    }

    /** @param eventRatingText טקסט הדירוג */
    public void setEventRatingText(String eventRatingText) {
        this.eventRatingText = eventRatingText;
    }

    /** @return סטטוס האירוע */
    public String getEventStatus() {
        return eventStatus;
    }

    /** @param eventStatus סטטוס האירוע */
    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    /** @return זמן התחלת האירוע */
    public Timestamp getEventTimeStarted() {
        return eventTimeStarted;
    }

    /** @param eventTimeStarted זמן התחלה */
    public void setEventTimeStarted(Timestamp eventTimeStarted) {
        this.eventTimeStarted = eventTimeStarted;
    }

    /** @return זמן סיום האירוע */
    public Timestamp getEventTimeEnded() {
        return eventTimeEnded;
    }

    /** @param eventTimeEnded זמן סיום */
    public void setEventTimeEnded(Timestamp eventTimeEnded) {
        this.eventTimeEnded = eventTimeEnded;
    }

    /** @return סוג האירוע */
    public String getEventType() {
        return eventType;
    }

    /** @param eventType סוג האירוע */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /** @return מזהה המסמך */
    public String getId() { return id; }

    /** @param id מזהה המסמך */
    public void setId(String id) { this.id = id; }
}
