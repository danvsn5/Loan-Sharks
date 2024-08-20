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
      Address physicalAddress,
      Address mailingAddress,
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
        physicalAddress,
        mailingAddress,
        contact,
        employer);
  }
}
