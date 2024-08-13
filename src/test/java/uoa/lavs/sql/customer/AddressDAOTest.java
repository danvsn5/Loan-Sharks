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
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;

public class AddressDAOTest {
  DatabaseConnection conn;
  AddressDAO addressDAO;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    addressDAO = new AddressDAO();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void testAddAddress() {
    Address address =
        new Address("Commercial", "123 Guy St", "Apt 1", "Muntown", "12345", "Tingcity", "TMG");

    addressDAO.addAddress(address);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement(
                "SELECT * FROM customer_address WHERE addressLineOne = ? AND addressLineTwo = ?")) {
      stmt.setString(1, "123 Guy St");
      stmt.setString(2, "Apt 1");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Address should be added to the database");
        Assertions.assertEquals("Commercial", rs.getString("addressType"));
        Assertions.assertEquals("123 Guy St", rs.getString("addressLineOne"));
        Assertions.assertEquals("Apt 1", rs.getString("addressLineTwo"));
        Assertions.assertEquals("Muntown", rs.getString("suburb"));
        Assertions.assertEquals("12345", rs.getString("postCode"));
        Assertions.assertEquals("Tingcity", rs.getString("city"));
        Assertions.assertEquals("TMG", rs.getString("country"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateAddress() {
    Address address =
        new Address("Commercial", "123 Guy St", "Apt 1", "Muntown", "12345", "Tingcity", "TMG");
    addressDAO.addAddress(address);

    address.setAddressType("Residential");
    address.setAddressLineOne("456 Guy St");
    address.setAddressLineTwo("Apt 2");
    address.setSuburb("Goonburb");
    address.setPostCode("67890");
    address.setCity("Gooncity");
    address.setCountry("NTG");

    addressDAO.updateAddress(address);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_address WHERE addressId = ?")) {
      stmt.setInt(1, address.getAddressId());

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Address should be updated in the database");
        Assertions.assertEquals("Residential", rs.getString("addressType"));
        Assertions.assertEquals("456 Guy St", rs.getString("addressLineOne"));
        Assertions.assertEquals("Apt 2", rs.getString("addressLineTwo"));
        Assertions.assertEquals("Goonburb", rs.getString("suburb"));
        Assertions.assertEquals("67890", rs.getString("postCode"));
        Assertions.assertEquals("Gooncity", rs.getString("city"));
        Assertions.assertEquals("NTG", rs.getString("country"));
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
