package uoa.lavs.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.customer.FindCustomer;
import uoa.lavs.mainframe.messages.customer.UpdateCustomer;
import uoa.lavs.sql.DatabaseConnection;

public class SyncCustomer extends Sync {
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * Syncs the customer records from the local database to the mainframe.
   * @param lastSyncTime
   * @throws IOException
   */
  @Override
  public void syncToMainframe(LocalDateTime lastSyncTime) throws IOException {
    uoa.lavs.mainframe.Connection connection = Instance.getConnection();
    String formattedLastSyncTime = lastSyncTime.format(FORMATTER);
    System.out.println("Last sync time: " + formattedLastSyncTime);

    // Updated SQL query to fetch only records where lastModified > lastSyncTime
    String sql = "SELECT * FROM customer WHERE lastModified > ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Set the lastSyncTime parameter in the query
      pstmt.setString(1, formattedLastSyncTime);
      ResultSet resultSet = pstmt.executeQuery();

      // If there are no new records since the last sync, exit the sync process
      if (!resultSet.isBeforeFirst()) {
        System.out.println("No new records since last sync. Exiting...");
        return;
      }

      while (resultSet.next()) {
        String lastModified = resultSet.getString("lastModified");
        System.out.println("lastModified (from DB): " + lastModified);

        // Get the customer ID we are handling, and set it as the active customer ID
        String customer_id = resultSet.getString("customerId");
        FindCustomer findCustomer = new FindCustomer();
        findCustomer.setCustomerId(customer_id);
        findCustomer.send(connection);

        // If the customer is not found in the mainframe, create a new customer by setting the ID to null.
        if (findCustomer.getCustomerCountFromServer() == 0) {
          System.out.println("Customer not found in mainframe. Creating new customer.");
          customer_id = null;
        }

        // Update (or create if customer_id = null) the customer in the mainframe
        UpdateCustomer updateCustomer = updateCustomer(resultSet, customer_id);

        Status status = updateCustomer.send(connection);

        // Retrieve new customer ID from the mainframe if it was created
        if (customer_id == null) {
          customer_id = updateCustomer.getCustomerIdFromServer();
          System.out.println("New customer ID from mainframe: " + customer_id);
        }

        // Update local database with the new customer ID if it was created
        if (customer_id != null) {
          updateCustomerIdInLocalDB(resultSet.getString("customerId"), customer_id, conn);
        }

        // Print the status of the update
        if (status.getErrorCode() == 0) {
          System.out.println(
              "Successfully sent customer ID: " + updateCustomer.getCustomerIdFromServer());
          System.out.println(updateCustomer.getTitleFromServer());
          System.out.println(updateCustomer.getNameFromServer());
          System.out.println(updateCustomer.getDateofBirthFromServer());
          System.out.println(updateCustomer.getOccupationFromServer());
          System.out.println(updateCustomer.getCitizenshipFromServer());
        } else {
          System.out.println(
              "Failed to send customer ID: " + updateCustomer.getCustomerIdFromServer());
        }
      }

      // Update the last sync time after the sync is successful
      LocalDateTime newSyncTime = LocalDateTime.now(ZoneOffset.UTC);
      updateLastSyncTimeInDB(newSyncTime);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } finally {
      connection.close();
    }
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
   * Updates the customer ID in the local database. This ensures that the ID for the customer 
   * in the local DB is the same as the one used by the mainframe.
   * 
   * @param oldCustomerId The old customer ID
   * @param newCustomerId The new customer ID
   * @param conn The connection to the local database
   * @throws SQLException
   */
  private void updateCustomerIdInLocalDB(
      String oldCustomerId, String newCustomerId, Connection conn) throws SQLException {
    String updateSql = "UPDATE customer SET customerId = ? WHERE customerId = ?";
    try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
      updatePstmt.setString(1, newCustomerId);
      updatePstmt.setString(2, oldCustomerId);
      int rowsAffected = updatePstmt.executeUpdate();
      if (rowsAffected > 0) {
        System.out.println("Local database updated with new customer ID: " + newCustomerId);
      } else {
        System.out.println("No records updated in local database.");
      }
    }
  }

  public static void main(String[] args) throws IOException {
    SyncCustomer syncCustomer = new SyncCustomer();
    LocalDateTime lastSyncTime = syncCustomer.getLastSyncTimeFromDB();

    if (lastSyncTime == null) {
      System.out.println("No last sync time found. Syncing all records.");
      lastSyncTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(1);
    }

    syncCustomer.syncToMainframe(lastSyncTime);
  }
}
