package uoa.lavs.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uoa.lavs.customer.Address;
import uoa.lavs.sql.DatabaseConnection;

public class AddressDAO {

  public void addAddress(Address address) {
    String customerId = address.getCustomerId();
    // Find the next addressId for this customerId
    int nextAddressId = getNextAddressIdForCustomer(customerId);

    String sql =
        "INSERT INTO customer_address (customerId, addressId, addressType, addressLineOne, "
            + "addressLineTwo, suburb, postCode, city, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      pstmt.setInt(2, nextAddressId);
      pstmt.setString(3, address.getAddressType());
      pstmt.setString(4, address.getAddressLineOne());
      pstmt.setString(5, address.getAddressLineTwo());
      pstmt.setString(6, address.getSuburb());
      pstmt.setString(7, address.getPostCode());
      pstmt.setString(8, address.getCity());
      pstmt.setString(9, address.getCountry());

      pstmt.executeUpdate();

      address.setAddressId(nextAddressId);
      address.setCustomerId(customerId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private int getNextAddressIdForCustomer(String customerId) {
    String sql = "SELECT MAX(addressId) FROM customer_address WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        int maxAddressId = rs.getInt(1);
        return maxAddressId + 1;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return 1; // Start with 1 if no address exists for this customer
  }

  public void updateAddress(Address address) {
    String sql =
        "UPDATE customer_address SET addressType = ?, addressLineOne = ?, addressLineTwo = ?,"
            + " suburb = ?, postCode = ?, city = ?, country = ? WHERE customerId = ? AND addressId"
            + " = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, address.getAddressType());
      pstmt.setString(2, address.getAddressLineOne());
      pstmt.setString(3, address.getAddressLineTwo());
      pstmt.setString(4, address.getSuburb());
      pstmt.setString(5, address.getPostCode());
      pstmt.setString(6, address.getCity());
      pstmt.setString(7, address.getCountry());
      pstmt.setString(8, address.getCustomerId());
      pstmt.setInt(9, address.getAddressId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

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

        Address retrievedAddress =
            new Address(
                customerId,
                addressType,
                addressLineOne,
                addressLineTwo,
                suburb,
                postCode,
                city,
                country);
        retrievedAddress.setAddressId(addressId);
        retrievedAddress.setCustomerId(customerId);
        return retrievedAddress;
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
