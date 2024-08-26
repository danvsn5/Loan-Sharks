package uoa.lavs.sql.sql_to_mainframe;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.oop.loan.LoanDuration;
import uoa.lavs.backend.oop.loan.LoanPayment;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.backend.sql.ResetDatabase;
import uoa.lavs.backend.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.backend.sql.sql_to_mainframe.LoanCreationHelper;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncLoan;
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
    try {
      SyncManager.checkIfNeedsSyncing();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMasterSync() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    SyncManager.masterSync(connection);

    connection.close();
  }

  @Test
  public void testSyncMainframeData_ExistingLoan() throws IOException {
    ResetDatabase reset = new ResetDatabase();
    reset.resetDatabase();
    InitialiseDatabase.createDatabase();

    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());

    ArrayList<String> coborrowers = new ArrayList<>();
    coborrowers.add("456");
    coborrowers.add("");
    coborrowers.add("");

    LoanDuration loanDuration = new LoanDuration("123-09", LocalDate.of(2024, 8, 3), 24, 30);
    LoanPayment loanPayment = new LoanPayment("123-09", "2", "4", "573.00", false);
    PersonalLoan personalLoan =
        new PersonalLoan(
            "123-09", "123", coborrowers, 10000.00, 7.65, "2", loanDuration, loanPayment);
    LoanCreationHelper.createLoan(personalLoan, connection);

    SyncLoan syncLoan = new SyncLoan();
    SyncManager syncManager = new SyncManager(List.of(syncLoan));
    Status status = syncManager.syncAll(LocalDateTime.now(ZoneOffset.UTC).minusDays(1), connection);

    connection.close();
  }

  @Test
  public void testSyncMainframeData_NewLoan() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());

    ArrayList<String> coborrowers = new ArrayList<>();
    coborrowers.add("456");
    coborrowers.add("");
    coborrowers.add("");

    LoanDuration loanDuration = new LoanDuration("123-09", LocalDate.of(2024, 8, 3), 24, 30);
    LoanPayment loanPayment = new LoanPayment("123-09", "2", "4", "573.00", false);
    PersonalLoan personalLoan =
        new PersonalLoan(
            "123-09", "123", coborrowers, 10000.00, 7.65, "2", loanDuration, loanPayment);
    LoanCreationHelper.createLoan(personalLoan, connection);

    SyncLoan syncLoan = new SyncLoan();
    SyncManager syncManager = new SyncManager(List.of(syncLoan));
    Status status = syncManager.syncAll(LocalDateTime.now(ZoneOffset.UTC).minusDays(1), connection);

    connection.close();
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
  }
}
