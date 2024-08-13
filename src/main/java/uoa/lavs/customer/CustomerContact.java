package uoa.lavs.customer;

public class CustomerContact {
  private int contactId;
  private String customerEmail;
  private Phone phoneOne;
  private Phone phoneTwo;
  private String preferredContact;
  private String alternateContact;

  public CustomerContact(
      String email,
      Phone phoneOne,
      Phone phoneTwo,
      String preferredContact,
      String alternateContact) {
    this.customerEmail = email;
    this.phoneOne = phoneOne;
    this.phoneTwo = phoneTwo;
    this.preferredContact = preferredContact;
    this.alternateContact = alternateContact;
  }

  public int getContactId() {
    return this.contactId;
  }

  public String getCustomerEmail() {
    return this.customerEmail;
  }

  public Phone getPhoneOne() {
    return this.phoneOne;
  }

  public Phone getPhoneTwo() {
    return this.phoneTwo;
  }

  public String getPreferredContact() {
    return this.preferredContact;
  }

  public String getAlternateContact() {
    return this.alternateContact;
  }

  public void setContactId(int id) {
    this.contactId = id;
  }

  public void setCustomerEmail(String email) {
    this.customerEmail = email;
  }

  public void setPhoneOne(Phone phone) {
    this.phoneOne = phone;
  }

  public void setPhoneTwo(Phone phone) {
    this.phoneTwo = phone;
  }

  public void setPreferredContact(String contact) {
    this.preferredContact = contact;
  }

  public void setAlternateContact(String contact) {
    this.alternateContact = contact;
  }
}
