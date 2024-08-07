package uoa.lavs.sql.loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import uoa.lavs.loan.LoanPayment;
import uoa.lavs.sql.DatabaseConnection;

public class LoanPaymentDAO {
  public void addLoanPayment(LoanPayment payment) {
    String sql =
        "INSERT INTO loan_payment (compounding, paymentFrequency, paymentAmount, interestOnly)"
            + " VALUES (?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      pstmt.setString(1, payment.getCompounding());
      pstmt.setString(2, payment.getPaymentFrequency());
      pstmt.setString(3, payment.getPaymentAmount());
      pstmt.setBoolean(4, payment.getInterestOnly());

      try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int generatedId = generatedKeys.getInt(1);
          payment.setPaymentId(generatedId);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
