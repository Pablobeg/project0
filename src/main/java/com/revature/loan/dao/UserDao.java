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
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             // Note: We request generated keys if needed
             // maybe in the future we redirect the user and use this key to get their todos
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, newUser.getName());
            stmt.setString(2, newUser.getPassword());

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
}
