package uoa.lavs.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InitialiseDatabaseTest {
  DatabaseConnection conn;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void testInit() {
    InitialiseDatabase init = new InitialiseDatabase();
    Assertions.assertNotNull(init);
  }

  @Test
  public void testCreateDatabase() {
    try (Connection conn = DatabaseConnection.connect()) {
      DatabaseMetaData metaData = conn.getMetaData();
      Set<String> expectedTables = new HashSet<>();
      expectedTables.add("customer");
      expectedTables.add("customer_phone");
      expectedTables.add("customer_email");
      expectedTables.add("customer_address");
      expectedTables.add("customer_notes");
      expectedTables.add("customer_employer");
      expectedTables.add("loan");
      expectedTables.add("loan_duration");
      expectedTables.add("loan_payment");
      expectedTables.add("loan_coborrower");

      Set<String> actualTables = new HashSet<>();
      try (ResultSet rs = metaData.getTables(null, null, "%", new String[] {"TABLE"})) {
        while (rs.next()) {
          String tableName = rs.getString("TABLE_NAME");
          actualTables.add(tableName);
        }
      }

      Assertions.assertTrue(
          actualTables.containsAll(expectedTables),
          "Not all expected tables were created in the database");
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Failed to check database tables");
    } finally {
      DatabaseConnection.close(null);
    }
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
