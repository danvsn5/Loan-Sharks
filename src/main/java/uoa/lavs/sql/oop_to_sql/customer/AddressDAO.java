package uoa.lavs.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.customer.Address;
import uoa.lavs.sql.DatabaseConnection;

public class AddressDAO {

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
            + " suburb = ?, postCode = ?, city = ?, country = ?, isPrimary = ?, isMailing = ? WHERE"
            + " customerId = ? AND addressId = ?";
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

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

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

  public static void main(String[] args) {
    AddressDAO addressDAO = new AddressDAO();
    Address address =
        new Address(
            "000001",
            "Residential",
            "123 Main St",
            "",
            "Auckland",
            "1010",
            "Auckland",
            "New Zealand",
            true,
            true);
    addressDAO.addAddress(address);
  }
}
