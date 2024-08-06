package uoa.lavs.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InitialiseDatabase {

  public InitialiseDatabase() {
    createDatabase();
  }

  private void createDatabase() {
    try (Connection conn = DatabaseConnection.connect()) {
      createCustomerEntity(conn);
      createCustomerContactEntity(conn);
      createCustomerPhonesEntity(conn);
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

  private void createCustomerEntity(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS customer (\n"
        + "  id TEXT NOT NULL,\n"
        + "  title TEXT NOT NULL,\n"
        + "  first_name TEXT NOT NULL,\n"
        + "  middle_name TEXT NOT NULL,\n"
        + "  last_name TEXT NOT NULL,\n"
        + "  date_of_birth TEXT NOT NULL,\n"
        + "  occupation TEXT NOT NULL,\n"
        + "  notes TEXT NOT NULL\n"
        + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void createCustomerContactEntity(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS customer_contact (\n"
        + "  customer_id INTEGER NOT NULL,\n"
        + "  email TEXT NOT NULL,\n"
        + "  pref_contact TEXT NOT NULL,\n"
        + "  alt_contact TEXT NOT NULL,\n"
        + "  FOREIGN KEY (customer_id) REFERENCES customer (id)\n"
        + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void createCustomerPhonesEntity(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS customer_phones (\n"
        + "  contact_id TEXT NOT NULL,\n"
        + "  home_phone TEXT NOT NULL,\n"
        + "  work_phone TEXT NOT NULL,\n"
        + "  mobile_phone TEXT NOT NULL,\n"
        + "  FOREIGN KEY (contact_id) REFERENCES customer_contact (customer_id)\n"
        + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void createCustomerAddressEntity(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS customer_address (\n"
        + "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
        + "  customer_id TEXT NOT NULL,\n"
        + "  addresss_line_one TEXT NOT NULL,\n"
        + "  addresss_line_two TEXT NOT NULL,\n"
        + "  suburb TEXT NOT NULL,\n"
        + "  post_code TEXT NOT NULL,\n"
        + "  city TEXT NOT NULL,\n"
        + "  address_type TEXT NOT NULL,\n"
        + "  FOREIGN KEY (customer_id) REFERENCES customer (id)\n"
        + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void createCustomerEmployerEntity(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS customer_employer (\n"
        + "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
        + "  customer_id TEXT NOT NULL,\n"
        + "  employer_name TEXT NOT NULL,\n"
        + "  addresss TEXT NOT NULL,\n"
        + "  website TEXT NOT NULL,\n"
        + "  phone TEXT NOT NULL,\n"
        + "  owner_of_company BOOLEAN NOT NULL,\n"
        + "  FOREIGN KEY (customer_id) REFERENCES customer (id)\n"
        + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void createLoanEntity(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS loan (\n"
        + "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
        + "  customer_id TEXT NOT NULL,\n"
        + "  coborrower_ids TEXT NOT NULL,\n"
        + "  principal,\n"
        + "  loan_purpose TEXT NOT NULL,\n"
        + "  loan_status TEXT NOT NULL,\n"
        + "  FOREIGN KEY (customer_id) REFERENCES customer (id)\n"
        + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void createLoanDurationEntity(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS loan_duration (\n"
        + "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
        + "  loan_id TEXT NOT NULL,\n"
        + "  start_date TEXT NOT NULL,\n"
        + "  period INTEGER NOT NULL,\n"
        + "  loan_term INTEGER NOT NULL,\n"
        + "  FOREIGN KEY (loan_id) REFERENCES loan (id)\n"
        + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void createLoanPaymentEntity(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS loan_payment (\n"
        + "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
        + "  loan_id TEXT NOT NULL,\n"
        + "  compounding TEXT NOT NULL,\n"
        + "  payment_frequency TEXT NOT NULL,\n"
        + "  payment_amount TEXT NOT NULL,\n"
        + "  interest_only BOOLEAN NOT NULL,\n"
        + "  FOREIGN KEY (loan_id) REFERENCES loan (id)\n"
        + ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void createLoanCoborrowerEntity(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS loan_coborrower (\n"
        + "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
        + "  loan_id TEXT NOT NULL,\n"
        + "  coborrower_id TEXT NOT NULL,\n"
        + "  FOREIGN KEY (loan_id) REFERENCES loan (id),\n"
        + "  FOREIGN KEY (coborrower_id) REFERENCES customer (id)\n"
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
