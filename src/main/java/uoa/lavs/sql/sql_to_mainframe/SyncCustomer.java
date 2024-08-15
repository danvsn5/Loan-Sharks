package uoa.lavs.sql.sql_to_mainframe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import uoa.lavs.mainframe.messages.customer.UpdateCustomer;

public class SyncCustomer {

  public SyncCustomer() {}

  private UpdateCustomer updateCustomer(ResultSet resultSet)
      throws SQLException {
    UpdateCustomer updateCustomer = new UpdateCustomer();
    updateCustomer.setCustomerId(resultSet.getString("customer_id"));
    updateCustomer.setTitle(resultSet.getString("title"));
    updateCustomer.setName(resultSet.getString("name"));
    updateCustomer.setDateofBirth(resultSet.getObject("date_of_birth", LocalDate.class));
    updateCustomer.setOccupation(resultSet.getString("occupation"));
    updateCustomer.setCitizenship(resultSet.getString("citizenship"));
    updateCustomer.setVisa(resultSet.getString("visa"));
    return updateCustomer;
  }

  public static void main(String[] args) {}
}
