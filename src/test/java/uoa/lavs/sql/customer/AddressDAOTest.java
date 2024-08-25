package uoa.lavs.sql.customer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.backend.sql.ResetDatabase;
import uoa.lavs.backend.sql.oop_to_sql.customer.AddressDAO;

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
        new Address(
            "000001",
            "Commercial",
            "123 Guy St",
            "Apt 1",
            "Muntown",
            "12345",
            "Tingcity",
            "TMG",
            true,
            false);

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
        Assertions.assertTrue(rs.getBoolean("isPrimary"));
        Assertions.assertFalse(rs.getBoolean("isMailing"));
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
        new Address(
            "000001",
            "Commercial",
            "123 Guy St",
            "Apt 1",
            "Muntown",
            "12345",
            "Tingcity",
            "TMG",
            false,
            true);
    addressDAO.addAddress(address);

    address.setAddressType("Residential");
    address.setAddressLineOne("456 Guy St");
    address.setAddressLineTwo("Apt 2");
    address.setSuburb("Goonburb");
    address.setPostCode("67890");
    address.setCity("Gooncity");
    address.setCountry("NTG");
    address.setIsPrimary(true);
    address.setIsMailing(false);

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
        Assertions.assertTrue(rs.getBoolean("isPrimary"));
        Assertions.assertFalse(rs.getBoolean("isMailing"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetAddress() {
    Address address =
        new Address(
            "000001",
            "Commercial",
            "123 Guy St",
            "Apt 1",
            "Muntown",
            "12345",
            "Tingcity",
            "TMG",
            true,
            false);
    addressDAO.addAddress(address);
    int addressId = address.getAddressId();

    Address retrievedAddress = addressDAO.getAddress("000001", addressId);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_address WHERE addressId = ?")) {
      stmt.setInt(1, addressId);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Address should be updated in the database");
        Assertions.assertEquals(retrievedAddress.getAddressType(), rs.getString("addressType"));
        Assertions.assertEquals(
            retrievedAddress.getAddressLineOne(), rs.getString("addressLineOne"));
        Assertions.assertEquals(
            retrievedAddress.getAddressLineTwo(), rs.getString("addressLineTwo"));
        Assertions.assertEquals(retrievedAddress.getSuburb(), rs.getString("suburb"));
        Assertions.assertEquals(retrievedAddress.getPostCode(), rs.getString("postCode"));
        Assertions.assertEquals(retrievedAddress.getCity(), rs.getString("city"));
        Assertions.assertEquals(retrievedAddress.getCountry(), rs.getString("country"));
        Assertions.assertEquals(retrievedAddress.getIsPrimary(), rs.getBoolean("isPrimary"));
        Assertions.assertEquals(retrievedAddress.getIsMailing(), rs.getBoolean("isMailing"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetAddresses() {
    Address address =
        new Address(
            "000001",
            "Commercial",
            "123 Guy St",
            "Apt 1",
            "Muntown",
            "12345",
            "Tingcity",
            "TMG",
            true,
            false);
    addressDAO.addAddress(address);
    int addressId = address.getAddressId();

    ArrayList<Address> retrievedAddresses = addressDAO.getAddresses("000001");

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_address WHERE addressId = ?")) {
      stmt.setInt(1, addressId);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Address should be updated in the database");
        Assertions.assertEquals(
            retrievedAddresses.get(0).getAddressType(), rs.getString("addressType"));
        Assertions.assertEquals(
            retrievedAddresses.get(0).getAddressLineOne(), rs.getString("addressLineOne"));
        Assertions.assertEquals(
            retrievedAddresses.get(0).getAddressLineTwo(), rs.getString("addressLineTwo"));
        Assertions.assertEquals(retrievedAddresses.get(0).getSuburb(), rs.getString("suburb"));
        Assertions.assertEquals(retrievedAddresses.get(0).getPostCode(), rs.getString("postCode"));
        Assertions.assertEquals(retrievedAddresses.get(0).getCity(), rs.getString("city"));
        Assertions.assertEquals(retrievedAddresses.get(0).getCountry(), rs.getString("country"));
        Assertions.assertEquals(
            retrievedAddresses.get(0).getIsPrimary(), rs.getBoolean("isPrimary"));
        Assertions.assertEquals(
            retrievedAddresses.get(0).getIsMailing(), rs.getBoolean("isMailing"));
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
