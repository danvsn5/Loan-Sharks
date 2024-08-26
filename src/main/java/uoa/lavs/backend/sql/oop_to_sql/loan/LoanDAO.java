package uoa.lavs.backend.sql.oop_to_sql.loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.backend.oop.customer.Customer;
import uoa.lavs.backend.oop.loan.ILoan;
import uoa.lavs.backend.oop.loan.Loan;
import uoa.lavs.backend.oop.loan.LoanDuration;
import uoa.lavs.backend.oop.loan.LoanPayment;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.oop_to_sql.customer.CustomerDAO;

public class LoanDAO {
  // Adds a loan to the database
  public void addLoan(ILoan loan) {
    String sql =
        "INSERT INTO loan (loanId, customerId, principal, rate, rateType) VALUES (?, ?, ?, ?, ?)";
    add(loan, sql);
  }

  // Helper to add a loan to the database
  public void add(ILoan loan, String sql) {
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loan.getLoanId());
      pstmt.setString(2, loan.getCustomerId());
      pstmt.setDouble(3, loan.getPrincipal());
      pstmt.setDouble(4, loan.getRate());
      pstmt.setString(5, loan.getRateType());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Updates a loan in the database with new details from the loan object
  public void updateLoan(ILoan loan) {
    String sql =
        "UPDATE loan SET customerId = ?, principal = ?, rate = ?, rateType = ?, lastModified ="
            + " CURRENT_TIMESTAMP WHERE loanId = ?";
    update(loan, sql);
  }

  // Helper to update a loan in the database
  public void update(ILoan loan, String sql) {
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loan.getCustomerId());
      pstmt.setDouble(2, loan.getPrincipal());
      pstmt.setDouble(3, loan.getRate());
      pstmt.setString(4, loan.getRateType());
      pstmt.setString(5, loan.getLoanId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Get a loan from the database using the loanId
  public Loan getLoan(String loanId) {
    String sql = "SELECT * FROM loan WHERE loanId = ?";
    return get(loanId, sql);
  }

  // Helper to get a loan from the database
  public Loan get(String loanId, String sql) {
    Loan loan = null;

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loanId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        String customerId = rs.getString("customerId");
        Double principal = rs.getDouble("principal");
        Double rate = rs.getDouble("rate");
        String rateType = rs.getString("rateType");

        LoanDurationDAO durationDAO = new LoanDurationDAO();
        LoanDuration duration = durationDAO.getLoanDuration(loanId);

        LoanPaymentDAO paymentDAO = new LoanPaymentDAO();
        LoanPayment payment = paymentDAO.getLoanPayment(loanId);

        LoanCoborrowersDAO coborrowersDAO = new LoanCoborrowersDAO();
        ArrayList<String> coborrowerIds = coborrowersDAO.getCoborrowers(loanId);

        loan =
            new PersonalLoan(
                loanId, customerId, coborrowerIds, principal, rate, rateType, duration, payment);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return loan;
  }

  // Get all loans from the database associated with a customerId
  public ArrayList<Loan> getLoansFromCustomerId(String customerId) {
    String sql = "SELECT * FROM loan WHERE customerId = ?";
    ArrayList<Loan> loans = new ArrayList<>();

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      LoanDurationDAO durationDAO = new LoanDurationDAO();
      LoanPaymentDAO paymentDAO = new LoanPaymentDAO();
      LoanCoborrowersDAO coborrowersDAO = new LoanCoborrowersDAO();

      while (rs.next()) {
        String loanId = rs.getString("loanId");
        Double principal = rs.getDouble("principal");
        Double rate = rs.getDouble("rate");
        String rateType = rs.getString("rateType");

        LoanDuration duration = durationDAO.getLoanDuration(loanId);
        LoanPayment payment = paymentDAO.getLoanPayment(loanId);
        ArrayList<String> coborrowerIds = coborrowersDAO.getCoborrowers(loanId);

        loans.add(
            new PersonalLoan(
                loanId, customerId, coborrowerIds, principal, rate, rateType, duration, payment));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return loans;
  }

  // Get all loans from the database associated with a customer name
  public ArrayList<Loan> getLoansFromCustomerName(String customerName) {
    CustomerDAO customerDAO = new CustomerDAO();
    ArrayList<Customer> customers = customerDAO.getCustomersByName(customerName);

    if (customers.isEmpty()) {
      return new ArrayList<>();
    }

    ArrayList<Loan> loans = new ArrayList<>();

    for (Customer customer : customers) {

      String sql = "SELECT * FROM loan WHERE customerId = ?";

      try (Connection conn = DatabaseConnection.connect();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, customer.getCustomerId());
        ResultSet rs = pstmt.executeQuery();

        LoanDurationDAO durationDAO = new LoanDurationDAO();
        LoanPaymentDAO paymentDAO = new LoanPaymentDAO();
        LoanCoborrowersDAO coborrowersDAO = new LoanCoborrowersDAO();

        while (rs.next()) {
          String loanId = rs.getString("loanId");
          String customerId = rs.getString("customerId");
          Double principal = rs.getDouble("principal");
          Double rate = rs.getDouble("rate");
          String rateType = rs.getString("rateType");

          LoanDuration duration = durationDAO.getLoanDuration(loanId);
          LoanPayment payment = paymentDAO.getLoanPayment(loanId);
          ArrayList<String> coborrowerIds = coborrowersDAO.getCoborrowers(loanId);

          loans.add(
              new PersonalLoan(
                  loanId, customerId, coborrowerIds, principal, rate, rateType, duration, payment));
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
    return loans;
  }
}
