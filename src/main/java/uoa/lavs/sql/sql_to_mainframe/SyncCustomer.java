package uoa.lavs.sql.sql_to_mainframe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.customer.FindCustomer;
import uoa.lavs.mainframe.messages.customer.UpdateCustomer;
import uoa.lavs.sql.DatabaseConnection;

public class SyncCustomer {

  public SyncCustomer() {}

      public void syncToMainframe(LocalDateTime lastSyncTime) throws SQLException {
        uoa.lavs.mainframe.Connection connection = Instance.getConnection();
        // Fetch new or updated records since the last sync
        String sql = "SELECT * FROM customer WHERE last_modified > ?";
            try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setString(1, lastSyncTime.toString());
            ResultSet resultSet = pstmt.executeQuery();
            System.out.println(resultSet.getFetchSize());

            while (resultSet.next()) {
                String customer_id = resultSet.getString("customer_id");
                FindCustomer findCustomer = new FindCustomer();
                findCustomer.setCustomerId(customer_id);
                if (findCustomer.getCustomerCountFromServer() == 0) {
                  customer_id = null;
                }
      
                UpdateCustomer updateCustomer = updateCustomer(resultSet, customer_id);

                // Send the update to the mainframe
                Status status = updateCustomer.send(connection);
                if (status.getErrorCode() == 0) {
                    System.out.println("Successfully sent customer ID: " + updateCustomer.getCustomerIdFromServer());
                } else {
                    System.out.println("Failed to send customer ID: " + updateCustomer.getCustomerIdFromServer());
                }
            }
        }
    }

  private UpdateCustomer updateCustomer(ResultSet resultSet, String customer_id)
      throws SQLException {
    UpdateCustomer updateCustomer = new UpdateCustomer();
    updateCustomer.setCustomerId(customer_id);
    updateCustomer.setTitle(resultSet.getString("title"));
    updateCustomer.setName(resultSet.getString("name"));
    updateCustomer.setDateofBirth(resultSet.getObject("date_of_birth", LocalDate.class));
    updateCustomer.setOccupation(resultSet.getString("occupation"));
    updateCustomer.setCitizenship(resultSet.getString("residency"));
    return updateCustomer;
  }

  public static void main(String[] args) {
    SyncCustomer syncCustomer = new SyncCustomer();
    try {
      syncCustomer.syncToMainframe(LocalDateTime.now().minusDays(1));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
