package uoa.lavs.sql.customer;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.CustomerContact;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;

public class CustomerDAOTest {
  DatabaseConnection conn;
  CustomerEmployerDAO employerDAO;
  CustomerEmployer employer;
  Address employerAddress;
  AddressDAO addressDAO;
  CustomerContactDAO contactDAO;
  CustomerContact contact;
  Address physicalAddress;
  CustomerDAO customerDAO;
  IndividualCustomer customer;
  LocalDate dateOfBirth;
  Phone phoneOne;
  Phone phoneTwo;

  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    dbFile = DatabaseState.DB_TEST_FILE;
    InitialiseDatabase.createDatabase();
    employerDAO = new CustomerEmployerDAO();
    addressDAO = new AddressDAO();
    contactDAO = new CustomerContactDAO();
    customerDAO = new CustomerDAO();
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

    addressDAO.addAddress(physicalAddress);
    addressDAO.addAddress(employerAddress);
    contactDAO.addCustomerContact(contact);
    employerDAO.addCustomerEmployer(employer);
  }

  @Test
  public void testAddCustomer() {
    customerDAO.addCustomer(customer);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer WHERE customerId = ?")) {
      stmt.setString(1, "000001");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer should be added to the database");
        Assertions.assertEquals("000001", rs.getString("customerId"));
        Assertions.assertEquals("Mr", rs.getString("title"));
        Assertions.assertEquals("Ting", rs.getString("firstName"));
        Assertions.assertEquals("Mun", rs.getString("middleName"));
        Assertions.assertEquals("Guy", rs.getString("lastName"));
        LocalDate actualDateOfBirth = rs.getDate("dateOfBirth").toLocalDate();
        Assertions.assertEquals(LocalDate.of(2024, 8, 6), actualDateOfBirth);
        Assertions.assertEquals("Engineer", rs.getString("occupation"));
        Assertions.assertEquals("NZ Citizen", rs.getString("residency"));
        Assertions.assertEquals(physicalAddress.getAddressId(), rs.getInt("physicalAddressId"));
        Assertions.assertEquals(physicalAddress.getAddressId(), rs.getInt("mailingAddressId"));
        Assertions.assertEquals(contact.getContactId(), rs.getInt("contactId"));
        Assertions.assertEquals(employer.getEmployerId(), rs.getInt("employerId"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateCustomer() {
    customerDAO.addCustomer(customer);
    customer.setTitle("Mrs");
    customer.setFirstName("Ting Ting");
    customer.setMiddleName("Mun Mun");
    customer.setLastName("Guy Guy");
    customer.setDateOfBirth(LocalDate.of(2024, 8, 7));
    customer.setOccupation("Doctor");
    customer.setResidency("NZ Permanent Resident");
    customer.setPhysicalAddress(employerAddress);
    customer.setMailingAddress(employerAddress);
    customer.setContact(contact);
    customer.setEmployer(employer);

    customerDAO.updateCustomer(customer);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer WHERE customerId = ?")) {
      stmt.setString(1, "000001");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer should be updated in the database");
        Assertions.assertEquals("000001", rs.getString("customerId"));
        Assertions.assertEquals("Mrs", rs.getString("title"));
        Assertions.assertEquals("Ting Ting", rs.getString("firstName"));
        Assertions.assertEquals("Mun Mun", rs.getString("middleName"));
        Assertions.assertEquals("Guy Guy", rs.getString("lastName"));
        LocalDate actualDateOfBirth = rs.getDate("dateOfBirth").toLocalDate();
        Assertions.assertEquals(LocalDate.of(2024, 8, 7), actualDateOfBirth);
        Assertions.assertEquals("Doctor", rs.getString("occupation"));
        Assertions.assertEquals("NZ Permanent Resident", rs.getString("residency"));
        Assertions.assertEquals(employerAddress.getAddressId(), rs.getInt("physicalAddressId"));
        Assertions.assertEquals(employerAddress.getAddressId(), rs.getInt("mailingAddressId"));
        Assertions.assertEquals(contact.getContactId(), rs.getInt("contactId"));
        Assertions.assertEquals(employer.getEmployerId(), rs.getInt("employerId"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
    if (!dbFile.delete()) {
      throw new RuntimeException(
          "Failed to delete test database file: " + dbFile.getAbsolutePath());
    }
  }
}
