package org.example;

public class Driver {
    private String id;//ת"ז
    private String name;//שם
    private String licenceNumber;//מס רישיון
    private String birthDate;//תאריך לידה
    private int illegalActs;//עבירות תנועה מס שלם
    private boolean isActive;// אם הרישיון פעיל או לא

    public Driver(String id, String name, String licenceNumber, String birthDate, int illegalActs, boolean isActive) {
        this.id = id;
        this.name = name;
        this.licenceNumber = licenceNumber;
        this.birthDate = birthDate;
        this.illegalActs = illegalActs;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getAge(){
        return 2024 - Integer.parseInt(this.birthDate.substring(6));
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getIllegalActs() {
        return illegalActs;
    }

    public void setIllegalActs(int illegalActs) {
        this.illegalActs = illegalActs;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", licenceNumber='" + licenceNumber + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", illegalActs=" + illegalActs +
                ", isActive=" + isActive +
                '}';
    }
}
