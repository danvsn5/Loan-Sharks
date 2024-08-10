package uoa.lavs.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private static String DB_URL;

  public DatabaseConnection() {
    DB_URL = "jdbc:sqlite:src/main/resources/db/lavs.db";
  }

  public DatabaseConnection(String dbUrl) {
    DB_URL = dbUrl;
  }

  public static Connection connect() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(DB_URL);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }

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
