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
import uoa.lavs.customer.Phone;
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
    Phone phoneOne = new Phone("mobile", "1234567890");
    Phone phoneTwo = new Phone("home", "0987654321");

    CustomerContact contact =
        new CustomerContact("tingtingguyguy@gmail.com", phoneOne, phoneTwo, "mobile sms", "email");

    customerContactDAO.addCustomerContact(contact);

    try (Connection conn = DatabaseConnection.connect()) {
      PreparedStatement stmt =
          conn.prepareStatement(
              "SELECT * FROM customer_contact WHERE customerEmail = ? AND phoneOneType = ? AND"
                  + " phoneOneNumber = ? AND phoneTwoType = ? AND phoneTwoNumber = ?");
      stmt.setString(1, "tingtingguyguy@gmail.com");
      stmt.setString(2, phoneOne.getType());
      stmt.setString(3, phoneOne.getPhoneNumber());
      stmt.setString(4, phoneTwo.getType());
      stmt.setString(5, phoneTwo.getPhoneNumber());

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer contact should be added to the database");
        Assertions.assertEquals("tingtingguyguy@gmail.com", rs.getString("customerEmail"));
        Assertions.assertEquals(phoneOne.getType(), rs.getString("phoneOneType"));
        Assertions.assertEquals(phoneOne.getPhoneNumber(), rs.getString("phoneOneNumber"));
        Assertions.assertEquals(phoneTwo.getType(), rs.getString("phoneTwoType"));
        Assertions.assertEquals(phoneTwo.getPhoneNumber(), rs.getString("phoneTwoNumber"));
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
    Phone phoneOne = new Phone("mobile", "1234567890");
    Phone phoneTwo = new Phone("home", "0987654321");
    CustomerContact contact =
        new CustomerContact("tingtingguyguy@gmail.com", phoneOne, phoneTwo, "mobile sms", "email");

    customerContactDAO.addCustomerContact(contact);

    contact.setCustomerEmail("guyguytingting@gmail.com");
    contact.setPhoneOne(phoneOne);
    contact.setPhoneTwo(phoneTwo);
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
        Assertions.assertEquals(phoneOne.getType(), rs.getString("phoneOneType"));
        Assertions.assertEquals(phoneOne.getPhoneNumber(), rs.getString("phoneOneNumber"));
        Assertions.assertEquals(phoneTwo.getType(), rs.getString("phoneTwoType"));
        Assertions.assertEquals(phoneTwo.getPhoneNumber(), rs.getString("phoneTwoNumber"));
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

  @Test
  public void testGetCustomerContact() {
    Phone phoneOne = new Phone("mobile", "1234567890");
    Phone phoneTwo = new Phone("home", "0987654321");
    CustomerContact contact =
        new CustomerContact("tingtingguyguy@gmail.com", phoneOne, phoneTwo, "mobile sms", "email");
    customerContactDAO.addCustomerContact(contact);

    int contactId = contact.getContactId();
    CustomerContact retrievedContact = customerContactDAO.getCustomerContact(contactId);

    try (Connection conn = DatabaseConnection.connect()) {
      PreparedStatement stmt =
          conn.prepareStatement("SELECT * FROM customer_contact WHERE contactId = ?");
      stmt.setInt(1, contactId);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer contact should be in the database");
        Assertions.assertEquals(retrievedContact.getCustomerEmail(), rs.getString("customerEmail"));
        Assertions.assertEquals(
            retrievedContact.getPhoneOne().getType(), rs.getString("phoneOneType"));
        Assertions.assertEquals(
            retrievedContact.getPhoneOne().getPhoneNumber(), rs.getString("phoneOneNumber"));
        Assertions.assertEquals(
            retrievedContact.getPhoneTwo().getType(), rs.getString("phoneTwoType"));
        Assertions.assertEquals(
            retrievedContact.getPhoneTwo().getPhoneNumber(), rs.getString("phoneTwoNumber"));
        Assertions.assertEquals(
            retrievedContact.getPreferredContact(), rs.getString("preferredContact"));
        Assertions.assertEquals(
            retrievedContact.getAlternateContact(), rs.getString("alternateContact"));
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
