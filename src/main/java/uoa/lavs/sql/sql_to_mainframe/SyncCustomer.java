package uoa.lavs.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.customer.FindCustomer;
import uoa.lavs.mainframe.messages.customer.UpdateCustomer;
import uoa.lavs.sql.DatabaseConnection;

public class SyncCustomer {

  public SyncCustomer() {}

public void syncToMainframe(LocalDateTime lastSyncTime) throws IOException {
    uoa.lavs.mainframe.Connection connection = Instance.getConnection();
    // Format lastSyncTime to match the SQLite timestamp format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedLastSyncTime = lastSyncTime.format(formatter);
    
    // Fetch new or updated records since the last sync
    String sql = "SELECT * FROM customer";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        // pstmt.setString(1, formattedLastSyncTime);
        ResultSet resultSet = pstmt.executeQuery();


        while (resultSet.next()) {
            String customer_id = resultSet.getString("customerId");
            FindCustomer findCustomer = new FindCustomer();
            findCustomer.setCustomerId(customer_id);

            findCustomer.send(connection);
            if (findCustomer.getCustomerCountFromServer() == 0) {
              System.out.println("ding");
                customer_id = null;
                
            }

            UpdateCustomer updateCustomer = updateCustomer(resultSet, customer_id);

            // Send the update to the mainframe
            Status status = updateCustomer.send(connection);
            if (status.getErrorCode() == 0) {
                System.out.println("Successfully sent customer ID: " + updateCustomer.getCustomerIdFromServer());
                // print all the details of the customer
                System.out.println(updateCustomer.getTitleFromServer());
                System.out.println(updateCustomer.getNameFromServer());
                System.out.println(updateCustomer.getDateofBirthFromServer());
                System.out.println(updateCustomer.getOccupationFromServer());
                System.out.println(updateCustomer.getCitizenshipFromServer());
            } else {
                System.out.println("Failed to send customer ID: " + updateCustomer.getCustomerIdFromServer());
            }
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    } finally {
        connection.close();
    }
}

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

  public static void main(String[] args) throws IOException {
    SyncCustomer syncCustomer = new SyncCustomer();

    syncCustomer.syncToMainframe(LocalDateTime.now().minusDays(1));

  }
}
