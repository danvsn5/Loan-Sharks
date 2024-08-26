package uoa.lavs.backend.oop.loan;

import java.util.ArrayList;

// personal loan class that is associated with each customer
public class PersonalLoan extends Loan {
  public PersonalLoan(
      String loanId,
      String customerId,
      ArrayList<String> coborrowerIds,
      double principal,
      double rate,
      String rateType,
      LoanDuration duration,
      LoanPayment payment) {
    super(loanId, customerId, coborrowerIds, principal, rate, rateType, duration, payment);
  }
}
