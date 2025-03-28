package com.revature.loan.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.revature.loan.model.User;

public class UserDao {
        private final String jdbcUrl;
        private final String db;
        private final String password;

    public UserDao(String jdbcUrl, String db, String password) {
        this.jdbcUrl = jdbcUrl;
        this.db = db;
        this.password = password;
    }

    public User createUser(User newUser) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?,?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             // Note: We request generated keys if needed
             // maybe in the future we redirect the user and use this key to get their todos
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, newUser.getName());
            stmt.setString(2, newUser.getEmail());
            stmt.setString(3, newUser.getPassword());

            stmt.executeUpdate();

            // Retrieve auto-generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // The first column in 'generatedKeys' is the newly inserted ID
                    int newId = generatedKeys.getInt(1);
                    newUser.setId(newId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the updated User object (now containing its DB-assigned ID)
        return newUser;
    }

    public User getUserByUsername(String name) {
        String sql = "SELECT * FROM users WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role")


                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByID(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role")


                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User updateUser(int id, String name, String email) {
        String updateSql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        String selectSql = "SELECT id, name, email, password, role FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            // Updating user
            updateStmt.setString(1, name);
            updateStmt.setString(2, email);
            updateStmt.setInt(3, id);
            int rowsUpdated = updateStmt.executeUpdate();

            // Checking if user exists
            if (rowsUpdated == 0) {
                return null;
            }

            // Getting data
            selectStmt.setInt(1, id);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }





}
