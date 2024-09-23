/**
 * Control class for Donee Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Sem Hong Fai
 * Student Id: 2409263
 */
package control;

import adt.HashMap;
import adt.HashMapInterface;
import entity.Donee;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoneeControl {

    private HashMapInterface<String, Donee> donees;
    private Scanner sc;

    public DoneeControl(HashMapInterface<String, Donee> donees) {
        this.donees = donees;
        sc = new Scanner(System.in);
    }

    public Scanner getSc() {
        return sc;
    }

    public HashMapInterface<String, Donee> getDonees() {
        return donees;
    }

//------------------------------------------------ADD-----------------------------
    public void addDonee() {
        boolean continueAdding = true;
        System.out.println("");
        System.out.println("+-----------------------------+");
        System.out.println("|        ADD NEW DONEE        |");
        System.out.println("+-----------------------------+");

        while (continueAdding) {
            String id = generateNextDoneeId();
            System.out.println("Generated Donee ID: " + id);

            System.out.print("Enter Donee Name: ");
            String name = sc.nextLine();

            String gender;
            while (true) {
                System.out.print("Enter Donee Gender (M - Male, F - Female): ");
                String genderInput = sc.nextLine().toUpperCase();
                switch (genderInput) {
                    case "M":
                        gender = "Male";
                        break;
                    case "F":
                        gender = "Female";
                        break;
                    default:
                        System.out.println("Invalid gender. Please enter M or F.");
                        continue;
                }
                break;
            }

            String type;
            while (true) {
                System.out.print("Enter Donee Type (I - Individual, O - Organization, F - Family): ");
                String typeInput = sc.nextLine().toUpperCase();
                switch (typeInput) {
                    case "I":
                        type = "Individual";
                        break;
                    case "O":
                        type = "Organization";
                        break;
                    case "F":
                        type = "Family";
                        break;
                    default:
                        System.out.println("Invalid type. Please enter I, O, or F.");
                        continue;
                }
                break;
            }

            String email;
            while (true) {
                System.out.print("Enter Donee Email: ");
                email = sc.nextLine();
                if (isValidEmail(email)) {
                    break;
                } else {
                    System.out.println("Invalid email format. Please enter a valid email address.");
                }
            }

            String contactInfo;
            while (true) {
                System.out.print("Enter Donee Contact Info (e.g., 0123456789): ");
                contactInfo = sc.nextLine();
                if (isValidPhoneNumber(contactInfo)) {
                    break;
                } else {
                    System.out.println("Invalid phone number. Please enter a valid phone number.");
                }
            }

            if (!donees.containsKey(id)) {
                Donee donee = new Donee(id, name, gender, type, contactInfo, email);
                donees.put(id, donee);
                System.out.println("Donee " + id + " added successfully.");
            } else {
                System.out.println("Donee with this ID already exists.");
            }

            System.out.print("Do you want to add another donee? (Y/N): ");
            String response = sc.nextLine().toUpperCase();
            if (!response.equals("Y")) {
                continueAdding = false;
            }
        }
    }

    private String generateNextDoneeId() {
        int maxIdNumber = 0;

        for (Object entryObj : donees.entrySet()) {
            HashMap.Entry<String, Donee> entry = (HashMap.Entry<String, Donee>) entryObj;
            String currentId = entry.getKey();
            int currentIdNumber = Integer.parseInt(currentId.substring(2)); // Extract the numeric part
            if (currentIdNumber > maxIdNumber) {
                maxIdNumber = currentIdNumber;
            }
        }

        int nextIdNumber = maxIdNumber + 1;
        return String.format("DN%03d", nextIdNumber);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9]+@[a-zA-Z]+\\.com$");
    }

//----------------------------------------Remove------------------------------------------
    public void removeDonee() {
        boolean continueRemoving = true;
        String id;

        System.out.println("");
        System.out.println("+------------------------------+");
        System.out.println("|        REMOVE DONEE          |");
        System.out.println("+------------------------------+");

        while (continueRemoving) {
            while (true) {
                System.out.print("Enter Donee ID to remove (format DN### or type 'exit' to cancel): ");
                id = sc.nextLine();

                if (id.equalsIgnoreCase("exit")) {
                    System.out.println("Operation cancelled.");
                    break; // Exit the inner loop and return to the outer loop
                }

                if (id.matches("DN\\d{3}")) {
                    Donee donee = donees.get(id);
                    if (donee != null) {
                        displayDoneeInfo(donee);
                        System.out.print("Are you sure you want to delete this Donee? (Y - yes/ N - no): ");
                        String confirmation = sc.nextLine();

                        if (confirmation.equalsIgnoreCase("Y")) {
                            donees.remove(id);
                            System.out.println("Donee removed successfully.");
                            break; // Exit the inner loop after deletion
                        } else {
                            System.out.println("Operation cancelled. You can enter a new ID to remove or type 'exit' to cancel.");
                            break; // Go back to input prompt
                        }
                    } else {
                        System.out.println("Donee not found. Please enter a valid ID.");
                    }
                } else {
                    System.out.println("Invalid format. Please follow the format DN### (e.g., DN001).");
                }
            }

            if (!id.equalsIgnoreCase("exit")) {
                System.out.print("Do you want to remove another donee? (Y/N): ");
                String response = sc.nextLine().toUpperCase();
                if (!response.equals("Y")) {
                    continueRemoving = false;
                }
            } else {
                // If the exit condition was met, don't prompt for continuation
                continueRemoving = false;
            }
        }
    }

    private void displayDoneeInfo(Donee donee) {
        String leftAlignFormat = "| %-10s | %-20s | %-6s | %-15s | %-25s | %-15s | %-15s |%n";
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
        System.out.format("| Donee ID   | Name                 | Gender | Type            | Email                     | Contact Info    | Total Donations |%n");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
        System.out.format(leftAlignFormat, donee.getId(), donee.getName(), donee.getGender(),
                donee.getType(), donee.getEmail(), donee.getContactInfo(), donee.getTotalDonations());
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
    }
//------------------------------------------------------------------------------------------------

    public void updateDonee() {
        boolean continueUpdating = true;

        System.out.println("");
        System.out.println("+------------------------------+");
        System.out.println("|        UPDATE DONEE          |");
        System.out.println("+------------------------------+");

        while (continueUpdating) {
            String id;
            while (true) {
                System.out.print("Enter Donee ID to update (format DN### or type 'exit' to cancel): ");
                id = sc.nextLine();

                if (id.equalsIgnoreCase("exit")) {
                    System.out.println("Operation cancelled.");
                    return; // Exit the method if the user types 'exit'
                }

                if (id.matches("DN\\d{3}")) {
                    Donee donee = donees.get(id);
                    if (donee != null) {
                        displayDoneeInfo(donee);

                        System.out.print("Are you sure you want to update this Donee? (Y - yes / N - no): ");
                        String confirmation = sc.nextLine();

                        if (confirmation.equalsIgnoreCase("Y")) {
                            updateDoneeDetails(donee);
                            System.out.println("Donee updated successfully.");
                        } else {
                            System.out.println("Update operation cancelled.");
                        }
                        break;
                    } else {
                        System.out.println("Donee not found. Please enter a valid ID.");
                    }
                } else {
                    System.out.println("Invalid format. Please follow the format DE### (e.g., DE001).");
                }
            }

            // Ask if the user wants to update another donee
            System.out.print("Do you want to update another donee? (Y/N): ");
            String response = sc.nextLine().toUpperCase();
            if (!response.equals("Y")) {
                continueUpdating = false;
            }
        }
    }

    private void updateDoneeDetails(Donee donee) {
        System.out.print("Enter New Donee Name (leave blank to keep existing): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) {
            donee.setName(name);
        }

        String type;
        while (true) {
            System.out.print("Enter New Donee Type (I - Individual / O - Organization / F - Family, leave blank to keep existing): ");
            String typeInput = sc.nextLine().toUpperCase();
            if (typeInput.isEmpty()) {
                type = donee.getType();
                break;
            } else if (typeInput.equals("I")) {
                type = "Individual";
                break;
            } else if (typeInput.equals("O")) {
                type = "Organization";
                break;
            } else if (typeInput.equals("F")) {
                type = "Family";
                break;
            } else {
                System.out.println("Invalid type. Please enter I, O, or F.");
            }
        }
        donee.setType(type);

        String gender;
        while (true) {
            System.out.print("Enter New Donee Gender (M - Male, F - Female, leave blank to keep existing): ");
            String genderInput = sc.nextLine().toUpperCase();
            if (genderInput.isEmpty()) {
                gender = donee.getGender();
                break;
            } else if (genderInput.equals("M")) {
                gender = "Male";
                break;
            } else if (genderInput.equals("F")) {
                gender = "Female";
                break;
            } else {
                System.out.println("Invalid gender. Please enter M or F.");
            }
        }
        donee.setGender(gender);

        String email;
        while (true) {
            System.out.print("Enter New Donee Email (format abc123456@[a-zA-Z].com, leave blank to keep existing): ");
            email = sc.nextLine();
            if (email.isEmpty()) {
                email = donee.getEmail();
                break;
            } else if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Invalid email format. Please enter a valid email address in the format abc123456@[a-zA-Z].com.");
            }
        }
        donee.setEmail(email);

        String contactInfo;
        while (true) {
            System.out.print("Enter New Donee Contact Info (e.g., 0123456789, leave blank to keep existing): ");
            contactInfo = sc.nextLine();
            if (contactInfo.isEmpty()) {
                contactInfo = donee.getContactInfo();
                break;
            } else if (isValidPhoneNumber(contactInfo)) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter a valid phone number.");
            }
        }
        donee.setContactInfo(contactInfo);
    }

//------------------------------------------------------------------------------------------------------
    public void searchDonee() {
        boolean continueSearching = true;

        while (continueSearching) {
            System.out.println("");
            System.out.println("+------------------------------+");
            System.out.println("|        SEARCH DONEE          |");
            System.out.println("+------------------------------+");
            System.out.println("Search Donee by:");
            System.out.println("1. ID");
            System.out.println("2. Name Substring");
            System.out.println("3. Contact Number");
            System.out.print("Enter choice (1-3): ");
            String choice = sc.nextLine();

            boolean found = false;
            String leftAlignFormat = "| %-10s | %-20s | %-6s | %-15s | %-25s | %-15s | %-15s |%n";
            Iterator<HashMap.Entry<String, Donee>> iterator = donees.iterator();

            switch (choice) {
                case "1":
                    System.out.println("");
                    System.out.print("Enter Donee ID to search (DN###): ");
                    String id = sc.nextLine();

                    while (iterator.hasNext()) {
                        HashMap.Entry<String, Donee> entry = iterator.next();
                        if (entry.getKey().equalsIgnoreCase(id)) {
                            Donee donee = entry.getValue();
                            displayDoneeInfo(donee);
                            found = true;
                            break;
                        }
                    }
                    break;

                case "2":
                    System.out.println("");
                    System.out.print("Enter name substring to search: ");
                    String nameSubstring = sc.nextLine();

                    System.out.println("");
                    System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
                    System.out.format("| Donee ID   | Name                 | Gender | Type            | Email                     | Contact Info    | Total Donations |%n");
                    System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");

                    while (iterator.hasNext()) {
                        HashMap.Entry<String, Donee> entry = iterator.next();
                        Donee donee = entry.getValue();

                        if (donee.getName().toLowerCase().contains(nameSubstring.toLowerCase())) {
                            found = true;
                            System.out.format(leftAlignFormat, donee.getId(), donee.getName(), donee.getGender(),
                                    donee.getType(), donee.getEmail(), donee.getContactInfo(), donee.getTotalDonations());
                        }
                    }

                    if (!found) {
                        System.out.println("| No donors found matching the specified criteria.                                                                             |");
                    }
                    System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
                    break;

                case "3":
                    System.out.println("");
                    System.out.print("Enter contact number to search: ");
                    String contactNumber = sc.nextLine();

                    while (iterator.hasNext()) {
                        HashMap.Entry<String, Donee> entry = iterator.next();
                        Donee donee = entry.getValue();
                        if (donee.getContactInfo().equals(contactNumber)) {
                            found = true;
                            displayDoneeInfo(donee);
                            break; // display only the first match, otherwise remove this break !!!!
                        }
                    }
                    break;

                default:
                    System.out.println("");
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
                    continue;
            }

            if (!found) {
                System.out.println("");
                System.out.println("No donee found with the given criteria.");
            }

            System.out.println("");
            System.out.print("Do you want to search for another Donee? (Y/N): ");
            String response = sc.nextLine().toUpperCase();
            if (!response.equals("Y")) {
                continueSearching = false;
            }
        }
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void listAllDonees() {
        String leftAlignFormat = "| %-10s | %-20s | %-6s | %-15s | %-25s | %-15s | %-15s |%n";

        System.out.println("");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
        System.out.format("| Donee ID   | Name                 | Gender | Type            | Email                     | Contact Info    | Total Donations |%n");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");

        Iterator<HashMap.Entry<String, Donee>> iterator = donees.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Donee> entry = iterator.next();
            Donee donee = entry.getValue();
            System.out.format(leftAlignFormat, donee.getId(), donee.getName(), donee.getGender(),
                    donee.getType(), donee.getEmail(), donee.getContactInfo(), donee.getTotalDonations());
        }

        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");

        char choice;
        boolean validChoice = false;
        do {
            System.out.println("\nChoose your option:");
            System.out.println("1. Sorted by Name");
            System.out.println("2. Sorted by Total Donations");
            System.out.println("0. Exit");
            System.out.print("Enter your choice (0-2): ");
            choice = sc.next().charAt(0);
            sc.nextLine(); // Consume the newline character

            switch (choice) {
                case '1':
                    displaySortedDonees(leftAlignFormat);
                    validChoice = false;
                    break;

                case '2':
                    displaySortedDoneesByTotalDonations(leftAlignFormat);
                    validChoice = false;
                    break;

                case '0':
                    validChoice = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 to sort by name or enter 2 to sort by total donations or 0 to exit.");
            }
        } while (!validChoice);
    }

    private void displaySortedDonees(String leftAlignFormat) {

        System.out.println("");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
        System.out.format("| Donee ID   | Name                 | Gender | Type            | Email                     | Contact Info    | Total Donations |%n");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");

        // Create a temporary HashMap to sort by donor names
        HashMapInterface<String, Donee> tempMap = new HashMap<>();

        // Use the iterator to iterate over donors and add them to the temporary map
        Iterator<HashMap.Entry<String, Donee>> iterator = donees.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Donee> entry = iterator.next();
            String doneeName = entry.getValue().getName();
            tempMap.put(doneeName, entry.getValue());
        }

        // Sort the entries by donor names and iterate over them
        Object[] sortedEntries = tempMap.sortByKey();
        for (Object obj : sortedEntries) {
            @SuppressWarnings("unchecked")
            HashMap.Entry<String, Donee> entry = (HashMap.Entry<String, Donee>) obj;
            Donee donee = entry.getValue();
            System.out.format(leftAlignFormat, donee.getId(), donee.getName(), donee.getGender(),
                    donee.getType(), donee.getEmail(), donee.getContactInfo(), donee.getTotalDonations());
        }
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
    }

    private void displaySortedDoneesByTotalDonations(String leftAlignFormat) {
        System.out.println("");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
        System.out.format("| Donee ID   | Name                 | Gender | Type            | Email                     | Contact Info    | Total Donations |%n");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");

        // Create a temporary HashMap to sort by total donations
        HashMapInterface<String, Donee> tempMap = new HashMap<>();

        // Use the iterator to iterate over donees and add them to the temporary map with total donations as the key
        Iterator<HashMap.Entry<String, Donee>> iterator = donees.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Donee> entry = iterator.next();
            String ttlDonation = String.format("%020.2f", entry.getValue().getTotalDonations()); // Format to keep sorting consistent
            tempMap.put(ttlDonation, entry.getValue());
        }

        // Sort the entries by total donations
        Object[] sortedEntries = tempMap.sortByKey(); // Assume sortByKey sorts keys in ascending order
        for (Object obj : sortedEntries) {
            @SuppressWarnings("unchecked")
            HashMap.Entry<String, Donee> entry = (HashMap.Entry<String, Donee>) obj;
            Donee donee = entry.getValue();
            System.out.format(leftAlignFormat, donee.getId(), donee.getName(), donee.getGender(),
                    donee.getType(), donee.getEmail(), donee.getContactInfo(), donee.getTotalDonations());
        }

        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void filterDoneesByCriteria() {
        int choice;
        do {
            System.out.println("");
            System.out.println("+-------------------------------------------+");
            System.out.println("|          FILTER DONEES BY CRITERIA        |");
            System.out.println("+-------------------------------------------+");
            System.out.println("1. Filter by Type");
            System.out.println("2. Filter by Gender");
            System.out.println("3. Filter by Total Donations");
            System.out.println("4. Filter by Name Substring");
            System.out.println("0. Back to Donee Management Subsystem Menu");
            System.out.print("Enter your choice (0-4): ");

            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    filterDoneesByType();
                    break;
                case 2:
                    filterDoneesByGender();
                    break;
                case 3:
                    filterDoneesByTotalDonations();
                    break;
                case 4:
                    filterDoneesByNameSubstring();
                    break;
                case 0:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    public void filterDoneesByType() {
        String type;

        System.out.println("");
        System.out.println("+-------------------------------+");
        System.out.println("|       FILTER BY TYPE          |");
        System.out.println("+-------------------------------+");

        while (true) {
            System.out.print("Enter Donee Type to filter (I-Individual, O-Organization, F-Family): ");
            type = sc.nextLine().toUpperCase();
            if (type.equals("I") || type.equals("O") || type.equals("F")) {
                break;
            } else {
                System.out.println("Invalid type. Please enter I, O, or F.");
            }
        }

        String fullType = "";
        switch (type) {
            case "I":
                fullType = "Individual";
                break;
            case "O":
                fullType = "Organization";
                break;
            case "F":
                fullType = "Family";
                break;
        }

        boolean found = false;
        System.out.println("");
        System.out.println("Filtered Donees:");
        String leftAlignFormat = "| %-10s | %-20s | %-6s | %-15s | %-25s | %-15s | %-15s |%n";
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
        System.out.format("| Donee ID   | Name                 | Gender | Type            | Email                     | Contact Info    | Total Donations |%n");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");

        Iterator<HashMap.Entry<String, Donee>> iterator = donees.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Donee> entry = iterator.next();
            Donee donee = entry.getValue();
            if (donee.getType().equalsIgnoreCase(fullType)) {
                found = true;
                System.out.format(leftAlignFormat, donee.getId(), donee.getName(), donee.getGender(),
                        donee.getType(), donee.getEmail(), donee.getContactInfo(), donee.getTotalDonations());
            }
        }

        if (!found) {
            System.out.format("| %-124s |%n", "No donees found for the specified type.");
        }

        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
    }

    public void filterDoneesByGender() {

        System.out.println("");
        System.out.println("+-------------------------------+");
        System.out.println("|       FILTER BY GENDER        |");
        System.out.println("+-------------------------------+");

        System.out.print("Enter Donee Gender to filter (M - Male, F - Female): ");
        String genderInput = sc.nextLine().toUpperCase();

        String gender;
        switch (genderInput) {
            case "M":
                gender = "Male";
                break;
            case "F":
                gender = "Female";
                break;
            default:
                System.out.println("Invalid gender. Please enter M or F.");
                return;
        }

        boolean found = false;
        System.out.println("");
        System.out.println("Filtered Donees by Gender:");
        String leftAlignFormat = "| %-10s | %-20s | %-6s | %-15s | %-25s | %-15s | %-15s |%n";
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
        System.out.format("| Donee ID   | Name                 | Gender | Type            | Email                     | Contact Info    | Total Donations |%n");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");

        Iterator<HashMap.Entry<String, Donee>> iterator = donees.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Donee> entry = iterator.next();
            Donee donee = entry.getValue();
            if (donee.getGender().equalsIgnoreCase(gender)) {
                found = true;
                System.out.format(leftAlignFormat, donee.getId(), donee.getName(), donee.getGender(),
                        donee.getType(), donee.getEmail(), donee.getContactInfo(), donee.getTotalDonations());
            }
        }

        if (!found) {
            System.out.println("No donees found for the specified gender.");
        }

        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
    }

    public void filterDoneesByTotalDonations() {
        System.out.println("");
        System.out.println("+-------------------------------+");
        System.out.println("| FILTER BY TOTAL DONATIONS     |");
        System.out.println("+-------------------------------+");

        System.out.print("Enter minimum total donations to filter: ");
        double minDonations;
        try {
            minDonations = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
            return;
        }

        boolean found = false;
        System.out.println("");
        System.out.println("Filtered Donees by Total Donations:");
        String leftAlignFormat = "| %-10s | %-20s | %-6s | %-15s | %-25s | %-15s | %-15s |%n";
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
        System.out.format("| Donee ID   | Name                 | Gender | Type            | Email                     | Contact Info    | Total Donations |%n");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");

        Iterator<HashMap.Entry<String, Donee>> iterator = donees.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Donee> entry = iterator.next();
            Donee donee = entry.getValue();
            if (donee.getTotalDonations() >= minDonations) {
                found = true;
                System.out.format(leftAlignFormat, donee.getId(), donee.getName(), donee.getGender(),
                        donee.getType(), donee.getEmail(), donee.getContactInfo(), donee.getTotalDonations());
            }
        }

        if (!found) {
            System.out.println("No donees found with total donations greater than or equal to the specified amount.");
        }

        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
    }

    public void filterDoneesByNameSubstring() {
        System.out.println("");
        System.out.println("+-------------------------------+");
        System.out.println("| FILTER BY NAME SUBSTRING      |");
        System.out.println("+-------------------------------+");

        System.out.print("Enter substring to filter donees by name: ");
        String substring = sc.nextLine().trim();

        if (substring.isEmpty()) {
            System.out.println("Substring cannot be empty. Please enter a valid substring.");
            return;
        }

        boolean found = false;
        System.out.println("Filtered Donees by Name Substring:");
        String leftAlignFormat = "| %-10s | %-20s | %-6s | %-15s | %-25s | %-15s | %-15s |%n";
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
        System.out.format("| Donee ID   | Name                 | Gender | Type            | Email                     | Contact Info    | Total Donations |%n");
        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");

        Iterator<HashMap.Entry<String, Donee>> iterator = donees.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Donee> entry = iterator.next();
            Donee donee = entry.getValue();
            if (donee.getName().toLowerCase().contains(substring.toLowerCase())) {
                found = true;
                System.out.format(leftAlignFormat, donee.getId(), donee.getName(), donee.getGender(),
                        donee.getType(), donee.getEmail(), donee.getContactInfo(), donee.getTotalDonations());
            }
        }

        if (!found) {
            System.out.println("No donees found with names containing the specified substring.");
        }

        System.out.format("+------------+----------------------+--------+-----------------+---------------------------+-----------------+-----------------+%n");
    }
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void generateSummaryReport() {
        int totalDonors = donees.size();
        double totalDonations = 0.0;
        double maxDonation = 0.0;
        Donee topDonee = null;
        int maleCount = 0, femaleCount = 0;
        int individualCount = 0, organizationCount = 0, familyCount = 0;

        Iterator<HashMap.Entry<String, Donee>> iterator = donees.iterator();

        while (iterator.hasNext()) {
            HashMap.Entry<String, Donee> entry = iterator.next();
            Donee donee = entry.getValue();
            totalDonations += donee.getTotalDonations();

            if (donee.getTotalDonations() > maxDonation) {
                maxDonation = donee.getTotalDonations();
                topDonee = donee;
            }

            if (donee.getGender().equalsIgnoreCase("Male")) {
                maleCount++;
            } else if (donee.getGender().equalsIgnoreCase("Female")) {
                femaleCount++;
            }

            switch (donee.getType()) {
                case "Individual":
                    individualCount++;
                    break;
                case "Organization":
                    organizationCount++;
                    break;
                case "Family":
                    familyCount++;
                    break;
            }
        }

        double averageDonation = (totalDonors > 0) ? totalDonations / totalDonors : 0.0;

        System.out.println("");
        System.out.println("+---------------------------------------------------------------+");
        System.out.println("|                       SUMMARY REPORT                          |");
        System.out.println("+---------------------------------------------------------------+");
        System.out.format("| %-32s | %-26d |%n", "Total Donees", totalDonors);
        System.out.format("| %-32s | %-26.2f |%n", "Total Donations (RM)", totalDonations);
        System.out.format("| %-32s | %-26.2f |%n", "Average Donation per Donee (RM)", averageDonation);
        System.out.println("+---------------------------------------------------------------+");
        System.out.format("| %-32s | %-26s |%n", "<Top Donee>", "");
        System.out.format("| %-32s | %-26s |%n", "Top Donee ID:", topDonee.getId());
        System.out.format("| %-32s | %-26s |%n", "Top Donee Name:", topDonee.getName());
        System.out.format("| %-32s | %-26s |%n", "Top Donee Gender:", topDonee.getGender());
        System.out.format("| %-32s | %-26s |%n", "Top Donee Type:", topDonee.getType());
        System.out.format("| %-32s | %-26s |%n", "Top Donee Email:", topDonee.getEmail());
        System.out.format("| %-32s | %-26s |%n", "Top Donee Contact Info:", topDonee.getContactInfo());
        System.out.format("| %-32s | %-26.2f |%n", "Donations Amount (RM)", maxDonation);
        System.out.println("+---------------------------------------------------------------+");
        System.out.format("| %-32s | %-26d |%n", "Number of Male Donees", maleCount);
        System.out.format("| %-32s | %-26d |%n", "Number of Female Donees", femaleCount);
        System.out.println("+---------------------------------------------------------------+");
        System.out.format("| %-32s | %-26d |%n", "Number of Individual Donees", individualCount);
        System.out.format("| %-32s | %-26d |%n", "Number of Organization Donees", organizationCount);
        System.out.format("| %-32s | %-26d |%n", "Number of Family Donees", familyCount);
        System.out.println("+---------------------------------------------------------------+");
    }
}
