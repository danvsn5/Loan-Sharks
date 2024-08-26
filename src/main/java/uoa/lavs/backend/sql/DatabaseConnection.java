package uoa.lavs.backend.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private static String DB_URL = DatabaseState.getActiveDB();

  public DatabaseConnection() {}

  // Connect to the database
  public static Connection connect() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(DB_URL);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }

  // Close the database connection
  public static void close(Connection conn) {
    try {
      if (conn != null) {
        conn.close();
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
