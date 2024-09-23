/**
 * Control class for Volunteer Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Chee Jia Yu
 * Student Id: 2412192
 */
package control;

import adt.HashMap;
import adt.HashMapInterface;
import entity.Event;
import entity.Volunteer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

public class VolunteerControl {

    private HashMapInterface<String, Volunteer> volunteers;
    private HashMapInterface<String, Event> events;
    private HashMapInterface<String, Boolean> validTypes;
    private HashMapInterface<String, String> typeMapping;
    private HashMapInterface<String, Volunteer> volunteerMap;
    private HashMapInterface<String, Event> eventMap;
    private Scanner sc;

    public VolunteerControl() {
        volunteers = new HashMap<>();
        events = new HashMap<>();
        validTypes = new HashMap<>();
        typeMapping = new HashMap<>();
        initializeValidTypes();
        sc = new Scanner(System.in);
    }

    public VolunteerControl(HashMapInterface<String, Volunteer> volunteerMap, HashMapInterface<String, Event> eventMap) {
        this.volunteers = volunteerMap;
        this.events = eventMap; // Initialize eventMap
        this.validTypes = new HashMap<>();
        this.typeMapping = new HashMap<>();
        initializeValidTypes();
        this.volunteerMap = volunteerMap;
        this.eventMap = eventMap;
        this.sc = new Scanner(System.in);
    }

    public Scanner getSc() {
        return sc;
    }

    private void initializeValidTypes() {
        String[] types = {"Community", "Corporate", "Government"};

        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            validTypes.put(type, true);
            typeMapping.put(String.valueOf(i + 1), type);  // Map "1" to "Community", "2" to "Corporate", etc.
        }
    }

    public void addVolunteer() {
        System.out.println("");
        System.out.println("+------------------------------+");
        System.out.println("|        ADD  VOLUNTEER        |");
        System.out.println("+------------------------------+");

        try {
            String id;
            while (true) {
                id = promptForValidInput("Enter Volunteer ID (VL001~999): ", this::validateVolunteerID);

                // Check if the volunteer ID already exists
                if (volunteers.containsKey(id)) {
                    System.out.println("Volunteer with this ID already exists. Please enter a different ID.");
                } else {
                    break; // Exit the loop if the ID is unique
                }
            }

            String name = promptForValidInput("Enter Volunteer Name: ", this::validateVolunteerName);
            String gender = promptForValidInput("Enter Gender (Male/Female/Other): ", this::validateGender);

            String typeIndex = promptForValidInput("Enter Volunteer Type (1.Community, 2.Corporate, 3.Government): ", this::validateVolunteerType);

            // Retrieve the corresponding type name from the typeMapping
            String typeName = typeMapping.get(typeIndex);
            if (typeName == null) {
                throw new IllegalArgumentException("Invalid volunteer type selected.");
            }

            String contactInfo = promptForValidInput("Enter Contact Info (Email): ", this::validateContactInfo);

            // Create a Volunteer with the type name and gender
            Volunteer volunteer = new Volunteer(id, name, gender, typeName, contactInfo);
            volunteers.put(id, volunteer);
            System.out.println("Volunteer added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding volunteer: " + e.getMessage());
        }
    }

    // Validation methods
    private boolean validateVolunteerID(String id) {
        return id.matches("^VL\\d{3}$") && Integer.parseInt(id.substring(2)) <= 999;
    }

    private boolean validateVolunteerType(String typeIndex) {
        return typeIndex.equals("1") || typeIndex.equals("2") || typeIndex.equals("3");
    }

    private boolean validateContactInfo(String contactInfo) {
        return contactInfo.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
    }

    private String validateGender(String gender) {
        if (gender.isEmpty()) {
            return "Gender is required.";
        }
        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female") && !gender.equalsIgnoreCase("Other")) {
            return "Invalid gender. Please enter Male, Female, or Other.";
        }
        return null;
    }

    private String promptForValidInput(String promptMessage, Function<String, String> validationFunction) {
        while (true) {
            System.out.print(promptMessage);
            String input = sc.nextLine().trim();
            String validationResult = validationFunction.apply(input);
            if (validationResult == null) {
                return input;
            } else {
                System.out.println(validationResult);
            }
        }
    }

    private String validateVolunteerName(String name) {
        if (name.isEmpty()) {
            return "Volunteer Name is required.";
        }
        if (!name.matches("[a-zA-Z\\s]+")) {
            return "Volunteer Name can only contain letters and spaces.";
        }
        return null;
    }

    private boolean isValidEmail(String email) {
        final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public void removeVolunteer() {
        boolean continueRemoving = true;

        System.out.println("");
        System.out.println("+------------------------------+");
        System.out.println("|      REMOVE VOLUNTEER        |");
        System.out.println("+------------------------------+");

        // List all volunteers before prompting for removal
        listVolunteers();

        while (continueRemoving) {
            String id = promptForVolunteerId();
            if (id == null || id.isEmpty()) {
                // User chose to exit the operation or input is invalid
                System.out.println("Operation cancelled or invalid input.");
                continueRemoving = false;
                continue;
            }

            Volunteer volunteer = volunteers.get(id);
            if (volunteer != null) {
                if (confirmDeletion(volunteer)) {
                    volunteers.remove(id);
                    System.out.println("Volunteer removed successfully.");
                } else {
                    System.out.println("Operation cancelled.");
                }
            } else {
                System.out.println("Volunteer not found. Please enter a valid ID.");
            }

            // Prompt the user if they want to continue removing other volunteers
            continueRemoving = promptForAnotherRemoval();
        }
    }

// Updating the confirmDeletion method to display volunteer details
    private boolean confirmDeletion(Volunteer volunteer) {
        System.out.println("Are you sure you want to delete this Volunteer?");
        System.out.printf("ID: %s | Name: %s | Type: %s | Contact Info: %s%n",
                volunteer.getId(),
                volunteer.getName(),
                volunteer.getType(),
                volunteer.getContactInfo());

        while (true) {
            System.out.print("Confirm deletion (Y - yes / N - no): ");
            String confirmation = sc.nextLine().trim().toUpperCase();

            if (confirmation.equals("Y")) {
                return true;
            } else if (confirmation.equals("N")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for yes or 'N' for no.");
            }
        }
    }

    private String promptForVolunteerId() {
        System.out.print("Enter Volunteer ID to remove or type 'exit' to cancel: ");
        String id = sc.nextLine().trim();
        return id.equalsIgnoreCase("exit") ? null : id;
    }

    private boolean promptForAnotherRemoval() {
        System.out.print("Do you want to remove another volunteer? (Y - yes / N - no): ");
        String response = sc.nextLine().trim().toUpperCase();
        return response.equals("Y");
    }

    public void searchEvent() {
        System.out.println("");
        System.out.println("+------------------------------+");
        System.out.println("|         SEARCH EVENT         |");
        System.out.println("+------------------------------+");

        // List all events before searching
        listEvents();

        // Prompt for a valid event ID
        String id = promptForValidInput("Enter Event ID to search (EV001-999): ", this::validateEventID);

        // Get sorted entries by event ID
        Object[] sortedEntries = events.sortByKey();

        // Flag to indicate if the event was found
        boolean eventFound = false;

        // Search through sorted entries
        for (Object entry : sortedEntries) {
            @SuppressWarnings("unchecked")
            HashMap.Entry<String, Event> hashMapEntry = (HashMap.Entry<String, Event>) entry;
            Event event = hashMapEntry.getValue();

            if (event.getId().equals(id)) {
                // Event found
                System.out.println("+---------------------------------------------+");
                System.out.println("|                EVENT DETAILS                |");
                System.out.println("+----------------------+----------------------|");
                System.out.printf("| %-20s | %-20s |%n", "ID", event.getId());
                System.out.printf("| %-20s | %-20s |%n", "Name", event.getName());
                System.out.printf("| %-20s | %-20s |%n", "Date", event.getDate());
                System.out.println("+----------------------+----------------------+");

                // Print assigned volunteers
                System.out.println("|          ASSIGNED VOLUNTEERS                |");
                System.out.println("+----------------------+----------------------+");
                System.out.printf("| %-20s | %-20s |%n", "Volunteer ID", "Volunteer Name");
                System.out.println("+----------------------+----------------------+");
                if (event.getAssignedVolunteers().isEmpty()) {
                    System.out.printf("| %-42s |%n", "No volunteers assigned.");
                } else {
                    for (Volunteer volunteer : event.getAssignedVolunteers()) {
                        System.out.printf("| %-20s | %-20s |%n", volunteer.getId(), volunteer.getName());
                    }
                }
                System.out.println("+----------------------+----------------------+");

                eventFound = true;
                break; // Exit loop once the event is found
            }
        }

        if (!eventFound) {
            // Handle case where the event is not found
            System.out.println("Event not found.");
        }
    }

    private String validateEventID(String id) {
        if (id.isEmpty()) {
            return "Event ID is required.";
        }
        String idPattern = "^EV\\d{3,}$";
        if (!id.matches(idPattern)) {
            return "Invalid Event ID. ID must start with 'EV' followed by at least three digits.";
        }
        if (!events.containsKey(id)) {
            return "Event not found.";
        }
        return null;
    }

    public void assignVolunteer() {
        System.out.println("");
        System.out.println("+------------------------------+");
        System.out.println("|      ASSIGN VOLUNTEER        |");
        System.out.println("+------------------------------+");
        printVolunteerTable();

        // Prompt for Volunteer ID with validation
        String volunteerId = promptForValidInput("Enter Volunteer ID to assign (VL001-999): ", this::validateVolunteerID);

        // Check for exit condition
        if (volunteerId.equals("0")) {
            System.out.println("Exiting assignment.");
            return; // Exit the method
        }

        // Prompt for Event ID with validation
        listEvents();
        String eventId = promptForValidInput("Enter Event ID to assign to (EV001-999): ", this::validateEventID);

        // Check for exit condition
        if (eventId.equals("0")) {
            System.out.println("Exiting assignment.");
            return; // Exit the method
        }

        // Retrieve Volunteer and Event
        Volunteer volunteer = volunteers.get(volunteerId);
        Event event = events.get(eventId);

        // Assign Volunteer to Event if both are found
        if (volunteer != null && event != null) {
            event.assignVolunteer(volunteer);
            System.out.println("Volunteer assigned to event successfully.");

            // Display assignment in a new table
            displayAssignmentTable(volunteer, event);
        } else {
            System.out.println("Volunteer or Event not found.");
        }
    }

    private String promptForValidInput(String prompt, Validator validator) {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.equals("0")) {
                return "0"; // Return "0" for exit
            }
            if (validator.validate(input)) {
                return input;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    @FunctionalInterface
    interface Validator {

        boolean validate(String input);
    }

    public void searchVolunteer() {
        System.out.println("");
        System.out.println("+------------------------------+");
        System.out.println("|      SEARCH VOLUNTEER        |");
        System.out.println("+------------------------------+");
        printVolunteerTable();

        // Prompt for Volunteer ID
        Scanner scanner = new Scanner(System.in);
        String id = null;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter Volunteer ID to search (VL001-999), or enter 0 to exit: ");
            id = scanner.nextLine();

            // Validate input
            if (id.equals("0")) {
                System.out.println("Exiting search.");
                return; // Exit the method
            }

            // Check if ID is in the correct format
            if (id.matches("VL\\d{3}")) {
                validInput = true; // Input is valid, exit loop
            } else {
                System.out.println("Invalid ID format. Please enter an ID in the format VL001-999.");
            }
        }

        // Search for the volunteer using the HashMap's get method
        Volunteer volunteer = (Volunteer) volunteers.get(id);

        if (volunteer != null) {
            // Print header for volunteer information
            System.out.println("+--------------------------------------------------------------------------------------------+");
            System.out.println(String.format("|%-15s | %-15s | %-6s | %-14s | %-30s|",
                    "Volunteer ID", "Name", "Gender", "Type", "Contact Info"));
            System.out.println("|--------------------------------------------------------------------------------------------|");

            // Print information for the found volunteer
            System.out.println(String.format("|%-15s | %-15s | %-6s | %-14s | %-30s|",
                    volunteer.getId(), volunteer.getName(), volunteer.getGender(),
                    volunteer.getType(), volunteer.getContactInfo()));
            System.out.println("+--------------------------------------------------------------------------------------------+");
        } else {
            System.out.println("Volunteer not found.");
        }
    }

    public void listVolunteers() {
        System.out.println("");
        System.out.println("LISTING ALL VOLUNTEERS:");
        System.out.println("+--------------------------------------------------------------------------------------------+");
        System.out.println(String.format("| %-15s | %-15s | %-6s | %-14s | %-28s |",
                "Volunteer ID", "Name", "Gender", "Type", "Contact Info"));
        System.out.println("|--------------------------------------------------------------------------------------------|");

        Iterator<HashMap.Entry<String, Volunteer>> iterator = volunteers.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Volunteer> entry = iterator.next();
            Volunteer volunteer = entry.getValue();
            System.out.printf("| %-15s | %-15s | %-6s | %-14s | %-28s |%n",
                    volunteer.getId(),
                    volunteer.getName(),
                    capitalize(volunteer.getGender()), // Ensure proper casing
                    volunteer.getType(), // Ensure this returns appropriate formatted values
                    volunteer.getContactInfo());
        }

        System.out.println("+--------------------------------------------------------------------------------------------+");
    }

// Helper method to capitalize the first letter of a string
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public void filterVolunteer() {
        Scanner sc = new Scanner(System.in);
        boolean continueFiltering = true;

        while (continueFiltering) {
            System.out.println("");
            System.out.println("+------------------------------+");
            System.out.println("|      FILTER VOLUNTEER        |");
            System.out.println("+------------------------------+");
            System.out.println("Select fields to display:");
            System.out.println("1. ID");
            System.out.println("2. Name");
            System.out.println("3. Type");
            System.out.println("4. Email");
            System.out.println("0. Exit");
            System.out.print("Enter your choice (e.g., 1,2,3) or 0 to exit: ");
            String[] fields = sc.nextLine().trim().split(",");

            // Check if the user wants to exit
            if (fields.length == 1 && fields[0].equals("0")) {
                continueFiltering = false;
                System.out.println("Exiting filter.");
                continue;
            }

            // Get sorted entries
            Object[] sortedEntries = volunteers.sortByKey();

            // Display report based on selected fields
            System.out.println();
            System.out.println("+------------------------------+");
            System.out.println("| Volunteer Filtered Report    |");
            System.out.println("+------------------------------+");

            // Print header based on the selected field
            for (String field : fields) {
                switch (field) {
                    case "1":
                        System.out.println("| ID                           |");
                        break;
                    case "2":
                        System.out.println("| Name                         |");
                        break;
                    case "3":
                        System.out.println("| Type                         |");
                        break;
                    case "4":
                        System.out.println("| Email                        |");
                        break;
                    default:
                        System.out.println("Invalid choice: " + field);
                        continue; // Skip invalid choices
                }
                System.out.println("+------------------------------+");

                // Print data rows
                for (Object entry : sortedEntries) {
                    @SuppressWarnings("unchecked")
                    HashMap.Entry<String, Volunteer> hashMapEntry = (HashMap.Entry<String, Volunteer>) entry;
                    Volunteer volunteer = hashMapEntry.getValue();

                    switch (field) {
                        case "1":
                            System.out.printf("| %-28s |%n", volunteer.getId());
                            break;
                        case "2":
                            System.out.printf("| %-28s |%n", volunteer.getName());
                            break;
                        case "3":
                            System.out.printf("| %-28s |%n", volunteer.getType());
                            break;
                        case "4":
                            System.out.printf("| %-28s |%n", volunteer.getContactInfo());
                            break;
                    }
                }
                System.out.println("+------------------------------+");
            }
        }
    }

    public void generateSummaryReport() {
        System.out.println("************************************************************");
        System.out.println("                     Summary Report                         ");
        System.out.println("************************************************************");
        System.out.println("|----------------------------------------------------------|");
        System.out.println("| Event Name                   |   Volunteers Assigned     |");
        System.out.println("|----------------------------------------------------------|");

        // Calculate total number of volunteers
        int totalVolunteers = volunteers.size();

        // Create a set to track unique volunteers assigned to any event
        Set<String> volunteersAssigned = new HashSet<>();

        // Iterate through events to track assigned volunteers
        for (Object eventKey : events.keySet()) {
            Event event = events.get((String) eventKey);
            for (Volunteer volunteer : event.getAssignedVolunteers()) {
                volunteersAssigned.add(volunteer.getId());
            }
        }

        // Number of volunteers assigned to any event
        int numberAssigned = volunteersAssigned.size();

        // Number of volunteers not assigned to any event
        int numberNotAssigned = totalVolunteers - numberAssigned;

        // Sort events by key (event ID) using the custom HashMap's sorting method
        Object[] sortedEntries = events.sortByKey();

        // Print the event summary
        for (Object entry : sortedEntries) {
            @SuppressWarnings("unchecked")
            HashMap.Entry<String, Event> mapEntry = (HashMap.Entry<String, Event>) entry;
            Event event = mapEntry.getValue();
            // Ensure the number is right-aligned within a 22-character wide field
            System.out.printf("| %-28s | %-25d |%n", event.getName(), event.getAssignedVolunteers().size());
        }

        // Add a line for separation
        System.out.println("|----------------------------------------------------------|");

        // Print total number of volunteers and their distribution
        System.out.printf("| %-45s %10d |%n", "Total number of volunteers:", totalVolunteers);
        System.out.printf("| %-45s %10d |%n", "Number of volunteers assigned to events:", numberAssigned);
        System.out.printf("| %-45s %8d |%n", "Number of volunteers not assigned to any event:", numberNotAssigned);

        // Add a final line for the footer
        System.out.println("|----------------------------------------------------------|");
        System.out.println("************************************************************");
        System.out.println("                        End of Summary Report              ");
        System.out.println("************************************************************");
    }

    public void listEvents() {
        if (events.isEmpty()) {
            System.out.println("No events to display.");
            return;
        }

        tableEvent();

        Iterator<HashMap.Entry<String, Event>> iterator = events.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Event> entry = iterator.next();
            Event event = entry.getValue();
            System.out.printf("| %-18s | %-18s |%n",
                    event.getId(),
                    event.getName());
        }
        System.out.println("+--------------------+--------------------+");
    }

    public void printVolunteerTable() {
        System.out.println("");
        System.out.println("+------------------------------+");
        System.out.println("|       VOLUNTEER ID TABLE     |");
        System.out.println("+------------------------------+");

        // Get sorted entries from the custom HashMap
        Object[] sortedEntries = volunteers.sortByKey();
        for (Object entryObj : sortedEntries) {
            @SuppressWarnings("unchecked")
            HashMap.Entry<String, Volunteer> entry = (HashMap.Entry<String, Volunteer>) entryObj;
            String id = entry.getKey();
            System.out.println("| " + id + "                        |");
        }

        System.out.println("+------------------------------+");
    }

    public void tableEvent() {
        System.out.println("+--------------------+--------------------+");
        System.out.println("| Event ID           | Event Name         |");
        System.out.println("+--------------------+--------------------+");
    }

    private void displayAssignmentTable(Volunteer volunteer, Event event) {
        System.out.println("");
        System.out.println("+------------------------------+");
        System.out.println("|  VOLUNTEER ASSIGNMENTS       |");
        System.out.println("+------------------------------+");
        System.out.println("+--------------------+--------------------+--------------------+");
        System.out.println("| Volunteer ID       | Event ID           | Event Name         |");
        System.out.println("+--------------------+--------------------+--------------------+");
        System.out.printf("| %-18s | %-18s | %-18s |%n",
                volunteer.getId(),
                event.getId(),
                event.getName());
        System.out.println("+--------------------+--------------------+--------------------+");
    }

}
