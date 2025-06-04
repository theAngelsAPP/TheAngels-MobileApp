package co.median.android.a2025_theangels_new.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class Event {

    private String eventCloseReason;
    private String eventCreatedBy;
    private Map<String, Object> eventForm;
    private String eventHandleBy;
    private GeoPoint eventLocation;
    private String eventQuestionChoice;
    private int eventRating;
    private String eventRatingText;
    private String eventStatus;
    private Timestamp eventTimeStarted;
    private Timestamp eventTimeEnded;
    private String eventType;

    public Event() {
        // דרוש לפיירבייס בעת שליפה
    }

    public String getEventCloseReason() {
        return eventCloseReason;
    }

    public void setEventCloseReason(String eventCloseReason) {
        this.eventCloseReason = eventCloseReason;
    }

    public String getEventCreatedBy() {
        return eventCreatedBy;
    }

    public void setEventCreatedBy(String eventCreatedBy) {
        this.eventCreatedBy = eventCreatedBy;
    }

    public Map<String, Object> getEventForm() {
        return eventForm;
    }

    public void setEventForm(Map<String, Object> eventForm) {
        this.eventForm = eventForm;
    }

    public String getEventHandleBy() {
        return eventHandleBy;
    }

    public void setEventHandleBy(String eventHandleBy) {
        this.eventHandleBy = eventHandleBy;
    }

    public GeoPoint getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(GeoPoint eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventQuestionChoice() {
        return eventQuestionChoice;
    }

    public void setEventQuestionChoice(String eventQuestionChoice) {
        this.eventQuestionChoice = eventQuestionChoice;
    }

    public int getEventRating() {
        return eventRating;
    }

    public void setEventRating(int eventRating) {
        this.eventRating = eventRating;
    }

    public String getEventRatingText() {
        return eventRatingText;
    }

    public void setEventRatingText(String eventRatingText) {
        this.eventRatingText = eventRatingText;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Timestamp getEventTimeStarted() {
        return eventTimeStarted;
    }

    public void setEventTimeStarted(Timestamp eventTimeStarted) {
        this.eventTimeStarted = eventTimeStarted;
    }

    public Timestamp getEventTimeEnded() {
        return eventTimeEnded;
    }

    public void setEventTimeEnded(Timestamp eventTimeEnded) {
        this.eventTimeEnded = eventTimeEnded;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
