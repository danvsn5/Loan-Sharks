package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTest {
  private Customer customer;
  private LocalDate dateOfBirth;
  private ArrayList<Address> addresses;
  private Address physicalAddress;
  private Phone phoneOne;
  private Phone phoneTwo;
  private CustomerContact contact;
  private CustomerEmployer employer;
  private ArrayList<Note> notes;
  private Note note;

  @BeforeEach
  public void setUp() {
    dateOfBirth = LocalDate.of(2024, 8, 6);
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

    addresses = new ArrayList<>();
    addresses.add(physicalAddress);

    phoneOne = new Phone("mobile", "1234567890");
    phoneTwo = new Phone("home", "0987654321");
    contact = new CustomerContact("abc@gmail.com", phoneOne, phoneTwo, "mobile sms", "email");
    employer =
        new CustomerEmployer(
            "Countdown",
            "123 Stonesuckle Ct",
            "",
            "Sunnynook",
            "Auckland",
            "12345",
            "Zimbabwe",
            null,
            null,
            null,
            false);

    notes = new ArrayList<>();
    note = new Note("000001", new String[] {"Allergic to peanuts"});
    notes.add(note);

    customer =
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
  public void testGetCustomerId() {
    assertEquals("000001", customer.getCustomerId());
  }

  @Test
  public void testSetCustomerId() {
    customer.setCustomerId("000002");
    assertEquals("000002", customer.getCustomerId());
  }

  @Test
  public void testGetTitle() {
    assertEquals("Mr", customer.getTitle());
  }

  @Test
  public void testSetTitle() {
    customer.setTitle("Mrs");
    assertEquals("Mrs", customer.getTitle());
  }

  @Test
  public void testGetName() {
    assertEquals("Ting Mun Guy", customer.getName());
  }

  @Test
  public void testSetName() {
    customer.setName("Guy Guy Guy");
    assertEquals("Guy Guy Guy", customer.getName());
  }

  @Test
  public void testGetDateOfBirth() {
    assertEquals(dateOfBirth, customer.getDateOfBirth());
  }

  @Test
  public void testSetDateOfBirth() {
    LocalDate newDate = LocalDate.of(2020, 8, 6);
    customer.setDateOfBirth(newDate);
    assertEquals(newDate, customer.getDateOfBirth());
  }

  @Test
  public void testGetOccupation() {
    assertEquals("Engineer", customer.getOccupation());
  }

  @Test
  public void testSetOccupation() {
    customer.setOccupation("Doctor");
    assertEquals("Doctor", customer.getOccupation());
  }

  @Test
  public void testGetResidency() {
    assertEquals("NZ Citizen", customer.getResidency());
  }

  @Test
  public void testSetResidency() {
    customer.setResidency("AUS Citizen");
    assertEquals("AUS Citizen", customer.getResidency());
  }

  @Test
  public void testGetNotes() {
    assertEquals(notes, customer.getNotes());
  }

  @Test
  public void testSetNotes() {
    customer.setNotes(notes);
    assertEquals(notes, customer.getNotes());
  }

  @Test
  public void testGetAddresses() {
    assertEquals(addresses, customer.getAddresses());
  }

  @Test
  public void testSetAddresses() {
    customer.setAddresses(addresses);
    assertEquals(addresses, customer.getAddresses());
  }

  @Test
  public void testGetContact() {
    assertEquals(contact, customer.getContact());
  }

  @Test
  public void testSetContact() {
    CustomerContact newContact =
        new CustomerContact("123@gmail.com", phoneTwo, phoneOne, "mobile call", "home call");
    customer.setContact(newContact);
    assertEquals(newContact, customer.getContact());
  }

  @Test
  public void testGetEmployer() {
    assertEquals(employer, customer.getEmployer());
  }

  @Test
  public void testSetEmployer() {
    CustomerEmployer newEmployer =
        new CustomerEmployer(
            "BES",
            "123 Stonesuckle Ct",
            "",
            "Sunnynook",
            "Auckland",
            "12345",
            "Zimbabwe",
            "besisbest@gmail.com",
            null,
            null,
            true);
    customer.setEmployer(newEmployer);
    assertEquals(newEmployer, customer.getEmployer());
  }
}
