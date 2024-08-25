package uoa.lavs.backend.oop.customer;

public class CustomerEmployer {
  private String customerId;
  private String employerName;
  public String lineOne;
  public String lineTwo;
  public String suburb;
  public String city;
  public String postCode;
  public String country;
  private String employerEmail;
  private String employerWebsite;
  private String employerPhone;
  private boolean ownerOfCompany;

  public CustomerEmployer(
      String customerId,
      String name,
      String lineOne,
      String lineTwo,
      String suburb,
      String city,
      String postCode,
      String country,
      String email,
      String website,
      String phone,
      boolean ownerOfCompany) {
    this.customerId = customerId;
    this.employerName = name;
    this.lineOne = lineOne;
    this.lineTwo = lineTwo;
    this.suburb = suburb;
    this.postCode = postCode;
    this.city = city;
    this.country = country;
    this.employerEmail = email;
    this.employerWebsite = website;
    this.employerPhone = phone;
    this.ownerOfCompany = ownerOfCompany;
  }

  public String getCustomerId() {
    return this.customerId;
  }

  public String getEmployerName() {
    return this.employerName;
  }

  public String getLineOne() {
    return this.lineOne;
  }

  public String getLineTwo() {
    return this.lineTwo;
  }

  public String getSuburb() {
    return this.suburb;
  }

  public String getCity() {
    return this.city;
  }

  public String getPostCode() {
    return this.postCode;
  }

  public String getCountry() {
    return this.country;
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

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setEmployerName(String name) {
    this.employerName = name;
  }

  public void setLineOne(String lineOne) {
    this.lineOne = lineOne;
  }

  public void setLineTwo(String lineTwo) {
    this.lineTwo = lineTwo;
  }

  public void setSuburb(String suburb) {
    this.suburb = suburb;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public void setCountry(String country) {
    this.country = country;
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
