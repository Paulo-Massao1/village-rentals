package com.villagerentals.dao;

import com.villagerentals.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    /**
     * Get all customers from the database.
     */
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT customer_id, last_name, first_name, contact_phone, email FROM customers ORDER BY customer_id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer cust = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("last_name"),
                    rs.getString("first_name"),
                    rs.getString("contact_phone"),
                    rs.getString("email")
                );
                customers.add(cust);
            }
        } catch (SQLException e) {
            System.err.println("Error getting customers: " + e.getMessage());
        }
        return customers;
    }

    /**
     * Get a single customer by ID.
     */
    public Customer getById(int customerId) {
        String sql = "SELECT customer_id, last_name, first_name, contact_phone, email FROM customers WHERE customer_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("last_name"),
                    rs.getString("first_name"),
                    rs.getString("contact_phone"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting customer: " + e.getMessage());
        }
        return null;
    }

    /**
     * Add a new customer to the database.
     * Returns true if successful.
     */
    public boolean add(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, last_name, first_name, contact_phone, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customer.getCustomerId());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getFirstName());
            pstmt.setString(4, customer.getContactPhone());
            pstmt.setString(5, customer.getEmail());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a customer by ID.
     * Returns true if a row was deleted.
     */
    public boolean delete(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check how many customers exist.
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM customers";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting customers: " + e.getMessage());
        }
        return 0;
    }
}
