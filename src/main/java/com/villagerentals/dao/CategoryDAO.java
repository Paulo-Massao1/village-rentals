package com.villagerentals.dao;

import com.villagerentals.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    /**
     * Get all categories from the database.
     */
    public List<Category> getAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, name FROM categories ORDER BY category_id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category cat = new Category(
                    rs.getInt("category_id"),
                    rs.getString("name")
                );
                categories.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("Error getting categories: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Get a single category by its ID.
     */
    public Category getById(int categoryId) {
        String sql = "SELECT category_id, name FROM categories WHERE category_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Category(
                    rs.getInt("category_id"),
                    rs.getString("name")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting category: " + e.getMessage());
        }
        return null;
    }

    /**
     * Add a new category to the database.
     */
    public boolean add(Category category) {
        String sql = "INSERT INTO categories (category_id, name) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, category.getCategoryId());
            pstmt.setString(2, category.getName());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding category: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check how many categories exist (used to know if sample data was loaded).
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM categories";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting categories: " + e.getMessage());
        }
        return 0;
    }
}
