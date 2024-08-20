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
import uoa.lavs.customer.CustomerContact;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.Note;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;
import uoa.lavs.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerContactDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerEmployerDAO;

public class CustomerDAOTest {
  DatabaseConnection conn;
  CustomerEmployerDAO employerDAO;
  CustomerEmployer employer;
  AddressDAO addressDAO;
  ArrayList<Note> notes;
  Note note;
  CustomerContactDAO contactDAO;
  CustomerContact contact;
  Address primaryAddress;
  CustomerDAO customerDAO;
  IndividualCustomer customer;
  LocalDate dateOfBirth;
  Phone phoneOne;
  Phone phoneTwo;

  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    dbFile = DatabaseState.DB_TEST_FILE;
    InitialiseDatabase.createDatabase();
    employerDAO = new CustomerEmployerDAO();
    addressDAO = new AddressDAO();
    contactDAO = new CustomerContactDAO();
    customerDAO = new CustomerDAO();
    dateOfBirth = LocalDate.of(2024, 8, 6);

    notes = new ArrayList<>();
    note = new Note("000001", new String[] {"Allergic to peanuts"});
    notes.add(note);

    primaryAddress =
        new Address(
            "000001",
            "Rural",
            "304 Rose St",
            "46",
            "Sunnynook",
            "12345",
            "Auckland",
            "Zimbabwe",
            true,
            false);
    phoneOne = new Phone("mobile", "1234567890");
    phoneTwo = new Phone("home", "0987654321");
    contact = new CustomerContact("abc@gmail.com", phoneOne, phoneTwo, "mobile sms", "email");
    employer =
        new CustomerEmployer(
            "000001",
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
            "000001",
            "Mr",
            "Ting Mun Guy",
            dateOfBirth,
            "Engineer",
            "NZ Citizen",
            notes,
            primaryAddress,
            primaryAddress,
            contact,
            employer);

    addressDAO.addAddress(primaryAddress);
    contactDAO.addCustomerContact(contact);
    employerDAO.addCustomerEmployer(employer);
  }

  @Test
  public void testAddCustomer() {
    customerDAO.addCustomer(customer);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer WHERE customerId = ?")) {
      stmt.setString(1, "000001");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer should be added to the database");
        Assertions.assertEquals("000001", rs.getString("customerId"));
        Assertions.assertEquals("Mr", rs.getString("title"));
        Assertions.assertEquals("Ting Mun Guy", rs.getString("name"));
        LocalDate actualDateOfBirth = rs.getDate("dateOfBirth").toLocalDate();
        Assertions.assertEquals(LocalDate.of(2024, 8, 6), actualDateOfBirth);
        Assertions.assertEquals("Engineer", rs.getString("occupation"));
        Assertions.assertEquals("NZ Citizen", rs.getString("residency"));

        // String notesString = rs.getString("notes");
        // String[] retrievedNotes = notesString.split("::");
        // for (int i = 0; i < retrievedNotes.length; i++) {
        //   Assertions.assertEquals(customer.getNotes().get(i).getLines(), retrievedNotes[i]);
        // }

        Assertions.assertEquals(primaryAddress.getAddressId(), rs.getInt("primaryAddressId"));
        Assertions.assertEquals(primaryAddress.getAddressId(), rs.getInt("mailingAddressId"));
        Assertions.assertEquals(contact.getContactId(), rs.getInt("contactId"));
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
    customer.setResidency("NZ Permanent Resident");
    customer.setPhysicalAddress(primaryAddress);
    customer.setMailingAddress(primaryAddress);
    customer.setContact(contact);
    customer.setEmployer(employer);

    customerDAO.updateCustomer(customer);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer WHERE customerId = ?")) {
      stmt.setString(1, "000001");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Customer should be updated in the database");
        Assertions.assertEquals("000001", rs.getString("customerId"));
        Assertions.assertEquals("Mrs", rs.getString("title"));
        Assertions.assertEquals("Ting Ting Mun Mun Guy Guy", rs.getString("name"));
        LocalDate actualDateOfBirth = rs.getDate("dateOfBirth").toLocalDate();
        Assertions.assertEquals(LocalDate.of(2024, 8, 7), actualDateOfBirth);
        Assertions.assertEquals("Doctor", rs.getString("occupation"));
        Assertions.assertEquals("NZ Permanent Resident", rs.getString("residency"));
        Assertions.assertEquals(primaryAddress.getAddressId(), rs.getInt("primaryAddressId"));
        Assertions.assertEquals(primaryAddress.getAddressId(), rs.getInt("mailingAddressId"));
        Assertions.assertEquals(contact.getContactId(), rs.getInt("contactId"));
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
        Assertions.assertEquals(retrievedCustomer.getResidency(), rs.getString("residency"));
        Assertions.assertEquals(
            retrievedCustomer.getPhysicalAddress().getAddressId(), rs.getInt("primaryAddressId"));
        Assertions.assertEquals(
            retrievedCustomer.getMailingAddress().getAddressId(), rs.getInt("mailingAddressId"));
        Assertions.assertEquals(
            retrievedCustomer.getContact().getContactId(), rs.getInt("contactId"));
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
    Address primaryAddress2 =
        new Address(
            "000001",
            "Rural",
            "100 Guy St",
            "",
            "Rosedale",
            "999",
            "Auckland",
            "New Zealand",
            true,
            false);
    Phone phoneOne2 = new Phone("mobile", "1111111");
    Phone phoneTwo2 = new Phone("work", "0987654321");
    CustomerContact contact2 =
        new CustomerContact("abc@gmail.com", phoneOne2, phoneTwo2, "mobile sms", "email");
    CustomerEmployer employer2 =
        new CustomerEmployer(
          "000001",
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
    Note note = new Note("000003", new String[] {"Allergic to peanuts"});
    notes.add(note);

    IndividualCustomer customer2 =
        new IndividualCustomer(
            "000003",
            "Mrs",
            name,
            dateOfBirth2,
            "Professional",
            "NZ Permanent Resident",
            notes,
            primaryAddress2,
            primaryAddress2,
            contact2,
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

    Address primaryAddress2 =
        new Address(
            "000001",
            "Rural",
            "100 Guy St",
            "",
            "Rosedale",
            "999",
            "Auckland",
            "New Zealand",
            true,
            false);
    Phone phoneOne2 = new Phone("mobile", "1111111");
    Phone phoneTwo2 = new Phone("work", "0987654321");
    CustomerContact contact2 =
        new CustomerContact("abc@gmail.com", phoneOne2, phoneTwo2, "mobile sms", "email");
    CustomerEmployer employer2 =
        new CustomerEmployer(
          "000001",
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
    Note note = new Note("000002", new String[] {"Allergic to peanuts"});
    notes.add(note);

    IndividualCustomer customer2 =
        new IndividualCustomer(
            "000002",
            "Mrs",
            "Zing Dingus",
            dateOfBirth,
            "Professional",
            "NZ Permanent Resident",
            notes,
            primaryAddress2,
            primaryAddress2,
            contact2,
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
