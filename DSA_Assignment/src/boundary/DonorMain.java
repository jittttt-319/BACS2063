/**
 * Boundary class for Donor Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Ling Jit Xuan
 * Student Id: 2409231
 */
package boundary;

import control.DonorControl;

public class DonorMain {

    private DonorControl donorControl;

    public DonorMain(DonorControl donorControl) {
        this.donorControl = donorControl;
    }

    public void displayDonorManagementMenu() {
        int choice;
        do {
            choice = getDonorManagementChoice();
            executeChoice(choice); // Moved execution logic here
        } while (choice != 0);
    }

    private int getDonorManagementChoice() {
        int option = -1;

        System.out.println("======================================");
        System.out.println("        Donor Management System       ");
        System.out.println("======================================");
        System.out.printf("%2s %-25s\n", "1.", "Add Donor");
        System.out.printf("%2s %-25s\n", "2.", "Remove Donor");
        System.out.printf("%2s %-25s\n", "3.", "Update Donor");
        System.out.printf("%2s %-25s\n", "4.", "Search Donor");
        System.out.printf("%2s %-25s\n", "5.", "List All Donors");
        System.out.printf("%2s %-25s\n", "6.", "Filter Donors by Type");
        System.out.printf("%2s %-25s\n", "7.", "Generate Summary Report");
        System.out.printf("%2s %-5s\n", "0.", "Exit");
        System.out.println("======================================");

        // Read the user's input as an integer
        while (true) {
            System.out.print("Enter Number (0-7): ");
            if (donorControl.getSc().hasNextInt()) {
                option = donorControl.getSc().nextInt();
                donorControl.getSc().nextLine(); // Consume newline character

                if (option >= 0 && option <= 7) {
                    break; // Valid input, exit loop
                } else {
                    System.out.println("Invalid choice! Please select a number between 0 and 7.");
                }
            } else {
                System.out.println("Invalid input! Please enter a number between 0 and 7.");
                donorControl.getSc().next(); // Consume the invalid input
            }
        }

        return option;
    }

    private void executeChoice(int choice) {
        switch (choice) {
            case 1:
                donorControl.addDonor();
                break;
            case 2:
                donorControl.removeDonor();
                break;
            case 3:
                donorControl.updateDonor();
                break;
            case 4:
                donorControl.searchDonor();
                break;
            case 5:
                donorControl.listAllDonors();
                break;
            case 6:
                donorControl.filterDonorsByCriteria();
                break;
            case 7:
                donorControl.generateSummaryReport();
                break;
            case 0:
                System.out.println("Exiting the Donor Management System...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

}
