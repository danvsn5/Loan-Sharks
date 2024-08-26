package uoa.lavs.backend.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.backend.oop.customer.Phone;
import uoa.lavs.backend.sql.DatabaseConnection;

public class PhoneDAO {

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

  // Adds multiple phone numbers to the database for a customer
  public void addPhones(ArrayList<Phone> phones) {
    String customerId = phones.get(0).getCustomerId();

    for (int i = 0; i < phones.size(); i++) {
      String sql =
          "INSERT INTO customer_phone (customerId, phoneId, type, prefix, phoneNumber, isPrimary,"
              + " canSendText) VALUES (?, ?, ?, ?, ?, ?, ?)";
      try (Connection conn = DatabaseConnection.connect();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {

        int phoneId = getNextPhoneIdForCustomer(customerId);

        pstmt.setString(1, customerId);
        pstmt.setInt(2, phoneId);
        pstmt.setString(3, phones.get(i).getType());
        pstmt.setString(4, phones.get(i).getPrefix());
        pstmt.setString(5, phones.get(i).getPhoneNumber());
        pstmt.setBoolean(6, phones.get(i).getIsPrimary());
        pstmt.setBoolean(7, phones.get(i).getCanSendText());

        pstmt.executeUpdate();

        phones.get(i).setPhoneId(phoneId);
        phones.get(i).setCustomerId(customerId);
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  // Finds the next phoneId for a customer to use when adding a new phone
  public int getNextPhoneIdForCustomer(String customerId) {
    String sql = "SELECT MAX(phoneId) FROM customer_phone WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        int maxPhoneId = rs.getInt(1);
        return maxPhoneId + 1;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return 1;
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
    String sql = "SELECT * FROM customer_phone WHERE customerId = ? AND phoneId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      pstmt.setInt(2, phoneId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        String type = rs.getString("type");
        String prefix = rs.getString("prefix");
        String phoneNumber = rs.getString("phoneNumber");
        boolean isPrimary = rs.getBoolean("isPrimary");
        boolean canSendText = rs.getBoolean("canSendText");

        Phone retrievedPhone =
            new Phone(customerId, type, prefix, phoneNumber, isPrimary, canSendText);
        retrievedPhone.setPhoneId(phoneId);
        return retrievedPhone;
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
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
