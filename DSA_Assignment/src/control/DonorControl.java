/**
 * Control class for Donor Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Ling Jit Xuan
 * Student Id: 2409231
 */
package control;

import adt.HashMap;
import adt.HashMapInterface;
import entity.Donor;
import entity.DonorType;
import entity.Gender;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DonorControl {

    private HashMapInterface<String, Donor> donors;
    private Scanner sc;

    public DonorControl(HashMapInterface<String, Donor> donors) {
        this.donors = donors;
        sc = new Scanner(System.in);
    }

    public Scanner getSc() {
        return sc;
    }

    public void addDonor() {
        do {
            System.out.println("==========================");
            System.out.println("         Add Donor        ");
            System.out.println("==========================\n");

            // Auto-generate Donor ID
            String id = generateUniqueDonorId();

            System.out.println("Generated Donor ID: " + id);
            System.out.print("Enter Donor Name: ");
            String name = sc.nextLine().trim();

            Gender genderType = selectGenderType();

            DonorType type = selectDonorType(); // Use method for type selection

            String email;
            do {
                System.out.print("Enter Donor Email: ");
                email = sc.nextLine().trim();
                if (!isValidEmail(email)) {
                    System.out.println("Invalid email format. Please try again.");
                }
            } while (!isValidEmail(email));

            String contactInfo;
            do {
                System.out.print("Enter Donor Contact Info (10-digit phone number): ");
                contactInfo = sc.nextLine().trim();
                if (!isValidPhoneNumber(contactInfo)) {
                    System.out.println("Invalid phone number. Please enter a 10-digit number.");
                }
            } while (!isValidPhoneNumber(contactInfo));

            // Add Donor to HashMap
            Donor donor = new Donor(id, name, genderType, type, email, contactInfo);
            donors.put(id, donor);
            System.out.println("Donor added successfully.");

            // List all donors without sorting after adding
            displayDonors(false);

        } while (confirmContinue());
    }

    private boolean isValidDonorIdFormat(String id) {
        return id.matches("^DR\\d{3}$");
    }

    public void removeDonor() {
        do {
            System.out.println("==========================");
            System.out.println("       Remove Donor       ");
            System.out.println("==========================\n");
            System.out.print("Enter Donor ID to remove (DR###): ");
            String id = sc.nextLine().trim();

            if (!isValidDonorIdFormat(id)) {
                System.out.println("Invalid Donor ID format. Please try again.");
                continue;
            }

            Donor donor = donors.get(id);
            if (donor != null) {
                printTableHeader();
                printDonorRow(donor);
                printTableFooter();

                System.out.print("Are you sure you want to remove? (Y/y=Yes, Any key=No): ");
                String confirm = sc.nextLine().trim();

                if (!confirm.equalsIgnoreCase("Y")) {
                    System.out.println("Removal cancelled.");
                } else {
                    try {
                        if (donors.remove(id) != null) {
                            System.out.println("Donor with ID " + id + " has been removed.");

                            // List all donors without sorting after removing
                            displayDonors(false);
                        } else {
                            System.out.println("Donor not found.");
                        }
                    } catch (Exception e) {
                        System.out.println("An error occurred while removing the donor: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Donor not found.");
            }
        } while (confirmContinue());
    }

    public void updateDonor() {
        do {
            System.out.println("==========================");
            System.out.println("       Update Donor       ");
            System.out.println("==========================\n");
            System.out.print("Enter Donor ID to update (DR###): ");
            String id = sc.nextLine().trim();

            if (!isValidDonorIdFormat(id)) {
                System.out.println("Invalid Donor ID format. Please try again.");
                continue;
            }

            Donor donor = donors.get(id);
            if (donor != null) {
                printTableHeader();
                printDonorRow(donor);
                printTableFooter();

                try {
                    System.out.print("Enter New Donor Name: ");
                    String name = sc.nextLine().trim();

                    Gender genderType = selectGenderType();

                    DonorType type = selectDonorType(); // Use method for type selection

                    String email;
                    do {
                        System.out.print("Enter New Donor Email: ");
                        email = sc.nextLine().trim();
                        if (!isValidEmail(email)) {
                            System.out.println("Invalid email format. Please try again.");
                        }
                    } while (!isValidEmail(email));

                    String contactInfo;
                    do {
                        System.out.print("Enter New Donor Contact Info (10-digit phone number): ");
                        contactInfo = sc.nextLine().trim();
                        if (!isValidPhoneNumber(contactInfo)) {
                            System.out.println("Invalid phone number. Please enter a 10-digit number.");
                        }
                    } while (!isValidPhoneNumber(contactInfo));

                    donor.setName(name);
                    donor.setGenderType(genderType);
                    donor.setType(type);
                    donor.setEmail(email);
                    donor.setContactInfo(contactInfo);
                    System.out.println("Donor updated successfully.");

                    // List all donors without sorting after updating
                    displayDonors(false);
                } catch (Exception e) {
                    System.out.println("An error occurred while updating the donor: " + e.getMessage());
                }
            } else {
                System.out.println("Donor not found.");
            }
        } while (confirmContinue());
    }

    public void searchDonor() {
        do {
            System.out.println("==========================");
            System.out.println("       Search Donor       ");
            System.out.println("==========================\n");

            System.out.println("Choose search criteria:");
            System.out.println("1. Search by Donor ID");
            System.out.println("2. Search by Name or Email");

            char choice;
            boolean validChoice = false;

            do {
                System.out.print("Enter your choice (1 or 2): ");
                choice = sc.next().charAt(0);
                sc.nextLine(); // Consume the newline character

                switch (choice) {
                    case '1':
                        System.out.print("Enter Donor ID to search (DR###): ");
                        String donorID = sc.nextLine().trim();

                        if (isValidDonorIdFormat(donorID)) {
                            searchByDonorId(donorID);
                        } else {
                            System.out.println("Invalid Donor ID format. Please try again.");
                        }
                        validChoice = true;
                        break;
                    case '2':
                        searchByNameOrEmail();
                        validChoice = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1 for Donor ID or 2 for Name or Email.");
                }
            } while (!validChoice);

        } while (confirmContinue());
    }

    public void listAllDonors() {
        do {
            System.out.println("==========================");
            System.out.println("        All Donors        ");
            System.out.println("==========================\n");

            System.out.println("Choose display option:");
            System.out.println("1. Display in Original Order");
            System.out.println("2. Display Sorted by Name");

            char choice;
            boolean validChoice = false;

            do {
                System.out.print("Enter your choice (1 or 2): ");
                choice = sc.next().charAt(0);
                sc.nextLine(); // Consume the newline character

                switch (choice) {
                    case '1':
                        displayDonors(false); // Display in original order
                        validChoice = true;
                        break;
                    case '2':
                        displayDonors(true); // Display sorted by name
                        validChoice = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1 for Original Order or 2 for Sorted by Name.");
                }
            } while (!validChoice);
        } while (confirmContinue());
    }

    private void displayDonors(boolean sorted) {
        printTableHeader();

        if (sorted) {
            // Create a temporary HashMap to sort by donor names
            HashMapInterface<String, Donor> tempMap = new HashMap<>();

            // Use the iterator to iterate over donors and add them to the temporary map
            Iterator<HashMap.Entry<String, Donor>> iterator = donors.iterator();
            while (iterator.hasNext()) {
                HashMap.Entry<String, Donor> entry = iterator.next();
                String donorName = entry.getValue().getName();
                tempMap.put(donorName, entry.getValue());
            }

            // Sort the entries by donor names and iterate over them
            Object[] sortedEntries = tempMap.sortByKey();
            for (Object obj : sortedEntries) {
                @SuppressWarnings("unchecked")
                HashMap.Entry<String, Donor> entry = (HashMap.Entry<String, Donor>) obj;
                printDonorRow(entry.getValue());
            }

        } else {
            // Iterate over unsorted donors using the iterator
            Iterator<HashMap.Entry<String, Donor>> iterator = donors.iterator();
            while (iterator.hasNext()) {
                HashMap.Entry<String, Donor> entry = iterator.next();
                printDonorRow(entry.getValue());
            }
        }

        printTableFooter();
    }

    public void filterDonorsByCriteria() {
        do {
            System.out.println("==========================");
            System.out.println("      Filter Donor        ");
            System.out.println("==========================\n");

            System.out.println("Choose filter criteria:");
            System.out.println("1. Filter by Gender");
            System.out.println("2. Filter by Donor Type");
            System.out.println("3. Filter by Both Gender and Donor Type");

            char choice;
            boolean validChoice = false;
            Gender genderType = null;
            DonorType donorType = null;
            String criteriaDescription = ""; // To hold the description of the chosen criteria

            do {
                System.out.print("Enter your choice (1, 2, or 3): ");
                choice = sc.next().charAt(0);
                sc.nextLine(); // Consume the newline character

                switch (choice) {
                    case '1':
                        genderType = selectGenderType();
                        criteriaDescription = "Gender: " + genderType;
                        validChoice = true;
                        break;
                    case '2':
                        donorType = selectDonorType();
                        criteriaDescription = "Donor Type: " + donorType;
                        validChoice = true;
                        break;
                    case '3':
                        genderType = selectGenderType();
                        donorType = selectDonorType();
                        criteriaDescription = "Gender: " + genderType + ", Donor Type: " + donorType;
                        validChoice = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1 for Gender, 2 for Donor Type, or 3 for Both.");
                }
            } while (!validChoice);

            // Display filtered donors based on the user's choice
            System.out.println("\nFiltered Donors (" + criteriaDescription + "):");
            boolean found = false;
            double totalFilteredDonations = 0.0; // Variable to track total donations of filtered donors

            printTableHeader();
            Iterator<HashMap.Entry<String, Donor>> iterator = donors.iterator();
            while (iterator.hasNext()) {
                HashMap.Entry<String, Donor> entry = iterator.next();
                Donor donor = entry.getValue();
                boolean match = true;

                if (choice == '1' && donor.getGenderType() != genderType) {
                    match = false;
                } else if (choice == '2' && donor.getType() != donorType) {
                    match = false;
                } else if (choice == '3' && (donor.getGenderType() != genderType || donor.getType() != donorType)) {
                    match = false;
                }

                if (match) {
                    found = true;
                    totalFilteredDonations += donor.getTotalDonations(); // Add to total donations
                    printDonorRow(donor);
                }
            }
            if (!found) {
                System.out.println("| No donors found matching the specified criteria.                                                                                    |");
            }
            printTableFooter();

            // Print total donations for the filtered donors
            if (found) {
                System.out.println("\nTotal Donations for Filtered Donors: RM " + totalFilteredDonations);
            }

        } while (confirmContinue());
    }

    public void generateSummaryReport() {

        System.out.println("==========================");
        System.out.println("      Summary Report      ");
        System.out.println("==========================\n");

        // Variables to track counts and total donations
        int totalDonors = donors.size();
        int maleDonors = 0;
        int femaleDonors = 0;
        int otherDonors = 0;
        int privateDonors = 0;
        int governmentDonors = 0;
        int publicDonors = 0;
        double totalDonations = 0.0;
        Donor topDonor = null;

        // Iterate through the donors using an Iterator
        Iterator<HashMap.Entry<String, Donor>> iterator = donors.iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, Donor> entry = iterator.next();
            Donor donor = entry.getValue();

            // Add to total donations
            totalDonations += donor.getTotalDonations();

            // Find the top donor
            if (topDonor == null || donor.getTotalDonations() > topDonor.getTotalDonations()) {
                topDonor = donor;
            }

            // Count donors by gender
            switch (donor.getGenderType()) {
                case MALE:
                    maleDonors++;
                    break;
                case FEMALE:
                    femaleDonors++;
                    break;
                case OTHERS:
                    otherDonors++;
                    break;
            }

            // Count donors by type
            switch (donor.getType()) {
                case GOVERNMENT:
                    governmentDonors++;
                    break;
                case PRIVATE:
                    privateDonors++;
                    break;
                case PUBLIC:
                    publicDonors++;
                    break;
            }
        }

        // Print summary report table
        printSummaryTableHeader();
        printSummaryTableRow("Total Donors", totalDonors);
        printSummaryTableRow("Male Donors", maleDonors);
        printSummaryTableRow("Female Donors", femaleDonors);
        printSummaryTableRow("Other Donors", otherDonors);
        printSummaryTableRow("Government Donor Type", governmentDonors);
        printSummaryTableRow("Private Donor Type", privateDonors);
        printSummaryTableRow("Public Donor Type", publicDonors);
        printSummaryTableRow("Total Donations (RM)", totalDonations);
        printSummaryTableFooter();

        // Print top donor details
        if (topDonor != null) {
            System.out.println("\nTop Donor:");
            printTableHeader();
            printDonorRow(topDonor);
            printTableFooter();
        }

        // List all donors
        System.out.println("\nList of All Donors:");
        displayDonors(false); // Display all donors without sorting
        printTotalDonationsRow(totalDonations);
    }

    private void printSummaryTableHeader() {
        System.out.println("+--------------------------------+------------------+");
        System.out.printf("| %-30s | %-16s |\n", "Summary", "Value");
        System.out.println("+--------------------------------+------------------+");
    }

    private void printTotalDonationsRow(double totalDonations) {
        System.out.printf("| %-108s | %-20.2f |\n", "Total", totalDonations);
        System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------+");
    }

    private void printSummaryTableRow(String label, Object value) {
        System.out.printf("| %-30s | %-16s |\n", label, value);
    }

    private void printSummaryTableFooter() {

        System.out.println("+--------------------------------+------------------+");
    }

    private void printTableHeader() {
        System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------+");
        System.out.printf("| %-10s | %-20s | %-8s | %-30s | %-10s | %-15s | %-20s |\n", "ID", "Name", "Gender", "Email", "Type", "Contact Info", "Total Donations (RM)");
        System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------+");
    }

    private void printDonorRow(Donor donor) {
        System.out.printf("| %-10s | %-20s | %-8s | %-30s | %-10s | %-15s | %-20.2f |\n",
                donor.getId(),
                donor.getName(),
                donor.getGenderType(),
                donor.getEmail(),
                donor.getType(),
                donor.getContactInfo(),
                donor.getTotalDonations());
    }

    private void printTableFooter() {
        System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------+");
    }

    public HashMap<String, Donor> getDonors() {
        return (HashMap<String, Donor>) donors;
    }

    private boolean isValidEmail(String email) {
        // Simple email validation regex
        String emailRegex = "^[a-zA-Z0-9_+.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    private String generateUniqueDonorId() {
        int donorNumber = donors.size() + 1;
        String id;

        // Generate an ID and ensure it's unique
        do {
            id = String.format("DR%03d", donorNumber);
            donorNumber++;
        } while (donors.containsKey(id));

        return id;
    }

    private void searchByDonorId(String donorID) {

        boolean found = false;

        Iterator<HashMap.Entry<String, Donor>> iterator = donors.iterator();

        while (iterator.hasNext()) {
            HashMap.Entry<String, Donor> entry = iterator.next();
            if (entry.getKey().equalsIgnoreCase(donorID)) {
                printTableHeader();
                printDonorRow(entry.getValue());
                printTableFooter();
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Donor not found.");
        }
    }

    private void searchByNameOrEmail() {
        System.out.print("Enter Donor Name or Email to search: ");
        String input = sc.nextLine().trim();
        boolean found = false;

        printTableHeader();
        Iterator<HashMap.Entry<String, Donor>> iterator = donors.iterator();

        while (iterator.hasNext()) {
            HashMap.Entry<String, Donor> entry = iterator.next();
            Donor donor = entry.getValue();

            if (donor.getName().equalsIgnoreCase(input) || donor.getEmail().equalsIgnoreCase(input)) {
                found = true;
                printDonorRow(donor);
            }
        }

        if (!found) {
            System.out.println("| No donors found matching the specified criteria.                                                  |");
        }
        printTableFooter();
    }

    private Gender selectGenderType() {
        System.out.println("Select Gender:");
        System.out.println("1. MALE");
        System.out.println("2. FEMALE");
        System.out.println("3. OTHER");

        char genderChar;
        Gender genderType = null;
        boolean validGender = false;

        do {
            System.out.print("Enter gender (1 for MALE, 2 for FEMALE, 3 for OTHER): ");
            genderChar = sc.next().charAt(0);
            sc.nextLine(); // Consume the newline character

            switch (genderChar) {
                case '1':
                    genderType = Gender.MALE;
                    validGender = true;
                    break;
                case '2':
                    genderType = Gender.FEMALE;
                    validGender = true;
                    break;
                case '3':
                    genderType = Gender.OTHERS;
                    validGender = true;
                    break;
                default:
                    System.out.println("Invalid gender. Please enter 1 for MALE, 2 for FEMALE, or 3 for OTHER.");
            }
        } while (!validGender);

        return genderType;
    }

    private DonorType selectDonorType() {
        System.out.println("Select Donor Type:");
        System.out.println("1. GOVERNMENT");
        System.out.println("2. PRIVATE");
        System.out.println("3. PUBLIC");

        char typeChar;
        DonorType type = null;
        boolean validType = false;

        do {
            System.out.print("Enter donor type (1 for GOVERNMENT, 2 for PRIVATE, 3 for PUBLIC): ");
            typeChar = sc.next().charAt(0);
            sc.nextLine(); // Consume the newline character

            switch (typeChar) {
                case '1':
                    type = DonorType.GOVERNMENT;
                    validType = true;
                    break;
                case '2':
                    type = DonorType.PRIVATE;
                    validType = true;
                    break;
                case '3':
                    type = DonorType.PUBLIC;
                    validType = true;
                    break;
                default:
                    System.out.println("Invalid type. Please enter 1 for GOVERNMENT, 2 for PRIVATE, or 3 for PUBLIC.");
            }
        } while (!validType);

        return type;
    }

    private boolean confirmContinue() {
        System.out.print("\nDo you want to perform another operation? (Y/y=Yes, Any key=No): ");
        String continueChoice = sc.nextLine().trim();
        return continueChoice.equalsIgnoreCase("Y");
    }
}
