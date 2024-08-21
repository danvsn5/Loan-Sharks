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
    String sql =
        "INSERT INTO loan (loanId, customerId, principal, rate, rateType) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, loan.getLoanId());
      pstmt.setString(2, loan.getCustomerId());
      pstmt.setDouble(3, loan.getPrincipal());
      pstmt.setDouble(4, loan.getRate());
      pstmt.setString(5, loan.getRateType());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateLoan(ILoan loan) {
    String sql =
        "UPDATE loan SET customerId = ?, principal = ?, rate = ?, rateType = ? WHERE loanId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loan.getCustomerId());
      pstmt.setDouble(2, loan.getPrincipal());
      pstmt.setDouble(3, loan.getRate());
      pstmt.setString(4, loan.getRateType());
      pstmt.setInt(5, loan.getLoanId());
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
        String rateType = rs.getString("rateType");

        LoanDurationDAO durationDAO = new LoanDurationDAO();
        LoanDuration duration = durationDAO.getLoanDuration(loanId);

        LoanPaymentDAO paymentDAO = new LoanPaymentDAO();
        LoanPayment payment = paymentDAO.getLoanPayment(loanId);

        LoanCoborrowersDAO coborrowersDAO = new LoanCoborrowersDAO();
        ArrayList<String> coborrowerIds = coborrowersDAO.getCoborrowers(loanId);

        return new PersonalLoan(
            loanId, customerId, coborrowerIds, principal, rate, rateType, duration, payment);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public ArrayList<Loan> getLoansFromCustomer(String customerId) {
    String sql = "SELECT * FROM loan WHERE customerId = ?";
    ArrayList<Loan> loans = new ArrayList<>();

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customerId);
      var rs = pstmt.executeQuery();

      LoanDurationDAO durationDAO = new LoanDurationDAO();
      LoanPaymentDAO paymentDAO = new LoanPaymentDAO();
      LoanCoborrowersDAO coborrowersDAO = new LoanCoborrowersDAO();

      while (rs.next()) {
        int loanId = rs.getInt("loanId");
        Double principal = rs.getDouble("principal");
        Double rate = rs.getDouble("rate");
        String rateType = rs.getString("rateType");

        LoanDuration duration = durationDAO.getLoanDuration(loanId);
        LoanPayment payment = paymentDAO.getLoanPayment(loanId);
        ArrayList<String> coborrowerIds = coborrowersDAO.getCoborrowers(loanId);

        loans.add(
            new PersonalLoan(
                loanId, customerId, coborrowerIds, principal, rate, rateType, duration, payment));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return loans;
  }
}
