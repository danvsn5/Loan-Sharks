package uoa.lavs.sql.customer;

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
import uoa.lavs.customer.Customer;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.Email;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.Note;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;
import uoa.lavs.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerEmployerDAO;
import uoa.lavs.sql.oop_to_sql.customer.EmailDAO;
import uoa.lavs.sql.oop_to_sql.customer.NotesDAO;
import uoa.lavs.sql.oop_to_sql.customer.PhoneDAO;

public class CustomerDAOTest {
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
            "B2",
            "New Zealand",
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
  }

  @Test
  public void testAddCustomer() {
    customerDAO.addCustomer(customer);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer WHERE customerId = ?")) {
      stmt.setString(1, "-1");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer should be added to the database");
        Assertions.assertEquals("-1", rs.getString("customerId"));
        Assertions.assertEquals("Mr", rs.getString("title"));
        Assertions.assertEquals("Ting Mun Guy", rs.getString("name"));
        LocalDate actualDateOfBirth = rs.getDate("dateOfBirth").toLocalDate();
        Assertions.assertEquals(LocalDate.of(2024, 8, 6), actualDateOfBirth);
        Assertions.assertEquals("Engineer", rs.getString("occupation"));
        Assertions.assertEquals("B2", rs.getString("visa"));
        Assertions.assertEquals("New Zealand", rs.getString("citizenship"));

        // String notesString = rs.getString("notes");
        // String[] retrievedNotes = notesString.split("::");
        // for (int i = 0; i < retrievedNotes.length; i++) {
        //   Assertions.assertEquals(customer.getNotes().get(i).getLines(), retrievedNotes[i]);
        // }

      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateCustomer() {
    customerDAO.addCustomer(customer);
    customer.setTitle("Mrs");
    customer.setName("Ting Ting Mun Mun Guy Guy");
    customer.setDateOfBirth(LocalDate.of(2024, 8, 7));
    customer.setOccupation("Doctor");
    customer.setVisa("B1");
    customer.setCitizenship("Zimbabwe");
    customer.setEmployer(employer);

    customerDAO.updateCustomer(customer);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer WHERE customerId = ?")) {
      stmt.setString(1, "-1");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer should be updated in the database");
        Assertions.assertEquals("-1", rs.getString("customerId"));
        Assertions.assertEquals("Mrs", rs.getString("title"));
        Assertions.assertEquals("Ting Ting Mun Mun Guy Guy", rs.getString("name"));
        LocalDate actualDateOfBirth = rs.getDate("dateOfBirth").toLocalDate();
        Assertions.assertEquals(LocalDate.of(2024, 8, 7), actualDateOfBirth);
        Assertions.assertEquals("Doctor", rs.getString("occupation"));
        Assertions.assertEquals("B1", rs.getString("visa"));
        Assertions.assertEquals("Zimbabwe", rs.getString("citizenship"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetCustomer() {
    customerDAO.addCustomer(customer);

    Customer retrievedCustomer = customerDAO.getCustomer(customer.getCustomerId());

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer WHERE customerId = ?")) {
      stmt.setString(1, customer.getCustomerId());

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer should be in the database");
        Assertions.assertEquals(retrievedCustomer.getCustomerId(), rs.getString("customerId"));
        Assertions.assertEquals(retrievedCustomer.getTitle(), rs.getString("title"));
        Assertions.assertEquals(retrievedCustomer.getName(), rs.getString("name"));
        LocalDate actualDateOfBirth = rs.getDate("dateOfBirth").toLocalDate();
        Assertions.assertEquals(retrievedCustomer.getDateOfBirth(), actualDateOfBirth);
        Assertions.assertEquals(retrievedCustomer.getOccupation(), rs.getString("occupation"));
        Assertions.assertEquals(retrievedCustomer.getVisa(), rs.getString("visa"));
        Assertions.assertEquals(retrievedCustomer.getCitizenship(), rs.getString("citizenship"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetCustomersByName() {
    customerDAO.addCustomer(customer);

    LocalDate dateOfBirth2 = LocalDate.of(2000, 6, 6);
    Phone phone2 = new Phone("-3", "mobile", "027", "1111111", true, true);
    Email email2 = new Email("-3", "aaa@gmail.com", true);
    ArrayList<Phone> phones2 = new ArrayList<>();
    phones2.add(phone2);
    ArrayList<Email> emails2 = new ArrayList<>();
    emails2.add(email2);

    CustomerEmployer employer2 =
        new CustomerEmployer(
            "-3",
            "Countdown",
            "",
            "",
            "",
            "",
            "",
            "",
            "manager@store.veryworth.org.au",
            "www.veryworth.org.au",
            "1234567890",
            false);

    String name = "Ting Mun Guy";

    ArrayList<Note> notes = new ArrayList<>();
    Note note = new Note("-3", new String[] {"Allergic to peanuts"});
    notes.add(note);

    IndividualCustomer customer2 =
        new IndividualCustomer(
            "-3",
            "Mrs",
            name,
            dateOfBirth2,
            "Professional",
            "B2",
            "Zimbabwe",
            notes,
            addresses,
            phones2,
            emails2,
            employer2);
    customerDAO.addCustomer(customer2);

    ArrayList<Customer> customers = customerDAO.getCustomersByName(name);

    Assertions.assertEquals(
        2, customers.size(), "There should be two customers with the same name");
    Customer retrievedCustomer1 = customers.get(0);
    Customer retrievedCustomer2 = customers.get(1);

    Assertions.assertEquals(customer.getCustomerId(), retrievedCustomer1.getCustomerId());
    Assertions.assertEquals(customer.getTitle(), retrievedCustomer1.getTitle());
    Assertions.assertEquals(customer.getName(), retrievedCustomer1.getName());
    Assertions.assertEquals(customer.getDateOfBirth(), retrievedCustomer1.getDateOfBirth());

    Assertions.assertEquals(customer2.getCustomerId(), retrievedCustomer2.getCustomerId());
    Assertions.assertEquals(customer2.getTitle(), retrievedCustomer2.getTitle());
    Assertions.assertEquals(customer2.getName(), retrievedCustomer2.getName());
    Assertions.assertEquals(dateOfBirth2, retrievedCustomer2.getDateOfBirth());
  }

  @Test
  public void testGetCustomersByBirth() {
    customerDAO.addCustomer(customer);

    Phone phone2 = new Phone("-2", "mobile", "027", "1111111", true, true);
    Email email2 = new Email("-2", "aaa@gmail.com", true);
    ArrayList<Phone> phones2 = new ArrayList<>();
    phones2.add(phone2);
    ArrayList<Email> emails2 = new ArrayList<>();
    emails2.add(email2);

    CustomerEmployer employer2 =
        new CustomerEmployer(
            "-2",
            "Countdown",
            "",
            "",
            "",
            "",
            "",
            "",
            "manager@store.veryworth.org.au",
            "www.veryworth.org.au",
            "1234567890",
            false);

    ArrayList<Note> notes = new ArrayList<>();
    Note note = new Note("-2", new String[] {"Allergic to peanuts"});
    notes.add(note);

    IndividualCustomer customer2 =
        new IndividualCustomer(
            "-2",
            "Mrs",
            "Zing Dingus",
            dateOfBirth,
            "Professional",
            "B2",
            "Zimbabwe",
            notes,
            addresses,
            phones2,
            emails2,
            employer2);
    customerDAO.addCustomer(customer2);

    ArrayList<Customer> customers = customerDAO.getCustomersByBirth(dateOfBirth);

    Assertions.assertEquals(
        2, customers.size(), "There should be two customers with the same name");
    Customer retrievedCustomer1 = customers.get(0);
    Customer retrievedCustomer2 = customers.get(1);

    Assertions.assertEquals(customer.getCustomerId(), retrievedCustomer1.getCustomerId());
    Assertions.assertEquals(customer.getTitle(), retrievedCustomer1.getTitle());
    Assertions.assertEquals(customer.getName(), retrievedCustomer1.getName());
    Assertions.assertEquals(customer.getDateOfBirth(), retrievedCustomer1.getDateOfBirth());

    Assertions.assertEquals(customer2.getCustomerId(), retrievedCustomer2.getCustomerId());
    Assertions.assertEquals(customer2.getTitle(), retrievedCustomer2.getTitle());
    Assertions.assertEquals(customer2.getName(), retrievedCustomer2.getName());
    Assertions.assertEquals(customer2.getDateOfBirth(), retrievedCustomer2.getDateOfBirth());
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
