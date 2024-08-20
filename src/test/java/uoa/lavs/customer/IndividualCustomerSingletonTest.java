package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IndividualCustomerSingletonTest {
  private IndividualCustomer customer;
  private LocalDate dateOfBirth;
  private ArrayList<Address> addresses;
  private Address physicalAddress;
  private CustomerContact contact;
  private CustomerEmployer employer;
  private IndividualCustomer newCustomer;
  private Phone phoneOne;
  private Phone phoneTwo;
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

    phoneOne = new Phone("mobile", "1234567890");
    phoneTwo = new Phone("home", "0987654321");
    contact = new CustomerContact("abc@gmail.com", phoneOne, phoneTwo, "mobile sms", "email");
    employer = new CustomerEmployer("Countdown", "", "", "", "", "", "", null, null, null, false);

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
            "NZ Citizen",
            notes,
            addresses,
            contact,
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
