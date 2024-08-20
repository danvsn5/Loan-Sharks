package uoa.lavs.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InitialiseDatabase {

  public static void createDatabase() {
    try (Connection conn = DatabaseConnection.connect()) {
      createCustomerEntity(conn);
      createCustomerContactEntity(conn);
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
            + "primaryAddressId INTEGER, \n"
            + "mailingAddressId INTEGER, \n"
            + "contactId VARCHAR(50), \n"
            + "employerId VARCHAR(50), \n"
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP, \n"
            + "FOREIGN KEY (primaryAddressId, customerId) REFERENCES customer_address(addressId,"
            + " customerId), \n"
            + "FOREIGN KEY (mailingAddressId, customerId) REFERENCES customer_address(addressId,"
            + " customerId), \n"
            + "FOREIGN KEY (contactId) REFERENCES CustomerContact(contactId), \n"
            + "FOREIGN KEY (employerId) REFERENCES CustomerEmployer(employerId)\n"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createCustomerContactEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_contact (\n"
            + "contactId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "customerEmail VARCHAR(100), "
            + "phoneOneType VARCHAR(20), "
            + "phoneOneNumber VARCHAR(20), "
            + "phoneTwoType VARCHAR(20), "
            + "phoneTwoNumber VARCHAR(20), "
            + "preferredContact VARCHAR(50), "
            + "alternateContact VARCHAR(50), "
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
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
            + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
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
