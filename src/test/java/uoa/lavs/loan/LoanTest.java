package uoa.lavs.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoanTest {
  private Loan loan;
  ArrayList<String> coborrowers;
  Double principal;
  Double rate;
  String rateType;
  LoanDuration duration;
  LoanPayment payment;

  @BeforeEach
  public void setUp() {
    coborrowers = new ArrayList<>();
    coborrowers.add("-2");
    coborrowers.add("-3");
    principal = 1000.00;
    rate = 0.1;
    rateType = "fixed";
    duration = new LoanDuration("-1", LocalDate.of(2024, 6, 8), 1, 12);
    payment = new LoanPayment("-1", "monthly", "100.0", "100.0", false);

    loan = new PersonalLoan("-1", "-1", coborrowers, principal, rate, rateType, duration, payment);
  }

  @Test
  public void testGetLoanId() {
    assertEquals("-1", loan.getLoanId());
  }

  @Test
  public void testSetLoanId() {
    loan.setLoanId("-2");
    assertEquals("-2", loan.getLoanId());
  }

  @Test
  public void testGetCustomerId() {
    assertEquals("-1", loan.getCustomerId());
  }

  @Test
  public void testSetCustomerId() {
    loan.setCustomerId("-2");
    assertEquals("-2", loan.getCustomerId());
  }

  @Test
  public void testGetCoborrowerIds() {
    assertEquals(coborrowers, loan.getCoborrowerIds());
  }

  @Test
  public void testSetCoborrowerIds() {
    ArrayList<String> newCoborrowers = new ArrayList<>();
    newCoborrowers.add("-3");
    newCoborrowers.add("-4");
    loan.setCoborrowerIds(newCoborrowers);
    assertEquals(newCoborrowers, loan.getCoborrowerIds());
  }

  @Test
  public void testGetPrincipal() {
    assertEquals(principal, loan.getPrincipal());
  }

  @Test
  public void testSetPrincipal() {
    loan.setPrincipal(2000.00);
    assertEquals(2000.00, loan.getPrincipal());
  }

  @Test
  public void testGetRate() {
    assertEquals(rate, loan.getRate());
  }

  @Test
  public void testSetRate() {
    loan.setRate(0.2);
    assertEquals(0.2, loan.getRate());
  }

  @Test
  public void testGetDuration() {
    assertEquals(duration, loan.getDuration());
  }

  @Test
  public void testSetDuration() {
    LoanDuration newDuration = new LoanDuration("-1", LocalDate.of(2024, 6, 8), 2, 12);
    loan.setDuration(newDuration);
    assertEquals(newDuration, loan.getDuration());
  }

  @Test
  public void testGetPayment() {
    assertEquals(payment, loan.getPayment());
  }

  @Test
  public void testSetPayment() {
    LoanPayment newPayment = new LoanPayment("-1", "monthly", "200.0", "200.0", false);
    loan.setPayment(newPayment);
    assertEquals(newPayment, loan.getPayment());
  }

  @Test
  public void testGetRateType() {
    assertEquals(rateType, loan.getRateType());
  }

  @Test 
  public void testSetRateType() {
    loan.setRateType("variable");
    assertEquals("variable", loan.getRateType());
  }

}
