package uoa.lavs.sql;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ResetDatabaseTest {
  DatabaseConnection conn;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  // test initialisation of class
  @Test
  public void testResetDatabase() {
    ResetDatabase reset = new ResetDatabase();
    Assertions.assertNotNull(reset);
  }

  // test main method
  @Test
  public void testResetDatabaseMain() {
    ResetDatabase.main(new String[] {});
  }

  
  

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
    if (!dbFile.delete()) {
      throw new RuntimeException(
          "Failed to delete test database file: " + dbFile.getAbsolutePath());
    }
  }
}
