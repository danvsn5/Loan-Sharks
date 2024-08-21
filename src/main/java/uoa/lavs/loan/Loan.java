package uoa.lavs.loan;

import java.util.ArrayList;

public abstract class Loan implements ILoan {
  private String loanId;
  private String customerId;
  private ArrayList<String> coborrowerIds;
  private double principal;
  private double rate;
  private String rateType;
  private LoanDuration duration;
  private LoanPayment payment;

  public Loan(
      String loanId,
      String customerId,
      ArrayList<String> coborrowerIds,
      double principal,
      double rate,
      String rateType,
      LoanDuration duration,
      LoanPayment payment) {
    this.loanId = loanId;
    this.customerId = customerId;
    this.coborrowerIds = coborrowerIds;
    this.principal = principal;
    this.rate = rate;
    this.rateType = rateType;
    this.duration = duration;
    this.payment = payment;
  }

  @Override
  public String getLoanId() {
    return loanId;
  }

  @Override
  public String getCustomerId() {
    return customerId;
  }

  @Override
  public ArrayList<String> getCoborrowerIds() {
    return coborrowerIds;
  }

  @Override
  public double getPrincipal() {
    return principal;
  }

  @Override
  public double getRate() {
    return rate;
  }

  @Override
  public String getRateType() {
    return this.rateType;
  }

  @Override
  public LoanDuration getDuration() {
    return duration;
  }

  @Override
  public LoanPayment getPayment() {
    return payment;
  }

  @Override
  public void setLoanId(String loanId) {
    this.loanId = loanId;
  }

  @Override
  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  @Override
  public void setCoborrowerIds(ArrayList<String> coborrowerIds) {
    this.coborrowerIds = coborrowerIds;
  }

  @Override
  public void setPrincipal(double principal) {
    this.principal = principal;
  }

  @Override
  public void setRate(double rate) {
    this.rate = rate;
  }

  @Override
  public void setRateType(String rateType) {
    this.rateType = rateType;
  }

  @Override
  public void setDuration(LoanDuration duration) {
    this.duration = duration;
  }

  @Override
  public void setPayment(LoanPayment payment) {
    this.payment = payment;
  }
}
