package uoa.lavs.customer;

public class CustomerContact {
  private String customerEmail;
  private String phoneOne;
  private String phoneTwo;
  private String preferredContact;
  private String alternateContact;

  public CustomerContact(
      String email,
      String phoneOne,
      String phoneTwo,
      String preferredContact,
      String alternateContact) {
    this.customerEmail = email;
    this.phoneOne = phoneOne;
    this.phoneTwo = (phoneTwo != null) ? phoneTwo : "";
    this.preferredContact = preferredContact;
    this.alternateContact = alternateContact;
  }

  public String getCustomerEmail() {
    return this.customerEmail;
  }

  public String getPhoneOne() {
    return this.phoneOne;
  }

  public String getPhoneTwo() {
    return this.phoneTwo;
  }

  public String getPreferredContact() {
    return this.preferredContact;
  }

  public String getAlternateContact() {
    return this.alternateContact;
  }

  public void setCustomerEmail(String email) {
    this.customerEmail = email;
  }

  public void setPhoneOne(String phone) {
    this.phoneOne = phone;
  }

  public void setPhoneTwo(String phone) {
    this.phoneTwo = phone;
  }

  public void setPreferredContact(String contact) {
    this.preferredContact = contact;
  }

  public void setAlternateContact(String contact) {
    this.alternateContact = contact;
  }
}
