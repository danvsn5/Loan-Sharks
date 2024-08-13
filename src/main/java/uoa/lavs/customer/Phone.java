package uoa.lavs.customer;

public class Phone {
  private String type;
  private String phoneNumber;

  public Phone(String type, String phoneNumber) {
    this.type = type;
    this.phoneNumber = phoneNumber;
  }

  public String getType() {
    return this.type;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
