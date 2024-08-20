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
  private Address physicalAddress;
  private Phone phoneOne;
  private Phone phoneTwo;
  private CustomerContact contact;
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
    phoneOne = new Phone("mobile", "1234567890");
    phoneTwo = new Phone("home", "0987654321");
    contact =
        new CustomerContact(
            "thisplaceisunreal@gmail.com", phoneOne, phoneTwo, "mobile call", "email");
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
            "NZ Citizen",
            notes,
            physicalAddress,
            physicalAddress,
            contact,
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
    assertEquals("NZ Citizen", individualCustomer.getResidency());
    assertEquals(notes, individualCustomer.getNotes());
    assertEquals(physicalAddress, individualCustomer.getPhysicalAddress());
    assertEquals(physicalAddress, individualCustomer.getMailingAddress());
    assertEquals(contact, individualCustomer.getContact());
    assertEquals(employer, individualCustomer.getEmployer());
  }
}
