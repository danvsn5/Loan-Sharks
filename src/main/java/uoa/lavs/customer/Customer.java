package uoa.lavs.customer;

import java.time.LocalDate;

public abstract class Customer implements ICustomer {
  private String customerId;
  private String title;
  private String firstName;
  private String middleName;
  private String lastName;
  private LocalDate dateOfBirth;
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
      LocalDate dateOfBirth,
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

  @Override
  public String getCustomerId() {
    return this.customerId;
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  @Override
  public String getFirstName() {
    return this.firstName;
  }

  @Override
  public String getMiddleName() {
    return this.middleName;
  }

  @Override
  public String getLastName() {
    return this.lastName;
  }

  @Override
  public LocalDate getDateOfBirth() {
    return this.dateOfBirth;
  }

  @Override
  public String getOccupation() {
    return this.occupation;
  }

  @Override
  public String getResidency() {
    return this.residency;
  }

  @Override
  public Address getPhysicalAddress() {
    return this.physicalAddress;
  }

  @Override
  public Address getMailingAddress() {
    return this.mailingAddress;
  }

  @Override
  public CustomerContact getContact() {
    return this.contact;
  }

  @Override
  public CustomerEmployer getEmployer() {
    return this.employer;
  }

  @Override
  public void setCustomerId(String id) {
    this.customerId = id;
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public void setDateOfBirth(LocalDate date) {
    this.dateOfBirth = date;
  }

  @Override
  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  @Override
  public void setResidency(String residency) {
    this.residency = residency;
  }

  @Override
  public void setPhysicalAddress(Address physicalAddress) {
    this.physicalAddress = physicalAddress;
  }

  @Override
  public void setMailingAddress(Address mailingAddress) {
    this.mailingAddress = mailingAddress;
  }

  @Override
  public void setContact(CustomerContact contact) {
    this.contact = contact;
  }

  @Override
  public void setEmployer(CustomerEmployer employer) {
    this.employer = employer;
  }
}
