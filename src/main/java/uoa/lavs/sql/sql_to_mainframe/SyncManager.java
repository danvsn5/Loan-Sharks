package uoa.lavs.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.sql.DatabaseConnection;

public class SyncManager {

  private final List<Sync> syncs;
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public SyncManager(List<Sync> syncs) {
    this.syncs = syncs;
  }

  public Status syncAll(LocalDateTime lastSyncTime) throws IOException {
    uoa.lavs.mainframe.Connection mainframeConnection = null;
    Connection localConnection = null;

    try {
      // Establish the connection to the mainframe and local database
      mainframeConnection = Instance.getConnection();
      localConnection = DatabaseConnection.connect();

      // Iterate through each sync and perform the sync operation
      for (Sync sync : syncs) {
        Status status = sync.syncToMainframe(lastSyncTime, mainframeConnection, localConnection);
        if (status.getErrorCode() != 0) {
          System.out.println(
              "Error syncing " + sync.getClass().getSimpleName() + ": " + status.getErrorMessage());
          System.out.println("Please try again.");
          return status;
        }
      }

      // Update the last sync time after all syncs are successful
      LocalDateTime newSyncTime = LocalDateTime.now(ZoneOffset.UTC);
      updateLastSyncTimeInDB(newSyncTime, localConnection);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } finally {
      try {
        if (localConnection != null) {
          localConnection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return new Status(0, "Successfully synced all data to mainframe", 0);
  }

  /**
   * Update the last sync time in the database
   *
   * @param syncTime
   * @param localConnection
   */
  protected void updateLastSyncTimeInDB(LocalDateTime syncTime, Connection localConnection)
      throws SQLException {
    // SQL query to insert (if there is no current sync time) or update the last sync time
    String sql =
        "INSERT INTO sync_info (id, lastSyncTime, needsSyncing) VALUES (1, ?, 0) "
            + "ON CONFLICT(id) DO UPDATE SET lastSyncTime = excluded.lastSyncTime, "
            + "needsSyncing = 0";

    // Try with resources to automatically close the prepared statement
    try (PreparedStatement pstmt = localConnection.prepareStatement(sql)) {
      pstmt.setString(1, syncTime.format(FORMATTER));
      pstmt.executeUpdate();
    }
  }

  public static boolean checkIfNeedsSyncing() throws SQLException {
    Connection localConnection = DatabaseConnection.connect();
    String sql = "SELECT needsSyncing FROM sync_info WHERE id = 1";
    try (PreparedStatement pstmt = localConnection.prepareStatement(sql)) {
      return pstmt.executeQuery().getBoolean("needsSyncing");
    }
  }

  public static void setNeedsSyncing(boolean needsSyncing) throws SQLException {
    Connection localConnection = DatabaseConnection.connect();
    String sql = "UPDATE sync_info SET needsSyncing = ? WHERE id = 1";
    try (PreparedStatement pstmt = localConnection.prepareStatement(sql)) {
      pstmt.setBoolean(1, needsSyncing);
      pstmt.executeUpdate();
    }
  }

  public static Status masterSync() throws IOException {
    SyncCustomer syncCustomer = new SyncCustomer();
    SyncAddress syncAddress = new SyncAddress();
    SyncEmployer syncEmployer = new SyncEmployer();
    SyncNotes syncNotes = new SyncNotes();
    SyncLoan syncLoan = new SyncLoan();
    SyncLoanCoborrower syncLoanCoborrower = new SyncLoanCoborrower();
    SyncPhone syncPhone = new SyncPhone();
    SyncEmail syncEmail = new SyncEmail();
    LocalDateTime lastSyncTime = syncCustomer.getLastSyncTimeFromDB();

    if (lastSyncTime == null) {
      System.out.println("No last sync time found. Syncing all records.");
      lastSyncTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(1);
    }

    SyncManager syncManager =
        new SyncManager(
            List.of(
                syncCustomer,
                syncAddress,
                syncEmployer,
                syncLoan,
                syncLoanCoborrower,
                syncPhone,
                syncEmail,
                syncNotes));

    Status status = syncManager.syncAll(lastSyncTime);

    return status;
  }

  public static void main(String[] args) throws IOException {
    Status status = masterSync();
    System.out.println(status.getErrorMessage());
  }
}
