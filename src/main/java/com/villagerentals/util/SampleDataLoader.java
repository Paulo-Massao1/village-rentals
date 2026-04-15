package com.villagerentals.util;

import com.villagerentals.dao.*;
import com.villagerentals.model.*;
import java.time.LocalDate;

/**
 * Loads sample data into the database on first run.
 * Data comes from the data-samples.xlsx spreadsheet provided by the instructor.
 */
public class SampleDataLoader {

    /**
     * Load sample data only if the database is empty.
     * This way it only runs once on first launch.
     */
    public static void loadIfEmpty() {
        CategoryDAO categoryDAO = new CategoryDAO();

        // If categories already exist, data was already loaded
        if (categoryDAO.count() > 0) {
            System.out.println("Sample data already loaded.");
            return;
        }

        System.out.println("Loading sample data...");
        loadCategories(categoryDAO);
        loadEquipment();
        loadCustomers();
        loadRentals();
        System.out.println("Sample data loaded successfully.");
    }

    private static void loadCategories(CategoryDAO dao) {
        dao.add(new Category(10, "Power tools"));
        dao.add(new Category(20, "Yard equipment"));
        dao.add(new Category(30, "Compressors"));
        dao.add(new Category(40, "Generators"));
        dao.add(new Category(50, "Air Tools"));
    }

    private static void loadEquipment() {
        EquipmentDAO dao = new EquipmentDAO();
        dao.add(new Equipment(101, 10, "Hammer drill", "Powerful drill for concrete and masonry", 25.99));
        dao.add(new Equipment(201, 20, "Chainsaw", "Gas-powered chainsaw for cutting wood", 49.99));
        dao.add(new Equipment(202, 20, "Lawn mower", "Self-propelled lawn mower with mulching function", 19.99));
        dao.add(new Equipment(301, 30, "Small Compressor", "5 Gallon Compressor-Portable", 14.99));
        dao.add(new Equipment(501, 50, "Brad Nailer", "Brad Nailer. Requires 3/4 to 1 1/2 Brad Nails", 10.99));
    }

    private static void loadCustomers() {
        CustomerDAO dao = new CustomerDAO();
        dao.add(new Customer(1001, "Doe", "John", "(555) 555-1212", "jd@sample.net"));
        dao.add(new Customer(1002, "Smith", "Jane", "(555) 555-3434", "js@live.com"));
        dao.add(new Customer(1003, "Lee", "Michael", "(555) 555-5656", "ml@sample.net"));
    }

    private static void loadRentals() {
        RentalDAO dao = new RentalDAO();
        dao.add(new Rental(1000,
            LocalDate.of(2024, 2, 15), 1001, 201,
            LocalDate.of(2024, 2, 20), LocalDate.of(2024, 2, 23),
            149.97));
        dao.add(new Rental(1001,
            LocalDate.of(2024, 2, 16), 1002, 501,
            LocalDate.of(2024, 2, 21), LocalDate.of(2024, 2, 25),
            43.96));
    }
}
