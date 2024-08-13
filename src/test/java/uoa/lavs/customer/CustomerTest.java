package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTest {
  private Customer customer;
  private LocalDate dateOfBirth;
  private Address physicalAddress;
  private Phone phoneOne;
  private Phone phoneTwo;
  private CustomerContact contact;
  private Address employerAddress;
  private CustomerEmployer employer;

  @BeforeEach
  public void setUp() {
    dateOfBirth = LocalDate.of(2024, 8, 6);
    physicalAddress =
        new Address("Rural", "304 Rose St", "46", "Sunnynook", "12345", "Auckland", "Zimbabwe");
    phoneOne = new Phone("mobile", "1234567890");
    phoneTwo = new Phone("home", "0987654321");
    contact = new CustomerContact("abc@gmail.com", phoneOne, phoneTwo, "mobile sms", "email");
    employerAddress =
        new Address(
            "Commercial", "123 Stonesuckle Ct", "", "Sunnynook", "12345", "Auckland", "Zimbabwe");
    employer = new CustomerEmployer("Countdown", physicalAddress, null, null, null, false);

    customer =
        new IndividualCustomer(
            "000001",
            "Mr",
            "Ting",
            "Mun",
            "Guy",
            dateOfBirth,
            "Engineer",
            "NZ Citizen",
            physicalAddress,
            physicalAddress,
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
  public void testGetFirstName() {
    assertEquals("Ting", customer.getFirstName());
  }

  @Test
  public void testSetFirstName() {
    customer.setFirstName("Ting Ting");
    assertEquals("Ting Ting", customer.getFirstName());
  }

  @Test
  public void testGetMiddleName() {
    assertEquals("Mun", customer.getMiddleName());
  }

  @Test
  public void testSetMiddleName() {
    customer.setMiddleName("Mun Mun");
    assertEquals("Mun Mun", customer.getMiddleName());
  }

  @Test
  public void testGetLastName() {
    assertEquals("Guy", customer.getLastName());
  }

  @Test
  public void testSetLastName() {
    customer.setLastName("Guy Guy");
    assertEquals("Guy Guy", customer.getLastName());
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
  public void testGetPhysicalAddress() {
    assertEquals(physicalAddress, customer.getPhysicalAddress());
  }

  @Test
  public void testSetPhysicalAddress() {
    customer.setPhysicalAddress(employerAddress);
    assertEquals(employerAddress, customer.getPhysicalAddress());
  }

  @Test
  public void testGetMailingAddress() {
    assertEquals(physicalAddress, customer.getMailingAddress());
  }

  @Test
  public void testSetMailingAddress() {
    customer.setMailingAddress(employerAddress);
    assertEquals(employerAddress, customer.getMailingAddress());
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
        new CustomerEmployer("BES", employerAddress, "besisbest@gmail.com", null, null, true);
    customer.setEmployer(newEmployer);
    assertEquals(newEmployer, customer.getEmployer());
  }
}
