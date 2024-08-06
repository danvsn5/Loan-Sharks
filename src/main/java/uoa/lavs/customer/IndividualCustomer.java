package uoa.lavs.customer;

import java.time.LocalDate;

public class IndividualCustomer extends Customer {
  public IndividualCustomer(
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
    super(
        customerId,
        title,
        firstName,
        middleName,
        lastName,
        dateOfBirth,
        occupation,
        residency,
        physicalAddress,
        mailingAddress,
        contact,
        employer);
  }
}
