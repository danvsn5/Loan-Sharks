package uoa.lavs.oop.customer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.oop.customer.CustomerEmployer;
import uoa.lavs.backend.oop.customer.Email;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.backend.oop.customer.Note;
import uoa.lavs.backend.oop.customer.Phone;

public class IndividualCustomerSingletonTest {
  private IndividualCustomer customer;
  private LocalDate dateOfBirth;
  private ArrayList<Address> addresses;
  private Address physicalAddress;
  private ArrayList<Phone> phones;
  private Phone phone;
  private ArrayList<Email> emails;
  private Email email;
  private CustomerEmployer employer;
  private IndividualCustomer newCustomer;
  private ArrayList<Note> notes;
  private Note note;

  @BeforeEach
  public void setUp() {
    IndividualCustomerSingleton.resetInstance();

    dateOfBirth = LocalDate.of(2024, 8, 6);
    addresses = new ArrayList<>();
    physicalAddress =
        new Address(
            "000001",
            "Rural",
            "304 Rose St",
            "46",
            "Sunnynook",
            "12345",
            "Auckland",
            "Zimbabwe",
            true,
            false);
    addresses.add(physicalAddress);

    phone = new Phone("000001", "mobile", "027", "1234567890", true, true);
    phones = new ArrayList<>();
    phones.add(phone);
    email = new Email("000001", "abc@gmail.com", true);
    emails = new ArrayList<>();
    emails.add(email);

    employer =
        new CustomerEmployer(
            "000001", "Countdown", "", "", "", "", "", "", null, null, null, false);

    notes = new ArrayList<>();
    note = new Note("000002", new String[] {"Allergic to peanuts"});
    notes.add(note);

    newCustomer =
        new IndividualCustomer(
            "000001",
            "Mr",
            "Ting Mun Guy",
            dateOfBirth,
            "Engineer",
            "B2",
            "Zimbabwe",
            notes,
            addresses,
            phones,
            emails,
            employer);
  }

  @Test
  public void testGetInstance() {
    customer = IndividualCustomerSingleton.getInstance();
    assertNotNull(customer);
  }

  @Test
  public void testSetInstance() {
    IndividualCustomerSingleton.setInstance(newCustomer);
    customer = IndividualCustomerSingleton.getInstance();

    assertSame(newCustomer, customer);
  }

  @Test
  public void testResetInstance() {
    IndividualCustomerSingleton.setInstance(newCustomer);
    customer = IndividualCustomerSingleton.getInstance();
    IndividualCustomerSingleton.resetInstance();
    IndividualCustomer customer2 = IndividualCustomerSingleton.getInstance();

    assertNotSame(customer, customer2);
  }

  @Test
  public void testSingleUniqueInstance() {
    assertSame(
        IndividualCustomerSingleton.getInstance(), IndividualCustomerSingleton.getInstance());
  }
}
