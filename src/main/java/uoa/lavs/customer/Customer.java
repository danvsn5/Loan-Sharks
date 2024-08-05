package uoa.lavs.customer;

import java.util.Date;

public abstract class Customer implements ICustomer {
  private String customerId;
  private String title;
  private String firstName;
  private String middleName;
  private String lastName;
  private Date dateOfBirth;
  private String occupation;
  private String residency;
  private Address physicalAddress;
  private Address mailingAddress;
  private CustomerContact contact;
  private CustomerEmployer employer;

  public Customer(
      String customerId,
      String title,
      String firstName,
      String middleName,
      String lastName,
      Date dateOfBirth,
      String occupation,
      String residency,
      Address physicalAddress,
      Address mailingAddress,
      CustomerContact contact,
      CustomerEmployer employer) {
    this.customerId = customerId;
    this.title = title;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.occupation = occupation;
    this.residency = residency;
    this.physicalAddress = physicalAddress;
    this.mailingAddress = mailingAddress;
    this.contact = contact;
    this.employer = employer;
  }

  public String getCustomerId() {
    return this.customerId;
  }

  public String getTitle() {
    return this.title;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getMiddleName() {
    return this.middleName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public Date getDateOfBirth() {
    return this.dateOfBirth;
  }

  public String getOccupation() {
    return this.occupation;
  }

  public String getResidency() {
    return this.residency;
  }

  public Address getPhysicalAddress() {
    return this.physicalAddress;
  }

  public Address getMailingAddress() {
    return this.mailingAddress;
  }

  public CustomerContact getContact() {
    return this.contact;
  }

  public CustomerEmployer getEmployer() {
    return this.employer;
  }

  public void setCustomerId(String id) {
    this.customerId = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setMiddleNames(String middleName) {
    this.middleName = middleName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setDateOfBirth(Date date) {
    this.dateOfBirth = date;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  public void setResidency(String residency) {
    this.residency = residency;
  }

  public void setPhysicalAddress(Address physicalAddress) {
    this.physicalAddress = physicalAddress;
  }

  public void setMailingAddress(Address mailingAddress) {
    this.mailingAddress = mailingAddress;
  }

  public void setContact(CustomerContact contact) {
    this.contact = contact;
  }

  public void setEmployer(CustomerEmployer employer) {
    this.employer = employer;
  }
}
