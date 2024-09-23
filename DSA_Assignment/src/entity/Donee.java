/**
 * Entity class for Donee Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Sem Hong Fai
 * Student Id: 2409263
 */
package entity;

public class Donee {

    private String id;
    private String name;
    private String gender;
    private String type;
    private String contactInfo;
    private String email;
    private double totalDonations;

    public Donee(String id, String name, String gender, String type, String contactInfo, String email) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.contactInfo = contactInfo;
        this.email = email;
        this.totalDonations = 0.0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getType() {
        return type;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public double getTotalDonations() {
        return totalDonations;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
