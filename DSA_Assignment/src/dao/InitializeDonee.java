/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import adt.HashMap;
import adt.HashMapInterface;
import entity.Donee;

/**
 *
 * @author hongf
 */
public class InitializeDonee {

    // HashMapInterface to store donors
    private HashMapInterface<String, Donee> donees;

    public InitializeDonee() {
        // Initialize the donor HashMap
        donees = new HashMap<>();

        // Create donor objects
        Donee donee1 = new Donee("DN001", "Sem Hong Fai", "Male", "Individual", "0182255723", "hongfai2004@gmail.com");
        donee1.addDonation(500.00);

        Donee donee2 = new Donee("DN002", "Ling Jit Xuan", "Male", "Individual", "01821321633", "ljx2004@gmail.com");
        donee2.addDonation(1000.00);

        Donee donee3 = new Donee("DN003", "Sarah Lee", "Female", "Family", "0198765432", "sarahlee@gmail.com");
        donee3.addDonation(750.00);

        Donee donee4 = new Donee("DN004", "Ahmad bin Yusof", "Male", "Individual", "0123456789", "ahmadyusof@yahoo.com");
        donee4.addDonation(300.00);

        Donee donee5 = new Donee("DN005", "Emily Tan", "Female", "Organization", "0176543210", "emilytan123@gmail.com");
        donee5.addDonation(1200.00);

        // Put donors into the map
        donees.put(donee1.getId(), donee1);
        donees.put(donee2.getId(), donee2);
        donees.put(donee3.getId(), donee3);
        donees.put(donee4.getId(), donee4);
        donees.put(donee5.getId(), donee5);
    }

    /**
     * Gets the initialized donor map.
     *
     * @return HashMapInterface of donors.
     */
    public HashMapInterface<String, Donee> getDonees() {
        return donees;
    }
}
