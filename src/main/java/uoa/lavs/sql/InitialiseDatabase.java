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
      createLoanEntity(conn);
      createLoanDurationEntity(conn);
      createLoanPaymentEntity(conn);
      createLoanCoborrowerEntity(conn);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createCustomerEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer ("
            + "customerId VARCHAR(50) PRIMARY KEY, "
            + "title VARCHAR(10), "
            + "name VARCHAR(100), "
            + "dateOfBirth DATE, "
            + "occupation VARCHAR(100), "
            + "residency VARCHAR(50), "
            + "notes VARCHAR(200), "
            + "physicalAddressId VARCHAR(50), "
            + "mailingAddressId VARCHAR(50), "
            + "contactId VARCHAR(50), "
            + "employerId VARCHAR(50), "
            + "FOREIGN KEY (physicalAddressId) REFERENCES Address(addressId), "
            + "FOREIGN KEY (mailingAddressId) REFERENCES Address(addressId), "
            + "FOREIGN KEY (contactId) REFERENCES CustomerContact(contactId), "
            + "FOREIGN KEY (employerId) REFERENCES CustomerEmployer(employerId)"
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
            + "alternateContact VARCHAR(50)"
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
            + "addressId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "addressType VARCHAR(50), "
            + "addressLineOne VARCHAR(100), "
            + "addressLineTwo VARCHAR(100), "
            + "suburb VARCHAR(50), "
            + "postCode VARCHAR(20), "
            + "city VARCHAR(50), "
            + "country VARCHAR(50)"
            + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void createCustomerEmployerEntity(Connection conn) {
    String sql =
        "CREATE TABLE IF NOT EXISTS customer_employer ("
            + "employerId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "employerName VARCHAR(100), "
            + "employerAddressId VARCHAR(50), "
            + "employerEmail VARCHAR(100), "
            + "employerWebsite VARCHAR(100), "
            + "employerPhone VARCHAR(20), "
            + "ownerOfCompany BOOLEAN, "
            + "FOREIGN KEY (employerAddressId) REFERENCES Address(addressId)"
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
            + "loanTerm INT"
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
            + "interestOnly BOOLEAN"
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

  public static void main(String[] args) {
    new InitialiseDatabase();
  }
}
