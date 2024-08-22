package uoa.lavs.loan;

import java.util.ArrayList;

public class PersonalLoanSingleton {
  private static PersonalLoan instance;

  private PersonalLoanSingleton() {}

  public static PersonalLoan getInstance() {
    if (instance == null) {

      ArrayList<String> coborrowerIds = new ArrayList<String>();

      for (int i = 0; i < 3; i++) {
        coborrowerIds.add("");
      }

      LoanDuration loanDuration = new LoanDuration("-1", null, 0, 0);
      LoanPayment loanPayment = new LoanPayment("-1", "", "", "", false);
      instance = new PersonalLoan("-1", "", coborrowerIds, 0, 0, "", loanDuration, loanPayment);
    }
    return instance;
  }

  public static void setInstance(PersonalLoan loan) {
    instance = loan;
  }

  public static void resetInstance() {
    instance = null;
  }
}
