package uoa.lavs.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.customer.FindCustomerEmail;
import uoa.lavs.mainframe.messages.customer.UpdateCustomerEmail;

public class SyncEmail extends Sync {
  @Override
  protected void syncMainframeData(
      ResultSet resultSet, uoa.lavs.mainframe.Connection connection, java.sql.Connection localConn)
      throws SQLException, IOException {
    String customer_id = resultSet.getString("customerId");
    Integer email_id = resultSet.getInt("emailId");
    FindCustomerEmail findCustomerEmail = new FindCustomerEmail();
    findCustomerEmail.setCustomerId(customer_id);
    findCustomerEmail.send(connection);

    email_id = findCustomerEmail.getNumberFromServer(email_id);

    UpdateCustomerEmail updateCustomerEmail = updateCustomerEmail(resultSet, customer_id);

    if (email_id == null) {
      System.out.println("Email not found in mainframe. Creating new email.");
    } else {
      System.out.println("EmailID from mainframe: " + email_id);
      updateCustomerEmail.setNumber(email_id);
    }

    Status status = updateCustomerEmail.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Email updated successfully.");
    } else {
      System.out.println("Error updating email: " + status.getErrorMessage());
    }
  }

  @Override
  protected String getSqlQuery() {
    return "SELECT * FROM customer_email WHERE lastModified > ?";
  }

  // update email
  private UpdateCustomerEmail updateCustomerEmail(ResultSet resultSet, String customerId) {
    UpdateCustomerEmail updateCustomerEmail = new UpdateCustomerEmail();
    try {
      updateCustomerEmail.setCustomerId(customerId);
      updateCustomerEmail.setAddress(resultSet.getString("emailAddress"));
      updateCustomerEmail.setIsPrimary(resultSet.getBoolean("isPrimary"));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return updateCustomerEmail;
  }
}
