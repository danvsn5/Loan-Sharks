package uoa.lavs.backend.sql.oop_to_sql.loan;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import uoa.lavs.backend.oop.loan.LoanDuration;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.oop_to_sql.AbstractDAO;

public class LoanDurationDAO extends AbstractDAO {

  // Adds a loan duration to the database
  public void addLoanDuration(LoanDuration duration) {
    String sql =
        "INSERT INTO loan_duration (loanId, durationId, startDate, period, loanTerm) VALUES (?, ?,"
            + " ?, ?, ?)";
    add(duration, sql);
  }

  public void add(LoanDuration duration, String sql) {
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

  // Get the next durationId for a loan
  private int getNextDurationIdForLoan(String loanId) {
    String sql = "SELECT MAX(durationId) FROM loan_duration WHERE loanId = ?";
    return getNextId(loanId, sql);
  }

  // Updates a loan duration in the database with new details from the duration object
  public void updateLoanDuration(LoanDuration duration) {
    String sql =
        "UPDATE loan_duration SET startDate = ?, period = ?, loanTerm = ?, lastModified ="
            + " CURRENT_TIMESTAMP WHERE durationId = ?";
    update(duration, sql);
  }

  public void update(LoanDuration duration, String sql) {
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

  // Get a loan duration from the database using the loanId
  public LoanDuration getLoanDuration(String loanId) {
    String sql = "SELECT * FROM loan_duration WHERE loanId = ?";
    return get(loanId, sql);
  }

  // Get a loan duration from the database using the durationId
  public LoanDuration get(String loanId, String sql) {
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
