/**
 * מחלקת Singleton האוחזת בפרטי המשתמש המחובר כעת.
 */
package co.median.android.a2025_theangels_new.data.models;

import java.util.Date;
import java.util.List;

public class UserSession {
    /** מופע יחיד של המחלקה */
    private static UserSession instance;

    /** כתובת האימייל של המשתמש */
    private String email;
    /** מספר הטלפון של המשתמש */
    private String phone;
    /** תאריך לידה */
    private Date birthDate;
    /** עיר מגורים */
    private String city;
    /** שם פרטי */
    private String firstName;
    /** האם למשתמש יש רישיון נשק */
    private boolean haveGunLicense;
    /** תעודת זהות */
    private String idNumber;
    /** כתובת תמונת פרופיל */
    private String imageURL;
    /** שם משפחה */
    private String lastName;
    /** פירוט רפואי רלוונטי */
    private List<String> medicalDetails;
    /** תפקיד המשתמש במערכת */
    private String role;

    /** זמינות כמתנדב - ימי השבוע */
    private List<String> volAvailable;
    /** ערים בהן מתנדב פועל */
    private List<String> volCities;
    /** האם למתנדב יש רישיון נהיגה */
    private Boolean volHaveDriverLicense;
    /** קישור להוכחת התנדבות */
    private String volVerification;
    /** תחומי התנדבות */
    private List<String> volSpecialty;

    /** בנאי פרטי למניעת יצירת מופעים נוספים */
    private UserSession() {}

    /**
     * בנאי ליצירת אובייקט עם מידע בסיסי בלבד
     */
    public UserSession(String firstName, String lastName, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
    }

    /**
     * @return המופע היחיד של UserSession
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * מאתחל את נתוני המשתמש שנשלפו מהמסד.
     */
    public void initialize(String email, String phone, Date birthDate, String city,
                           String firstName, boolean haveGunLicense, String idNumber,
                           String imageURL, String lastName, List<String> medicalDetails, String role,
                           List<String> volAvailable, List<String> volCities,
                           Boolean volHaveDriverLicense, String volVerification,
                           List<String> volSpecialty) {
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.city = city;
        this.firstName = firstName;
        this.haveGunLicense = haveGunLicense;
        this.idNumber = idNumber;
        this.imageURL = imageURL;
        this.lastName = lastName;
        this.medicalDetails = medicalDetails;
        this.role = role;
        this.volAvailable = volAvailable;
        this.volCities = volCities;
        this.volHaveDriverLicense = volHaveDriverLicense;
        this.volVerification = volVerification;
        this.volSpecialty = volSpecialty;
    }

    /** @return כתובת האימייל */
    public String getEmail() { return email; }
    /** @return מספר הטלפון */
    public String getPhone() { return phone; }
    /** @return תאריך הלידה */
    public Date getBirthDate() { return birthDate; }
    /** @return עיר המגורים */
    public String getCity() { return city; }
    /** @return שם פרטי */
    public String getFirstName() { return firstName; }
    /** @return האם יש רישיון נשק */
    public boolean hasGunLicense() { return haveGunLicense; }
    /** @return מספר תעודת הזהות */
    public String getIdNumber() { return idNumber; }
    /** @return כתובת תמונת הפרופיל */
    public String getImageURL() { return imageURL; }
    /** @return שם משפחה */
    public String getLastName() { return lastName; }
    /** @return פרטים רפואיים */
    public List<String> getMedicalDetails() { return medicalDetails; }
    /** @return תפקיד המשתמש */
    public String getRole() { return role; }

    /** @return זמינות כמתנדב */
    public List<String> getVolAvailable() { return volAvailable; }

    /** @return ערי ההתנדבות */
    public List<String> getVolCities() { return volCities; }

    /** @return האם יש רישיון נהיגה */
    public Boolean getVolHaveDriverLicense() { return volHaveDriverLicense; }

    /** @return קישור להוכחת ההתנדבות */
    public String getVolVerification() { return volVerification; }

    /** @return תחומי ההתנדבות */
    public List<String> getVolSpecialty() { return volSpecialty; }

    /** נקה את המופע המאוחסן */
    public void clear() {
        instance = null;
    }
}
