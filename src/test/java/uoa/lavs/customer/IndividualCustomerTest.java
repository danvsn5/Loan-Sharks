package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IndividualCustomerTest {
  private IndividualCustomer individualCustomer;
  private LocalDate dateOfBirth;
  private Address physicalAddress;
  private Phone phoneOne;
  private Phone phoneTwo;
  private CustomerContact contact;
  private Address employerAddress;
  private CustomerEmployer employer;

  @BeforeEach
  public void setUp() {
    dateOfBirth = LocalDate.of(2000, 1, 1);
    physicalAddress =
        new Address("Commercial", "999 Unreal Pl", "", "Arashiyama", "6160007", "Kyoto", "Japan");
    phoneOne = new Phone("mobile", "1234567890");
    phoneTwo = new Phone("home", "0987654321");
    contact =
        new CustomerContact(
            "thisplaceisunreal@gmail.com", phoneOne, phoneTwo, "mobile call", "email");
    employerAddress =
        new Address("Rural", "1 Real Pl", "", "Auckland CBD", "1010", "Auckland", "New Zealand");
    employer =
        new CustomerEmployer(
            "Company", employerAddress, "thisplaceisprettyreal@gmail.com", null, null, false);

    individualCustomer =
        new IndividualCustomer(
            "000001",
            "Mr",
            "Rule Number Ten",
            dateOfBirth,
            "Software Engineer",
            "NZ Citizen",
            "Allergic to peanuts",
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
    assertEquals("Allergic to peanuts", individualCustomer.getNotes());
    assertEquals(physicalAddress, individualCustomer.getPhysicalAddress());
    assertEquals(physicalAddress, individualCustomer.getMailingAddress());
    assertEquals(contact, individualCustomer.getContact());
    assertEquals(employer, individualCustomer.getEmployer());
  }
}
