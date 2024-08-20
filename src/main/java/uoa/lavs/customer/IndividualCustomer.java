package uoa.lavs.customer;

import java.time.LocalDate;
import java.util.ArrayList;

public class IndividualCustomer extends Customer {
  public IndividualCustomer(
      String customerId,
      String title,
      String name,
      LocalDate dateOfBirth,
      String occupation,
      String residency,
      ArrayList<Note> notes,
      ArrayList<Address> addresses,
      CustomerContact contact,
      CustomerEmployer employer) {
    super(
        customerId,
        title,
        name,
        dateOfBirth,
        occupation,
        residency,
        notes,
        addresses,
        contact,
        employer);
  }
}
