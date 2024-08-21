package uoa.lavs.sql.loan;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.loan.LoanPayment;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;
import uoa.lavs.sql.oop_to_sql.loan.LoanPaymentDAO;

public class LoanPaymentDAOTest {
  DatabaseConnection conn;

  LoanPayment loanPayment;
  LoanPaymentDAO loanPaymentDAO;

  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    dbFile = DatabaseState.DB_TEST_FILE;
    InitialiseDatabase.createDatabase();

    loanPayment = new LoanPayment(-1, "daily", "weekly", "1000", false);
    loanPaymentDAO = new LoanPaymentDAO();
  }

  @Test
  public void testAddLoanPayment() {
    loanPaymentDAO.addLoanPayment(loanPayment);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM loan_payment WHERE loanId = ?")) {
      stmt.setInt(1, -1);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Loan payment should be added to the database");
        Assertions.assertEquals("daily", rs.getString("compounding"));
        Assertions.assertEquals("weekly", rs.getString("paymentFrequency"));
        Assertions.assertEquals("1000", rs.getString("paymentAmount"));
        Assertions.assertEquals(false, rs.getBoolean("interestOnly"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed: " + e.getMessage());
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateLoanPayment() {
    loanPaymentDAO.addLoanPayment(loanPayment);

    LoanPayment updatedLoanPayment = new LoanPayment(-1, "monthly", "fortnightly", "2000", true);
    loanPaymentDAO.updateLoanPayment(updatedLoanPayment);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM loan_payment WHERE loanId = ?")) {
      stmt.setInt(1, -1);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Loan payment should be updated in the database");
        Assertions.assertEquals("monthly", rs.getString("compounding"));
        Assertions.assertEquals("fortnightly", rs.getString("paymentFrequency"));
        Assertions.assertEquals("2000", rs.getString("paymentAmount"));
        Assertions.assertEquals(true, rs.getBoolean("interestOnly"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed: " + e.getMessage());
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetLoanPayment() {
    loanPaymentDAO.addLoanPayment(loanPayment);

    LoanPayment retrievedLoanPayment = loanPaymentDAO.getLoanPayment(-1);

    Assertions.assertEquals("daily", retrievedLoanPayment.getCompounding());
    Assertions.assertEquals("weekly", retrievedLoanPayment.getPaymentFrequency());
    Assertions.assertEquals("1000", retrievedLoanPayment.getPaymentAmount());
    Assertions.assertEquals(false, retrievedLoanPayment.getInterestOnly());
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
