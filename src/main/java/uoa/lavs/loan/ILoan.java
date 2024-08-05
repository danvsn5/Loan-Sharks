package uoa.lavs.loan;

import java.util.ArrayList;

public interface ILoan {
  public int getLoanId();

  public String getCustomerId();

  public ArrayList<String> getCoborrowerIds();

  public Double getPrincipal();

  public Double getRate();

  public LoanDuration getDuration();

  public LoanPayment getPayment();

  public void setLoanId(int loanId);

  public void setCustomerId(String customerId);

  public void setCoborrowerIds(ArrayList<String> coborrowerIds);

  public void setPrincipal(Double principal);

  public void setRate(Double rate);

  public void setDuration(LoanDuration duration);

  public void setPayment(LoanPayment payment);
}
