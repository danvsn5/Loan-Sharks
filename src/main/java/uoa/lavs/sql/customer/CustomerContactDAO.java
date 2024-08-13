package uoa.lavs.sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uoa.lavs.customer.CustomerContact;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerContactDAO {

  public void addCustomerContact(CustomerContact contact) {
    String sql =
        "INSERT INTO customer_contact (contactId, customerEmail, phoneOneType, phoneOneNumber,"
            + " phoneTwoType, phoneTwoNumber, preferredContact, alternateContact) VALUES (?, ?, ?,"
            + " ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, contact.getContactId());
      pstmt.setString(2, contact.getCustomerEmail());
      pstmt.setString(3, contact.getPhoneOne().getType());
      pstmt.setString(4, contact.getPhoneOne().getPhoneNumber());
      pstmt.setString(5, contact.getPhoneTwo().getType());
      pstmt.setString(6, contact.getPhoneTwo().getPhoneNumber());
      pstmt.setString(7, contact.getPreferredContact());
      pstmt.setString(8, contact.getAlternateContact());
      pstmt.executeUpdate();

      try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int generatedId = generatedKeys.getInt(1);
          contact.setContactId(generatedId);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateCustomerContact(CustomerContact contact) {
    String sql =
        "UPDATE customer_contact SET customerEmail = ?, phoneOneType = ?, phoneOneNumber = ?,"
            + " phoneTwoType = ?, phoneTwoNumber = ?, preferredContact = ?, alternateContact = ?"
            + " WHERE contactId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, contact.getCustomerEmail());
      pstmt.setString(2, contact.getPhoneOne().getType());
      pstmt.setString(3, contact.getPhoneOne().getPhoneNumber());
      pstmt.setString(4, contact.getPhoneTwo().getType());
      pstmt.setString(5, contact.getPhoneTwo().getPhoneNumber());
      pstmt.setString(6, contact.getPreferredContact());
      pstmt.setString(7, contact.getAlternateContact());
      pstmt.setInt(8, contact.getContactId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public CustomerContact getCustomerContact(int contactId) {
    String sql = "SELECT * FROM customer_contact WHERE contactId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, contactId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        String customerEmail = rs.getString("customerEmail");
        Phone phoneOne = new Phone(rs.getString("phoneOneType"), rs.getString("phoneOneNumber"));
        Phone phoneTwo = new Phone(rs.getString("phoneTwoType"), rs.getString("phoneTwoNumber"));
        String preferredContact = rs.getString("preferredContact");
        String alternateContact = rs.getString("alternateContact");

        return new CustomerContact(
            customerEmail, phoneOne, phoneTwo, preferredContact, alternateContact);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
