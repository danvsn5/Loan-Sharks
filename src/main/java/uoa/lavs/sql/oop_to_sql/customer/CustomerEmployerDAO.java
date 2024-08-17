package uoa.lavs.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerEmployerDAO {

  public void addCustomerEmployer(CustomerEmployer employer) {
    String sql =
        "INSERT INTO customer_employer (employerName, employerAddressId, employerEmail,"
            + " employerWebsite, employerPhone, ownerOfCompany) VALUES (?, ?, ?, ?, ?, ?)";
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

  public void updateCustomerEmployer(CustomerEmployer employer) {
    String sql =
        "UPDATE customer_employer SET employerName = ?, employerAddressId = ?, employerEmail = ?,"
            + " employerWebsite = ?, employerPhone = ?, ownerOfCompany = ? WHERE employerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, employer.getEmployerName());
      pstmt.setInt(2, employer.getEmployerAddress().getAddressId());
      pstmt.setString(3, employer.getEmployerEmail());
      pstmt.setString(4, employer.getEmployerWebsite());
      pstmt.setString(5, employer.getEmployerPhone());
      pstmt.setBoolean(6, employer.getOwnerOfCompany());
      pstmt.setInt(7, employer.getEmployerId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public CustomerEmployer getCustomerEmployer(int employerId) {
    String sql = "SELECT * FROM customer_employer WHERE employerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, employerId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        String employerName = rs.getString("employerName");
        int employerAddressId = rs.getInt("employerAddressId");
        String employerEmail = rs.getString("employerEmail");
        String employerWebsite = rs.getString("employerWebsite");
        String employerPhone = rs.getString("employerPhone");
        boolean ownerOfCompany = rs.getBoolean("ownerOfCompany");

        AddressDAO addressdao = new AddressDAO();
        Address employerAddress = addressdao.getAddress("000001", employerAddressId);

        CustomerEmployer customerEmployer =
            new CustomerEmployer(
                employerName,
                employerAddress,
                employerEmail,
                employerWebsite,
                employerPhone,
                ownerOfCompany);
        customerEmployer.setEmployerId(employerId);
        return customerEmployer;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
