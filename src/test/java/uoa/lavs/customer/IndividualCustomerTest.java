package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IndividualCustomerTest {
  private IndividualCustomer individualCustomer;
  private LocalDate dateOfBirth;
  private ArrayList<Address> addresses;
  private Address physicalAddress;
  private ArrayList<Phone> phones;
  private Phone phone;
  private ArrayList<Email> emails;
  private Email email;
  private CustomerEmployer employer;
  private ArrayList<Note> notes;
  private Note note;

  @BeforeEach
  public void setUp() {
    dateOfBirth = LocalDate.of(2000, 1, 1);
    physicalAddress =
        new Address(
            "000001",
            "Commercial",
            "999 Unreal Pl",
            "",
            "Arashiyama",
            "6160007",
            "Kyoto",
            "Japan",
            true,
            false);
    addresses = new ArrayList<>();
    addresses.add(physicalAddress);

    phone = new Phone("000001", "mobile", "027", "1234567890", true, true);
    phones = new ArrayList<>();
    phones.add(phone);

    email = new Email("000001", "aaaa@gmail.com", true);
    emails = new ArrayList<>();
    emails.add(email);

    employer =
        new CustomerEmployer(
            "000001",
            "Company",
            "1 Real Pl",
            "",
            "Auckland CBD",
            "Auckland",
            "1010",
            "New Zealand",
            "thisplaceisprettyreal@gmail.com",
            null,
            null,
            false);

    notes = new ArrayList<>();
    note = new Note("000002", new String[] {"Allergic to peanuts"});
    notes.add(note);

    individualCustomer =
        new IndividualCustomer(
            "000001",
            "Mr",
            "Rule Number Ten",
            dateOfBirth,
            "Software Engineer",
            "B2",
            "New Zealand",
            notes,
            addresses,
            phones,
            emails,
            employer);
  }

  @Test
  public void testIndividualCustomerCreation() {
    assertNotNull(individualCustomer);
    assertEquals("000001", individualCustomer.getCustomerId());
    assertEquals("Mr", individualCustomer.getTitle());
    assertEquals("Rule Number Ten", individualCustomer.getName());
    assertEquals(dateOfBirth, individualCustomer.getDateOfBirth());
    assertEquals("Software Engineer", individualCustomer.getOccupation());
    assertEquals("B2", individualCustomer.getVisa());
    assertEquals("New Zealand", individualCustomer.getCitizenship());
    assertEquals(notes, individualCustomer.getNotes());
    assertEquals(addresses, individualCustomer.getAddresses());
    assertEquals(phones, individualCustomer.getPhones());
    assertEquals(emails, individualCustomer.getEmails());
    assertEquals(employer, individualCustomer.getEmployer());
  }
}
