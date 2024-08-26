package uoa.lavs.backend.oop.customer;

import java.time.LocalDate;
import java.util.ArrayList;

// individual customer instance used inside customer singleton with constructor
public class IndividualCustomer extends Customer {
  public IndividualCustomer(
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
    super(
        customerId,
        title,
        name,
        dateOfBirth,
        occupation,
        visa,
        citizenship,
        notes,
        addresses,
        phones,
        emails,
        employer);
  }
}
