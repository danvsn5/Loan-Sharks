package uoa.lavs.loan;

import java.time.LocalDate;

public class LoanDuration {
  private LocalDate startDate;
  private int period;
  private int loanTerm;

  public LoanDuration(LocalDate startDate, int period, int loanTerm) {
    this.startDate = startDate;
    this.period = period;
    this.loanTerm = loanTerm;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public int getPeriod() {
    return period;
  }

  public int getLoanTerm() {
    return loanTerm;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public void setLoanTerm(int loanTerm) {
    this.loanTerm = loanTerm;
  }
}
