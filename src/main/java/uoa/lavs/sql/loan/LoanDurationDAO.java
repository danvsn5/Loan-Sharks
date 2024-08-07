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
}
