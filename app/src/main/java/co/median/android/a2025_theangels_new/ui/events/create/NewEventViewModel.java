package co.median.android.a2025_theangels_new.ui.events.create;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

import co.median.android.a2025_theangels_new.data.services.EventDataManager;

/**
 * ViewModel for collecting data during the new event creation flow.
 */
public class NewEventViewModel extends ViewModel {
    private String eventType;
    private String eventQuestionChoice;
    private final Map<String, Boolean> eventForm = new HashMap<>();
    private GeoPoint eventLocation;

    public void setEventType(String type) {
        this.eventType = type;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventQuestionChoice(String choice) {
        this.eventQuestionChoice = choice;
    }

    public String getEventQuestionChoice() {
        return eventQuestionChoice;
    }

    public void setFormAnswer(String question, boolean answer) {
        eventForm.put(question, answer);
    }

    public Map<String, Boolean> getEventForm() {
        return eventForm;
    }

    public void setEventLocation(GeoPoint location) {
        this.eventLocation = location;
    }

    public GeoPoint getEventLocation() {
        return eventLocation;
    }

    /**
     * Clears all stored event data.
     */
    public void clear() {
        eventType = null;
        eventQuestionChoice = null;
        eventForm.clear();
        eventLocation = null;
    }

    /**
     * Creates a new event document in Firestore.
     */
    public void createEvent(String uid,
                            EventDataManager.StringCallback onSuccess,
                            EventDataManager.ErrorCallback onError) {
        Map<String, Object> data = new HashMap<>();
        data.put("eventCreatedBy", uid);
        data.put("eventType", eventType);
        data.put("eventQuestionChoice", eventQuestionChoice);
        data.put("eventForm", eventForm);
        data.put("eventLocation", eventLocation);
        data.put("eventStatus", co.median.android.a2025_theangels_new.data.models.UserEventStatus.LOOKING_FOR_VOLUNTEER.getDbValue());
        data.put("eventTimeStarted", com.google.firebase.firestore.FieldValue.serverTimestamp());

        EventDataManager.createNewEvent(data, onSuccess, onError);
    }
}
