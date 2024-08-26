package uoa.lavs.backend.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ResetDatabase {

  public ResetDatabase() {
    resetDatabase();
  }

  // Reset the database
  public void resetDatabase() {
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

  // Drop the loan_coborrower table
  private void dropLoanCoborrowerEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS loan_coborrower;";
    stmt.execute(sql);
  }

  // Drop the loan table
  private void dropLoanEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS loan;";
    stmt.execute(sql);
  }

  // Drop the loan_payment table
  private void dropLoanPaymentEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS loan_payment;";
    stmt.execute(sql);
  }

  // Drop the loan_duration table
  private void dropLoanDurationEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS loan_duration;";
    stmt.execute(sql);
  }

  // Drop the customer_employer table
  private void dropCustomerEmployerEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_employer;";
    stmt.execute(sql);
  }

  // Drop the customer_address table
  private void dropCustomerAddressEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_address;";
    stmt.execute(sql);
  }

  // Drop the customer_phone table
  private void dropCustomerPhoneEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_phone;";
    stmt.execute(sql);
  }

  // Drop the customer_email table
  private void dropCustomerEmailEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_email;";
    stmt.execute(sql);
  }

  // Drop the customer_notes table
  private void dropCustomerNotesEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer_notes;";
    stmt.execute(sql);
  }

  // Drop the customer table
  private void dropCustomerEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS customer;";
    stmt.execute(sql);
  }

  // Drop the sync_info table
  private void dropSyncInfoEntity(Statement stmt) throws SQLException {
    String sql = "DROP TABLE IF EXISTS sync_info;";
    stmt.execute(sql);
  }

  public static void main(String[] args) {
    new ResetDatabase();
  }
}
