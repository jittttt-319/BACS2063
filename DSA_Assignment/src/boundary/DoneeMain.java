/**
 * Boundary class for Donee Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Sem Hong Fai
 * Student Id: 2409263
 */
package boundary;

import control.DoneeControl;

public class DoneeMain {

    private DoneeControl doneeControl;

    public DoneeMain(DoneeControl doneeControl) {
        this.doneeControl = doneeControl;
    }

    public void displayDoneeMenu() {
        int choice;
        do {
            choice = getDoneeChoice();
            switch (choice) {
                case 1:
                    doneeControl.addDonee();
                    break;
                case 2:
                    doneeControl.removeDonee();
                    break;
                case 3:
                    doneeControl.updateDonee();
                    break;
                case 4:
                    doneeControl.searchDonee();
                    break;
                case 5:
                    doneeControl.listAllDonees();
                    break;
                case 6:
                    doneeControl.filterDoneesByCriteria();
                    break;
                case 7:
                    doneeControl.generateSummaryReport();
                    break;
                case 0:
                    System.out.println("Exiting the Donor Management System...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private int getDoneeChoice() {
        int option;

        System.out.println("======================================");
        System.out.println("        Donee Management System       ");
        System.out.println("======================================");
        System.out.printf("%2s %-25s\n", "1.", "Add Donee");
        System.out.printf("%2s %-25s\n", "2.", "Remove Donee");
        System.out.printf("%2s %-25s\n", "3.", "Update Donee");
        System.out.printf("%2s %-25s\n", "4.", "Search Donee");
        System.out.printf("%2s %-25s\n", "5.", "List All Donees");
        System.out.printf("%2s %-25s\n", "6.", "Filter Donees by Criteria");
        System.out.printf("%2s %-25s\n", "7.", "Generate Summary Report");
        System.out.printf("%2s %-5s\n", "0.", "Exit");
        System.out.println("======================================");
        System.out.print("Enter Number (0-7): ");
        option = doneeControl.getSc().nextInt();
        doneeControl.getSc().nextLine(); // Consume newline character

        while (option < 0 || option > 7) {
            System.out.print("Invalid option! Please select a number between 0 and 7: ");
            option = doneeControl.getSc().nextInt();
            doneeControl.getSc().nextLine(); // Consume newline character
        }
        return option;
    }
}
