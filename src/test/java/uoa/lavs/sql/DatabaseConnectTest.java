package uoa.lavs.sql;

import java.sql.Connection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseConnectTest {
  DatabaseConnection conn;
  private static final String DB_URL = "jdbc:sqlite:src/test/java/uoa/lavs/sql/test.db";

  @BeforeEach
  public void setUp() {
    conn = new DatabaseConnection(DB_URL);
  }

  @Test
  public void testConnect() {
    Connection connection = conn.connect();
    Assertions.assertNotNull(connection);
    conn.close(connection);
  }
}
