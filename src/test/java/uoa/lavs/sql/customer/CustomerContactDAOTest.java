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
import uoa.lavs.customer.CustomerContact;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;

public class CustomerContactDAOTest {
  DatabaseConnection conn;
  CustomerContactDAO customerContactDAO;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    customerContactDAO = new CustomerContactDAO();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void testAddCustomerContact() {
    CustomerContact contact =
        new CustomerContact(
            "tingtingguyguy@gmail.com", "1234567890", "0987654321", "mobile sms", "email");

    customerContactDAO.addCustomerContact(contact);

    try (Connection conn = DatabaseConnection.connect()) {
      PreparedStatement stmt =
          conn.prepareStatement(
              "SELECT * FROM customer_contact WHERE customerEmail = ? AND phoneOne = ? AND phoneTwo"
                  + " = ?");
      stmt.setString(1, "tingtingguyguy@gmail.com");
      stmt.setString(2, "1234567890");
      stmt.setString(3, "0987654321");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer contact should be added to the database");
        Assertions.assertEquals("tingtingguyguy@gmail.com", rs.getString("customerEmail"));
        Assertions.assertEquals("1234567890", rs.getString("phoneOne"));
        Assertions.assertEquals("0987654321", rs.getString("phoneTwo"));
        Assertions.assertEquals("mobile sms", rs.getString("preferredContact"));
        Assertions.assertEquals("email", rs.getString("alternateContact"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateCustomerContact() {
    CustomerContact contact =
        new CustomerContact(
            "tingtingguyguy@gmail.com", "1234567890", "0987654321", "mobile sms", "email");

    customerContactDAO.addCustomerContact(contact);

    contact.setCustomerEmail("guyguytingting@gmail.com");
    contact.setPhoneOne("0800838383");
    contact.setPhoneTwo("0800838383");
    contact.setPreferredContact("phone call");
    contact.setAlternateContact("mobile sms");

    customerContactDAO.updateCustomerContact(contact);

    try (Connection conn = DatabaseConnection.connect();
      PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_contact WHERE contactId = ?")) {
      stmt.setInt(1, contact.getContactId());

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer contact should be added to the database");
        Assertions.assertEquals("guyguytingting@gmail.com", rs.getString("customerEmail"));
        Assertions.assertEquals("0800838383", rs.getString("phoneOne"));
        Assertions.assertEquals("0800838383", rs.getString("phoneTwo"));
        Assertions.assertEquals("phone call", rs.getString("preferredContact"));
        Assertions.assertEquals("mobile sms", rs.getString("alternateContact"));
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
      throw new RuntimeException("Failed to delete test database file: " + dbFile.getAbsolutePath());
    }
  }
}
