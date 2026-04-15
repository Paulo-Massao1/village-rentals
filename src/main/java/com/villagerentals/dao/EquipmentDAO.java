package com.villagerentals.dao;

import com.villagerentals.model.Equipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {

    /**
     * Get all equipment from the database.
     */
    public List<Equipment> getAll() {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT equipment_id, category_id, name, description, daily_rate FROM equipment ORDER BY equipment_id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Equipment eq = new Equipment(
                    rs.getInt("equipment_id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("daily_rate")
                );
                equipmentList.add(eq);
            }
        } catch (SQLException e) {
            System.err.println("Error getting equipment: " + e.getMessage());
        }
        return equipmentList;
    }

    /**
     * Get a single piece of equipment by its ID.
     */
    public Equipment getById(int equipmentId) {
        String sql = "SELECT equipment_id, category_id, name, description, daily_rate FROM equipment WHERE equipment_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, equipmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Equipment(
                    rs.getInt("equipment_id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("daily_rate")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting equipment: " + e.getMessage());
        }
        return null;
    }

    /**
     * Add a new piece of equipment to the database.
     * Returns true if successful.
     */
    public boolean add(Equipment equipment) {
        String sql = "INSERT INTO equipment (equipment_id, category_id, name, description, daily_rate) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, equipment.getEquipmentId());
            pstmt.setInt(2, equipment.getCategoryId());
            pstmt.setString(3, equipment.getName());
            pstmt.setString(4, equipment.getDescription());
            pstmt.setDouble(5, equipment.getDailyRate());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding equipment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a piece of equipment by its ID.
     * Returns true if a row was deleted.
     */
    public boolean delete(int equipmentId) {
        String sql = "DELETE FROM equipment WHERE equipment_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, equipmentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting equipment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all equipment in a specific category.
     */
    public List<Equipment> getByCategory(int categoryId) {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT equipment_id, category_id, name, description, daily_rate FROM equipment WHERE category_id = ? ORDER BY equipment_id";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Equipment eq = new Equipment(
                    rs.getInt("equipment_id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("daily_rate")
                );
                equipmentList.add(eq);
            }
        } catch (SQLException e) {
            System.err.println("Error getting equipment by category: " + e.getMessage());
        }
        return equipmentList;
    }

    /**
     * Check how many equipment items exist.
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM equipment";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting equipment: " + e.getMessage());
        }
        return 0;
    }
}
