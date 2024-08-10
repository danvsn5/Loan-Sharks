package uoa.lavs.sql.loan;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import uoa.lavs.loan.LoanDuration;
import uoa.lavs.sql.DatabaseConnection;

public class LoanDurationDAO {
  public void addLoanDuration(LoanDuration duration) {
    String sql = "INSERT INTO loan_duration (startDate, period, loanTerm) VALUES (?, ?, ?)";
    DatabaseConnection connection = new DatabaseConnection();
    try (Connection conn = connection.connect();
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
    DatabaseConnection connection = new DatabaseConnection();
    try (Connection conn = connection.connect();
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
}
