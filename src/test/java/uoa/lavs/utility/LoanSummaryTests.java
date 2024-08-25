package uoa.lavs.utility;

import org.junit.jupiter.api.Test;

import uoa.lavs.legacy.utility.LoanRepayment;
import uoa.lavs.legacy.utility.LoanSummary;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LoanSummaryTests {
    @Test
    public void calculatesTotalInterest() {
        // Arrange
        ArrayList<LoanRepayment> repayments = new ArrayList<>();
        repayments.add(new LoanRepayment(1, LocalDate.now(), 500.0, 1000.0, 2000.0));
        repayments.add(new LoanRepayment(2, LocalDate.now(), 750.0, 750.0, 1250.0));
        repayments.add(new LoanRepayment(3, LocalDate.now(), 1250.0, 250.0, 1250.0));

        // Act
        LoanSummary loanSummary = new LoanSummary(repayments);

        // Assert
        assertEquals(2000.0, loanSummary.getTotalInterest());
    }

    @Test
    public void calculatesTotalCost() {
        // Arrange
        ArrayList<LoanRepayment> repayments = new ArrayList<>();
        repayments.add(new LoanRepayment(1, LocalDate.now(), 500.0, 1000.0, 2000.0));
        repayments.add(new LoanRepayment(2, LocalDate.now(), 750.0, 750.0, 1250.0));
        repayments.add(new LoanRepayment(3, LocalDate.now(), 1250.0, 250.0, 1250.0));

        // Act
        LoanSummary loanSummary = new LoanSummary(repayments);

        // Assert
        assertEquals(4500.0, loanSummary.getTotalCost());
    }
}