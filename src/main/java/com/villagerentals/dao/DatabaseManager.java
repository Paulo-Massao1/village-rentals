package com.villagerentals.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:villagerentals.db";

    /**
     * Get a connection to the SQLite database.
     * The database file is created automatically if it doesn't exist.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite driver not found: " + e.getMessage());
        }
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Initialize the database by creating all tables if they don't exist.
     * Call this once when the application starts.
     */
    public static void initialize() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Categories table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS categories (
                    category_id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL
                )
            """);

            // Equipment table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS equipment (
                    equipment_id INTEGER PRIMARY KEY,
                    category_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT,
                    daily_rate REAL NOT NULL,
                    FOREIGN KEY (category_id) REFERENCES categories(category_id)
                )
            """);

            // Customers table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INTEGER PRIMARY KEY,
                    last_name TEXT NOT NULL,
                    first_name TEXT NOT NULL,
                    contact_phone TEXT,
                    email TEXT
                )
            """);

            // Rentals table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS rentals (
                    rental_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    date TEXT NOT NULL,
                    customer_id INTEGER NOT NULL,
                    equipment_id INTEGER NOT NULL,
                    rental_date TEXT NOT NULL,
                    return_date TEXT NOT NULL,
                    cost REAL NOT NULL,
                    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
                    FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
                )
            """);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

