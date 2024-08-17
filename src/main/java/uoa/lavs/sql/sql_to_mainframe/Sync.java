package uoa.lavs.sql.sql_to_mainframe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import uoa.lavs.sql.DatabaseConnection;

public abstract class Sync {
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public Sync() {}

  /**
   * Get the last sync time from the database
   *
   * @return
   */
  protected LocalDateTime getLastSyncTimeFromDB() {
    // SQL query to fetch the last sync time (this will always be the first row of the table)
    String sql = "SELECT lastSyncTime FROM sync_info WHERE id = 1";

    // Try with resources to automatically close the connection, statement and result set
    try (Connection conn = DatabaseConnection.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      // If the query returns a result, get the last sync time from the result set
      if (rs.next()) {
        String lastSyncTimeStr = rs.getString("lastSyncTime");
        if (lastSyncTimeStr != null) {
          return LocalDateTime.parse(lastSyncTimeStr, FORMATTER);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   * Update the last sync time in the database
   *
   * @param syncTime
   */
  protected void updateLastSyncTimeInDB(LocalDateTime syncTime) {
    // SQL query to insert (if there is no current sync time) or update the last sync time (if there
    // is)
    String sql =
        "INSERT INTO sync_info (id, lastSyncTime) VALUES (1, ?) "
            + "ON CONFLICT(id) DO UPDATE SET lastSyncTime = excluded.lastSyncTime";

    // Try with resources to automatically close the connection and prepared statement
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, syncTime.format(FORMATTER));
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Sync data to mainframe
   *
   * @throws Exception
   */
  protected abstract void syncToMainframe(LocalDateTime lastSyncTime) throws Exception;
}
