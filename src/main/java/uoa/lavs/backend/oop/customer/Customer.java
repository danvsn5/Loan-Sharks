package uoa.lavs.backend.oop.customer;

import java.time.LocalDate;
import java.util.ArrayList;

// sets the customer details with constructor with getters and setters
public abstract class Customer implements ICustomer {
  private String customerId;
  private String title;
  private String name;
  private LocalDate dateOfBirth;
  private String occupation;
  private String visa;
  private String citizenship;
  private ArrayList<Note> notes;
  private ArrayList<Address> addresses;
  private ArrayList<Phone> phones;
  private ArrayList<Email> emails;
  private CustomerEmployer employer;

  public Customer(
      String customerId,
      String title,
      String name,
      LocalDate dateOfBirth,
      String occupation,
      String visa,
      String citizenship,
      ArrayList<Note> notes,
      ArrayList<Address> addresses,
      ArrayList<Phone> phones,
      ArrayList<Email> emails,
      CustomerEmployer employer) {
    this.customerId = customerId;
    this.title = title;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.occupation = occupation;
    this.visa = visa;
    this.citizenship = citizenship;
    this.notes = notes;
    this.addresses = addresses;
    this.phones = phones;
    this.emails = emails;
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
  public String getVisa() {
    return this.visa;
  }

  @Override
  public String getCitizenship() {
    return this.citizenship;
  }

  @Override
  public ArrayList<Note> getNotes() {
    return this.notes;
  }

  @Override
  public ArrayList<Address> getAddresses() {
    return this.addresses;
  }

  @Override
  public ArrayList<Phone> getPhones() {
    return this.phones;
  }

  @Override
  public ArrayList<Email> getEmails() {
    return this.emails;
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
  public void setVisa(String visa) {
    this.visa = visa;
  }

  @Override
  public void setCitizenship(String citizenship) {
    this.citizenship = citizenship;
  }

  @Override
  public void setNotes(ArrayList<Note> notes) {
    this.notes = notes;
  }

  @Override
  public void setAddresses(ArrayList<Address> addresses) {
    this.addresses = addresses;
  }

  @Override
  public void setPhones(ArrayList<Phone> phones) {
    this.phones = phones;
  }

  @Override
  public void setEmails(ArrayList<Email> emails) {
    this.emails = emails;
  }

  @Override
  public void setEmployer(CustomerEmployer employer) {
    this.employer = employer;
  }
}
