package com.revature.loan.dto;

public class LoanDto {
    /**
     * Handles post /loans/
     * {
     *    "quantity": "someQuantity",
     *    "loanType":"someLoanType",
     *    "user_id":"someUser_id"

     * }
     */
    private int quantity;
    private String loanType;



    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
