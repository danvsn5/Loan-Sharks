package uoa.lavs.customer;

public class CustomerEmployer {
  private String employerName;
  private Address employerAddress;
  private String employerEmail;
  private String employerWebsite;
  private String employerPhone;
  private boolean ownerOfCompany;

  public CustomerEmployer(
      String name,
      Address address,
      String email,
      String website,
      String phone,
      boolean ownerOfCompany) {
    this.employerName = name;
    this.employerAddress = address;
    this.employerEmail = (email != null) ? email : "";
    this.employerWebsite = (website != null) ? website : "";
    this.employerPhone = (phone != null) ? phone : "";
    this.ownerOfCompany = ownerOfCompany;
  }

  public String getEmployerName() {
    return this.employerName;
  }

  public Address getEmployerAddress() {
    return this.employerAddress;
  }

  public String getEmployerEmail() {
    return this.employerEmail;
  }

  public String getEmployerWebsite() {
    return this.employerWebsite;
  }

  public String getEmployerPhone() {
    return this.employerPhone;
  }

  public boolean getOwnerOfCompany() {
    return this.ownerOfCompany;
  }

  public void setEmployerName(String name) {
    this.employerName = name;
  }

  public void setEmployerAddress(Address address) {
    this.employerAddress = address;
  }

  public void setEmployerEmail(String email) {
    this.employerEmail = email;
  }

  public void setEmployerWebsite(String website) {
    this.employerWebsite = website;
  }

  public void setEmployerPhone(String phone) {
    this.employerPhone = phone;
  }

  public void setOwnerOfCompany(boolean owner) {
    this.ownerOfCompany = owner;
  }
}
