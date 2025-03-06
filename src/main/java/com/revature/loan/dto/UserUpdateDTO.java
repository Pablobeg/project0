package com.revature.loan.dto;

public class UserUpdateDTO {
    private String name;
    private String email;

    /**
     * Simple DTO for update JSON requests.
     * Example JSON:
     * {
     *   "username": "someUser",
     *   "email":"someEmail",
     * }
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
