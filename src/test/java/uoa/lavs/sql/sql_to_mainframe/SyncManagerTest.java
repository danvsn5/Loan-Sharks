package uoa.lavs.sql.sql_to_mainframe;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.backend.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncAddress;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncCustomer;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncEmail;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncEmployer;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncLoan;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncLoanCoborrower;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncManager;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncNotes;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncPhone;
import uoa.lavs.legacy.mainframe.Status;

public class SyncManagerTest {
  DatabaseConnection conn;
  AddressDAO addressDAO;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    addressDAO = new AddressDAO();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void testSync() throws IOException {
    SyncCustomer syncCustomer = new SyncCustomer();
    SyncAddress syncAddress = new SyncAddress();
    SyncEmployer syncEmployer = new SyncEmployer();
    SyncNotes syncNotes = new SyncNotes();
    SyncLoan syncLoan = new SyncLoan();
    SyncLoanCoborrower syncLoanCoborrower = new SyncLoanCoborrower();
    SyncPhone syncPhone = new SyncPhone();
    SyncEmail syncEmail = new SyncEmail();

    LocalDateTime lastSyncTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(1);

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
  }

  @Test
  public void testSyncNothing() throws IOException {
    SyncManager syncManager = new SyncManager(List.of());
    Status status = syncManager.syncAll(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));
  }

  @Test
  public void testCheckIfNeedSyncing() {
    try {
      SyncManager.checkIfNeedsSyncing();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMasterSync() {
    try {
      SyncManager.masterSync();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
  }
}
