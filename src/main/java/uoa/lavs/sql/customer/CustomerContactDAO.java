package uoa.lavs.sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;

import uoa.lavs.customer.CustomerContact;
import uoa.lavs.customer.ICustomer;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerContactDAO {
  public void addCustomerContact(ICustomer customer) {
    CustomerContact contact = customer.getContact();
    String sql = "INSERT INTO customer_contact (customer_id, customer_email, preferred_contact, alternate_contact) VALUES (?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getCustomerId());
      pstmt.setString(2, contact.getCustomerEmail());
      pstmt.setString(3, contact.getPreferredContact());
      pstmt.setString(4, contact.getAlternateContact());
      pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
