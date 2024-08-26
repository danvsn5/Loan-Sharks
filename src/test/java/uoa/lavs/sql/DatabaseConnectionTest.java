package uoa.lavs.sql;

import java.io.File;
import java.sql.Connection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;

public class DatabaseConnectionTest {
  DatabaseConnection conn;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void testDatabaseConnection() {
    conn = new DatabaseConnection();
    Assertions.assertNotNull(conn);
  }

  @Test
  public void testConnect() {
    Connection connection = DatabaseConnection.connect();
    Assertions.assertNotNull(connection);
    DatabaseConnection.close(connection);
  }

  @Test // throw sql exception
  public void testClose() {
    Connection connection = DatabaseConnection.connect();
    DatabaseConnection.close(connection);
    Assertions.assertThrows(Exception.class, () -> connection.createStatement());
  }

  @AfterAll
  public static void tearDown() {
    if (!dbFile.delete()) {
      throw new RuntimeException(
          "Failed to delete test database file: " + dbFile.getAbsolutePath());
    }
  }
}
