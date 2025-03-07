package com.revature.loan.service;

import com.revature.loan.dao.LoanDao;
import com.revature.loan.model.Loan;

import java.util.List;

public class LoanService {

   private final LoanDao loanDao;

    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    public boolean createLoan(int id, String type, int quantity){
        Loan loanData = new Loan();
        loanData.setIdUser(id);
        loanData.setType(type);
        loanData.setQuantity(quantity);

        loanDao.createLoan(loanData);
        if(loanData!=null){
            return true;
        }
        return false;
    }

    public List<Loan> getAllLoansAdmin(){
        return loanDao.getAllLoans();

    }

    public List<Loan> getAllLoansRegularUser(int id){
        return loanDao.getUserLoans(id);
    }


    public Loan getLoanById(int loanId) {
        Loan loanById =loanDao.getLoanById(loanId);
        if(loanById!=null){
            return loanById;
        }

        return null;

    }

    public Loan updateLoan(int id, int quantity, String loanType){

        Loan updateLoan = loanDao.updateLoan(id,quantity, loanType);

        if(updateLoan!=null){
            return updateLoan;
        }
        return null;

    }

    public Loan approveLoan(int id, String status){

        Loan approved = loanDao.approveLoan(id,status);

        if(approved!=null){
            return approved;
        }
        return null;

    }

    public Loan rejectLoan(int id, String status){

        Loan reject = loanDao.rejectLoan(id,status);

        if(reject!=null){
            return reject;
        }
        return null;

    }


}
