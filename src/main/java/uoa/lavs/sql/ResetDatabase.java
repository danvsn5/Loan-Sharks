package uoa.lavs.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ResetDatabase {

  public ResetDatabase() {
    resetDatabase();
  }

  private void resetDatabase() {
    try (Connection conn = DatabaseConnection.connect();
        Statement stmt = conn.createStatement()) {
      dropLoanCoborrowerEntity(stmt);
      dropLoanEntity(stmt);
      dropLoanPaymentEntity(stmt);
      dropLoanDurationEntity(stmt);
      dropCustomerEmployerEntity(stmt);
      dropCustomerAddressEntity(stmt);
      dropCustomerPhoneEntity(stmt);
      dropCustomerEmailEntity(stmt);
      dropCustomerNotesEntity(stmt);
      dropCustomerEntity(stmt);
      dropSyncInfoEntity(stmt);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void dropLoanCoborrowerEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS loan_coborrower;";
    stmt.execute(sql);
  }

  private void dropLoanEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS loan;";
    stmt.execute(sql);
  }

  private void dropLoanPaymentEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS loan_payment;";
    stmt.execute(sql);
  }

  private void dropLoanDurationEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS loan_duration;";
    stmt.execute(sql);
  }

  private void dropCustomerEmployerEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_employer;";
    stmt.execute(sql);
  }

  private void dropCustomerAddressEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_address;";
    stmt.execute(sql);
  }

  private void dropCustomerPhoneEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_phone;";
    stmt.execute(sql);
  }

  private void dropCustomerEmailEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_email;";
    stmt.execute(sql);
  }

  private void dropCustomerNotesEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_notes;";
    stmt.execute(sql);
  }

  private void dropCustomerEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer;";
    stmt.execute(sql);
  }

  private void dropSyncInfoEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS sync_info;";
    stmt.execute(sql);
  }

  public static void main(String[] args) {
    new ResetDatabase();
  }
}
