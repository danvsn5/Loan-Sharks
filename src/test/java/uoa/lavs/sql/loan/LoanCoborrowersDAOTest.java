package uoa.lavs.sql.loan;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanCoborrowersDAO;

public class LoanCoborrowersDAOTest {
  DatabaseConnection conn;

  ArrayList<String> coborrowers;
  LoanCoborrowersDAO loanCoborrowersDAO;

  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    dbFile = DatabaseState.DB_TEST_FILE;
    InitialiseDatabase.createDatabase();

    coborrowers = new ArrayList<>();
    coborrowers.add("-1");
    loanCoborrowersDAO = new LoanCoborrowersDAO();
    loanCoborrowersDAO.addCoborrowers("-1", coborrowers);
  }

  @Test
  public void testAddCoborrowers() {
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM loan_coborrower WHERE loanId = ?")) {
      stmt.setString(1, "-1");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Coborrowers should be added to the database");
        Assertions.assertEquals("-1", rs.getString("coborrowerId"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed: " + e.getMessage());
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateCoborrowers() {
    coborrowers.add("-2");
    loanCoborrowersDAO.updateCoborrowers("-1", coborrowers);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM loan_coborrower WHERE loanId = ?")) {
      stmt.setString(1, "-1");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Coborrowers should be updated in the database");
        Assertions.assertEquals("-1", rs.getString("coborrowerId"));
        Assertions.assertTrue(rs.next(), "Coborrowers should be updated in the database");
        Assertions.assertEquals("-2", rs.getString("coborrowerId"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed: " + e.getMessage());
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetCoborrowers() {
    ArrayList<String> coborrowers = loanCoborrowersDAO.getCoborrowers("-1");

    Assertions.assertEquals(1, coborrowers.size());
    Assertions.assertEquals("-1", coborrowers.get(0));
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
