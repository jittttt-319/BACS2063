/**
 * Control class for Donation Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Phua Qing Hao
 * Student Id: 2409259
 */
package control;

import adt.HashMap;
import adt.HashMapInterface;
import entity.Donation;
import entity.Donee;
import entity.Donor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class DonationControl {

    private HashMapInterface<String, Donation> donations;
    private HashMapInterface<String, Donor> donors;
    private HashMapInterface<String, Donee> donees;
    private Scanner sc;
    private DateTimeFormatter dateTimeFormatter;
    private DonorControl donorControl;
    private DoneeControl doneeControl;

    public DonationControl(DonorControl donorControl, DoneeControl doneeControl) {
        this.donorControl = donorControl;
        this.doneeControl = doneeControl;
        this.donations = new HashMap<>();
        this.donors = donorControl.getDonors();
        this.donees = doneeControl.getDonees();
        this.sc = new Scanner(System.in);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
    }

    public Scanner getSc() {
        return sc;
    }

    public void addDonation() {
        char continueAdd = '0';

        System.out.println("==========================");
        System.out.println("       Add Donation       ");
        System.out.println("==========================\n");

        String id = generateUniqueDonationId();
        System.out.println("Generated Donation ID: " + id);

        do {

            // Validate Donation ID format
            String donorId = null;
            String doneeId = null;

            // Get Donor ID
            while (donorId == null) {
                try {
                    System.out.print("Enter Donor ID: ");
                    donorId = sc.nextLine();

                    // Check if Donor ID exists
                    if (!donors.containsKey(donorId)) {
                        throw new IllegalArgumentException("Donor ID not found.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.print("Do you want to try again or exit? (T/t = Try again, any other key = Exit): ");
                    String input = sc.nextLine();
                    if (input.isEmpty() || Character.toUpperCase(input.charAt(0)) != 'T') {
                        return; // Exit the method if the user chooses anything other than T/t
                    }
                    // Retry entering the donor ID
                    donorId = null;
                }
            }

            // Get Donee ID
            while (doneeId == null) {
                try {
                    System.out.print("Enter Donee ID: ");
                    doneeId = sc.nextLine();

                    // Check if Donee ID exists
                    if (!donees.containsKey(doneeId)) {
                        throw new IllegalArgumentException("Donee ID not found.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.print("Do you want to try again or exit? (T/t = Try again, any other key = Exit): ");
                    String input = sc.nextLine();
                    if (input.isEmpty() || Character.toUpperCase(input.charAt(0)) != 'T') {
                        return; // Exit the method if the user chooses anything other than T/t
                    }
                    // Retry entering the donee ID
                    doneeId = null;
                }
            }

            LocalDateTime date = LocalDateTime.now();

            // Enter Item Category
            System.out.print("Enter Item Category (Cash/Funds/Things): ");
            String itemCategory = sc.nextLine();

            Double itemAmount = null;
            while (itemAmount == null) {
                try {
                    // Enter Item Amount
                    System.out.print("Enter Item Amount: ");
                    itemAmount = Double.valueOf(sc.nextLine());

                    if (itemAmount <= 0) {
                        throw new IllegalArgumentException("Amount must be greater than 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount. Please enter a valid number.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    itemAmount = null; // Reset itemAmount to retry input
                }
            }

            String status = null;
            while (status == null) {
                // Enter Donation Status
                System.out.print("Enter Donation Status (1 = Pending, 2 = Received): ");
                char statusOption = sc.nextLine().charAt(0);

                switch (statusOption) {
                    case '1':
                        status = "Pending";
                        break;
                    case '2':
                        status = "Received";
                        break;
                    default:
                        System.out.println("Invalid option. Please enter 1 for Pending or 2 for Received.");
                        break;
                }
            }

            // Get the donor and donee names
            String donorName = donors.get(donorId).getName();
            String doneeName = donees.get(doneeId).getName();

            // Create Donation object with donor and donee names
            Donation donation = new Donation(id, donorId, donorName, doneeId, doneeName, date, itemCategory, itemAmount, status);

            donations.put(id, donation);

            // Update total donations for the donor and donee
            Donor donor = donors.get(donorId);
            donor.addDonation(itemAmount);
            Donee donee = donees.get(doneeId);
            donee.addDonation(itemAmount);

            System.out.println("Donation added successfully with ID: " + id + " and Status: " + status);

            // Ask if the user wants to add more donations
            System.out.print("Do you want to add more? (Y/y = Yes, any other key = No): ");
            continueAdd = sc.nextLine().charAt(0);
        } while (Character.toUpperCase(continueAdd) == 'Y');
    }

    public void removeDonation() {
        char continueDel;

        System.out.println("==========================");
        System.out.println("      Remove Donation     ");
        System.out.println("==========================\n");

        do {
            System.out.print("Enter Donation ID to remove: ");
            String donationId = sc.nextLine();

            // Retrieve the donation to get the donor and amount before removing it
            boolean found = false;

            // Use the iterator to iterate through the donations
            Iterator<HashMap.Entry<String, Donation>> iterator = donations.iterator();

            while (iterator.hasNext()) {
                HashMap.Entry<String, Donation> entry = iterator.next();
                Donation donation = entry.getValue();
                if (donation.getDonationId().equals(donationId)) {
                    found = true;
                    // Print each donation in table format
                    printTable();
                    printDonationEntry(entry);
                    break;
                }
            }

            if (!found) {
                System.out.println("No donations found.");
            } else {
                System.out.print("Are you sure you want to remove? (Y/y=Yes): ");
                String confirm = sc.nextLine();

                if (!confirm.equalsIgnoreCase("Y")) {
                    System.out.println("Removal cancelled.");
                } else {
                    // Remove the donation
                    Donation donation = donations.get(donationId);

                    donations.remove(donationId);

                    // Update the donor's and donee's total donations
                    String donorId = donation.getDonorId();
                    String doneeId = donation.getDoneeId();
                    Double itemAmount = donation.getItemAmount();

                    Donor donor = donors.get(donorId);
                    if (donor != null) {
                        donor.removeDonation(itemAmount);
                    }

                    Donee donee = donees.get(doneeId);
                    if (donee != null) {
                        donee.removeDonation(itemAmount);
                    }

                    System.out.println("Donation with ID " + donationId + " has been removed.");
                }
            }
            System.out.print("Continue Remove? (Y/y=Yes): ");
            continueDel = sc.nextLine().charAt(0);
        } while (Character.toUpperCase(continueDel) == 'Y');
    }

    public void searchDonation() {
        char continueSearch;

        System.out.println("==========================");
        System.out.println("      Search Donation     ");
        System.out.println("==========================\n");

        do {
            System.out.print("Enter Donation ID or Item Name to search: ");
            String donationId = sc.nextLine();

            boolean found = false;

            // Use the iterator to iterate through the donations
            Iterator<HashMap.Entry<String, Donation>> iterator = donations.iterator();

            while (iterator.hasNext()) {
                HashMap.Entry<String, Donation> entry = iterator.next();
                Donation donation = entry.getValue();
                if (donation.getDonationId().equals(donationId) || donation.getItemCategory().equals(donationId)) {
                    found = true;
                    printTable(); // Print the table header
                    printDonationEntry(entry); // Print the matching donation
                    break; // Stop the iteration once the donation is found
                }
            }

            if (!found) {
                System.out.println("Donation not found.");
            }

            System.out.print("Continue search? (Y/y = Yes) : ");
            try {
                continueSearch = sc.nextLine().charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                continueSearch = 'N';
            }
        } while (Character.toUpperCase(continueSearch) == 'Y');
    }

    public void amendDonation() {
        char continueAmend;

        System.out.println("==========================");
        System.out.println("       Amend Donation     ");
        System.out.println("==========================\n");

        do {
            System.out.print("Enter Donation ID to amend: ");
            String id = sc.nextLine();

            Donation donation = donations.get(id);
            if (donation != null) {
                String oldDonorId = donation.getDonorId();
                String oldDoneeId = donation.getDoneeId();
                Double oldItemAmount = donation.getItemAmount();

                // Handle new donor ID
                String newDonorId = null;
                while (newDonorId == null) {
                    try {
                        System.out.print("Enter New Donor ID: ");
                        newDonorId = sc.nextLine();

                        // Check if the donor ID exists
                        if (!donorControl.getDonors().containsKey(newDonorId)) {
                            throw new IllegalArgumentException("Donor ID not found.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        System.out.print("Do you want to exit or continue? (E/e=Exit): ");
                        char exitOrContinue = sc.nextLine().charAt(0);
                        if (Character.toUpperCase(exitOrContinue) == 'E') {
                            return;
                        } else {
                            newDonorId = null;
                        }
                    }
                }

                // Handle new donee ID
                String newDoneeId = null;
                while (newDoneeId == null) {
                    try {
                        System.out.print("Enter New Donee ID: ");
                        newDoneeId = sc.nextLine();

                        // Check if the donee ID exists
                        if (!doneeControl.getDonees().containsKey(newDoneeId)) {
                            throw new IllegalArgumentException("Donee ID not found.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        System.out.print("Do you want to exit or continue? (E/e=Exit): ");
                        char exitOrContinue = sc.nextLine().charAt(0);
                        if (Character.toUpperCase(exitOrContinue) == 'E') {
                            return;
                        } else {
                            newDoneeId = null;
                        }
                    }
                }

                // Update item category
                System.out.print("Enter New Item Category (Cash/Funds/Things): ");
                String itemCategory = sc.nextLine();

                // Handle new item amount
                Double newItemAmount = null;
                while (newItemAmount == null) {
                    try {
                        System.out.print("Enter New Item Amount: ");
                        newItemAmount = Double.valueOf(sc.nextLine());

                        if (newItemAmount <= 0) {
                            throw new IllegalArgumentException("Amount must be greater than 0.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount. Please enter a valid number.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        newItemAmount = null;
                    }
                }

                // Handle donation status
                String status = null;
                while (status == null) {
                    System.out.print("Enter Donation Status (1. Pending, 2. Received): ");
                    String statusChoice = sc.nextLine();

                    switch (statusChoice) {
                        case "1":
                            status = "Pending";
                            break;
                        case "2":
                            status = "Received";
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter 1 for Pending or 2 for Received.");
                            break;
                    }
                }

                // Remove the previous donation amount from the old donor and donee
                Donor oldDonor = donors.get(oldDonorId);
                Donee oldDonee = donees.get(oldDoneeId);
                if (oldDonor != null) {
                    oldDonor.removeDonation(oldItemAmount);
                }
                if (oldDonee != null) {
                    oldDonee.removeDonation(oldItemAmount);
                }

                // Update the donation with the new values
                donation.setDonorId(newDonorId);
                donation.setDoneeId(newDoneeId);
                donation.setDateTime(LocalDateTime.now());
                donation.setItemCategory(itemCategory);
                donation.setItemAmount(newItemAmount);
                donation.setStatus(status);

                // Add the new donation amount to the new donor and donee
                Donor newDonor = donors.get(newDonorId);
                Donee newDonee = donees.get(newDoneeId);
                newDonor.setDonation(newItemAmount);
                newDonee.setDonation(newItemAmount);

                System.out.println("Donation amended successfully.");
            } else {
                System.out.println("Donation not found.");
            }

            System.out.print("Do you want to amend another donation? (Y/y=Yes, N/n=No): ");
            continueAmend = sc.nextLine().charAt(0);
        } while (Character.toUpperCase(continueAmend) == 'Y');
    }

    public void listAllDonations() {
        printTable(); // Print the table header

        // Use the iterator provided by the custom HashMap
        Iterator<HashMap.Entry<String, Donation>> iterator = donations.iterator();

        while (iterator.hasNext()) {
            HashMap.Entry<String, Donation> entry = iterator.next();
            // Print each donation in table format
            printDonationEntry(entry);
        }
    }

    public void listDonationsByDonor() {
        System.out.println("==========================");
        System.out.println("    Donation by Donor     ");
        System.out.println("==========================\n");
        System.out.print("Enter Donor Id or Name: ");
        String donorId = sc.nextLine();

        boolean found = false;

        // Use the iterator to iterate through the donations
        printTable();

        Iterator<HashMap.Entry<String, Donation>> iterator = donations.iterator();

        while (iterator.hasNext()) {
            HashMap.Entry<String, Donation> entry = iterator.next();
            Donation donation = entry.getValue();
            if (donation.getDonorId().equals(donorId) || donation.getDonorName().equals(donorId)) {
                found = true;
                // Print each donation in table format
                printDonationEntry(entry);
            }
        }

        if (!found) {
            System.out.println("No donations found for the specified donor.");
        }
    }

    private void printTable() {
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s| %-10s| %-20s| %-10s| %-20s| %-25s| %-25s| %-16s| %-15s%n",
                "Donation ID",
                "Donor ID",
                "Donor Name",
                "Donee ID",
                "Donee Name",
                "Date Time",
                "Item Category",
                "Item Amount",
                "Status");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");

    }

    public void filterDonations() {
        System.out.println("==========================");
        System.out.println("      Filter Donation     ");
        System.out.println("==========================\n");

        System.out.println("Filter Donations by:");
        System.out.println("1. Time (Newest to Oldest / Oldest to Newest)");
        System.out.println("2. Amount (Highest to Lowest / Lowest to Highest)");
        System.out.println("3. Specific Amount (Less than or Equal / More than or Equal)");

        char filterOption;
        boolean validOption = false;

        do {
            try {
                System.out.print("Enter filter option (1-3) Exit = e: ");
                filterOption = sc.next().charAt(0);
                sc.nextLine(); // Consume newline

                if (filterOption == 'e' || filterOption == 'E') { // Corrected condition
                    return;
                } else {
                    switch (filterOption) {
                        case '1': // Filter by Time
                            char timeSortOption;
                            do {
                                System.out.print("Choose option:\n1. Oldest to Newest\n2. Newest to Oldest\nEnter option (1-2): ");
                                timeSortOption = sc.next().charAt(0);
                                sc.nextLine(); // Consume newline

                                if (timeSortOption != '1' && timeSortOption != '2') {
                                    System.out.println("Invalid option. Please enter '1' for Newest to Oldest or '2' for Oldest to Newest.");
                                }
                            } while (timeSortOption != '1' && timeSortOption != '2');

                            Object[] timeSortedEntries = donations.sortByKey(); // Assuming sorted by date (or key if dates are keys)
                            if (timeSortOption == '2') {
                                // Reverse the sorted array for Oldest to Newest
                                for (int i = 0, j = timeSortedEntries.length - 1; i < j; i++, j--) {
                                    Object temp = timeSortedEntries[i];
                                    timeSortedEntries[i] = timeSortedEntries[j];
                                    timeSortedEntries[j] = temp;
                                }
                            }

                            printTable();
                            for (Object entryObj : timeSortedEntries) {
                                @SuppressWarnings("unchecked")
                                HashMap.Entry<String, Donation> entry = (HashMap.Entry<String, Donation>) entryObj;
                                printDonationEntry(entry);
                            }
                            validOption = true;
                            break;

                        case '2': // Filter by Amount
                            char amountSortOption;
                            do {
                                System.out.print("Choose option:\n1. Highest to Lowest\n2. Lowest to Highest\nEnter option (1-2): ");
                                amountSortOption = sc.next().charAt(0);
                                sc.nextLine(); // Consume newline

                                if (amountSortOption != '1' && amountSortOption != '2') {
                                    System.out.println("Invalid option. Please enter '1' for Highest to Lowest or '2' for Lowest to Highest.");
                                }
                            } while (amountSortOption != '1' && amountSortOption != '2');

                            Object[] amountSortedEntries = donations.sortByKey(); // Assuming sorting by amount
                            if (amountSortOption == '2') {
                                // Reverse the sorted array for Lowest to Highest
                                for (int i = 0, j = amountSortedEntries.length - 1; i < j; i++, j--) {
                                    Object temp = amountSortedEntries[i];
                                    amountSortedEntries[i] = amountSortedEntries[j];
                                    amountSortedEntries[j] = temp;
                                }
                            }

                            printTable();
                            for (Object entryObj : amountSortedEntries) {
                                @SuppressWarnings("unchecked")
                                HashMap.Entry<String, Donation> entry = (HashMap.Entry<String, Donation>) entryObj;
                                printDonationEntry(entry);
                            }
                            validOption = true;
                            break;

                        case '3': // Specific Amount
                            char amountFilterOption;
                            do {
                                System.out.print("Filter Amount: Less than or Equal to (L) or More than or Equal to (M)? Enter option (L/M): ");
                                amountFilterOption = sc.next().charAt(0);
                                sc.nextLine(); // Consume newline

                                if (amountFilterOption != 'L' && amountFilterOption != 'M') {
                                    System.out.println("Invalid option. Please enter 'L' for Less than or Equal or 'M' for More than or Equal.");
                                }
                            } while (amountFilterOption != 'L' && amountFilterOption != 'M');

                            double amount = 0;
                            boolean validAmount = false;
                            do {
                                try {
                                    System.out.print("Enter Amount: ");
                                    amount = Double.parseDouble(sc.nextLine());
                                    validAmount = true;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid amount format. Please enter a valid number.");
                                }
                            } while (!validAmount);

                            printTable();
                            Iterator<HashMap.Entry<String, Donation>> iterator = donations.iterator();

                            while (iterator.hasNext()) {
                                HashMap.Entry<String, Donation> entry = iterator.next();
                                Donation donation = entry.getValue();
                                boolean amountMatches = false;

                                if (amountFilterOption == 'L' && donation.getItemAmount() <= amount) {
                                    amountMatches = true;
                                } else if (amountFilterOption == 'M' && donation.getItemAmount() >= amount) {
                                    amountMatches = true;
                                }

                                if (amountMatches) {
                                    printDonationEntry(entry);
                                }
                            }
                            validOption = true;
                            break;

                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                sc.nextLine(); // Clear invalid input
            }
        } while (!validOption);
    }

    private void printDonationEntry(HashMap.Entry<String, Donation> entry) {
        Donation donation = entry.getValue();

        System.out.printf("%-12s| %-10s| %-20s| %-10s| %-20s| %-25s| %-25s| %-16.2f| %-15s%n",
                donation.getDonationId(),
                donation.getDonorId(),
                donation.getDonorName(),
                donation.getDoneeId(),
                donation.getDoneeName(),
                donation.getDateTime().format(dateTimeFormatter),
                donation.getItemCategory(),
                donation.getItemAmount(),
                donation.getStatus()
        );
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");

    }

    public void generateSummaryReport() {
        int totalDonations = 0;
        int totalDonorsWithDonations = 0;
        int totalUniqueDonees = 0;
        double maxDonationAmount = 0.0;
        double totalAmount = 0;
        String topDonorId = "";
        String topDonorName = "";
        String topDoneeId = "";
        String topDoneeName = "";
        double totalCashOrFundsAmount = 0.0;
        double totalOtherAmount = 0.0;

        String[] donorsWithDonations = new String[donations.size()];
        String[] uniqueDonees = new String[donations.size()];
        int donorCount = 0;
        int doneeCount = 0;

        Iterator<HashMap.Entry<String, Donation>> iterator = donations.iterator();

        while (iterator.hasNext()) {
            HashMap.Entry<String, Donation> entry = iterator.next();
            Donation donation = entry.getValue();
            totalDonations++;

            double donationAmount = donation.getItemAmount();
            if (donationAmount > maxDonationAmount) {
                maxDonationAmount = donationAmount;
                topDonorId = donation.getDonorId();
                topDoneeId = donation.getDoneeId();
            }

            String itemCategory = donation.getItemCategory();
            if (itemCategory.equalsIgnoreCase("Cash") || itemCategory.equalsIgnoreCase("Funds")) {
                totalCashOrFundsAmount += donationAmount;
            } else {
                totalOtherAmount += donationAmount;
            }

            boolean donorExists = false;
            for (int i = 0; i < donorCount; i++) {
                if (donorsWithDonations[i].equals(donation.getDonorId())) {
                    donorExists = true;
                    break;
                }
            }
            if (!donorExists) {
                donorsWithDonations[donorCount++] = donation.getDonorId();
                totalDonorsWithDonations++;
            }

            boolean doneeExists = false;
            for (int i = 0; i < doneeCount; i++) {
                if (uniqueDonees[i].equals(donation.getDoneeId())) {
                    doneeExists = true;
                    break;
                }
            }
            if (!doneeExists) {
                uniqueDonees[doneeCount++] = donation.getDoneeId();
                totalUniqueDonees++;
            }

            totalAmount = totalCashOrFundsAmount + totalOtherAmount;
        }

        Donor topDonor = donors.get(topDonorId);
        Donee topDonee = donees.get(topDoneeId);

        System.out.println("+---------------------------------------------------------------+");
        System.out.println("|                         SUMMARY REPORT                        |");
        System.out.println("+---------------------------------------------------------------+");
        System.out.format("| %-32s | %-26d |%n", "Total Donations", totalDonations);
        System.out.format("| %-32s | %-26d |%n", "Total Donors Donate", totalDonorsWithDonations);
        System.out.format("| %-32s | %-26d |%n", "Total Donees Receive", totalUniqueDonees);
        System.out.format("| %-32s | RM %-23.2f |%n", "Total Cash or Funds Amount", totalCashOrFundsAmount);
        System.out.format("| %-32s | RM %-23.2f |%n", "Total Other Amount", totalOtherAmount);
        System.out.format("| %-32s | RM %-23.2f |%n", "Total Amount", totalAmount);
        System.out.println("+---------------------------------------------------------------+");

        System.out.format("| %-32s | %-26s |%n", "<Top Donor>", "");
        System.out.format("| %-32s | %-26s |%n", "Top Donor ID", topDonor.getId());
        System.out.format("| %-32s | %-26s |%n", "Top Donor Name", topDonor.getName());
        System.out.format("| %-32s | RM %-23.2f |%n", "Highest Donation Amount", maxDonationAmount);
        System.out.println("+---------------------------------------------------------------+");

        System.out.format("| %-32s | %-26s |%n", "<Top Donee>", "");
        System.out.format("| %-32s | %-26s |%n", "Top Donee ID", topDonee.getId());
        System.out.format("| %-32s | %-26s |%n", "Top Donee Name", topDonee.getName());
        System.out.format("| %-32s | RM %-23.2f |%n", "Highest Received Amount", maxDonationAmount);

        System.out.println("+---------------------------------------------------------------+");
    }

    public void initializeDonations() {
        // Example donation initialization with status
        Donation donation1 = new Donation("D001", "DR001", "Alice Johnson", "DN001", "Sem Hong Fai", LocalDateTime.now(), "Food", 100.0, "Pending");
        donations.put(donation1.getDonationId(), donation1);

        Donation donation2 = new Donation("D002", "DR002", "Bob Smith", "DN002", "Ling Jit Xuan", LocalDateTime.now(), "Clothes", 200.0, "Received");
        donations.put(donation2.getDonationId(), donation2);

        Donation donation3 = new Donation("D003", "DR003", "Charlie Brown", "DN003", "Sarah Lee", LocalDateTime.now(), "Books", 150.0, "Pending");
        donations.put(donation3.getDonationId(), donation3);

        Donation donation4 = new Donation("D004", "DR004", "Diana Prince", "DN004", "Ahmad bin Yusof", LocalDateTime.now(), "Toys", 80.0, "Received");
        donations.put(donation4.getDonationId(), donation4);

        Donation donation5 = new Donation("D005", "DR005", "Edward Norton", "DN005", "Emily Tan", LocalDateTime.now(), "Furniture", 300.0, "Pending");
        donations.put(donation5.getDonationId(), donation5);

        // Update donor and donee records
        Donor donor1 = donors.get("DR001");
        donor1.addDonation(100.0);
        Donee donee1 = donees.get("DN001");
        donee1.addDonation(100.0);

        Donor donor2 = donors.get("DR002");
        donor2.addDonation(200.0);
        Donee donee2 = donees.get("DN002");
        donee2.addDonation(200.0);

        Donor donor3 = donors.get("DR003");
        donor3.addDonation(150.0);
        Donee donee3 = donees.get("DN003");
        donee3.addDonation(150.0);

        Donor donor4 = donors.get("DR004");
        donor4.addDonation(80.0);
        Donee donee4 = donees.get("DN004");
        donee4.addDonation(80.0);

        Donor donor5 = donors.get("DR005");
        donor5.addDonation(300.0);
        Donee donee5 = donees.get("DN005");
        donee5.addDonation(300.0);
    }

    public void trackDonations() {

        System.out.println("==========================");
        System.out.println("     Track Donations      ");
        System.out.println("==========================\n");

        int pendingCount = 0;
        int receivedCount = 0;

        // Iterate through the donations to count Pending and Received statuses
        for (Object entryObj : donations.entrySet()) {
            HashMap.Entry<String, Donation> entry = (HashMap.Entry<String, Donation>) entryObj;
            Donation donation = entry.getValue();

            if (donation.getStatus().equalsIgnoreCase("Pending")) {
                pendingCount++;
            } else if (donation.getStatus().equalsIgnoreCase("Received")) {
                receivedCount++;
            }
        }

        // Print the counts below the logo
        System.out.println("Total Pending Donations: " + pendingCount);
        System.out.println("Total Received Donations: " + receivedCount);
        System.out.println();

        System.out.print("Enter Item Category (Cash/Funds/Things): ");
        String category = sc.nextLine().trim();

        System.out.print("Enter Donation Status (1 = Pending, 2 = Received): ");
        int statusOption;
        try {
            statusOption = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid status option. Please enter 1 for Pending or 2 for Received.");
            return;
        }

        String status;
        switch (statusOption) {
            case 1:
                status = "Pending";
                break;
            case 2:
                status = "Received";
                break;
            default:
                System.out.println("Invalid status option. Please enter 1 for Pending or 2 for Received.");
                return;
        }

        boolean found = false;

        System.out.println("\n" + "Status: " + status + "\n");

        printTable();

        for (Object entryObj : donations.entrySet()) {
            HashMap.Entry<String, Donation> entry = (HashMap.Entry<String, Donation>) entryObj;
            Donation donation = entry.getValue();

            if (donation.getItemCategory().equalsIgnoreCase(category) && donation.getStatus().equalsIgnoreCase(status)) {
                found = true;

                printDonationEntry(entry);
            }
        }

        if (!found) {
            System.out.println("No donations found for Category: " + category + " and Status: " + status);
        }
    }

    private String generateUniqueDonationId() {
        int maxIdNumber = 0;

        for (Object entryObj : donations.entrySet()) {
            HashMap.Entry<String, Donation> entry = (HashMap.Entry<String, Donation>) entryObj;
            String currentId = entry.getKey();
            int currentIdNumber = Integer.parseInt(currentId.substring(2)); // Extract the numeric part
            if (currentIdNumber > maxIdNumber) {
                maxIdNumber = currentIdNumber;
            }
        }

        int nextIdNumber = maxIdNumber + 1;
        return String.format("D%03d", nextIdNumber);
    }

}
