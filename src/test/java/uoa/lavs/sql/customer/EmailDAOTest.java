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
import uoa.lavs.customer.Email;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;
import uoa.lavs.sql.oop_to_sql.customer.EmailDAO;

public class EmailDAOTest {
  EmailDAO emailDAO;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    emailDAO = new EmailDAO();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void testAddEmail() {
    Email email = new Email("000001", "test@example.com", true);
    emailDAO.addEmail(email);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement(
                "SELECT * FROM customer_email WHERE customerId = ? AND emailAddress = ?")) {
      stmt.setString(1, "000001");
      stmt.setString(2, "test@example.com");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Email should be added to the database");
        Assertions.assertEquals("test@example.com", rs.getString("emailAddress"));
        Assertions.assertTrue(rs.getBoolean("isPrimary"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateEmail() {
    Email email = new Email("000001", "oldemail@example.com", false);
    emailDAO.addEmail(email);

    email.setEmailAddress("newemail@example.com");
    email.setIsPrimary(true);

    emailDAO.updateEmail(email);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement(
                "SELECT * FROM customer_email WHERE customerId = ? AND emailId = ?")) {
      stmt.setString(1, email.getCustomerId());
      stmt.setInt(2, email.getEmailId());

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Email should be updated in the database");
        Assertions.assertEquals("newemail@example.com", rs.getString("emailAddress"));
        Assertions.assertTrue(rs.getBoolean("isPrimary"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetEmail() {
    Email email = new Email("000001", "oldemail@example.com", false);
    emailDAO.addEmail(email);

    Email retrievedEmail = emailDAO.getEmail("000001", email.getEmailId());

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_email WHERE emailId = ?")) {
      stmt.setInt(1, email.getEmailId());

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Email should be retrieved from the database");
        Assertions.assertEquals(retrievedEmail.getEmailAddress(), rs.getString("emailAddress"));
        Assertions.assertEquals(retrievedEmail.getIsPrimary(), rs.getBoolean("isPrimary"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetEmails() {
    Email email = new Email("000001", "test@example.com", true);
    emailDAO.addEmail(email);
    int emailId = email.getEmailId();

    ArrayList<Email> retrievedEmails = emailDAO.getEmails("000001");

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_email WHERE emailId = ?")) {
      stmt.setInt(1, emailId);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Email should be retrieved from the database");
        Assertions.assertEquals(
            retrievedEmails.get(0).getEmailAddress(), rs.getString("emailAddress"));
        Assertions.assertEquals(retrievedEmails.get(0).getIsPrimary(), rs.getBoolean("isPrimary"));
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
