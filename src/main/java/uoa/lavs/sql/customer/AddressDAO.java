package uoa.lavs.sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import uoa.lavs.customer.Address;
import uoa.lavs.sql.DatabaseConnection;

public class AddressDAO {

  public void addAddress(Address address) {
    String sql =
        "INSERT INTO customer_address (addressType, addressLineOne, addressLineTwo, suburb,"
            + " postCode, city, country) VALUES (?, ?, ?, ?, ?, ?, ?)";
    DatabaseConnection connection = new DatabaseConnection();
    try (Connection conn = connection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, address.getAddressType());
      pstmt.setString(2, address.getAddressLineOne());
      pstmt.setString(3, address.getAddressLineTwo());
      pstmt.setString(4, address.getSuburb());
      pstmt.setString(5, address.getPostCode());
      pstmt.setString(6, address.getCity());
      pstmt.setString(7, address.getCountry());

      pstmt.executeUpdate();

      try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int generatedId = generatedKeys.getInt(1);
          address.setAddressId(generatedId);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateAddress(Address address) {
    String sql =
        "UPDATE customer_address SET addressType = ?, addressLineOne = ?, addressLineTwo = ?,"
            + " suburb = ?, postCode = ?, city = ?, country = ? WHERE addressId = ?";
    DatabaseConnection connection = new DatabaseConnection();
    try (Connection conn = connection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, address.getAddressType());
      pstmt.setString(2, address.getAddressLineOne());
      pstmt.setString(3, address.getAddressLineTwo());
      pstmt.setString(4, address.getSuburb());
      pstmt.setString(5, address.getPostCode());
      pstmt.setString(6, address.getCity());
      pstmt.setString(7, address.getCountry());
      pstmt.setInt(8, address.getAddressId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
