package uoa.lavs.sql.sql_to_mainframe;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.backend.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.CustomerEmployerDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.EmailDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.NotesDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.PhoneDAO;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncManager;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncNotes;
import uoa.lavs.legacy.mainframe.Status;

public class SyncNotesTest {
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
    CustomerEmployer employer;
    ArrayList<Note> notes;
    Note note;
    ArrayList<Address> addresses;
    Address address;
    ArrayList<Phone> phones;
    Phone phone;
    ArrayList<Email> emails;
    Email email;
    IndividualCustomer customer;
    LocalDate dateOfBirth;
    dateOfBirth = LocalDate.of(2024, 8, 6);
    notes = new ArrayList<>();
    note = new Note("-1", new String[19]);
    note.getLines()[0] = "This is a note";
    notes.add(note);
    addresses = new ArrayList<>();
    address =
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
    addresses.add(address);

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
            "goon@gmail.com",
            "goon.com",
            "000222",
            false);

    customer =
        new IndividualCustomer(
            "-1",
            "Mr",
            "Ting Mun Guy",
            dateOfBirth,
            "Engineer",
            "B2",
            "New Zealand",
            notes,
            addresses,
            phones,
            emails,
            employer);

    CustomerEmployerDAO employerDAO;
    AddressDAO addressDAO;
    PhoneDAO phoneDAO;
    EmailDAO emailDAO;
    NotesDAO notesDAO;
    CustomerDAO customerDAO;
    employerDAO = new CustomerEmployerDAO();
    addressDAO = new AddressDAO();
    phoneDAO = new PhoneDAO();
    emailDAO = new EmailDAO();
    notesDAO = new NotesDAO();
    customerDAO = new CustomerDAO();

    employerDAO.addCustomerEmployer(employer);
    for (Address a : addresses) {
      addressDAO.addAddress(a);
    }
    for (Phone a : phones) {
      phoneDAO.addPhone(a);
    }
    for (Email a : emails) {
      emailDAO.addEmail(a);
    }
    for (Note a : notes) {
      notesDAO.addNote(a);
    }
    customerDAO.addCustomer(customer);

    SyncNotes syncNotes = new SyncNotes();
    SyncManager syncManager = new SyncManager(List.of(syncNotes));
    Status status = syncManager.syncAll(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
  }
}
