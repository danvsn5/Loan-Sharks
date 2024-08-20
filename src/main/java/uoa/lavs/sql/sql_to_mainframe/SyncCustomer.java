package uoa.lavs.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.customer.FindCustomer;
import uoa.lavs.mainframe.messages.customer.UpdateCustomer;

public class SyncCustomer extends Sync {

  @Override
  protected void syncMainframeData(
      ResultSet resultSet, uoa.lavs.mainframe.Connection connection, java.sql.Connection localConn)
      throws SQLException, IOException {
    String customer_id = resultSet.getString("customerId");
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer_id);
    findCustomer.send(connection);

    if (findCustomer.getCustomerCountFromServer() == 0) {
      System.out.println("Customer not found in mainframe. Creating new customer.");
      customer_id = null;
    }

    UpdateCustomer updateCustomer = updateCustomer(resultSet, customer_id);
    Status status = updateCustomer.send(connection);

    if (customer_id == null) {
      customer_id = updateCustomer.getCustomerIdFromServer();
      System.out.println("New customer ID from mainframe: " + customer_id);
    }

    if (customer_id != null) {
      System.out.print("Updating customer ID in local database... ");
      updateCustomerIdInLocalDB(resultSet.getString("customerId"), customer_id, localConn);
    }

    if (status.getErrorCode() == 0) {
      System.out.println(
          "Successfully sent customer ID: " + updateCustomer.getCustomerIdFromServer());
    } else {
      System.out.println("Failed to send customer ID: " + updateCustomer.getCustomerIdFromServer());
    }
  }

  @Override
  protected String getSqlQuery() {
    return "SELECT * FROM customer WHERE lastModified > ?";
  }

  /**
   * Updates the customer object with the values from the result set.
   *
   * @param resultSet
   * @param customer_id
   * @return
   * @throws SQLException
   */
  private UpdateCustomer updateCustomer(ResultSet resultSet, String customer_id)
      throws SQLException {
    UpdateCustomer updateCustomer = new UpdateCustomer();
    updateCustomer.setCustomerId(customer_id);
    updateCustomer.setTitle(resultSet.getString("title"));
    updateCustomer.setName(resultSet.getString("name"));
    updateCustomer.setDateofBirth(resultSet.getDate("dateOfBirth").toLocalDate());
    updateCustomer.setOccupation(resultSet.getString("occupation"));
    updateCustomer.setCitizenship(resultSet.getString("residency"));
    return updateCustomer;
  }

  /**
   * Updates the customer ID in the local database. This ensures that the ID for the customer in the
   * local DB is the same as the one used by the mainframe.
   *
   * @param oldCustomerId The old customer ID
   * @param newCustomerId The new customer ID
   * @param conn The connection to the local database
   * @throws SQLException
   */
  private void updateCustomerIdInLocalDB(
      String oldCustomerId, String newCustomerId, Connection conn) throws SQLException {
        System.out.println(oldCustomerId);
        System.out.println(newCustomerId);

    // Update customerId in customer table
    String updateCustomerSql = "UPDATE customer SET customerId = ? WHERE customerId = ?";
    try (PreparedStatement updateCustomerPstmt = conn.prepareStatement(updateCustomerSql)) {
      updateCustomerPstmt.setString(1, newCustomerId);
      updateCustomerPstmt.setString(2, oldCustomerId);
      int customerRowsAffected = updateCustomerPstmt.executeUpdate();
      if (customerRowsAffected > 0) {
        System.out.println("Customer table updated with new customer ID: " + newCustomerId);
      } else {
        System.out.println("No records updated in customer table.");
      }
    }

    // Update customerId in customer_address table
    String updateAddressSql = "UPDATE customer_address SET customerId = ? WHERE customerId = ?";
    try (PreparedStatement updateAddressPstmt = conn.prepareStatement(updateAddressSql)) {
      updateAddressPstmt.setString(1, newCustomerId);
      updateAddressPstmt.setString(2, oldCustomerId);
      int addressRowsAffected = updateAddressPstmt.executeUpdate();
      if (addressRowsAffected > 0) {
        System.out.println("Customer address table updated with new customer ID: " + newCustomerId);
      } else {
        System.out.println("No records updated in customer address table.");
      }
    }

    // Update customerId in customer_employer table
    String updateEmployerSql = "UPDATE customer_employer SET customerId = ? WHERE customerId = ?";
    try (PreparedStatement updateEmployerPstmt = conn.prepareStatement(updateEmployerSql)) {
      updateEmployerPstmt.setString(1, newCustomerId);
      updateEmployerPstmt.setString(2, oldCustomerId);
      int employerRowsAffected = updateEmployerPstmt.executeUpdate();
      if (employerRowsAffected > 0) {
        System.out.println("Customer employer table updated with new customer ID: " + newCustomerId);
      } else {
        System.out.println("No records updated in customer employer table.");
      }
    }

    /* 
    String updateNotesSql = "UPDATE customer_notes SET customerId = ? WHERE customerId = ?";
    try (PreparedStatement updateNotesPstmt = conn.prepareStatement(updateNotesSql)) {
      updateNotesPstmt.setString(1, newCustomerId);
      updateNotesPstmt.setString(2, oldCustomerId);
      int notesRowsAffected = updateNotesPstmt.executeUpdate();
      if (notesRowsAffected > 0) {
        System.out.println("Customer notes table updated with new customer ID: " + newCustomerId);
      } else {
        System.out.println("No records updated in customer notes table.");
      }
    } */

    // Update customerId in customer_phone table
    String updatePhoneSql = "UPDATE customer_phone SET customerId = ? WHERE customerId = ?";
    try (PreparedStatement updatePhonePstmt = conn.prepareStatement(updatePhoneSql)) {
      updatePhonePstmt.setString(1, newCustomerId);
      updatePhonePstmt.setString(2, oldCustomerId);
      int phoneRowsAffected = updatePhonePstmt.executeUpdate();
      if (phoneRowsAffected > 0) {
        System.out.println("Customer phone table updated with new customer ID: " + newCustomerId);
      } else {
        System.out.println("No records updated in customer phone table.");
      }
    }

    // Update customerId in customer_email table
    String updateEmailSql = "UPDATE customer_email SET customerId = ? WHERE customerId = ?";
    try (PreparedStatement updateEmailPstmt = conn.prepareStatement(updateEmailSql)) {
      updateEmailPstmt.setString(1, newCustomerId);
      updateEmailPstmt.setString(2, oldCustomerId);
      int emailRowsAffected = updateEmailPstmt.executeUpdate();
      if (emailRowsAffected > 0) {
        System.out.println("Customer email table updated with new customer ID: " + newCustomerId);
      } else {
        System.out.println("No records updated in customer email table.");
      }
    }
  }


}
