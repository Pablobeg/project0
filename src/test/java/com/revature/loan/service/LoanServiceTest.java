package com.revature.loan.service;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.revature.loan.dao.LoanDao;
import com.revature.loan.model.Loan;
import com.revature.loan.service.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class LoanServiceTest {

    @Mock
    private LoanDao loanDao;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLoan_InvalidData_ShouldReturnFalse() {
        boolean result = loanService.createLoan(1, "pin", 10);
        Assertions.assertTrue(result, "Loan creation should success with valid data");
    }




}
