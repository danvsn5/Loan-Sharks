package uoa.lavs.sql.loan;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.Email;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.Note;
import uoa.lavs.customer.Phone;
import uoa.lavs.loan.Loan;
import uoa.lavs.loan.LoanDuration;
import uoa.lavs.loan.LoanPayment;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;
import uoa.lavs.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerEmployerDAO;
import uoa.lavs.sql.oop_to_sql.customer.EmailDAO;
import uoa.lavs.sql.oop_to_sql.customer.NotesDAO;
import uoa.lavs.sql.oop_to_sql.customer.PhoneDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanCoborrowersDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanDurationDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanPaymentDAO;

public class LoanDAOTest {
  DatabaseConnection conn;
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

  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    dbFile = DatabaseState.DB_TEST_FILE;
    InitialiseDatabase.createDatabase();

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
  }

  @Test
  public void testAddLoan() {
    loanDAO.addLoan(loan);

    String loanId = loan.getLoanId();

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM loan WHERE loanId = ?")) {
      stmt.setString(1, loanId);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Loan should be added to the database");
        Assertions.assertEquals(loanId, rs.getString("loanId"));
        Assertions.assertEquals("-1", rs.getString("customerId"));
        Assertions.assertEquals(0, rs.getDouble("principal"));
        Assertions.assertEquals(0, rs.getDouble("rate"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateLoan() {
    loanDAO.addLoan(loan);
    loan.setPrincipal(1000);
    loan.setRate(5);

    loanDAO.updateLoan(loan);

    String loanId = loan.getLoanId();

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM loan WHERE loanId = ?")) {
      stmt.setString(1, loanId);

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Loan should be updated in the database");
        Assertions.assertEquals(loanId, rs.getString("loanId"));
        Assertions.assertEquals("-1", rs.getString("customerId"));
        Assertions.assertEquals(1000, rs.getDouble("principal"));
        Assertions.assertEquals(5, rs.getDouble("rate"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetLoan() {
    loanDAO.addLoan(loan);

    Loan retrievedLoan = loanDAO.getLoan("-1");

    Assertions.assertEquals("-1", retrievedLoan.getLoanId());
    Assertions.assertEquals("-1", retrievedLoan.getCustomerId());
    Assertions.assertEquals(0, retrievedLoan.getPrincipal());
    Assertions.assertEquals(0, retrievedLoan.getRate());
    Assertions.assertEquals(startDate, retrievedLoan.getDuration().getStartDate());
    Assertions.assertEquals(0, retrievedLoan.getDuration().getPeriod());
    Assertions.assertEquals(0, retrievedLoan.getDuration().getLoanTerm());
    Assertions.assertEquals("daily", retrievedLoan.getPayment().getCompounding());
    Assertions.assertEquals("weekly", retrievedLoan.getPayment().getPaymentFrequency());
    Assertions.assertEquals("1000", retrievedLoan.getPayment().getPaymentAmount());
    Assertions.assertEquals(false, retrievedLoan.getPayment().getInterestOnly());
  }

  @Test
  public void testGetLoansFromCustomerId() {
    loanDAO.addLoan(loan);

    ArrayList<Loan> loans = loanDAO.getLoansFromCustomerId("-1");

    Assertions.assertEquals(1, loans.size());
    Loan retrievedLoan = loans.get(0);
    Assertions.assertEquals("-1", retrievedLoan.getLoanId());
    Assertions.assertEquals("-1", retrievedLoan.getCustomerId());
    Assertions.assertEquals(0, retrievedLoan.getPrincipal());
    Assertions.assertEquals(0, retrievedLoan.getRate());
    Assertions.assertEquals(startDate, retrievedLoan.getDuration().getStartDate());
    Assertions.assertEquals(0, retrievedLoan.getDuration().getPeriod());
    Assertions.assertEquals(0, retrievedLoan.getDuration().getLoanTerm());
    Assertions.assertEquals("daily", retrievedLoan.getPayment().getCompounding());
    Assertions.assertEquals("weekly", retrievedLoan.getPayment().getPaymentFrequency());
    Assertions.assertEquals("1000", retrievedLoan.getPayment().getPaymentAmount());
    Assertions.assertEquals(false, retrievedLoan.getPayment().getInterestOnly());
  }

  @Test
  public void testGetLoansFromCustomerName() {
    loanDAO.addLoan(loan);

    ArrayList<Loan> loans = loanDAO.getLoansFromCustomerName("Ting Mun Guy");

    Assertions.assertEquals(1, loans.size());
    Loan retrievedLoan = loans.get(0);
    Assertions.assertEquals("-1", retrievedLoan.getLoanId());
    Assertions.assertEquals("-1", retrievedLoan.getCustomerId());
    Assertions.assertEquals(0, retrievedLoan.getPrincipal());
    Assertions.assertEquals(0, retrievedLoan.getRate());
    Assertions.assertEquals(startDate, retrievedLoan.getDuration().getStartDate());
    Assertions.assertEquals(0, retrievedLoan.getDuration().getPeriod());
    Assertions.assertEquals(0, retrievedLoan.getDuration().getLoanTerm());
    Assertions.assertEquals("daily", retrievedLoan.getPayment().getCompounding());
    Assertions.assertEquals("weekly", retrievedLoan.getPayment().getPaymentFrequency());
    Assertions.assertEquals("1000", retrievedLoan.getPayment().getPaymentAmount());
    Assertions.assertEquals(false, retrievedLoan.getPayment().getInterestOnly());
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
    if (!dbFile.delete()) {
      throw new RuntimeException(
          "Failed to delete test database file: " + dbFile.getAbsolutePath());
    }
  }
}
