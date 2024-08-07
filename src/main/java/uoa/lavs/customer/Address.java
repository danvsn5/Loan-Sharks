package uoa.lavs.customer;

public class Address {
  private int addressId; 
  private String addressType;
  private String addressLineOne;
  private String addressLineTwo;
  private String suburb;
  private String postCode;
  private String city;
  private String country;

  public Address(
      String addressType,
      String addressLineOne,
      String addressLineTwo,
      String suburb,
      String postCode,
      String city,
      String country) {
    this.addressType = addressType;
    this.addressLineOne = addressLineOne;
    this.addressLineTwo = addressLineTwo;
    this.suburb = suburb;
    this.postCode = postCode;
    this.city = city;
    this.country = country;
  }

  public int getAddressId() {
    return this.addressId;
  }

  public String getAddressType() {
    return this.addressType;
  }

  public String getAddressLineOne() {
    return this.addressLineOne;
  }

  public String getAddressLineTwo() {
    return this.addressLineTwo;
  }

  public String getSuburb() {
    return this.suburb;
  }

  public String getPostCode() {
    return this.postCode;
  }

  public String getCity() {
    return this.city;
  }

  public String getCountry() {
    return this.country;
  }

  public void setAddressId(int addressId) {
    this.addressId = addressId;
  }

  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  public void setAddressLineOne(String address) {
    this.addressLineOne = address;
  }

  public void setAddressLineTwo(String address) {
    this.addressLineTwo = address;
  }

  public void setSuburb(String suburb) {
    this.suburb = suburb;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
