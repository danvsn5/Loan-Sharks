package uoa.lavs.customer;

import java.time.LocalDate;

public interface ICustomer {
  public String getCustomerId();

  public String getTitle();

  public String getFirstName();

  public String getMiddleName();

  public String getLastName();

  public LocalDate getDateOfBirth();

  public String getOccupation();

  public String getResidency();

  public String getNotes();

  public Address getPhysicalAddress();

  public Address getMailingAddress();

  public CustomerContact getContact();

  public CustomerEmployer getEmployer();

  public void setCustomerId(String id);

  public void setTitle(String title);

  public void setFirstName(String firstName);

  public void setMiddleName(String middleName);

  public void setLastName(String lastName);

  public void setDateOfBirth(LocalDate date);

  public void setOccupation(String occupation);

  public void setResidency(String residency);

  public void setNotes(String notes);

  public void setPhysicalAddress(Address physicalAddress);

  public void setMailingAddress(Address mailingAddress);

  public void setContact(CustomerContact contact);

  public void setEmployer(CustomerEmployer employer);
}
