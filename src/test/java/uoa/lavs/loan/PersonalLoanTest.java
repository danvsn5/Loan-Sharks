package uoa.lavs.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonalLoanTest {
  private PersonalLoan loan;
  ArrayList<String> coborrowers;
  Double principal;
  Double rate;
  LoanDuration duration;
  LoanPayment payment;

  @BeforeEach
  public void setUp() {
    coborrowers = new ArrayList<>();
    principal = 1.00;
    rate = 1.0;
    duration = new LoanDuration(LocalDate.of(2000, 1, 30), 12, 36);
    payment = new LoanPayment("annually", "100.0", "100.0", false);

    loan = new PersonalLoan(10, "000001", coborrowers, principal, rate, duration, payment);
  }

  @Test
  public void testPersonalLoanCreation() {
    assertNotNull(loan);
    assertEquals(10, loan.getLoanId());
    assertEquals("000001", loan.getCustomerId());
    assertEquals(coborrowers, loan.getCoborrowerIds());
    assertEquals(principal, loan.getPrincipal());
    assertEquals(rate, loan.getRate());
    assertEquals(duration, loan.getDuration());
    assertEquals(payment, loan.getPayment());
  }
}
