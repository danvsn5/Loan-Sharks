package uoa.lavs.sql.customer;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;
import uoa.lavs.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerEmployerDAO;

public class CustomerEmployerDAOTest {
  DatabaseConnection conn;
  CustomerEmployerDAO employerDAO;
  CustomerEmployer employer;
  Address employerAddress;
  AddressDAO addressDAO;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    employerDAO = new CustomerEmployerDAO();
    addressDAO = new AddressDAO();
    dbFile = DatabaseState.DB_TEST_FILE;
    employerAddress =
        new Address(
            "Commercial", "123 Stonesuckle Ct", "", "Sunnynook", "12345", "Auckland", "Zimbabwe");
    employer =
        new CustomerEmployer(
            "Countdown",
            employerAddress,
            "lowestprices@outlook.com",
            "www.notworth.org.au",
            "0102030405",
            false);
    addressDAO.addAddress(employerAddress);
  }

  @Test
  public void testAddCustomerEmployer() {
    employerDAO.addCustomerEmployer(employer);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_employer WHERE employerName = ?")) {
      stmt.setString(1, "Countdown");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Employer should be added to the database");
        Assertions.assertEquals("Countdown", rs.getString("employerName"));
        Assertions.assertEquals(employerAddress.getAddressId(), rs.getInt("employerAddressId"));
        Assertions.assertEquals("lowestprices@outlook.com", rs.getString("employerEmail"));
        Assertions.assertEquals("www.notworth.org.au", rs.getString("employerWebsite"));
        Assertions.assertEquals("0102030405", rs.getString("employerPhone"));
        Assertions.assertEquals(false, rs.getBoolean("ownerOfCompany"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateCustomerEmployer() {
    employerDAO.addCustomerEmployer(employer);
    employer.setEmployerEmail("manager@store.veryworth.org.au");
    employer.setEmployerWebsite("www.veryworth.org.au");
    employer.setEmployerPhone("1234567890");
    employer.setOwnerOfCompany(true);

    employerDAO.updateCustomerEmployer(employer);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_employer WHERE employerId = ?")) {
      stmt.setInt(1, employer.getEmployerId());

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Employer should be updated in the database");
        Assertions.assertEquals("Countdown", rs.getString("employerName"));
        Assertions.assertEquals(employerAddress.getAddressId(), rs.getInt("employerAddressId"));
        Assertions.assertEquals("manager@store.veryworth.org.au", rs.getString("employerEmail"));
        Assertions.assertEquals("www.veryworth.org.au", rs.getString("employerWebsite"));
        Assertions.assertEquals("1234567890", rs.getString("employerPhone"));
        Assertions.assertEquals(true, rs.getBoolean("ownerOfCompany"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetCustomerEmployer() {
    employerDAO.addCustomerEmployer(employer);
    int employerId = employer.getEmployerId();

    CustomerEmployer retrievedEmployer = employerDAO.getCustomerEmployer(employerId);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_employer WHERE employerId = ?")) {
      stmt.setInt(1, employerId);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Employer should be in the database");
        Assertions.assertEquals(retrievedEmployer.getEmployerName(), rs.getString("employerName"));
        Assertions.assertEquals(
            retrievedEmployer.getEmployerAddress().getAddressId(), rs.getInt("employerAddressId"));
        Assertions.assertEquals(
            retrievedEmployer.getEmployerEmail(), rs.getString("employerEmail"));
        Assertions.assertEquals(
            retrievedEmployer.getEmployerWebsite(), rs.getString("employerWebsite"));
        Assertions.assertEquals(
            retrievedEmployer.getEmployerPhone(), rs.getString("employerPhone"));
        Assertions.assertEquals(
            retrievedEmployer.getOwnerOfCompany(), rs.getBoolean("ownerOfCompany"));
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
