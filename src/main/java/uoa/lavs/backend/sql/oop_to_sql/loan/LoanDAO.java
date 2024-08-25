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
  public void addLoan(ILoan loan) {
    String sql =
        "INSERT INTO loan (loanId, customerId, principal, rate, rateType) VALUES (?, ?, ?, ?, ?)";
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

  public void updateLoan(ILoan loan) {
    String sql =
        "UPDATE loan SET customerId = ?, principal = ?, rate = ?, rateType = ? WHERE loanId = ?";
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

  public Loan getLoan(String loanId) {
    String sql = "SELECT * FROM loan WHERE loanId = ?";

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

        return new PersonalLoan(
            loanId, customerId, coborrowerIds, principal, rate, rateType, duration, payment);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

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

  /*
  public static void addLoanTest() {
    CustomerEmployerDAO employerDAO;
    CustomerEmployer employer;
    AddressDAO addressDAO;
    ArrayList<Note> notes;
    Note note;
    NotesDAO notesDAO;
    ArrayList<Address> addresses;
    Address primaryAddress;
    ArrayList<Phone> phones;
    Phone phone;
    PhoneDAO phoneDAO;
    ArrayList<Email> emails;
    Email email;
    EmailDAO emailDAO;
    CustomerDAO customerDAO;
    IndividualCustomer customer;
    LocalDate dateOfBirth;

    PersonalLoan loan;
    LoanDAO loanDAO;
    LoanPayment loanPayment;
    LoanPaymentDAO loanPaymentDAO;
    LoanDuration loanDuration;
    LoanDurationDAO loanDurationDAO;
    ArrayList<String> coborrowers;
    LoanCoborrowersDAO coborrowersDAO;

    LocalDate startDate;

    employerDAO = new CustomerEmployerDAO();
    addressDAO = new AddressDAO();
    phoneDAO = new PhoneDAO();
    emailDAO = new EmailDAO();
    notesDAO = new NotesDAO();
    customerDAO = new CustomerDAO();
    dateOfBirth = LocalDate.of(2024, 8, 6);

    notes = new ArrayList<>();
    note = new Note("-1", new String[] {"Allergic to peanuts"});
    notes.add(note);
    addresses = new ArrayList<>();
    primaryAddress =
        new Address(
            "-1",
            "Rural",
            "304 Rose St",
            "46",
            "Sunnynook",
            "12345",
            "Auckland",
            "Zimbabwe",
            true,
            false);
    addresses.add(primaryAddress);

    phones = new ArrayList<>();
    phone = new Phone("-1", "mobile", "027", "1234567890", true, true);
    phones.add(phone);
    emails = new ArrayList<>();
    email = new Email("-1", "abc@gmail.com", true);
    emails.add(email);

    employer =
        new CustomerEmployer(
            "-1",
            "Countdown",
            "123 Stonesuckle Ct",
            "",
            "Sunnynook",
            "Auckland",
            "12345",
            "Zimbabwe",
            null,
            null,
            null,
            false);

    customer =
        new IndividualCustomer(
            "-1",
            "Mr",
            "Ting Mun Guy",
            dateOfBirth,
            "Engineer",
            "NZ Citizen",
            "Zimbabwe",
            notes,
            addresses,
            phones,
            emails,
            employer);

    addressDAO.addAddress(primaryAddress);
    notesDAO.addNote(note);
    phoneDAO.addPhone(phone);
    emailDAO.addEmail(email);
    employerDAO.addCustomerEmployer(employer);

    startDate = LocalDate.now();
    loanDuration = new LoanDuration("-1", startDate, 0, 0);
    loanDurationDAO = new LoanDurationDAO();
    loanDurationDAO.addLoanDuration(loanDuration);

    loanPayment = new LoanPayment("-1", "daily", "weekly", "1000", false);
    loanPaymentDAO = new LoanPaymentDAO();
    loanPaymentDAO.addLoanPayment(loanPayment);

    coborrowers = new ArrayList<>();
    coborrowersDAO = new LoanCoborrowersDAO();
    coborrowersDAO.addCoborrowers("-1", coborrowers);

    customerDAO.addCustomer(customer);

    loan = new PersonalLoan("-1", "-1", coborrowers, 0, 0, "fixed", loanDuration, loanPayment);
    loanDAO = new LoanDAO();

    loanDAO.addLoan(loan);
  }

  public static void main(String[] args) {
    addLoanTest();
  }

   */
}
