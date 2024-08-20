package uoa.lavs.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.DatabaseConnection;

public class PhoneDAO {

  public void addPhone(Phone phone) {
    String customerId = phone.getCustomerId();
    // Find the next phoneId for this customerId
    int nextPhoneId = getNextPhoneIdForCustomer(customerId);

    String sql =
        "INSERT INTO customer_phone (customerId, phoneId, type, phoneNumber, isPrimary,"
            + " canSendText) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      pstmt.setInt(2, nextPhoneId);
      pstmt.setString(3, phone.getType());
      pstmt.setString(4, phone.getPhoneNumber());
      pstmt.setBoolean(5, phone.getIsPrimary());
      pstmt.setBoolean(6, phone.getCanSendText());

      pstmt.executeUpdate();

      phone.setPhoneId(nextPhoneId);
      phone.setCustomerId(customerId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void addPhones(ArrayList<Phone> phones) {
    String customerId = phones.get(0).getCustomerId();

    for (int i = 0; i < phones.size(); i++) {
      String sql =
          "INSERT INTO customer_phones (customerId, phoneId, type, phoneNumber, isPrimary,"
              + " canSendText) VALUES (?, ?, ?, ?, ?, ?)";
      try (Connection conn = DatabaseConnection.connect();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {

        int phoneId = getNextPhoneIdForCustomer(customerId);

        pstmt.setString(1, customerId);
        pstmt.setInt(2, phoneId);
        pstmt.setString(3, phones.get(i).getType());
        pstmt.setString(4, phones.get(i).getPhoneNumber());
        pstmt.setBoolean(5, phones.get(i).getIsPrimary());
        pstmt.setBoolean(6, phones.get(i).getCanSendText());

        pstmt.executeUpdate();

        phones.get(i).setPhoneId(phoneId);
        phones.get(i).setCustomerId(customerId);
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private int getNextPhoneIdForCustomer(String customerId) {
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

  public void updatePhone(Phone phone) {
    String sql =
        "UPDATE customer_phone SET type = ?, phoneNumber = ?, isPrimary = ?, canSendText = ?,"
            + " lastModified = CURRENT_TIMESTAMP WHERE customerId = ? AND phoneId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, phone.getType());
      pstmt.setString(2, phone.getPhoneNumber());
      pstmt.setBoolean(3, phone.getIsPrimary());
      pstmt.setBoolean(4, phone.getCanSendText());
      pstmt.setString(5, phone.getCustomerId());
      pstmt.setInt(6, phone.getPhoneId());

      System.out.println("Updating phone: " + phone.getPhoneId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public Phone getPhone(String customerId, int phoneId) {
    String sql = "SELECT * FROM customer_phone WHERE customerId = ? AND phoneId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      pstmt.setInt(2, phoneId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        String type = rs.getString("type");
        String phoneNumber = rs.getString("phoneNumber");
        boolean isPrimary = rs.getBoolean("isPrimary");
        boolean canSendText = rs.getBoolean("canSendText");

        Phone retrievedPhone = new Phone(customerId, type, phoneNumber, isPrimary, canSendText);
        retrievedPhone.setPhoneId(phoneId);
        return retrievedPhone;
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

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
        String phoneNumber = rs.getString("phoneNumber");
        boolean isPrimary = rs.getBoolean("isPrimary");
        boolean canSendText = rs.getBoolean("canSendText");

        Phone retrievedPhone = new Phone(customerId, type, phoneNumber, isPrimary, canSendText);
        retrievedPhone.setPhoneId(phoneId);

        phones.add(retrievedPhone);
      }
      return phones;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public static void main(String[] args) {
    PhoneDAO phoneDAO = new PhoneDAO();
    Phone phone = phoneDAO.getPhone("000001", 1);
    phone.setType("Mobile");
    phoneDAO.updatePhone(phone);
  }
}
