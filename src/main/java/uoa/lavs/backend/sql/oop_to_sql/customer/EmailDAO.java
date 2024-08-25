package uoa.lavs.backend.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import uoa.lavs.backend.oop.customer.Email;
import uoa.lavs.backend.sql.DatabaseConnection;

public class EmailDAO {
  public void addEmail(Email email) {
    String customerId = email.getCustomerId();
    // Find the next emailId for this customerId
    int nextEmailId = getNextEmailIdForCustomer(customerId);

    String sql =
        "INSERT INTO customer_email (customerId, emailId, emailAddress, isPrimary)"
            + " VALUES (?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      pstmt.setInt(2, nextEmailId);
      pstmt.setString(3, email.getEmailAddress());
      pstmt.setBoolean(4, email.getIsPrimary());

      pstmt.executeUpdate();

      email.setEmailId(nextEmailId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public int getNextEmailIdForCustomer(String customerId) {
    String sql = "SELECT MAX(emailId) FROM customer_email WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        int maxEmailId = rs.getInt(1);
        return maxEmailId + 1;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return 1;
  }

  public void updateEmail(Email email) {
    String sql =
        "UPDATE customer_email SET emailAddress = ?, isPrimary = ?,"
            + " lastModified = CURRENT_TIMESTAMP WHERE customerId = ? AND emailId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, email.getEmailAddress());
      pstmt.setBoolean(2, email.getIsPrimary());
      pstmt.setString(3, email.getCustomerId());
      pstmt.setInt(4, email.getEmailId());

      System.out.println("Updating email: " + email.getEmailId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public Email getEmail(String customerId, int emailId) {
    String sql = "SELECT * FROM customer_email WHERE customerId = ? AND emailId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      pstmt.setInt(2, emailId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        String emailAddress = rs.getString("emailAddress");
        boolean isPrimary = rs.getBoolean("isPrimary");

        Email retrievedEmail = new Email(customerId, emailAddress, isPrimary);
        retrievedEmail.setEmailId(emailId);
        return retrievedEmail;
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public ArrayList<Email> getEmails(String customerId) {
    ArrayList<Email> emails = new ArrayList<>();

    String sql = "SELECT * FROM customer_email WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        int emailId = rs.getInt("emailId");
        String emailAddress = rs.getString("emailAddress");
        boolean isPrimary = rs.getBoolean("isPrimary");

        Email retrievedEmail = new Email(customerId, emailAddress, isPrimary);
        retrievedEmail.setEmailId(emailId);

        emails.add(retrievedEmail);
      }
      return emails;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return emails;
  }
}
