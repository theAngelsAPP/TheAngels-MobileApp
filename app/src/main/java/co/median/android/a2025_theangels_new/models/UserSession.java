package co.median.android.a2025_theangels_new.models;

import java.util.List;

public class UserSession {
    private static UserSession instance;

    private String email;
    private String phone;
    private String birthDate;
    private String city;
    private String firstName;
    private boolean haveGunLicense;
    private String idNumber;
    private String imageURL;
    private String lastName;
    private List<String> medicalDetails;
    private String role;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void initialize(String email, String phone, String birthDate, String city,
                           String firstName, boolean haveGunLicense, String idNumber,
                           String imageURL, String lastName, List<String> medicalDetails, String role) {
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
    }

    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getBirthDate() { return birthDate; }
    public String getCity() { return city; }
    public String getFirstName() { return firstName; }
    public boolean hasGunLicense() { return haveGunLicense; }
    public String getIdNumber() { return idNumber; }
    public String getImageURL() { return imageURL; }
    public String getLastName() { return lastName; }
    public List<String> getMedicalDetails() { return medicalDetails; }
    public String getRole() { return role; }

    public void clear() {
        instance = null;
    }
}
