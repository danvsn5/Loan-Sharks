package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerEmployers;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomerEmployer;

public class SyncEmployer extends Sync {
  @Override
  protected Status syncMainframeData(
      ResultSet resultSet, uoa.lavs.legacy.mainframe.Connection connection, java.sql.Connection localConn)
      throws SQLException, IOException {
    String customer_id = resultSet.getString("customerId");
    LoadCustomerEmployers loadCustomerEmployers = new LoadCustomerEmployers();
    loadCustomerEmployers.setCustomerId(customer_id);
    loadCustomerEmployers.send(connection);

    Integer count = loadCustomerEmployers.getCountFromServer();

    UpdateCustomerEmployer updateCustomerEmployer = updateCustomerEmployer(resultSet, customer_id);
    if (count == null || count == 0) {
      System.out.println("Employer not found in mainframe. Creating new employer.");
      updateCustomerEmployer.setNumber(null);
    } else {
      updateCustomerEmployer.setNumber(1);
    }

    Status status = updateCustomerEmployer.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Employer updated successfully.");
    } else {
      System.out.println("Error dinging employer: " + status.getErrorMessage());
    }

    return status;
  }

  private UpdateCustomerEmployer updateCustomerEmployer(ResultSet resultSet, String customer_id) {
    UpdateCustomerEmployer updateCustomerEmployer = new UpdateCustomerEmployer();
    try {
      updateCustomerEmployer.setCustomerId(customer_id);
      updateCustomerEmployer.setName(resultSet.getString("employerName"));
      updateCustomerEmployer.setLine1(resultSet.getString("addressLineOne"));
      updateCustomerEmployer.setLine2(resultSet.getString("addressLineTwo"));
      updateCustomerEmployer.setSuburb(resultSet.getString("suburb"));
      updateCustomerEmployer.setPostCode(resultSet.getString("postCode"));
      updateCustomerEmployer.setCity(resultSet.getString("city"));
      updateCustomerEmployer.setCountry(resultSet.getString("country"));
      updateCustomerEmployer.setEmailAddress(resultSet.getString("employerEmail"));
      updateCustomerEmployer.setWebsite(resultSet.getString("employerWebsite"));
      updateCustomerEmployer.setPhoneNumber(resultSet.getString("employerPhone"));
      updateCustomerEmployer.setIsOwner(resultSet.getBoolean("ownerOfCompany"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return updateCustomerEmployer;
  }

  @Override
  protected String getSqlQuery() {
    return "SELECT * FROM customer_employer WHERE lastModified > ?";
  }
}
