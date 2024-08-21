package uoa.lavs.customer;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.commons.math3.random.StableRandomGenerator;

public interface ICustomer {
  public String getCustomerId();

  public String getTitle();

  public String getName();

  public LocalDate getDateOfBirth();

  public String getOccupation();

  public String getVisa();

  public String getCitizenship();

  public ArrayList<Note> getNotes();

  public ArrayList<Address> getAddresses();

  public ArrayList<Phone> getPhones();

  public ArrayList<Email> getEmails();

  public CustomerEmployer getEmployer();

  public void setCustomerId(String id);

  public void setTitle(String title);

  public void setName(String name);

  public void setDateOfBirth(LocalDate date);

  public void setOccupation(String occupation);

  public void setVisa(String visa);

  public void setCitizenship(String citizenship);

  public void setNotes(ArrayList<Note> notes);

  public void setAddresses(ArrayList<Address> addresses);

  public void setPhones(ArrayList<Phone> phones);

  public void setEmails(ArrayList<Email> emails);

  public void setEmployer(CustomerEmployer employer);
}
