package uoa.lavs.backend.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InitialiseDatabase {

  public static void createDatabase() {
    try (Connection conn = DatabaseConnection.connect()) {

      try (Statement stmt = conn.createStatement()) {
        String sql1 = getCreateCustomerEntityQuery();
        String sql2 = getCreatePhoneEntityQuery();
        String sql3 = getCreateEmailEntityQuery();
        String sql4 = getCreateCustomerAddressEntityQuery();
        String sql5 = getCreateCustomerNotesEntityQuery();
        String sql6 = getCreateCustomerEmployerEntity();
        String sql7 = getCreateLoanEntityQuery();
        String sql8 = getCreateLoanDurationEntityQuery();
        String sql9 = getCreateLoanPaymentEntityQuery();
        String sql10 = getCreateLoanCoborrowerEntityQuery();
        String sql11 = getCreateSyncInfoEntityQuery();
        String sql12 = getInitialiseSyncInfoQuery();
        stmt.addBatch(sql1);
        stmt.addBatch(sql2);
        stmt.addBatch(sql3);
        stmt.addBatch(sql4);
        stmt.addBatch(sql5);
        stmt.addBatch(sql6);
        stmt.addBatch(sql7);
        stmt.addBatch(sql8);
        stmt.addBatch(sql9);
        stmt.addBatch(sql10);
        stmt.addBatch(sql11);
        stmt.addBatch(sql12);

        stmt.executeBatch();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static String getCreateCustomerEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer (\n"
            + "customerId VARCHAR(50) PRIMARY KEY, \n"
            + "title VARCHAR(10), \n"
            + "name VARCHAR(100), \n"
            + "dateOfBirth DATE, \n"
            + "occupation VARCHAR(100), \n"
            + "visa VARCHAR(50), \n"
            + "citizenship VARCHAR(50), \n"
            + "employerId VARCHAR(50), \n"
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, \n"
            + "FOREIGN KEY (employerId) REFERENCES CustomerEmployer(employerId)\n"
            + ");";
    return sql;
  }

  private static String getCreatePhoneEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_phone (\n"
            + "customerId VARCHAR(50), "
            + "phoneId INTEGER, "
            + "type VARCHAR(20), "
            + "prefix VARCHAR(10), "
            + "phoneNumber VARCHAR(20), "
            + "isPrimary BOOLEAN, "
            + "canSendText BOOLEAN, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "PRIMARY KEY (customerId, phoneId),\n"
            + "FOREIGN KEY (customerId) REFERENCES customer(customerId)\n"
            + ");";
    return sql;
  }

  private static String getCreateEmailEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_email (\n"
            + "customerId VARCHAR(50), "
            + "emailId INTEGER, "
            + "emailAddress VARCHAR(100), "
            + "isPrimary BOOLEAN, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "PRIMARY KEY (customerId, emailId),\n"
            + "FOREIGN KEY (customerId) REFERENCES customer(customerId)\n"
            + ");";

    return sql;
  }

  private static String getCreateCustomerAddressEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_address (\n"
            + "customerId VARCHAR(50),\n"
            + "addressId INTEGER,\n"
            + "addressType VARCHAR(50),\n"
            + "addressLineOne VARCHAR(100),\n"
            + "addressLineTwo VARCHAR(100),\n"
            + "suburb VARCHAR(50),\n"
            + "postCode VARCHAR(20),\n"
            + "city VARCHAR(50),\n"
            + "country VARCHAR(50),\n"
            + "isPrimary BOOLEAN,\n"
            + "isMailing BOOLEAN,\n"
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
            + "PRIMARY KEY (customerId, addressId),\n"
            + "FOREIGN KEY (customerId) REFERENCES customer(customerId)\n"
            + ");";

    return sql;
  }

  private static String getCreateCustomerNotesEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_notes (\n"
            + "customerId VARCHAR(50),\n"
            + "noteId INTEGER,\n"
            + "note VARCHAR(1368),\n"
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
            + "PRIMARY KEY (customerId, noteId),\n"
            + "FOREIGN KEY (customerId) REFERENCES customer(customerId)\n"
            + ");";

    return sql;
  }

  private static String getCreateCustomerEmployerEntity() {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_employer ( "
            + "customerId VARCHAR(50) UNIQUE, "
            + "employerId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "employerName VARCHAR(100), "
            + "addressLineOne VARCHAR(100), "
            + "addressLineTwo VARCHAR(100), "
            + "suburb VARCHAR(50), "
            + "postCode VARCHAR(20), "
            + "city VARCHAR(50), "
            + "country VARCHAR(50), "
            + "employerEmail VARCHAR(100), "
            + "employerWebsite VARCHAR(100), "
            + "employerPhone VARCHAR(20), "
            + "ownerOfCompany BOOLEAN, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY (customerId) REFERENCES customer(customerId) "
            + ");";

    return sql;
  }

  private static String getCreateLoanEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS loan (\n"
            + "loanId VARCHAR(50) PRIMARY KEY, "
            + "customerId VARCHAR(50), "
            + "principal DOUBLE, "
            + "rate DOUBLE, "
            + "rateType VARCHAR(50), "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY (customerId) REFERENCES Customer(customerId)"
            + ");";

    return sql;
  }

  private static String getCreateLoanDurationEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS loan_duration (\n"
            + "loanId VARCHAR(50), "
            + "durationId INTEGER, "
            + "startDate DATE, "
            + "period INT, "
            + "loanTerm INT, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "PRIMARY KEY (loanId, durationId), "
            + "FOREIGN KEY (loanId) REFERENCES Loan(loanId)"
            + ");";

    return sql;
  }

  private static String getCreateLoanPaymentEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS loan_payment (\n"
            + "loanId VARCHAR(50), "
            + "paymentId INTEGER, "
            + "compounding VARCHAR(50), "
            + "paymentFrequency VARCHAR(50), "
            + "paymentAmount VARCHAR(50), "
            + "interestOnly BOOLEAN, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "PRIMARY KEY (loanId, paymentId), "
            + "FOREIGN KEY (loanId) REFERENCES Loan(loanId)"
            + ");";

    return sql;
  }

  private static String getCreateLoanCoborrowerEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS loan_coborrower (\n"
            + "loanId VARCHAR(50), "
            + "coborrowerId VARCHAR(50), "
            + "number INTEGER, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "PRIMARY KEY (loanId, coborrowerId), "
            + "FOREIGN KEY (loanId) REFERENCES Loan(loanId), "
            + "FOREIGN KEY (coborrowerId) REFERENCES Customer(customerId)"
            + ");";

    return sql;
  }

  private static String getCreateSyncInfoEntityQuery() {
    String sql =
        "CREATE TABLE IF NOT EXISTS sync_info (\n"
            + "id INTEGER PRIMARY KEY, "
            + "lastSyncTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "needsSyncing BOOLEAN DEFAULT 1);";

    return sql;
  }

  private static String getInitialiseSyncInfoQuery() {
    String insert = "INSERT INTO sync_info (id, needsSyncing) VALUES (1, 1);";
    return insert;
  }

  /*
  public static void main(String[] args) {
    createDatabase();
  }

  */
}
