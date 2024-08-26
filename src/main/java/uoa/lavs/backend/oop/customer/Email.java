package uoa.lavs.backend.oop.customer;

// sets the email object with constructor and getters and setters
public class Email {
  private String customerId;
  private int emailId;
  private String emailAddress;
  private boolean isPrimary;

  public Email(String customerId, String emailAddress, boolean isPrimary) {
    this.customerId = customerId;
    this.emailAddress = emailAddress;
    this.isPrimary = isPrimary;
  }

  public String getCustomerId() {
    return this.customerId;
  }

  public int getEmailId() {
    return this.emailId;
  }

  public String getEmailAddress() {
    return this.emailAddress;
  }

  public boolean getIsPrimary() {
    return this.isPrimary;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setEmailId(int emailId) {
    this.emailId = emailId;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setIsPrimary(boolean isPrimary) {
    this.isPrimary = isPrimary;
  }
}
