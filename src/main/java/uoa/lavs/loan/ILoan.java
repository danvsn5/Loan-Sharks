package uoa.lavs.loan;

import java.util.ArrayList;

public interface ILoan {
  public int getLoanId();

  public String getCustomerId();

  public ArrayList<String> getCoborrowerIds();

  public double getPrincipal();

  public double getRate();

  public String getRateType();

  public LoanDuration getDuration();

  public LoanPayment getPayment();

  public void setLoanId(int loanId);

  public void setCustomerId(String customerId);

  public void setCoborrowerIds(ArrayList<String> coborrowerIds);

  public void setPrincipal(double principal);

  public void setRate(double rate);

  public void setRateType(String rateType);

  public void setDuration(LoanDuration duration);

  public void setPayment(LoanPayment payment);
}
