package uoa.lavs.sql.oop_to_sql.loan;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import uoa.lavs.loan.LoanDuration;
import uoa.lavs.sql.DatabaseConnection;

public class LoanDurationDAO {
  public void addLoanDuration(LoanDuration duration) {
    String sql =
        "INSERT INTO loan_duration (loanId, durationId, startDate, period, loanTerm) VALUES (?, ?,"
            + " ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      int nextDurationId = getNextDurationIdForLoan(duration.getLoanId());

      pstmt.setString(1, duration.getLoanId());
      pstmt.setInt(2, nextDurationId);

      pstmt.setDate(3, Date.valueOf(duration.getStartDate()));
      pstmt.setInt(4, duration.getPeriod());
      pstmt.setInt(5, duration.getLoanTerm());

      pstmt.executeUpdate();

      duration.setDurationId(nextDurationId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private int getNextDurationIdForLoan(String loanId) {
    String sql = "SELECT MAX(durationId) FROM loan_duration WHERE loanId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, loanId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        int maxDurationId = rs.getInt(1);
        return maxDurationId + 1;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return 1;
  }

  public void updateLoanDuration(LoanDuration duration) {
    String sql =
        "UPDATE loan_duration SET startDate = ?, period = ?, loanTerm = ? WHERE durationId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setDate(1, Date.valueOf(duration.getStartDate()));
      pstmt.setInt(2, duration.getPeriod());
      pstmt.setInt(3, duration.getLoanTerm());
      pstmt.setInt(4, duration.getDurationId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public LoanDuration getLoanDuration(String loanId) {
    String sql = "SELECT * FROM loan_duration WHERE loanId = ?";
    LoanDuration duration = null;

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loanId);
      var rs = pstmt.executeQuery();

      if (rs.next()) {
        int durationId = rs.getInt("durationId");
        LocalDate startDate = rs.getDate("startDate").toLocalDate();
        int period = rs.getInt("period");
        int loanTerm = rs.getInt("loanTerm");

        duration = new LoanDuration(loanId, startDate, period, loanTerm);
        duration.setDurationId(durationId);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return duration;
  }
}
