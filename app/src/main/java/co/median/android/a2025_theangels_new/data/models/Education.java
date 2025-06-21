package co.median.android.a2025_theangels_new.data.models;

public class Education {

    private String eduTitle;
    private String eduData;
    private String eduImageURL;
    private String eduType;

    public Education() {
        // נדרש עבור Firebase
    }

    public Education(String eduTitle, String eduData, String eduImageURL, String eduType) {
        this.eduTitle = eduTitle;
        this.eduData = eduData;
        this.eduImageURL = eduImageURL;
        this.eduType = eduType;
    }

    public String getEduTitle() {
        return eduTitle;
    }

    public void setEduTitle(String eduTitle) {
        this.eduTitle = eduTitle;
    }

    public String getEduData() {
        return eduData;
    }

    public void setEduData(String eduData) {
        this.eduData = eduData;
    }

    public String getEduImageURL() {
        return eduImageURL;
    }

    public void setEduImageURL(String eduImageURL) {
        this.eduImageURL = eduImageURL;
    }

    public String getEduType() {
        return eduType;
    }

    public void setEduType(String eduType) {
        this.eduType = eduType;
    }
}
