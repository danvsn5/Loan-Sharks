package uoa.lavs.sql.sql_to_mainframe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.oop.customer.CustomerEmployer;
import uoa.lavs.backend.oop.customer.Email;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.Note;
import uoa.lavs.backend.oop.customer.Phone;
import uoa.lavs.backend.oop.loan.Loan;
import uoa.lavs.backend.oop.loan.LoanDuration;
import uoa.lavs.backend.oop.loan.LoanPayment;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.backend.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.CustomerEmployerDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.EmailDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.NotesDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.PhoneDAO;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanCoborrowersDAO;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanDAO;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanDurationDAO;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanPaymentDAO;
import uoa.lavs.backend.sql.sql_to_mainframe.LoanCreationHelper;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoan;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanSummary;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;
import uoa.lavs.mainframe.simulator.nitrite.DatabaseHelper;

public class LoanCreationHelperTest {
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
    InitialiseDatabase.createDatabase();
    dbFile = DatabaseState.DB_TEST_FILE;

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
            true);
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
            "c@gmail.com",
            "xsaa.com",
            "832168516",
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

    startDate = LocalDate.now();
    loanDuration = new LoanDuration("-1", startDate, 1, 1);

    loanPayment = new LoanPayment("-1", "daily", "weekly", "1000", false);

    coborrowers = new ArrayList<>();
    coborrowers.add("-2");
    coborrowers.add("");
    coborrowers.add("");

    loan = new PersonalLoan("-1", "-1", coborrowers, 1, 1, "fixed", loanDuration, loanPayment);
  }

  @Test
  public void testValidateLoan() {
    assert LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_PrincipalZero() {
    loan.setPrincipal(0);
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_RateZero() {
    loan.setRate(0);
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_RateTypeNull() {
    loan.setRateType(null);
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_RateTypeEmpty() {
    loan.setRateType("");
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_StartDateNull() {
    loan.getDuration().setStartDate(null);
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_PeriodZero() {
    loan.getDuration().setPeriod(0);
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_LoanTermZero() {
    LoanCreationHelper loanCreationHelper = new LoanCreationHelper();
    loan.getDuration().setLoanTerm(0);
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_CompoundingEmpty() {
    loan.getPayment().setCompounding("");
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_PaymentFrequencyEmpty() {
    loan.getPayment().setPaymentFrequency("");
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_PaymentAmountEmpty() {
    loan.getPayment().setPaymentAmount("");
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testValidateLoan_PaymentAmountInvalid() {
    loan.getPayment().setPaymentAmount("abc");
    assert !LoanCreationHelper.validateLoan(loan);
  }

  @Test
  public void testCreateLoan_ExistingLoan() throws IOException {
    addressDAO.addAddress(primaryAddress);
    notesDAO.addNote(note);
    phoneDAO.addPhone(phone);
    emailDAO.addEmail(email);
    employerDAO.addCustomerEmployer(employer);

    loanDurationDAO = new LoanDurationDAO();
    loanDurationDAO.addLoanDuration(loanDuration);

    loanPaymentDAO = new LoanPaymentDAO();
    loanPaymentDAO.addLoanPayment(loanPayment);

    coborrowersDAO = new LoanCoborrowersDAO();
    coborrowersDAO.addCoborrowers("-1", coborrowers);

    customerDAO.addCustomer(customer);

    loanDAO = new LoanDAO();
    loanDAO.addLoan(loan);
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());

    LoanCreationHelper.createLoan(loan, connection);
    connection.close();

    Loan retrievedLoan = loanDAO.getLoan(loan.getLoanId());
    assertEquals(loan.getLoanId(), retrievedLoan.getLoanId());
  }

  @Test
  public void testCreateLoan_NoCoborrowers() throws IOException {
    ArrayList<String> newCoborrowers = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      newCoborrowers.add("");
    }
    loan.setCoborrowerIds(newCoborrowers);

    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());

    LoanCreationHelper.createLoan(loan, connection);

    LoadLoan loadLoan = new LoadLoan();
    loadLoan.setLoanId(loan.getLoanId());

    Status loadStatus = loadLoan.send(connection);
    connection.close();
    assert (loadStatus.getErrorCode() != 0);
  }

  @Test
  public void testGetLoanSummary() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    loan.setLoanId("123-09");
    LoadLoanSummary loanSummary = LoanCreationHelper.getLoanSummary(loan, connection);
    connection.close();
    assertNotNull(loanSummary);
  }

  @Test
  public void testGetLoanSummary_StatusError() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    connection.close();

    LoadLoanSummary loanSummary = LoanCreationHelper.getLoanSummary(loan, connection);
    assertNull(loanSummary);
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
  }
}
