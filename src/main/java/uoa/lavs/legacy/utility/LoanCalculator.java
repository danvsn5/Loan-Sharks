package uoa.lavs.legacy.utility;

import java.time.LocalDate;
import java.util.ArrayList;

public interface LoanCalculator {
    ArrayList<LoanRepayment> generateRepaymentSchedule(
            Double principal,
            Double interestRate,
            Double paymentAmount,
            PaymentFrequency frequency,
            LocalDate startDate);
}
