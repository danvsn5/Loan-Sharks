package uoa.lavs.loan;

import java.util.ArrayList;

public abstract class Loan implements ILoan {
  private int loanId;
  private String customerId;
  private ArrayList<String> coborrowerIds;
  private Double principal;
  private Double rate;
  private LoanDuration duration;
  private LoanPayment payment;

  public Loan(
      int loanId,
      String customerId,
      ArrayList<String> coborrowerIds,
      Double principal,
      Double rate,
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

  public int getLoanId() {
    return loanId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public ArrayList<String> getCoborrowerIds() {
    return coborrowerIds;
  }

  public Double getPrincipal() {
    return principal;
  }

  public Double getRate() {
    return rate;
  }

  public LoanDuration getDuration() {
    return duration;
  }

  public LoanPayment getPayment() {
    return payment;
  }

  public void setLoanId(int loanId) {
    this.loanId = loanId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setCoborrowerIds(ArrayList<String> coborrowerIds) {
    this.coborrowerIds = coborrowerIds;
  }

  public void setPrincipal(Double principal) {
    this.principal = principal;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public void setDuration(LoanDuration duration) {
    this.duration = duration;
  }

  public void setPayment(LoanPayment payment) {
    this.payment = payment;
  }
}
