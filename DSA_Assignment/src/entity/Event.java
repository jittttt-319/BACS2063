/**
 * Entity class for Volunteer Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Chee Jia Yu
 * Student Id: 2412192
 */
package entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Event {

    private String id;
    private String name;
    private LocalDate date; // Added field for event date
    private Map<String, Volunteer> assignedVolunteers;

    public Event(String id, String name, LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.assignedVolunteers = new HashMap<>();
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

    public LocalDate getDate() {
        return date; // Return the event date
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Collection<Volunteer> getAssignedVolunteers() {
        return assignedVolunteers.values();
    }

    public void assignVolunteer(Volunteer volunteer) {
        if (volunteer != null) {
            assignedVolunteers.put(volunteer.getId(), volunteer);
            volunteer.assignToEvent(this.id);
        }
    }

    @Override
    public String toString() {
        return "Event{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", date=" + date
                + // Include date in the toString method
                ", assignedVolunteers=" + assignedVolunteers.keySet()
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
