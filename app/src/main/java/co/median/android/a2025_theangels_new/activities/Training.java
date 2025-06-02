package co.median.android.a2025_theangels_new.activities;

public class Training {

    private String title;

    private int trainingID;

    public Training(String Title,int trainingID){
        setTitle(Title);
        setTrainingID(trainingID);

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTrainingID(int trainingID) {
        this.trainingID = trainingID;
    }

    public int getTrainingID() {
        return trainingID;
    }
}
