package uoa.lavs.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseStateTest {
  @Test
  public void testGetActiveDB() {
    String expected = DatabaseState.active_db;
    String actual = DatabaseState.getActiveDB();
    Assertions.assertEquals(expected, actual);
  }

  @Test 
  public void testSetActiveDBTrue() {
    DatabaseState.setActiveDB(true);
    String expected = DatabaseState.DB_TEST_URL;
    String actual = DatabaseState.getActiveDB();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testSetActiveDBFalse() {
    DatabaseState.setActiveDB(false);
    String expected = DatabaseState.DB_URL;
    String actual = DatabaseState.getActiveDB();
    Assertions.assertEquals(expected, actual);
  }
}
