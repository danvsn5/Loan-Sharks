package uoa.lavs.sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerEmployerDAO {

  public void addCustomerEmployer(CustomerEmployer employer) {
      String sql = "INSERT INTO customer_employer (employerName, employerAddressId, employerEmail, employerWebsite, employerPhone, ownerOfCompany) VALUES (?, ?, ?, ?, ?, ?)";
      try (Connection conn = DatabaseConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

          pstmt.setString(1, employer.getEmployerName());
          pstmt.setInt(2, employer.getEmployerAddress().getAddressId());
          pstmt.setString(3, employer.getEmployerEmail());
          pstmt.setString(4, employer.getEmployerWebsite());
          pstmt.setString(5, employer.getEmployerPhone());
          pstmt.setBoolean(6, employer.getOwnerOfCompany());

          pstmt.executeUpdate();

          try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
              if (generatedKeys.next()) {
                  int generatedId = generatedKeys.getInt(1);
                  employer.setEmployerId(generatedId);
              }
          }
      } catch (SQLException e) {
          System.out.println(e.getMessage());
      }
  }
}
