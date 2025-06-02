package co.median.android.a2025_theangels_new.activities;

import java.time.LocalDate;

public class Event {

    public int EventID;
    public String Case;
    public String Address;
    public LocalDate Date;

    public String Vol_name;

    public String Status;

    public Event(int EventID,String Case,String Address,LocalDate Date,String Vol_name,String Status){
        setEventID(EventID);
        setCase(Case);
        setAddress(Address);
        setDate(Date);
        setVol_name(Vol_name);
        setStatus(Status);

    }

    public void setEventID(int eventID) {
        EventID = eventID;
    }

    public int getEventID() {
        return EventID;
    }

    public void setCase(String aCase) {
        Case = aCase;
    }

    public String getCase() {
        return Case;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddress() {
        return Address;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setVol_name(String vol_name) {
        Vol_name = vol_name;
    }

    public String getVol_name() {
        return Vol_name;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatus() {
        return Status;
    }
}
