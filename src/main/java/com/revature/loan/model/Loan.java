package com.revature.loan.model;

public class Loan {
    public int id;
    public int idUser;
    public int quantity;
    public String type;
    public String status;

    public Loan(){}

    public Loan(int id, int idUser, int quantity,String type, String status){
        this.id=id;
        this.idUser=idUser;
        this.quantity=quantity;
        this.type=type;
        this.status=status;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
