package uoa.lavs.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoanPaymentTest {
  LoanPayment loanPayment;

  @BeforeEach
  public void setUp() {
    loanPayment = new LoanPayment(-1, "monthly", "monthly", "1000", false);
  }

  @Test
  public void testGetCompounding() {
    assertEquals("monthly", loanPayment.getCompounding());
  }

  @Test
  public void testSetCompounding() {
    loanPayment.setCompounding("yearly");
    assertEquals("yearly", loanPayment.getCompounding());
  }

  @Test
  public void testGetPaymentFrequency() {
    assertEquals("monthly", loanPayment.getPaymentFrequency());
  }

  @Test
  public void testSetPaymentFrequency() {
    loanPayment.setPaymentFrequency("yearly");
    assertEquals("yearly", loanPayment.getPaymentFrequency());
  }

  @Test
  public void testGetPaymentAmount() {
    assertEquals("1000", loanPayment.getPaymentAmount());
  }

  @Test
  public void testSetPaymentAmount() {
    loanPayment.setPaymentAmount("2000");
    assertEquals("2000", loanPayment.getPaymentAmount());
  }

  @Test
  public void testGetInterestOnly() {
    assertEquals(false, loanPayment.getInterestOnly());
  }

  @Test
  public void testSetInterestOnly() {
    loanPayment.setInterestOnly(true);
    assertEquals(true, loanPayment.getInterestOnly());
  }
}
