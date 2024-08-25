package uoa.lavs.legacy.utility;

import java.util.ArrayList;

public class LoanSummary {
    private Double totalInterest = 0.0;
    private Double totalCost = 0.0;

    public LoanSummary(ArrayList<LoanRepayment> repayments) {

        for (LoanRepayment repayment : repayments) {
            totalInterest += repayment.getInterestAmount();
            totalCost += repayment.getPrincipalAmount();
        }
        totalCost += totalInterest;
    }

    public Double getTotalInterest() {
        return totalInterest;
    }

    public Double getTotalCost() {
        return totalCost;
    }
}
