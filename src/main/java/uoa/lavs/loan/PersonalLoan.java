package uoa.lavs.loan;

import java.util.ArrayList;

public class PersonalLoan extends Loan {
  public PersonalLoan(
      Integer loanId,
      String customerId,
      ArrayList<String> coborrowerIds,
      Float principal,
      Float rate,
      LoanDuration duration,
      LoanPayment payment) {
    super(loanId, customerId, coborrowerIds, principal, rate, duration, payment);
  }
}
