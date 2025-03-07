package com.revature.loan.dao;

import com.revature.loan.model.Loan;
import com.revature.loan.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDao {

    private final String jdbcUrl;
    private final String db;
    private final String password;

    public LoanDao(String jdbcUrl, String db, String password) {
        this.jdbcUrl = jdbcUrl;
        this.db = db;
        this.password = password;
    }

    public Loan createLoan(Loan newLoan) {
        String sql = "INSERT INTO loans (quantity, loanType,user_id) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             // Note: We request generated keys if needed
             // maybe in the future we redirect the user and use this key to get their todos
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, newLoan.getQuantity());
            stmt.setString(2, newLoan.getType());
            stmt.setInt(3, newLoan.getIdUser());

            stmt.executeUpdate();

            // Retrieve auto-generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // The first column in 'generatedKeys' is the newly inserted ID
                    int newId = generatedKeys.getInt(1);
                    newLoan.setId(newId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the updated User object (now containing its DB-assigned ID)
        return newLoan;
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Loan alls = new Loan(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("quantity"),
                        rs.getString("loanType"),
                        rs.getString("status")
                );
                loans.add(alls);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public List<Loan> getUserLoans(int id){

        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE user_id=?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Loan alls= new Loan(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("quantity"),
                            rs.getString("loanType"),
                            rs.getString("status")


                    );
                    loans.add(alls);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public Loan getLoanById(int id){
        String sql = "SELECT * FROM loans WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Loan(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("quantity"),
                            rs.getString("loanType"),
                            rs.getString("status")


                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    public Loan updateLoan(int id, int quantity, String loanType) {
        String updateSql = "UPDATE loans SET quantity = ?, loanType = ? WHERE id = ?";
        String selectSql = "SELECT * FROM loans WHERE id = ?";



        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            // Updating loan
            updateStmt.setInt(1, quantity);
            updateStmt.setString(2, loanType);
            updateStmt.setInt(3, id);
            int rowsUpdated = updateStmt.executeUpdate();

            // Checking if loan exists
            if (rowsUpdated == 0) {
                return null;
            }

            // Getting data
            selectStmt.setInt(1, id);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new Loan(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("quantity"),
                            rs.getString("loanType"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Loan approveLoan(int id, String status) {
        String updateSql = "UPDATE loans SET status = ? WHERE id = ?";
        String selectSql = "SELECT * FROM loans WHERE id = ?";



        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            // Updating loan
            updateStmt.setString(1, status);
            updateStmt.setInt(2, id );

            int rowsUpdated = updateStmt.executeUpdate();

            // Checking if loan exists
            if (rowsUpdated == 0) {
                return null;
            }

            // Getting data
            selectStmt.setInt(1, id);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new Loan(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("quantity"),
                            rs.getString("loanType"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Loan rejectLoan(int id, String status) {
        String updateSql = "UPDATE loans SET status = ? WHERE id = ?";
        String selectSql = "SELECT * FROM loans WHERE id = ?";



        try (Connection conn = DriverManager.getConnection(jdbcUrl, db, password);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            // Updating loan
            updateStmt.setString(1, status);
            updateStmt.setInt(2, id );

            int rowsUpdated = updateStmt.executeUpdate();

            // Checking if loan exists
            if (rowsUpdated == 0) {
                return null;
            }

            // Getting data
            selectStmt.setInt(1, id);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new Loan(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("quantity"),
                            rs.getString("loanType"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
