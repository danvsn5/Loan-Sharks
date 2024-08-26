package uoa.lavs.oop.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.oop.loan.LoanDuration;

public class LoanDurationTest {
  LoanDuration loanDuration;

  @BeforeEach
  public void setUp() {
    loanDuration = new LoanDuration("-1", LocalDate.of(2024, 6, 1), 4, 12);
  }

  @Test
  public void testGetStartDate() {
    assertEquals(LocalDate.of(2024, 6, 1), loanDuration.getStartDate());
  }

  @Test
  public void testSetStartDate() {
    loanDuration.setStartDate(LocalDate.of(2024, 6, 2));
    assertEquals(LocalDate.of(2024, 6, 2), loanDuration.getStartDate());
  }

  @Test
  public void testGetPeriod() {
    assertEquals(4, loanDuration.getPeriod());
  }

  @Test
  public void testSetPeriod() {
    loanDuration.setPeriod(5);
    assertEquals(5, loanDuration.getPeriod());
  }

  @Test
  public void testGetLoanTerm() {
    assertEquals(12, loanDuration.getLoanTerm());
  }

  @Test
  public void testSetLoanTerm() {
    loanDuration.setLoanTerm(24);
    assertEquals(24, loanDuration.getLoanTerm());
  }

  @Test
  public void testSetLoadId() {
    loanDuration.setLoanId("-99");
    assertEquals("-99", loanDuration.getLoanId());
  }
}
