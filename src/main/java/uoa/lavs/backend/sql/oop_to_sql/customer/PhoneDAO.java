package uoa.lavs.backend.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.backend.oop.customer.Phone;
import uoa.lavs.backend.sql.DatabaseConnection;

public class PhoneDAO extends AbstractDAO{

  // Adds a phone to the database
  public void addPhone(Phone phone) {
    String customerId = phone.getCustomerId();
    // Find the next phoneId for this customerId
    int nextPhoneId = getNextPhoneIdForCustomer(customerId);

    String sql =
        "INSERT INTO customer_phone (customerId, phoneId, type, prefix, phoneNumber, isPrimary,"
            + " canSendText) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      pstmt.setInt(2, nextPhoneId);
      pstmt.setString(3, phone.getType());
      pstmt.setString(4, phone.getPrefix());
      pstmt.setString(5, phone.getPhoneNumber());
      pstmt.setBoolean(6, phone.getIsPrimary());
      pstmt.setBoolean(7, phone.getCanSendText());

      pstmt.executeUpdate();

      phone.setPhoneId(nextPhoneId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public int getNextPhoneIdForCustomer(String customerId) {
    String sql = "SELECT MAX(phoneId) FROM customer_phone WHERE customerId = ?";
    return getNextId(customerId, sql);
  }

  // Updates a phone in the database with new details from the phone object
  public void updatePhone(Phone phone) {
    String sql =
        "UPDATE customer_phone SET type = ?, prefix = ?, phoneNumber = ?, isPrimary = ?,"
            + " canSendText = ?, lastModified = CURRENT_TIMESTAMP WHERE customerId = ? AND phoneId"
            + " = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, phone.getType());
      pstmt.setString(2, phone.getPrefix());
      pstmt.setString(3, phone.getPhoneNumber());
      pstmt.setBoolean(4, phone.getIsPrimary());
      pstmt.setBoolean(5, phone.getCanSendText());
      pstmt.setString(6, phone.getCustomerId());
      pstmt.setInt(7, phone.getPhoneId());

      System.out.println("Updating phone: " + phone.getPhoneId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Retrieves a phone from the database with the given customerId and phoneId
  public Phone getPhone(String customerId, int phoneId) {
    Phone retrievedPhone = null;
    ArrayList<Phone> phones = getPhones(customerId);
    for (Phone phone : phones) {
      if (phone.getPhoneId() == phoneId) {
        retrievedPhone = phone;
        break;
      }
    }
    return retrievedPhone;
  }

  // Retrieves all phones for a customer from the database
  public ArrayList<Phone> getPhones(String customerId) {
    ArrayList<Phone> phones = new ArrayList<>();

    String sql = "SELECT * FROM customer_phone WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        int phoneId = rs.getInt("phoneId");
        String type = rs.getString("type");
        String prefix = rs.getString("prefix");
        String phoneNumber = rs.getString("phoneNumber");
        boolean isPrimary = rs.getBoolean("isPrimary");
        boolean canSendText = rs.getBoolean("canSendText");

        Phone retrievedPhone =
            new Phone(customerId, type, prefix, phoneNumber, isPrimary, canSendText);
        retrievedPhone.setPhoneId(phoneId);

        phones.add(retrievedPhone);
      }
      return phones;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
