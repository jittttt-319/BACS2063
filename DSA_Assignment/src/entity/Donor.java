/**
 * Entity class for Donor Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Ling Jit Xuan
 * Student Id: 2409231
 */
package entity;

public class Donor {

    private String id;
    private String name;
    private Gender genderType;
    private DonorType type; // e.g., government, private, public
    private String contactInfo;
    private double totalDonations;
    private String email;

    public Donor(String id, String name, Gender genderType, DonorType type, String email, String contactInfo) {
        this.id = id;
        this.name = name;
        this.genderType = genderType;
        this.type = type;
        this.email = email;
        this.contactInfo = contactInfo;
        this.totalDonations = 0.0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Gender getGenderType() {
        return genderType;
    }

    public DonorType getType() {
        return type;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public double getTotalDonations() {
        return totalDonations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenderType(Gender genderType) {
        this.genderType = genderType;
    }

    public void setType(DonorType type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTotalDonations(double totalDonations) {
        this.totalDonations = totalDonations;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void addDonation(double amount) {
        this.totalDonations += amount;
    }

    public void removeDonation(double amount) {
        this.totalDonations -= amount;
    }

    public void setDonation(double amount) {
        this.totalDonations = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
