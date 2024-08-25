package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.FindCustomerAddress;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomerAddress;

public class SyncAddress extends Sync {
  @Override
  protected Status syncMainframeData(
      ResultSet resultSet, uoa.lavs.legacy.mainframe.Connection connection, java.sql.Connection localConn)
      throws SQLException, IOException {
    String customer_id = resultSet.getString("customerId");
    Integer address_id = resultSet.getInt("addressId");
    FindCustomerAddress findCustomerAddress = new FindCustomerAddress();
    findCustomerAddress.setCustomerId(customer_id);
    findCustomerAddress.send(connection);

    address_id = findCustomerAddress.getNumberFromServer(address_id);

    UpdateCustomerAddress updateCustomerAddress = updateCustomerAddress(resultSet, customer_id);

    if (address_id == null) {
      System.out.println("Address not found in mainframe. Creating new address.");
    } else {
      System.out.println("AddressID from mainframe: " + address_id);
    }

    updateCustomerAddress.setNumber(address_id);

    Status status = updateCustomerAddress.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Address updated successfully.");
    } else {
      System.out.println("Error updating address: " + status.getErrorMessage());
    }

    return status;
  }

  @Override
  protected String getSqlQuery() {
    return "SELECT * FROM customer_address WHERE lastModified > ?";
  }

  // update address
  private UpdateCustomerAddress updateCustomerAddress(ResultSet resultSet, String customerId) {
    UpdateCustomerAddress updateCustomerAddress = new UpdateCustomerAddress();
    try {
      updateCustomerAddress.setCustomerId(customerId);
      updateCustomerAddress.setType(resultSet.getString("addressType"));
      updateCustomerAddress.setLine1(resultSet.getString("addressLineOne"));
      updateCustomerAddress.setLine2(resultSet.getString("addressLineTwo"));
      updateCustomerAddress.setSuburb(resultSet.getString("suburb"));
      updateCustomerAddress.setPostCode(resultSet.getString("postCode"));
      updateCustomerAddress.setCity(resultSet.getString("city"));
      updateCustomerAddress.setCountry(resultSet.getString("country"));
      updateCustomerAddress.setIsPrimary(resultSet.getBoolean("isPrimary"));
      updateCustomerAddress.setIsMailing(resultSet.getBoolean("isMailing"));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return updateCustomerAddress;
  }
}
