package uoa.lavs.loan;

import java.util.ArrayList;

public class PersonalLoanSingleton {
  private static PersonalLoan instance;

  private PersonalLoanSingleton() {}

  public static PersonalLoan getInstance() {
    if (instance == null) {
      instance = new PersonalLoan("-1", "", new ArrayList<String>(), 0, 0, "", null, null);
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
