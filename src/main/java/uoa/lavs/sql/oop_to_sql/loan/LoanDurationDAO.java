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
    String sql = "INSERT INTO loan_duration (startDate, period, loanTerm) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      pstmt.setDate(1, Date.valueOf(duration.getStartDate()));
      pstmt.setInt(2, duration.getPeriod());
      pstmt.setInt(3, duration.getLoanTerm());

      try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int generatedId = generatedKeys.getInt(1);
          duration.setDurationId(generatedId);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
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

  public LoanDuration findLoanDuration(int loanId) {
    String sql = "SELECT * FROM loan_duration WHERE loanId = ?";
    LoanDuration duration = null;

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, loanId);
      var rs = pstmt.executeQuery();

      if (rs.next()) {
        LocalDate startDate = rs.getDate("startDate").toLocalDate();
        int period = rs.getInt("period");
        int loanTerm = rs.getInt("loanTerm");
        int durationId = rs.getInt("durationId");
        duration = new LoanDuration(loanId, startDate, period, loanTerm);
        duration.setDurationId(durationId);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return duration;
  }
}
