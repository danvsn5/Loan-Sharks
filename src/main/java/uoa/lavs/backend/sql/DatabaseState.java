package uoa.lavs.backend.sql;

import java.io.File;

public class DatabaseState {
  public static final String DB_URL = "jdbc:sqlite:src/main/resources/db/lavs.db";
  public static final String DB_TEST_URL = "jdbc:sqlite:src/test/java/uoa/lavs/sql/test.db";
  public static File DB_TEST_FILE = new File("src/test/java/uoa/lavs/sql/test.db");

  public static String active_db = DB_URL;

  public static String getActiveDB() {
    return active_db;
  }

  public static void setActiveDB(boolean test) {
    active_db = test ? DB_TEST_URL : DB_URL;
  }

}
