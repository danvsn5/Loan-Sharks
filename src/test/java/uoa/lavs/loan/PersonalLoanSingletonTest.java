package uoa.lavs.loan;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonalLoanSingletonTest {
  private PersonalLoan newLoan;
  private ArrayList<String> coborrowers;
  private Double principal;
  private Double rate;
  private LoanDuration duration;
  private LoanPayment payment;
  private PersonalLoan loan;

  @BeforeEach
  public void setUp() {
    PersonalLoanSingleton.resetInstance();

    coborrowers = new ArrayList<>();
    coborrowers.add("-2");
    coborrowers.add("-3");
    principal = 1000.00;
    rate = 0.1;
    duration = new LoanDuration(-1, LocalDate.of(2024, 6, 8), 1, 12);
    payment = new LoanPayment(-1, "monthly", "100.0", "100.0", false);

    newLoan = new PersonalLoan(-1, "-1", coborrowers, principal, rate, duration, payment);
  }

  @Test
  public void testGetInstance() {
    loan = PersonalLoanSingleton.getInstance();
    assertNotNull(loan);
  }

  @Test
  public void testSetInstance() {
    PersonalLoanSingleton.setInstance(newLoan);
    loan = PersonalLoanSingleton.getInstance();

    assertSame(newLoan, loan);
  }

  @Test
  public void testResetInstance() {
    PersonalLoanSingleton.setInstance(newLoan);
    loan = PersonalLoanSingleton.getInstance();
    PersonalLoanSingleton.resetInstance();
    PersonalLoan loan2 = PersonalLoanSingleton.getInstance();

    assertNotSame(loan, loan2);
  }

  @Test
  public void testSingleUniqueInstance() {
    assertSame(PersonalLoanSingleton.getInstance(), PersonalLoanSingleton.getInstance());
  }
}
