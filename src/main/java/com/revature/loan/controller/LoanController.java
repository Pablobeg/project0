package com.revature.loan.controller;

import com.revature.loan.dto.LoanDto;
import com.revature.loan.dto.LoanDtoStatus;
import com.revature.loan.dto.UserUpdateDTO;
import com.revature.loan.model.Loan;
import com.revature.loan.model.User;
import com.revature.loan.service.LoanService;
import io.javalin.http.Context;

import java.util.List;

import static com.revature.loan.controller.UserController.checkSession;
import static com.revature.loan.controller.UserController.getUserSession;

public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Handles post /loans/
     * {
     *    "quantity": "someQuantity",
     *    "loanType":"someLoanType",

     * }
     */
    public void createLoan(Context ctx){
        if (!checkSession(ctx)){
            ctx.status(400).json("{\"error\":\"No active session\"}");
            return;
        }



        //Getting id from actual session
        User userSession= getUserSession(ctx);

        int sessionUserId=userSession.getId();

        //Passing body as loanDto
        LoanDto loanData= ctx.bodyAsClass(LoanDto.class);
        //Checking if all fields are ok
        if (loanData.getLoanType() == null || loanData.getQuantity() == 0) {
            ctx.status(400).json("{\"error\":\"Missing type or quantity, loan can't be zero\"}");
            return;
        }
        //Checking if the query was ok
        boolean success = loanService.createLoan(sessionUserId, loanData.getLoanType(), loanData.getQuantity());
        if (success) {
            ctx.status(201).json("{\"message\":\"Loan success\"}");
        } else {
            ctx.status(409).json("{\"error\":\"\"}");
       }




    }
    /**
     * Handles get /loans/

     */

    public void getAllLoans(Context ctx){
        if (!checkSession(ctx)){
            ctx.status(400).json("{\"error\":\"No active session\"}");
            return;
        }
        //Get the session and attributes its admin
        User userSession=getUserSession(ctx);

        boolean isAdmin = userSession.getRol() != null && userSession.getRol().equals("admin");
        //Checking user rol to send the right query
        if(isAdmin){
            List<Loan> allLoans =loanService.getAllLoansAdmin();
            if(allLoans==null || allLoans.isEmpty()){
                ctx.status(404).json("There is no data");
                return;
            }
            ctx.json(allLoans);
        }else if (userSession.getRol().equals("user")){
            int idUser=userSession.getId();
            List<Loan> allLoans =loanService.getAllLoansRegularUser(idUser);
            if(allLoans==null || allLoans.isEmpty()){
                ctx.status(404).json("There is no data");
                return;
            }
            ctx.json(allLoans);
        }
    }

    /**
     * Handles get /loans/{loanid}
     */

    public void viewLoan(Context ctx){

        if (!checkSession(ctx)){
            ctx.status(400).json("{\"error\":\"No active session\"}");
            return;
        }
        //Getting id user from url
        String id= ctx.pathParam("loanId");
        int loanId = Integer.parseInt(id);
        //Getting id user, role from session actual

        User userSession=getUserSession(ctx);
        boolean isAdmin = userSession.getRol() != null && userSession.getRol().equals("admin");
        //Checking if the current user is admin and if not block them

        if(!isAdmin && userSession.getId() != loanId){
            ctx.status(403).json("{\\\"error\\\":\\\"Unauthorized access\\\"}");
            return;
        }

        Loan loan =loanService.getLoanById(loanId);
        if(loan==null){
            ctx.status(404).json("{\"error\":\"Loan not found\"}");

        }else{


            ctx.json(loan);
        }

    }

    /**
     * Handles put /loans/{loanId} with a JSON body:
     * {
     *    "quantity": "someQuantity",
     *    "loanType":"sometype",
     * }
     */

    public void updateLoan(Context ctx){
        if (!checkSession(ctx)){
            ctx.status(400).json("{\"error\":\"No active session\"}");
            return;
        }
        //Getting id loan from url
        String id= ctx.pathParam("loanId");
        int loanId= Integer.parseInt(id);
        //Getting id loan, role from session actual

        User userSession= getUserSession(ctx);

        boolean isAdmin = userSession.getRol() != null && userSession.getRol().equals("admin");



        //Checking if the current user is admin and if not block them

        if(!isAdmin && userSession.getId() != loanId){
            ctx.status(403).json("{\"error\":\"Unauthorized access\"}");
            return;
        }

        //Body data to object
        LoanDto loanUpdate = ctx.bodyAsClass(LoanDto.class);

        if (loanUpdate.getQuantity() == 0 || loanUpdate.getLoanType() == null) {
            ctx.status(400).json("{\"error\":\"Missing information\"}");
            return;
        }

        //Checking if loan exist
        Loan loan = loanService.getLoanById(loanId);
        if(loan==null){
            ctx.status(404).json("{\"error\":\"Loan not found\"}");
            return;
        }else{

            //Passing data to user object
            Loan updateloan=loanService.updateLoan(loanId, loanUpdate.getQuantity(), loanUpdate.getLoanType());

            ctx.json(updateloan);
        }

    }

    /**
     * Handles put /loans/{loanId}/approve with a JSON body:
     * {
     *    "status": "approved",
     *
     * }
     */

    public void approveLoan(Context ctx){
        if (!checkSession(ctx)){
            ctx.status(400).json("{\"error\":\"No active session\"}");
            return;
        }
        //Getting id loan from url
        String id= ctx.pathParam("loanId");
        int loanId= Integer.parseInt(id);
        //Getting id loan, role from session actual

        User userSession= getUserSession(ctx);

        boolean isAdmin = userSession.getRol() != null && userSession.getRol().equals("admin");



        //Checking if the current user is admin and if not block them

        if(!isAdmin){
            ctx.status(403).json("{\"error\":\"Unauthorized access\"}");
            return;
        }

        //Body data to object
        LoanDtoStatus loanApprove = ctx.bodyAsClass(LoanDtoStatus.class);

        if (!(loanApprove.getStatus().equals("approved"))) {
            ctx.status(400).json("{\"error\":\"Bad status\"}");
            return;
        }

        //Checking if loan exist
        Loan loan = loanService.getLoanById(loanId);
        if(loan==null){
            ctx.status(404).json("{\"error\":\"Loan not found\"}");
            return;
        }else{

            //Passing data to user object
            Loan approvedLoan=loanService.approveLoan(loanId, loanApprove.getStatus());

            ctx.json(approvedLoan);
        }
    }

    public void rejectLoan(Context ctx){
        if (!checkSession(ctx)){
            ctx.status(400).json("{\"error\":\"No active session\"}");
            return;
        }
        //Getting id loan from url
        String id= ctx.pathParam("loanId");
        int loanId= Integer.parseInt(id);
        //Getting id loan, role from session actual

        User userSession= getUserSession(ctx);

        boolean isAdmin = userSession.getRol() != null && userSession.getRol().equals("admin");



        //Checking if the current user is admin and if not block them

        if(!isAdmin){
            ctx.status(403).json("{\"error\":\"Unauthorized access\"}");
            return;
        }

        //Body data to object
        LoanDtoStatus loanReject = ctx.bodyAsClass(LoanDtoStatus.class);

        if (!(loanReject.getStatus().equals("rejected"))) {
            ctx.status(400).json("{\"error\":\"Bad status\"}");
            return;
        }

        //Checking if loan exist
        Loan loan = loanService.getLoanById(loanId);
        if(loan==null){
            ctx.status(404).json("{\"error\":\"Loan not found\"}");
            return;
        }else{

            //Passing data to user object
            Loan rejectLoan=loanService.rejectLoan(loanId, loanReject.getStatus());

            ctx.json(rejectLoan);
        }
    }





    }

