package uoa.lavs.backend.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.oop_to_sql.AbstractDAO;

public class AddressDAO extends AbstractDAO {

  // Handles the addition of an address to the database
  public void addAddress(Address address) {
    String customerId = address.getCustomerId();
    // Find the next addressId for this customerId
    int nextAddressId = getNextAddressIdForCustomer(customerId);

    String sql =
        "INSERT INTO customer_address (customerId, addressId, addressType, addressLineOne,"
            + " addressLineTwo, suburb, postCode, city, country, isPrimary, isMailing) VALUES (?,"
            + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Set the values for the prepared statement from the address object
      pstmt.setString(1, customerId);
      pstmt.setInt(2, nextAddressId);
      pstmt.setString(3, address.getAddressType());
      pstmt.setString(4, address.getAddressLineOne());
      pstmt.setString(5, address.getAddressLineTwo());
      pstmt.setString(6, address.getSuburb());
      pstmt.setString(7, address.getPostCode());
      pstmt.setString(8, address.getCity());
      pstmt.setString(9, address.getCountry());
      pstmt.setBoolean(10, address.getIsPrimary());
      pstmt.setBoolean(11, address.getIsMailing());

      pstmt.executeUpdate();

      address.setAddressId(nextAddressId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Finds the next addressId for a customer
  private int getNextAddressIdForCustomer(String customerId) {
    String sql = "SELECT MAX(addressId) FROM customer_address WHERE customerId = ?";
    return getNextId(customerId, sql);
  }

  // Updates the address in the database with the new address paramaters
  public void updateAddress(Address address) {
    String sql =
        "UPDATE customer_address SET addressType = ?, addressLineOne = ?, addressLineTwo = ?,"
            + " suburb = ?, postCode = ?, city = ?, country = ?, isPrimary = ?, isMailing = ?,"
            + " lastModified = CURRENT_TIMESTAMP WHERE customerId = ? AND addressId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, address.getAddressType());
      pstmt.setString(2, address.getAddressLineOne());
      pstmt.setString(3, address.getAddressLineTwo());
      pstmt.setString(4, address.getSuburb());
      pstmt.setString(5, address.getPostCode());
      pstmt.setString(6, address.getCity());
      pstmt.setString(7, address.getCountry());
      pstmt.setBoolean(8, address.getIsPrimary());
      pstmt.setBoolean(9, address.getIsMailing());
      pstmt.setString(10, address.getCustomerId());
      pstmt.setInt(11, address.getAddressId());

      System.out.println("Updating address: " + address.getAddressId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Retrieves an address from the database
  public Address getAddress(String customerId, int addressId) {
    String sql = "SELECT * FROM customer_address WHERE customerId = ? AND addressId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      pstmt.setInt(2, addressId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        String addressType = rs.getString("addressType");
        String addressLineOne = rs.getString("addressLineOne");
        String addressLineTwo = rs.getString("addressLineTwo");
        String suburb = rs.getString("suburb");
        String postCode = rs.getString("postCode");
        String city = rs.getString("city");
        String country = rs.getString("country");
        boolean isPrimary = rs.getBoolean("isPrimary");
        boolean isMailing = rs.getBoolean("isMailing");

        Address retrievedAddress =
            new Address(
                customerId,
                addressType,
                addressLineOne,
                addressLineTwo,
                suburb,
                postCode,
                city,
                country,
                isPrimary,
                isMailing);
        retrievedAddress.setAddressId(addressId);
        retrievedAddress.setCustomerId(customerId);
        System.out.println("Address ID: " + retrievedAddress.getAddressId());
        return retrievedAddress;
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  // Retrieves all addresses for a customer
  public ArrayList<Address> getAddresses(String customerId) {
    ArrayList<Address> addresses = new ArrayList<>();

    String sql = "SELECT * FROM customer_address WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        int addressId = rs.getInt("addressId");
        String addressType = rs.getString("addressType");
        String addressLineOne = rs.getString("addressLineOne");
        String addressLineTwo = rs.getString("addressLineTwo");
        String suburb = rs.getString("suburb");
        String postCode = rs.getString("postCode");
        String city = rs.getString("city");
        String country = rs.getString("country");
        boolean isPrimary = rs.getBoolean("isPrimary");
        boolean isMailing = rs.getBoolean("isMailing");

        Address retrievedAddress =
            new Address(
                customerId,
                addressType,
                addressLineOne,
                addressLineTwo,
                suburb,
                postCode,
                city,
                country,
                isPrimary,
                isMailing);
        retrievedAddress.setAddressId(addressId);

        addresses.add(retrievedAddress);
      }
      return addresses;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
