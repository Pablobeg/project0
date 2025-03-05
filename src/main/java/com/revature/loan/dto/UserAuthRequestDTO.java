package com.revature.loan.dto;

public class UserAuthRequestDTO {

    /**
     * Simple DTO for registration/login JSON requests.
     * Example JSON:
     * {
     *   "username": "someUser",
     *   "email":"someEmail",
     *   "password": "somePass"
     * }
     */
    private String username;
    private String email;
    private String password;

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
