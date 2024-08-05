package uoa.lavs.loan;

import java.util.ArrayList;

public interface ILoan {
  public Integer getLoanId();

  public String getCustomerId();

  public ArrayList<String> getCoborrowerIds();

  public Float getPrincipal();

  public Float getRate();

  public LoanDuration getDuration();

  public LoanPayment getPayment();

  public void setLoanId(Integer loanId);

  public void setCustomerId(String customerId);

  public void setCoborrowerIds(ArrayList<String> coborrowerIds);

  public void setPrincipal(Float principal);

  public void setRate(Float rate);

  public void setDuration(LoanDuration duration);

  public void setPayment(LoanPayment payment);
}
