package uoa.lavs.sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uoa.lavs.customer.CustomerContact;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerContactDAO {

    public void addCustomerContact(CustomerContact contact) {
        String sql = "INSERT INTO customer_contact (contactId, customerEmail, phoneOne, phoneTwo, preferredContact, alternateContact) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contact.getContactId());
            pstmt.setString(2, contact.getCustomerEmail());
            pstmt.setString(3, contact.getPhoneOne());
            pstmt.setString(4, contact.getPhoneTwo());
            pstmt.setString(5, contact.getPreferredContact());
            pstmt.setString(6, contact.getAlternateContact());
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
        String sql = "UPDATE customer_contact SET customerEmail = ?, phoneOne = ?, phoneTwo = ?, preferredContact = ?, alternateContact = ? WHERE contactId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.getCustomerEmail());
            pstmt.setString(2, contact.getPhoneOne());
            pstmt.setString(3, contact.getPhoneTwo());
            pstmt.setString(4, contact.getPreferredContact());
            pstmt.setString(5, contact.getAlternateContact());
            pstmt.setInt(6, contact.getContactId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
