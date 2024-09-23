/**
 * Boundary class for Donation Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Phua Qing Hao
 * Student Id: 2409259
 */
package boundary;

import control.DonationControl;
import java.util.Scanner;

public class DonationMain {

    private DonationControl donationControl;
    private Scanner sc;

    public DonationMain(DonationControl donationControl) {
        this.donationControl = donationControl;
        this.sc = new Scanner(System.in);
    }

    public void displayDonationManagementMenu() {
        int choice;
        do {
            choice = getDonationManagementChoice();
            executeChoice(choice);
        } while (choice != 0);
    }

    public void executeChoice(int choice) {
        switch (choice) {
            case 1:
                donationControl.addDonation();
                break;
            case 2:
                donationControl.removeDonation();
                break;
            case 3:
                donationControl.searchDonation();
                break;
            case 4:
                donationControl.amendDonation();
                break;
            case 5:
                donationControl.listDonationsByDonor();
                break;
            case 6:
                donationControl.trackDonations();
                break;
            case 7:
                donationControl.listAllDonations();
                break;
            case 8:
                donationControl.filterDonations();
                break;
            case 9:
                donationControl.generateSummaryReport();
                break;

            case 0:
                System.out.println("Exiting the Donation Management System...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private int getDonationManagementChoice() {
        int option;

        System.out.println("======================================");
        System.out.println("       Donation Management System     ");
        System.out.println("======================================");
        System.out.printf("%2s %-25s\n", "1.", "Add Donation");
        System.out.printf("%2s %-25s\n", "2.", "Remove Donation");
        System.out.printf("%2s %-25s\n", "3.", "Search Donation");
        System.out.printf("%2s %-25s\n", "4.", "Amend Donation");
        System.out.printf("%2s %-25s\n", "5.", "List Donations by Donor");
        System.out.printf("%2s %-25s\n", "6.", "Track Donations by Categories");
        System.out.printf("%2s %-25s\n", "7.", "List All Donations");
        System.out.printf("%2s %-25s\n", "8.", "Filter Donations");
        System.out.printf("%2s %-25s\n", "9.", "Generate Summary Report");
        System.out.printf("%2s %-5s\n", "0.", "Exit");
        System.out.println("======================================");
        System.out.print("Enter Number (0-8): ");
        option = donationControl.getSc().nextInt();
        donationControl.getSc().nextLine(); // Consume newline character

        while (option < 0 || option > 10) {
            System.out.print("Invalid option! Please select a number between 0 and 8: ");
            option = donationControl.getSc().nextInt();
            donationControl.getSc().nextLine(); // Consume newline character
        }
        return option;
    }

}
