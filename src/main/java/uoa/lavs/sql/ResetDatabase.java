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
            dropCustomerContactEntity(stmt);
            dropCustomerEntity(stmt);
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

    private void dropCustomerContactEntity(Statement stmt) throws SQLException {
        String sql = "DROP TABLE IF EXISTS customer_contact;";
        stmt.execute(sql);
    }

    private void dropCustomerEntity(Statement stmt) throws SQLException {
        String sql = "DROP TABLE IF EXISTS customer;";
        stmt.execute(sql);
    }

    public static void main(String[] args) {
        new ResetDatabase();
    }
}

