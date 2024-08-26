package uoa.lavs.sql.sql_to_mainframe;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
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
import uoa.lavs.backend.sql.sql_to_mainframe.SyncEmail;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncManager;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;
import uoa.lavs.mainframe.simulator.nitrite.DatabaseHelper;

public class SyncEmailTest {
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
  public void testSync_ValidEmail() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    SyncEmail syncEmail = new SyncEmail();
    SyncManager syncManager = new SyncManager(List.of(syncEmail));

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
    note = new Note("-12929292", new String[19]);
    note.getLines()[0] = "This is a note";
    notes.add(note);
    addresses = new ArrayList<>();
    address =
        new Address(
            "-12929292",
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
    phone = new Phone("-12929292", "mobile", "027", "1234567890", true, true);
    phones.add(phone);
    emails = new ArrayList<>();
    email = new Email("-12929292", "abc@gmail.com", true);
    emails.add(email);

    employer =
        new CustomerEmployer(
            "-12929292",
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
            "-12929292",
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

    Status status = syncManager.syncAll(LocalDateTime.now(ZoneOffset.UTC).minusDays(1), connection);

    connection.close();

    assert (status.getErrorCode() != 0);
  }
}
