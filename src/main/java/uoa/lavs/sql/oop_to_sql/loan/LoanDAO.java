package uoa.lavs.sql.oop_to_sql.loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.loan.ILoan;
import uoa.lavs.loan.Loan;
import uoa.lavs.loan.LoanDuration;
import uoa.lavs.loan.LoanPayment;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.sql.DatabaseConnection;

public class LoanDAO {
  public void addLoan(ILoan loan) {
    String sql = "INSERT INTO loan (loanId, customerId, principal, rate) VALUES (?," + " ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, loan.getLoanId());
      pstmt.setString(2, loan.getCustomerId());
      pstmt.setDouble(3, loan.getPrincipal());
      pstmt.setDouble(4, loan.getRate());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateLoan(ILoan loan) {
    String sql = "UPDATE loan SET customerId = ?, principal = ?, rate = ?" + " WHERE loanId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loan.getCustomerId());
      pstmt.setDouble(2, loan.getPrincipal());
      pstmt.setDouble(3, loan.getRate());
      pstmt.setInt(4, loan.getLoanId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public Loan getLoan(int loanId) {
    String sql = "SELECT * FROM loan WHERE loanId = ?";

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, loanId);
      var rs = pstmt.executeQuery();

      if (rs.next()) {
        String customerId = rs.getString("customerId");
        Double principal = rs.getDouble("principal");
        Double rate = rs.getDouble("rate");

        LoanDurationDAO durationDAO = new LoanDurationDAO();
        LoanDuration duration = durationDAO.getLoanDuration(loanId);

        LoanPaymentDAO paymentDAO = new LoanPaymentDAO();
        LoanPayment payment = paymentDAO.getLoanPayment(loanId);

        LoanCoborrowersDAO coborrowersDAO = new LoanCoborrowersDAO();
        ArrayList<String> coborrowerIds = coborrowersDAO.getCoborrowers(loanId);

        return new PersonalLoan(
            loanId, customerId, coborrowerIds, principal, rate, duration, payment);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
