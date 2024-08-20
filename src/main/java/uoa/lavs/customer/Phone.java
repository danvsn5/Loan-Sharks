package uoa.lavs.customer;

public class Phone {
  private String customerId;
  private int phoneId;
  private String type;
  private String prefix;
  private String phoneNumber;
  private boolean isPrimary;
  private boolean canSendText;

  public Phone(
      String customerId,
      String type,
      String prefix,
      String phoneNumber,
      boolean isPrimary,
      boolean canSendText) {
    this.customerId = customerId;
    this.type = type;
    this.prefix = prefix;
    this.phoneNumber = phoneNumber;
    this.isPrimary = isPrimary;
    this.canSendText = canSendText;
  }

  public String getCustomerId() {
    return this.customerId;
  }

  public int getPhoneId() {
    return this.phoneId;
  }

  public String getType() {
    return this.type;
  }

  public String getPrefix() {
    return this.prefix;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public boolean getIsPrimary() {
    return this.isPrimary;
  }

  public boolean getCanSendText() {
    return this.canSendText;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setPhoneId(int phoneId) {
    this.phoneId = phoneId;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setIsPrimary(boolean isPrimary) {
    this.isPrimary = isPrimary;
  }

  public void setCanSendText(boolean canSendText) {
    this.canSendText = canSendText;
  }
}
