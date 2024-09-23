package dao;

import adt.HashMap;
import adt.HashMapInterface;
import entity.Donor;
import entity.DonorType;
import entity.Gender;

/**
 * InitializeDonor class for setting up initial donors in the system. Provides a
 * method to get the initialized donor map.
 *
 * Author: Jit Xuan
 */
public class InitializeDonor {

    // HashMapInterface to store donors
    private HashMapInterface<String, Donor> donors;

    /**
     * Constructor to initialize donors.
     */
    public InitializeDonor() {
        // Initialize the donor HashMap
        donors = new HashMap<>();

        // Create donor objects
        Donor donor1 = new Donor("DR001", "Alice Johnson", Gender.FEMALE, DonorType.PUBLIC, "alice.johnson@example.com", "0123456789");

        Donor donor2 = new Donor("DR002", "Bob Smith", Gender.MALE, DonorType.PRIVATE, "bob.smith@example.com", "0129876543");

        Donor donor3 = new Donor("DR003", "Charlie Brown", Gender.MALE, DonorType.GOVERNMENT, "charlie.brown@example.com", "0124356789");

        Donor donor4 = new Donor("DR004", "Diana Prince", Gender.FEMALE, DonorType.PUBLIC, "diana.prince@example.com", "0182039405");

        Donor donor5 = new Donor("DR005", "Edward Norton", Gender.MALE, DonorType.PRIVATE, "edward.norton@example.com", "0192345672");

        Donor donor6 = new Donor("DR006", "Alex Jack", Gender.MALE, DonorType.PRIVATE, "alexJack@example.com", "0122771035");

        // Put donors into the map
        donors.put(donor1.getId(), donor1);
        donors.put(donor2.getId(), donor2);
        donors.put(donor3.getId(), donor3);
        donors.put(donor4.getId(), donor4);
        donors.put(donor5.getId(), donor5);
        donors.put(donor6.getId(), donor6);

    }

    /**
     * Gets the initialized donor map.
     *
     * @return HashMapInterface of donors.
     */
    public HashMapInterface<String, Donor> getDonors() {
        return donors;
    }
}
