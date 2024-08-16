package uoa.lavs.customer;

import java.time.LocalDate;

public abstract class Customer implements ICustomer {
  private String customerId;
  private String title;
  private String name;
  private LocalDate dateOfBirth;
  private String occupation;
  private String residency;
  private String notes;
  private Address physicalAddress;
  private Address mailingAddress;
  private CustomerContact contact;
  private CustomerEmployer employer;

  public Customer(
      String customerId,
      String title,
      String name,
      LocalDate dateOfBirth,
      String occupation,
      String residency,
      String notes,
      Address physicalAddress,
      Address mailingAddress,
      CustomerContact contact,
      CustomerEmployer employer) {
    this.customerId = customerId;
    this.title = title;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.occupation = occupation;
    this.residency = residency;
    this.notes = notes;
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
  public String getName() {
    return this.name;
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
  public String getNotes() {
    return this.notes;
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
  public void setName(String name) {
    this.name = name;
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
  public void setNotes(String notes) {
    this.notes = notes;
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
