package uoa.lavs.sql.sql_to_mainframe;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.oop.customer.CustomerEmployer;
import uoa.lavs.backend.oop.customer.Email;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.Note;
import uoa.lavs.backend.oop.customer.Phone;
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
import uoa.lavs.backend.sql.sql_to_mainframe.SyncLoan;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncManager;

public class SyncLoanTest {
  DatabaseConnection conn;
  AddressDAO addressDAO;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    addressDAO = new AddressDAO();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void testSync() throws IOException {
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

    LocalDate startDate = LocalDate.now();
    loanDuration = new LoanDuration("-1", startDate, 1, 1);

    loanPayment = new LoanPayment("-1", "daily", "weekly", "1000", false);

    coborrowers = new ArrayList<>();
    coborrowers.add("-2");
    coborrowers.add("");
    coborrowers.add("");

    loan = new PersonalLoan("-1", "-1", coborrowers, 1, 1, "fixed", loanDuration, loanPayment);

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

    SyncLoan syncLoan = new SyncLoan();
    SyncManager syncManager = new SyncManager(List.of(syncLoan));

    syncManager.syncAll(LocalDateTime.now());

    PersonalLoan updatedLoan =
        new PersonalLoan("-1", "-1", coborrowers, 10, 10, "fixed", loanDuration, loanPayment);
    loanDAO.updateLoan(updatedLoan);

    syncManager.syncAll(LocalDateTime.now());
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
  }
}
