package uoa.lavs.loan;

import java.time.LocalDate;

public class LoanDuration {
  private LocalDate startDate;
  private Integer period;
  private Integer loanTerm;

  public LoanDuration(LocalDate startDate, Integer period, Integer loanTerm) {
    this.startDate = startDate;
    this.period = period;
    this.loanTerm = loanTerm;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public Integer getPeriod() {
    return period;
  }

  public Integer getLoanTerm() {
    return loanTerm;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public void setPeriod(Integer period) {
    this.period = period;
  }

  public void setLoanTerm(Integer loanTerm) {
    this.loanTerm = loanTerm;
  }
}
