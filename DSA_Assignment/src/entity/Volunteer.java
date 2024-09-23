/**
 * Entity class for Volunteer Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Chee Jia Yu
 * Student Id: 2412192
 */
package entity;

import java.util.HashSet;
import java.util.Set;

public class Volunteer {

    private String id;
    private String name;
    private String gender;
    private String type;
    private String contactInfo;
    private int totalVolunteerHours;
    private Set<String> eventIds;

    public Volunteer(String id, String name, String gender, String type, String contactInfo) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.contactInfo = contactInfo;
        this.eventIds = new HashSet<>();
        this.totalVolunteerHours = 0;
    }

    // Getters
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

    public int getTotalVolunteerHours() {
        return totalVolunteerHours;
    }

    public Set<String> getEventIds() {
        return eventIds;
    }

    // Methods
    public void assignToEvent(String eventId) {
        if (eventId != null && !eventIds.contains(eventId)) {
            eventIds.add(eventId);
        }
    }

    public void addVolunteerHours(int hours) {
        if (hours > 0) {
            this.totalVolunteerHours += hours;
        }
    }

    @Override
    public String toString() {
        return "Volunteer{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", gender='" + gender + '\''
                + ", type='" + type + '\''
                + ", contactInfo='" + contactInfo + '\''
                + ", totalVolunteerHours=" + totalVolunteerHours
                + ", eventIds=" + eventIds
                + '}';
    }
}
