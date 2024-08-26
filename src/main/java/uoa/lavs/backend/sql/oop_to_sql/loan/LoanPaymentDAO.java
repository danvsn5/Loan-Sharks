package uoa.lavs.backend.sql.oop_to_sql.loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import uoa.lavs.backend.oop.loan.LoanPayment;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.oop_to_sql.AbstractDAO;

public class LoanPaymentDAO extends AbstractDAO {

  // Adds a loan payment to the database
  public void addLoanPayment(LoanPayment payment) {
    String sql =
        "INSERT INTO loan_payment (loanId, paymentId, compounding, paymentFrequency, paymentAmount,"
            + " interestOnly) VALUES (?, ?, ?, ?, ?, ?)";
    add(payment, sql);
  }

  // Helper to add a loan payment to the database
  public void add(LoanPayment payment, String sql) {
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      int nextPaymentId = getNextPaymentIdForLoan(payment.getLoanId());

      pstmt.setString(1, payment.getLoanId());
      pstmt.setInt(2, nextPaymentId);

      pstmt.setString(3, payment.getCompounding());
      pstmt.setString(4, payment.getPaymentFrequency());
      pstmt.setString(5, payment.getPaymentAmount());
      pstmt.setBoolean(6, payment.getInterestOnly());

      pstmt.executeUpdate();

      payment.setPaymentId(nextPaymentId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Get the next paymentId for a loan when adding a new payment
  private int getNextPaymentIdForLoan(String loanId) {
    String sql = "SELECT MAX(paymentId) FROM loan_payment WHERE loanId = ?";
    return getNextId(loanId, sql);
  }

  // Updates a loan payment in the database with new details from the payment object
  public void updateLoanPayment(LoanPayment payment) {
    String sql =
        "UPDATE loan_payment SET compounding = ?, paymentFrequency = ?, paymentAmount = ?,"
            + " interestOnly = ?, lastModified = CURRENT_TIMESTAMP WHERE loanId = ?";
    update(payment, sql);
  }

  // Helper to update a loan payment in the database
  public void update(LoanPayment payment, String sql) {
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, payment.getCompounding());
      pstmt.setString(2, payment.getPaymentFrequency());
      pstmt.setString(3, payment.getPaymentAmount());
      pstmt.setBoolean(4, payment.getInterestOnly());
      pstmt.setString(5, payment.getLoanId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Get a loan payment from the database using the loanId
  public LoanPayment getLoanPayment(String loanId) {
    String sql = "SELECT * FROM loan_payment WHERE loanId = ?";
    return get(loanId, sql);  
  }

  // Helper to get a loan payment from the database
  public LoanPayment get(String loanId, String sql) {
    LoanPayment payment = null;

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loanId);
      var rs = pstmt.executeQuery();

      if (rs.next()) {
        int paymentId = rs.getInt("paymentId");
        String compounding = rs.getString("compounding");
        String paymentFrequency = rs.getString("paymentFrequency");
        String paymentAmount = rs.getString("paymentAmount");
        boolean interestOnly = rs.getBoolean("interestOnly");

        payment =
            new LoanPayment(loanId, compounding, paymentFrequency, paymentAmount, interestOnly);
        payment.setPaymentId(paymentId);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return payment;
  }
}
