package uoa.lavs.sql.loan;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.loan.LoanDuration;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;
import uoa.lavs.sql.oop_to_sql.loan.LoanDurationDAO;

public class LoanDurationDAOTest {
  DatabaseConnection conn;
  LoanDuration loanDuration;
  LoanDurationDAO loanDurationDAO;

  LocalDate startDate;

  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    dbFile = DatabaseState.DB_TEST_FILE;
    InitialiseDatabase.createDatabase();
    startDate = LocalDate.now();
    loanDuration = new LoanDuration(-1, startDate, 5, 12);
    loanDurationDAO = new LoanDurationDAO();
  }

  @Test
  public void testAddLoanDuration() {
    loanDurationDAO.addLoanDuration(loanDuration);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM loan_duration WHERE loanId = ?")) {
      stmt.setInt(1, -1);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Loan duration should be added to the database");
        Assertions.assertEquals(startDate, rs.getDate("startDate").toLocalDate());
        Assertions.assertEquals(5, rs.getInt("period"));
        Assertions.assertEquals(12, rs.getInt("loanTerm"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed: " + e.getMessage());
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateLoanDuration() {
    loanDurationDAO.addLoanDuration(loanDuration);
    LocalDate newStartDate = LocalDate.now().minusDays(1);

    loanDuration.setStartDate(newStartDate);
    loanDuration.setPeriod(10);
    loanDuration.setLoanTerm(24);

    loanDurationDAO.updateLoanDuration(loanDuration);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM loan_duration WHERE loanId = ?")) {
      stmt.setInt(1, -1);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Loan duration should be updated in the database");
        Assertions.assertEquals(newStartDate, rs.getDate("startDate").toLocalDate());
        Assertions.assertEquals(10, rs.getInt("period"));
        Assertions.assertEquals(24, rs.getInt("loanTerm"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed: " + e.getMessage());
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetLoanDuration() {
    loanDurationDAO.addLoanDuration(loanDuration);

    LoanDuration retrievedLoanDuration = loanDurationDAO.getLoanDuration(-1);

    Assertions.assertEquals(-1, retrievedLoanDuration.getLoanId());
    Assertions.assertEquals(startDate, retrievedLoanDuration.getStartDate());
    Assertions.assertEquals(5, retrievedLoanDuration.getPeriod());
    Assertions.assertEquals(12, retrievedLoanDuration.getLoanTerm());
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
    if (!dbFile.delete()) {
      throw new RuntimeException(
          "Failed to delete test database file: " + dbFile.getAbsolutePath());
    }
  }
}
