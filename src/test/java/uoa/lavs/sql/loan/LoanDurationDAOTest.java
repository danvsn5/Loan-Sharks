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
import uoa.lavs.backend.oop.loan.LoanDuration;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanDurationDAO;

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
    loanDuration = new LoanDuration("-1", startDate, 5, 12);
    loanDurationDAO = new LoanDurationDAO();
  }

  @Test
  public void testAddLoanDuration() {
    loanDurationDAO.addLoanDuration(loanDuration);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM loan_duration WHERE loanId = ?")) {
      stmt.setString(1, "-1");

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
  public void testAddLoanDurationSQLException() {
    String invalidSql =
        "INSERT INTO non_existent_table (loanId, durationId, startDate, period, loanTerm) VALUES"
            + " (?, ?, ?, ?, ?)";

    loanDurationDAO.add(loanDuration, invalidSql);
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
      stmt.setString(1, "-1");

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
  public void testUpdateLoanDurationSQLException() {
    loanDurationDAO.addLoanDuration(loanDuration);
    LocalDate newStartDate = LocalDate.now().minusDays(1);

    loanDuration.setStartDate(newStartDate);
    loanDuration.setPeriod(10);
    loanDuration.setLoanTerm(24);

    String invalidSql =
        "UPDATE non_existent_table SET startDate = ?, period = ?, loanTerm = ?, lastModified ="
            + " CURRENT_TIMESTAMP WHERE durationId = ?";

    loanDurationDAO.update(loanDuration, invalidSql);
  }

  @Test
  public void testGetLoanDuration() {
    loanDurationDAO.addLoanDuration(loanDuration);

    LoanDuration retrievedLoanDuration = loanDurationDAO.getLoanDuration("-1");

    Assertions.assertEquals("-1", retrievedLoanDuration.getLoanId());
    Assertions.assertEquals(startDate, retrievedLoanDuration.getStartDate());
    Assertions.assertEquals(5, retrievedLoanDuration.getPeriod());
    Assertions.assertEquals(12, retrievedLoanDuration.getLoanTerm());
  }

  @Test
  public void testGetLoanDurationSQLException() {
    loanDurationDAO.addLoanDuration(loanDuration);

    String invalidSql = "SELECT * FROM non_existent_table WHERE loanId = ?";

    loanDurationDAO.get("-1", invalidSql);
  }

  @Test
  public void testGetLoanDurationNonExistent() {
    LoanDuration retrievedLoanDuration = loanDurationDAO.getLoanDuration("-1");

    Assertions.assertNull(retrievedLoanDuration);
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
