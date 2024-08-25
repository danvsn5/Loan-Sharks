package uoa.lavs.legacy.utility;

import org.apache.commons.math3.util.Precision;

import java.time.LocalDate;
import java.util.ArrayList;

public class AmortizingLoanCalculator implements LoanCalculator {
    @Override
    public ArrayList<LoanRepayment> generateRepaymentSchedule(
            Double principal,
            Double interestRate,
            Double paymentAmount,
            PaymentFrequency frequency,
            LocalDate startDate) {
        ArrayList<LoanRepayment> repayments = new ArrayList<>();

        Double remainingPrincipal = principal;
        Double paymentInterestRate = interestRate;
        LocalDate paymentDate = startDate;
        Integer number = 1;
        switch (frequency) {
            case Weekly -> paymentInterestRate /= 52.14;
            case Fortnightly -> paymentInterestRate /= 26.07;
            case Monthly -> paymentInterestRate /= 12.00;
        }
        while (remainingPrincipal > 0) {
            Double interestPayment = Precision.round(paymentInterestRate * remainingPrincipal / 100, 2);
            Double principalPayment = paymentAmount - interestPayment;
            if (principalPayment > remainingPrincipal) principalPayment = remainingPrincipal;
            remainingPrincipal -= principalPayment;

            switch (frequency) {
                case Weekly -> paymentDate = paymentDate.plusDays(7);
                case Fortnightly -> paymentDate = paymentDate.plusDays(14);
                case Monthly -> paymentDate = paymentDate.plusMonths(1);
            }

            repayments.add(new LoanRepayment(
                    number++,
                    paymentDate,
                    principalPayment,
                    interestPayment,
                    remainingPrincipal));
        }

        return repayments;
    }
}
