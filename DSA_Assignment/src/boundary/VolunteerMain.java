/**
 * Boundary class for Volunteer Management subsystem of the CareNet Charity Centre Management System.
 *
 * Author: Chee Jia Yu
 * Student Id: 2412192
 */
package boundary;

import control.VolunteerControl;

public class VolunteerMain {

    private VolunteerControl volunteerControl;

    public VolunteerMain(VolunteerControl volunteerControl) {
        this.volunteerControl = volunteerControl;
    }

    public void displayVolunteerMenu() {
        int choice;
        do {
            choice = getVolunteerChoice();
            switch (choice) {
                case 1:
                    volunteerControl.addVolunteer();
                    break;
                case 2:
                    volunteerControl.removeVolunteer();
                    break;
                case 3:
                    volunteerControl.searchVolunteer();
                    break;
                case 4:
                    volunteerControl.assignVolunteer();
                    break;
                case 5:
                    volunteerControl.filterVolunteer();
                    break;
                case 6:
                    volunteerControl.searchEvent();
                    break;
                case 7:
                    volunteerControl.listVolunteers();
                    break;

                case 8:
                    volunteerControl.generateSummaryReport();
                    break;
                case 0:
                    System.out.println("Exiting the Volunteer Management System...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private int getVolunteerChoice() {
        int option;

        System.out.println("======================================");
        System.out.println("      Volunteer Management System     ");
        System.out.println("======================================");
        System.out.printf("%2s %-25s\n", "1.", "Add Volunteer");
        System.out.printf("%2s %-25s\n", "2.", "Remove Volunteer");
        System.out.printf("%2s %-25s\n", "3.", "Search Volunteer");
        System.out.printf("%2s %-25s\n", "4.", "Assign Volunteer to Event");
        System.out.printf("%2s %-25s\n", "5.", "Filter Volunteers");
        System.out.printf("%2s %-25s\n", "6.", "Search Event Under a Volunteer");
        System.out.printf("%2s %-25s\n", "7.", "List All Volunteers");
        System.out.printf("%2s %-25s\n", "8.", "Generate Summary Report");
        System.out.printf("%2s %-5s\n", "0.", "Exit");
        System.out.println("======================================");
        System.out.print("Enter Number (0-8): ");
        option = volunteerControl.getSc().nextInt();
        volunteerControl.getSc().nextLine(); // Consume newline character

        while (option < 0 || option > 8) {
            System.out.print("Invalid option! Please select a number between 0 and 8: ");
            option = volunteerControl.getSc().nextInt();
            volunteerControl.getSc().nextLine(); // Consume newline character
        }
        return option;
    }

}
