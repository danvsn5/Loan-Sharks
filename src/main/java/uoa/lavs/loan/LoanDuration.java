package uoa.lavs.loan;

import java.time.LocalDate;

public class LoanDuration {
  private int durationId;
  private LocalDate startDate;
  private int period;
  private int loanTerm;

  public LoanDuration(LocalDate startDate, int period, int loanTerm) {
    this.startDate = startDate;
    this.period = period;
    this.loanTerm = loanTerm;
  }

  public int getDurationId() {
    return durationId;
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

  public void setDurationId(int durationId) {
    this.durationId = durationId;
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
