package uoa.lavs.loan;

public class LoanPayment {
  private int paymentId;
  private String compounding;
  private String paymentFrequency;
  private String paymentAmount;
  private boolean interestOnly;

  public LoanPayment(
      String compounding, String paymentFrequency, String paymentAmount, boolean interestOnly) {
    this.compounding = compounding;
    this.paymentFrequency = paymentFrequency;
    this.paymentAmount = paymentAmount;
    this.interestOnly = interestOnly;
  }

  public int getPaymentId() {
    return paymentId;
  }

  public String getCompounding() {
    return compounding;
  }

  public String getPaymentFrequency() {
    return paymentFrequency;
  }

  public String getPaymentAmount() {
    return paymentAmount;
  }

  public boolean getInterestOnly() {
    return interestOnly;
  }

  public void setPaymentId(int paymentId) {
    this.paymentId = paymentId;
  }

  public void setCompounding(String compounding) {
    this.compounding = compounding;
  }

  public void setPaymentFrequency(String paymentFrequency) {
    this.paymentFrequency = paymentFrequency;
  }

  public void setPaymentAmount(String paymentAmount) {
    this.paymentAmount = paymentAmount;
  }

  public void setInterestOnly(boolean interestOnly) {
    this.interestOnly = interestOnly;
  }
}
