package uoa.lavs.sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;

import uoa.lavs.customer.Address;
import uoa.lavs.customer.ICustomer;
import uoa.lavs.sql.DatabaseConnection;

public class AddressDAO {
  public void addAddress(ICustomer customer, Address address) {
    String sql = "INSERT INTO customer_address (customer_id, address_line_one, address_line_two, suburb, post_code, city, address_type) VALUES(?,?,?,?,?,?,?)";
    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getCustomerId());
      pstmt.setString(2, address.getAddressLineOne());
      pstmt.setString(3, address.getAddressLineTwo());
      pstmt.setString(4, address.getSuburb());
      pstmt.setString(5, address.getPostCode());
      pstmt.setString(6, address.getCity());
      pstmt.setString(7, address.getAddressType());
      pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
