package uoa.lavs.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerEmployerDAO {

  public void addCustomerEmployer(CustomerEmployer employer) {
    String sql =
        "INSERT INTO customer_employer (customerId, employerName, addressLineOne, addressLineTwo,"
            + " suburb, postCode, city, country, employerEmail, employerWebsite, employerPhone,"
            + " ownerOfCompany) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, employer.getCustomerId());
      pstmt.setString(2, employer.getEmployerName());
      pstmt.setString(3, employer.getLineOne());
      pstmt.setString(4, employer.getLineTwo());
      pstmt.setString(5, employer.getSuburb());
      pstmt.setString(6, employer.getPostCode());
      pstmt.setString(7, employer.getCity());
      pstmt.setString(8, employer.getCountry());
      pstmt.setString(9, employer.getEmployerEmail());
      pstmt.setString(10, employer.getEmployerWebsite());
      pstmt.setString(11, employer.getEmployerPhone());
      pstmt.setBoolean(12, employer.getOwnerOfCompany());

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
        "UPDATE customer_employer SET employerName = ?, addressLineOne = ?, addressLineTwo = ?,"
            + " suburb = ?, postCode = ?, city = ?, country = ?, employerEmail = ?, employerWebsite"
            + " = ?, employerPhone = ?, ownerOfCompany = ? WHERE employerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, employer.getEmployerName());
      pstmt.setString(2, employer.getLineOne());
      pstmt.setString(3, employer.getLineTwo());
      pstmt.setString(4, employer.getSuburb());
      pstmt.setString(5, employer.getPostCode());
      pstmt.setString(6, employer.getCity());
      pstmt.setString(7, employer.getCountry());
      pstmt.setString(8, employer.getEmployerEmail());
      pstmt.setString(9, employer.getEmployerWebsite());
      pstmt.setString(10, employer.getEmployerPhone());
      pstmt.setBoolean(11, employer.getOwnerOfCompany());
      pstmt.setInt(12, employer.getEmployerId());

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
        String customerId = rs.getString("customerId");
        String employerName = rs.getString("employerName");
        String addressLineOne = rs.getString("addressLineOne");
        String addressLineTwo = rs.getString("addressLineTwo");
        String suburb = rs.getString("suburb");
        String postCode = rs.getString("postCode");
        String city = rs.getString("city");
        String country = rs.getString("country");
        String employerEmail = rs.getString("employerEmail");
        String employerWebsite = rs.getString("employerWebsite");
        String employerPhone = rs.getString("employerPhone");
        boolean ownerOfCompany = rs.getBoolean("ownerOfCompany");

        CustomerEmployer customerEmployer =
            new CustomerEmployer(
                customerId,
                employerName,
                addressLineOne,
                addressLineTwo,
                suburb,
                city,
                postCode,
                country,
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
