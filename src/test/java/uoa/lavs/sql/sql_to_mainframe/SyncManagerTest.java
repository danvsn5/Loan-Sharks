package uoa.lavs.sql.sql_to_mainframe;

import static org.junit.jupiter.api.Assertions.assertFalse;

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
import uoa.lavs.backend.sql.sql_to_mainframe.SyncManager;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;
import uoa.lavs.mainframe.simulator.nitrite.DatabaseHelper;

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
  public void testSyncNothing() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());

    SyncManager syncManager = new SyncManager(List.of());
    Status status = syncManager.syncAll(LocalDateTime.now(ZoneOffset.UTC).minusDays(1), connection);

    connection.close();

    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCheckIfNeedSyncing() {
    boolean syncNeed = false;
    try {
      syncNeed = SyncManager.checkIfNeedsSyncing();
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertFalse(syncNeed);
  }

  @Test
  public void testMasterSync() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    Status status = SyncManager.masterSync(connection);

    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
  }
}
