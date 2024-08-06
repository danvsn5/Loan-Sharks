package uoa.lavs.sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uoa.lavs.customer.ICustomer;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerDAO {
  public void addCustomer(ICustomer customer) {
            String sql = "INSERT INTO customer(id, title, first_name, middle_name, last_name, date_of_birth, occupation, notes) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getTitle());
            pstmt.setString(3, customer.getFirstName());
            pstmt.setString(4, customer.getMiddleName());
            pstmt.setString(5, customer.getLastName());
            pstmt.setString(6, String.valueOf(customer.getDateOfBirth()));
            pstmt.setString(7, customer.getOccupation());
            pstmt.setString(8, "");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
  }
}
