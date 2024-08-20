package uoa.lavs.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerEmployerDAO {

  public void addCustomerEmployer(CustomerEmployer employer) {
    String sql =
        "INSERT INTO customer_employer (customerId, employerName, addressLineOne, addressLineTwo, suburb,"
            + " postCode, city, country, employerEmail, employerWebsite, employerPhone, ownerOfCompany) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
      System.out.println("New employer added for customer ID: " + employer.getCustomerId());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateCustomerEmployer(CustomerEmployer employer) {
    String sql =
        "UPDATE customer_employer SET employerName = ?, addressLineOne = ?, addressLineTwo = ?,"
            + " suburb = ?, postCode = ?, city = ?, country = ?, employerEmail = ?, employerWebsite"
            + " = ?, employerPhone = ?, ownerOfCompany = ?, lastModified = CURRENT_TIMESTAMP"
            + " WHERE customerId = ?";
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
      pstmt.setString(12, employer.getCustomerId());

      pstmt.executeUpdate();
      System.out.println("Employer updated for customer ID: " + employer.getCustomerId());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public CustomerEmployer getCustomerEmployer(String customerId) {
    String sql = "SELECT * FROM customer_employer WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
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
        return customerEmployer;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public static void main(String[] args) {
    CustomerEmployerDAO employerDAO = new CustomerEmployerDAO();

    // Test saving a new employer
    CustomerEmployer newEmployer = new CustomerEmployer(
        "000001", "Tech Corp", "123 Tech Street", "Suite 100", "Techville", "12345", "Metropolis",
        "Countryland", "contact@techcorp.com", "www.techcorp.com", "123-456-7890", true);
    employerDAO.addCustomerEmployer(newEmployer);

    // Test updating the existing employer
    newEmployer.setEmployerName("New Tech Corp");
    employerDAO.updateCustomerEmployer(newEmployer);
  }
}