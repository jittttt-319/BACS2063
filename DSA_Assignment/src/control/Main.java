package control;

import adt.HashMapInterface;
import boundary.DonationMain;
import boundary.DoneeMain;
import boundary.DonorMain;
import boundary.VolunteerMain;
import dao.InitializeDonee;
import dao.InitializeDonor;
import dao.InitializeVolunteer;
import entity.Donee;
import entity.Donor;
import entity.Event;
import entity.Volunteer;
import java.util.Scanner;

/**
 * Main class for the CareNet Charity Centre Management System. Handles the main
 * menu and subsystem navigation.
 *
 */
public class Main {

    public static void main(String[] args) {

        // Initialize donors and donees using existing classes
        InitializeDonor initializeDonor = new InitializeDonor();
        InitializeDonee initializeDonee = new InitializeDonee();
        InitializeVolunteer initializeVolunteer = new InitializeVolunteer();

        // Retrieve initialized data
        HashMapInterface<String, Donor> donorMap = initializeDonor.getDonors();
        HashMapInterface<String, Donee> doneeMap = initializeDonee.getDonees();
        HashMapInterface<String, Volunteer> volunteerMap = initializeVolunteer.getVolunteers();
        HashMapInterface<String, Event> eventMap = initializeVolunteer.getEvents();

        // Initialize control classes
        DonorControl donorControl = new DonorControl(donorMap);
        DoneeControl doneeControl = new DoneeControl(doneeMap);
        DonationControl donationControl = new DonationControl(donorControl, doneeControl);
        donationControl.initializeDonations();
        VolunteerControl volunteerControl = new VolunteerControl(volunteerMap, eventMap);

        Scanner sc = new Scanner(System.in);
        char selection;

        do {
            printCareNet();

            System.out.println("========================================");
            System.out.println("CareNet Charity Centre Management System");
            System.out.println("========================================");
            System.out.println("1. Donor Management Subsystem");
            System.out.println("2. Donee Management Subsystem");
            System.out.println("3. Donation Management Subsystem");
            System.out.println("4. Volunteer Management Subsystem");
            System.out.println("0. Exit");
            System.out.print("Enter your selection: ");
            selection = sc.next().charAt(0);

            switch (selection) {
                case '1':
                    DonorMain donorUI = new DonorMain(donorControl);
                    donorUI.displayDonorManagementMenu();
                    break;
                case '2':
                    DoneeMain doneeUI = new DoneeMain(doneeControl);
                    doneeUI.displayDoneeMenu();
                    break;
                case '3':
                    DonationMain donationUI = new DonationMain(donationControl);
                    donationUI.displayDonationManagementMenu();
                    break;
                case '4':
                    VolunteerMain volunteerUI = new VolunteerMain(volunteerControl);
                    volunteerUI.displayVolunteerMenu();
                    break;
                case '0':
                    System.out.println("Exiting the Charity Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
            System.out.println(); // Print a newline for better readability
        } while (selection != '0');

        sc.close();
    }

    private static void printCareNet() {
        System.out.println("  ________  ________  ________  _______   ________   _______  _________  ");
        System.out.println(" |\\   ____\\|\\   __  \\|\\   __  \\|\\  ___ \\ |\\   ___  \\|\\  ___ \\|\\___   ___\\ ");
        System.out.println(" \\ \\  \\___|\\ \\  \\|\\  \\ \\  \\|\\  \\ \\   __/|\\ \\  \\\\ \\  \\ \\   __/\\|___ \\  \\_| ");
        System.out.println("  \\ \\  \\    \\ \\   __  \\ \\   _  _\\ \\  \\_|/_\\ \\  \\\\ \\  \\ \\  \\_|/__  \\ \\  \\  ");
        System.out.println("   \\ \\  \\____\\ \\  \\ \\  \\ \\  \\\\  \\\\ \\  \\_|\\ \\ \\  \\\\ \\  \\ \\  \\_|\\ \\  \\ \\  \\ ");
        System.out.println("    \\ \\_______\\ \\__\\ \\__\\ \\__\\\\ _\\\\ \\_______\\ \\__\\\\ \\__\\ \\_______\\  \\ \\__\\");
        System.out.println("     \\|_______|\\|__|\\|__|\\|__|\\|__|\\|_______|\\|__| \\|__|\\|_______|   \\|__|");
        System.out.println();
    }
}
