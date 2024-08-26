package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.legacy.mainframe.Status;

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
    String sql = "SELECT lastSyncTime FROM sync_info WHERE id = 1";

    try (Connection conn = DatabaseConnection.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

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
    String sql =
        "INSERT INTO sync_info (id, lastSyncTime) VALUES (1, ?) "
            + "ON CONFLICT(id) DO UPDATE SET lastSyncTime = excluded.lastSyncTime";

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, syncTime.format(FORMATTER));
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Sync data to the mainframe
   *
   * @return
   * @throws SQLException
   * @throws IOException
   */
  protected abstract Status syncMainframeData(
      ResultSet resultSet, uoa.lavs.legacy.mainframe.Connection connection, Connection localConn)
      throws SQLException, IOException;

  /**
   * Sync data to the mainframe
   *
   * @param lastSyncTime
   * @param mainframeConnection
   * @param localConnection
   * @return
   * @throws IOException
   */
  public Status syncToMainframe(
      LocalDateTime lastSyncTime,
      uoa.lavs.legacy.mainframe.Connection mainframeConnection,
      Connection localConnection)
      throws IOException {
    String formattedLastSyncTime = lastSyncTime.format(FORMATTER);
    System.out.println("Last sync time: " + formattedLastSyncTime);

    String sql = getSqlQuery();
    try (PreparedStatement pstmt = localConnection.prepareStatement(sql)) {

      pstmt.setString(1, formattedLastSyncTime);
      ResultSet resultSet = pstmt.executeQuery();

      Status status = null;

      if (!resultSet.isBeforeFirst()) {
        System.out.println("No new records since last sync. Exiting...");
        return new Status(0, "No new records since last sync", 0);
      }

      while (resultSet.next()) {
        status = syncMainframeData(resultSet, mainframeConnection, localConnection);
        if (status.getErrorCode() != 0) {
          System.out.println("Error syncing data to mainframe: " + status.getErrorMessage());
          return status;
        }
      }

      LocalDateTime newSyncTime = LocalDateTime.now(ZoneOffset.UTC);
      updateLastSyncTimeInDB(newSyncTime);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return new Status(0, "Successfully synced data to mainframe", 0);
  }

  // Get the SQL query to retrieve data from the local database
  protected abstract String getSqlQuery();
}
