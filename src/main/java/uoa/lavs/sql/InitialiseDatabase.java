package uoa.lavs.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InitialiseDatabase {

  public static void createDatabase() {
    try (Connection conn = DatabaseConnection.connect()) {
      createCustomerEntity(conn);
      createPhoneEntity(conn);
      createEmailEntity(conn);
      createCustomerAddressEntity(conn);
      createCustomerEmployerEntity(conn);
      createCustomerNotesEntity(conn);
      createLoanEntity(conn);
      createLoanDurationEntity(conn);
      createLoanPaymentEntity(conn);
      createLoanCoborrowerEntity(conn);
      createSyncInfoEntity(conn);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createCustomerEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer (\n"
            + "customerId VARCHAR(50) PRIMARY KEY, \n"
            + "title VARCHAR(10), \n"
            + "name VARCHAR(100), \n"
            + "dateOfBirth DATE, \n"
            + "occupation VARCHAR(100), \n"
            + "residency VARCHAR(50), \n"
            + "employerId VARCHAR(50), \n"
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, \n"
            + "FOREIGN KEY (employerId) REFERENCES CustomerEmployer(employerId)\n"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createPhoneEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_phone (\n"
            + "customerId VARCHAR(50), "
            + "phoneId INTEGER, "
            + "type VARCHAR(20), "
            + "phoneNumber VARCHAR(20), "
            + "isPrimary BOOLEAN, "
            + "canSendText BOOLEAN, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "PRIMARY KEY (customerId, phoneId),\n"
            + "FOREIGN KEY (customerId) REFERENCES customer(customerId)\n"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createEmailEntity(Connection conn) {
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

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createCustomerAddressEntity(Connection conn) {
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

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createCustomerNotesEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_notes (\n"
            + "customerId VARCHAR(50),\n"
            + "noteId INTEGER,\n"
            + "note VARCHAR(1368),\n"
            + "PRIMARY KEY (customerId, noteId),\n"
            + "FOREIGN KEY (customerId) REFERENCES customer(customerId)\n"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createCustomerEmployerEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_employer (\n"
            + "    customerId VARCHAR(50) UNIQUE,\n"
            + "    employerId INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "    employerName VARCHAR(100),\n"
            + "    addressLineOne VARCHAR(100),\n"
            + "    addressLineTwo VARCHAR(100),\n"
            + "    suburb VARCHAR(50),\n"
            + "    postCode VARCHAR(20),\n"
            + "    city VARCHAR(50),\n"
            + "    country VARCHAR(50),\n"
            + "    employerEmail VARCHAR(100),\n"
            + "    employerWebsite VARCHAR(100),\n"
            + "    employerPhone VARCHAR(20),\n"
            + "    ownerOfCompany BOOLEAN,\n"
            + "    lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
            + "    FOREIGN KEY (customerId) REFERENCES customer(customerId)\n"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createLoanEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS loan (\n"
            + "loanId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "customerId VARCHAR(50), "
            + "principal DOUBLE, "
            + "rate DOUBLE, "
            + "durationId INT, "
            + "paymentId INT, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY (customerId) REFERENCES Customer(customerId), "
            + "FOREIGN KEY (durationId) REFERENCES LoanDuration(durationId), "
            + "FOREIGN KEY (paymentId) REFERENCES LoanPayment(paymentId)"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createLoanDurationEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS loan_duration (\n"
            + "durationId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "startDate DATE, "
            + "period INT, "
            + "loanTerm INT, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createLoanPaymentEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS loan_payment (\n"
            + "paymentId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "compounding VARCHAR(50), "
            + "paymentFrequency VARCHAR(50), "
            + "paymentAmount VARCHAR(50), "
            + "interestOnly BOOLEAN, "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createLoanCoborrowerEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS loan_coborrower (\n"
            + "loanId INT, "
            + "coborrowerId VARCHAR(50), "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "PRIMARY KEY (loanId, coborrowerId), "
            + "FOREIGN KEY (loanId) REFERENCES Loan(loanId), "
            + "FOREIGN KEY (coborrowerId) REFERENCES Customer(customerId)"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createSyncInfoEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS sync_info (\n"
            + "id INTEGER PRIMARY KEY, "
            + "lastSyncTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    String insert = "INSERT INTO sync_info (id) VALUES (1);";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
      stmt.execute(insert);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    createDatabase();
  }
}
