package uoa.lavs.loan;

import java.util.ArrayList;

public abstract class Loan implements ILoan {
  private Integer loanId;
  private String customerId;
  private ArrayList<String> coborrowerIds;
  private Float principal;
  private Float rate;
  private LoanDuration duration;
  private LoanPayment payment;

  public Loan(
      Integer loanId,
      String customerId,
      ArrayList<String> coborrowerIds,
      Float principal,
      Float rate,
      LoanDuration duration,
      LoanPayment payment) {
    this.loanId = loanId;
    this.customerId = customerId;
    this.coborrowerIds = coborrowerIds;
    this.principal = principal;
    this.rate = rate;
    this.duration = duration;
    this.payment = payment;
  }

  public Integer getLoanId() {
    return loanId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public ArrayList<String> getCoborrowerIds() {
    return coborrowerIds;
  }

  public Float getPrincipal() {
    return principal;
  }

  public Float getRate() {
    return rate;
  }

  public LoanDuration getDuration() {
    return duration;
  }

  public LoanPayment getPayment() {
    return payment;
  }

  public void setLoanId(Integer loanId) {
    this.loanId = loanId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setCoborrowerIds(ArrayList<String> coborrowerIds) {
    this.coborrowerIds = coborrowerIds;
  }

  public void setPrincipal(Float principal) {
    this.principal = principal;
  }

  public void setRate(Float rate) {
    this.rate = rate;
  }

  public void setDuration(LoanDuration duration) {
    this.duration = duration;
  }

  public void setPayment(LoanPayment payment) {
    this.payment = payment;
  }
}
