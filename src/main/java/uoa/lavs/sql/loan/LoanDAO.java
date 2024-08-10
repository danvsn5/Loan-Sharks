package uoa.lavs.sql.loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import uoa.lavs.loan.ILoan;
import uoa.lavs.sql.DatabaseConnection;

public class LoanDAO {
  public void addLoan(ILoan loan) {
    String sql =
        "INSERT INTO loan (loanId, customerId, principal, rate, durationId, paymentId) VALUES (?,"
            + " ?, ?, ?, ?, ?)";
    DatabaseConnection connection = new DatabaseConnection();
    try (Connection conn = connection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, loan.getLoanId());
      pstmt.setString(2, loan.getCustomerId());
      pstmt.setDouble(3, loan.getPrincipal());
      pstmt.setDouble(4, loan.getRate());
      pstmt.setInt(5, loan.getDuration().getDurationId());
      pstmt.setInt(6, loan.getPayment().getPaymentId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateLoan(ILoan loan) {
    String sql =
        "UPDATE loan SET customerId = ?, principal = ?, rate = ?, durationId = ?, paymentId = ?"
            + " WHERE loanId = ?";
    DatabaseConnection connection = new DatabaseConnection();
    try (Connection conn = connection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loan.getCustomerId());
      pstmt.setDouble(2, loan.getPrincipal());
      pstmt.setDouble(3, loan.getRate());
      pstmt.setInt(4, loan.getDuration().getDurationId());
      pstmt.setInt(5, loan.getPayment().getPaymentId());
      pstmt.setInt(6, loan.getLoanId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
