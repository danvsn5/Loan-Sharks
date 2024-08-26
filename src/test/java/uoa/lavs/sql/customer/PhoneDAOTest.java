package uoa.lavs.sql.customer;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.oop.customer.Phone;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.backend.sql.oop_to_sql.customer.PhoneDAO;

public class PhoneDAOTest {
  DatabaseConnection conn;
  PhoneDAO phoneDAO;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    phoneDAO = new PhoneDAO();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void testAddPhone() {
    Phone phone = new Phone("000001", "Mobile", "027", "1234567890", true, true);
    phoneDAO.addPhone(phone);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_phone WHERE phoneNumber = ?")) {
      stmt.setString(1, "1234567890");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Phone should be added to the database");
        Assertions.assertEquals("Mobile", rs.getString("type"));
        Assertions.assertEquals("027", rs.getString("prefix"));
        Assertions.assertEquals("1234567890", rs.getString("phoneNumber"));
        Assertions.assertTrue(rs.getBoolean("isPrimary"));
        Assertions.assertTrue(rs.getBoolean("canSendText"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdatePhone() {
    Phone phone = new Phone("000001", "Mobile", "027", "1234567890", true, true);
    phoneDAO.addPhone(phone);

    phone.setType("Home");
    phone.setPhoneNumber("0987654321");
    phone.setIsPrimary(false);
    phone.setCanSendText(false);

    phoneDAO.updatePhone(phone);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_phone WHERE phoneId = ?")) {
      stmt.setInt(1, phone.getPhoneId());

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Phone should be updated in the database");
        Assertions.assertEquals("Home", rs.getString("type"));
        Assertions.assertEquals("027", rs.getString("prefix"));
        Assertions.assertEquals("0987654321", rs.getString("phoneNumber"));
        Assertions.assertFalse(rs.getBoolean("isPrimary"));
        Assertions.assertFalse(rs.getBoolean("canSendText"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetPhone() {
    Phone phone = new Phone("000001", "mobile", "027", "1234567890", true, true);
    phoneDAO.addPhone(phone);
    int phoneId = phone.getPhoneId();

    Phone retrievedPhone = phoneDAO.getPhone("000001", phoneId);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_phone WHERE phoneId = ?")) {
      stmt.setInt(1, phoneId);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Phone should be retrieved from the database");
        Assertions.assertEquals(retrievedPhone.getType(), rs.getString("type"));
        Assertions.assertEquals(retrievedPhone.getPrefix(), rs.getString("prefix"));
        Assertions.assertEquals(retrievedPhone.getPhoneNumber(), rs.getString("phoneNumber"));
        Assertions.assertEquals(retrievedPhone.getIsPrimary(), rs.getBoolean("isPrimary"));
        Assertions.assertEquals(retrievedPhone.getCanSendText(), rs.getBoolean("canSendText"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetPhones() {
    Phone phone = new Phone("000001", "Mobile", "027", "1234567890", true, true);
    phoneDAO.addPhone(phone);
    int phoneId = phone.getPhoneId();

    ArrayList<Phone> retrievedPhones = phoneDAO.getPhones("000001");

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_phone WHERE phoneId = ?")) {
      stmt.setInt(1, phoneId);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Phone should be retrieved from the database");
        Assertions.assertEquals(retrievedPhones.get(0).getType(), rs.getString("type"));
        Assertions.assertEquals(retrievedPhones.get(0).getPrefix(), rs.getString("prefix"));
        Assertions.assertEquals(
            retrievedPhones.get(0).getPhoneNumber(), rs.getString("phoneNumber"));
        Assertions.assertEquals(retrievedPhones.get(0).getIsPrimary(), rs.getBoolean("isPrimary"));
        Assertions.assertEquals(
            retrievedPhones.get(0).getCanSendText(), rs.getBoolean("canSendText"));
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
