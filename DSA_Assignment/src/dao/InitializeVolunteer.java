/**
 * Dao class for Volunteer Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Chee Jia Yu
 */
package dao;

import java.time.LocalDate;
import adt.HashMap;
import adt.HashMapInterface;
import entity.Event;
import entity.Volunteer;

public class InitializeVolunteer {

    private HashMapInterface<String, Volunteer> volunteers;
    private HashMapInterface<String, Event> events;

    public InitializeVolunteer() {
        volunteers = new HashMap<>();
        events = new HashMap<>();

        // Initialize Volunteers
        Volunteer volunteer1 = new Volunteer("VL001", "John Doe", "Male", "Community", "john.doe@example.com");
        Volunteer volunteer2 = new Volunteer("VL002", "Jane Smith", "Female", "Community", "jane.smith@example.com");
        Volunteer volunteer3 = new Volunteer("VL003", "Alice Green", "Female", "Corporate", "alice.green@example.com");
        Volunteer volunteer4 = new Volunteer("VL004", "Bob Brown", "Male", "Government", "bob.brown@example.com");

        volunteers.put(volunteer1.getId(), volunteer1);
        volunteers.put(volunteer2.getId(), volunteer2);
        volunteers.put(volunteer3.getId(), volunteer3);
        volunteers.put(volunteer4.getId(), volunteer4);

        // Initialize Events
        LocalDate date1 = LocalDate.of(2024, 9, 15);
        LocalDate date2 = LocalDate.of(2024, 10, 20);
        LocalDate date3 = LocalDate.of(2024, 11, 5);

        Event event1 = new Event("EV001", "Charity Run", date1);
        Event event2 = new Event("EV002", "Food Drive", date2);
        Event event3 = new Event("EV003", "Community Clean-Up", date3);

        event1.assignVolunteer(volunteer1);
        event1.assignVolunteer(volunteer2);

        event2.assignVolunteer(volunteer3);

        event3.assignVolunteer(volunteer4);

        events.put(event1.getId(), event1);
        events.put(event2.getId(), event2);
        events.put(event3.getId(), event3);

    }

    public HashMapInterface<String, Volunteer> getVolunteers() {
        return volunteers;
    }

    public HashMapInterface<String, Event> getEvents() {
        return events;
    }

}
