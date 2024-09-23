/**
 * Entity class for Donation Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Phua Qing Hao
 * Student Id: 2409259
 */
package entity;

import java.time.LocalDateTime;

public class Donation {

    private String donationId;
    private String donorId;
    private String doneeId;
    private String donorName;
    private String doneeName;
    private LocalDateTime dateTime;
    private String itemCategory;
    private double itemAmount;
    private String status;

    // Updated constructor to include donor and donee names
    public Donation(String donationId, String donorId, String donorName, String doneeId, String doneeName, LocalDateTime dateTime, String itemCategory, double itemAmount, String status) {
        this.donationId = donationId;
        this.donorId = donorId;
        this.donorName = donorName;
        this.doneeId = doneeId;
        this.doneeName = doneeName;
        this.dateTime = dateTime;
        this.itemCategory = itemCategory;
        this.itemAmount = itemAmount;
        this.status = status;
    }

    // Getters and setters for donorName and doneeName if not already present
    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDoneeName() {
        return doneeName;
    }

    public void setDoneeName(String doneeName) {
        this.doneeName = doneeName;
    }

    // Getters and setters for all fields
    public String getDonationId() {
        return donationId;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDoneeId() {
        return doneeId;
    }

    public void setDoneeId(String doneeId) {
        this.doneeId = doneeId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public double getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(double itemAmount) {
        if (itemAmount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.itemAmount = itemAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
