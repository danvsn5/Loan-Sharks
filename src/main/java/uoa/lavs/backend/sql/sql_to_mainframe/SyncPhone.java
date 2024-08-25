package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.FindCustomerPhoneNumber;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomerPhoneNumber;

public class SyncPhone extends Sync {
  @Override
  protected Status syncMainframeData(
      ResultSet resultSet, uoa.lavs.legacy.mainframe.Connection connection, java.sql.Connection localConn)
      throws SQLException, IOException {
    String customer_id = resultSet.getString("customerId");
    Integer phone_id = resultSet.getInt("phoneId");
    FindCustomerPhoneNumber findCustomerPhone = new FindCustomerPhoneNumber();
    findCustomerPhone.setCustomerId(customer_id);
    findCustomerPhone.send(connection);

    phone_id = findCustomerPhone.getNumberFromServer(phone_id);

    UpdateCustomerPhoneNumber updateCustomerPhone = updateCustomerPhone(resultSet, customer_id);

    if (phone_id == null) {
      System.out.println("Phone number not found in mainframe. Creating new phone number.");
    } else {
      System.out.println("PhoneID from mainframe: " + phone_id);
      updateCustomerPhone.setNumber(phone_id);
    }

    Status status = updateCustomerPhone.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Phone number updated successfully.");
    } else {
      System.out.println("Error updating phone number: " + status.getErrorMessage());
    }

    return status;
  }

  @Override
  protected String getSqlQuery() {
    return "SELECT * FROM customer_phone WHERE lastModified > ?";
  }

  // update address
  private UpdateCustomerPhoneNumber updateCustomerPhone(ResultSet resultSet, String customerId) {
    UpdateCustomerPhoneNumber updateCustomerPhone = new UpdateCustomerPhoneNumber();
    try {
      // while (resultSet.next()) {
      updateCustomerPhone.setCustomerId(customerId);
      updateCustomerPhone.setType(resultSet.getString("type"));
      updateCustomerPhone.setPrefix(resultSet.getString("prefix"));
      updateCustomerPhone.setPhoneNumber(resultSet.getString("phoneNumber"));
      updateCustomerPhone.setIsPrimary(resultSet.getBoolean("isPrimary"));
      updateCustomerPhone.setCanSendTxt(resultSet.getBoolean("canSendText"));
      // }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return updateCustomerPhone;
  }
}
