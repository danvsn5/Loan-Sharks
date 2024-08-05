package uoa.lavs.loan;

import java.util.Date;

public class LoanDuration {
  private Date startDate;
  private Integer period;
  private Integer loanTerm;

  public LoanDuration(Date startDate, Integer period, Integer loanTerm) {
    this.startDate = startDate;
    this.period = period;
    this.loanTerm = loanTerm;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Integer getPeriod() {
    return period;
  }

  public Integer getLoanTerm() {
    return loanTerm;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public void setPeriod(Integer period) {
    this.period = period;
  }

  public void setLoanTerm(Integer loanTerm) {
    this.loanTerm = loanTerm;
  }
}
