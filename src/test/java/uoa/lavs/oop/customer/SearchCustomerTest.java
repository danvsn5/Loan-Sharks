package uoa.lavs.oop.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.oop.customer.Customer;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.SearchCustomer;
import uoa.lavs.backend.sql.DatabaseConnection;
import uoa.lavs.backend.sql.DatabaseState;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.backend.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Instance;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerAddresses;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;
import uoa.lavs.mainframe.simulator.nitrite.DatabaseHelper;

public class SearchCustomerTest {
  SearchCustomer searchCustomer;
  DatabaseConnection conn;
  AddressDAO addressDAO;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    searchCustomer = new SearchCustomer();
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    addressDAO = new AddressDAO();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void getStatusInstance() {
    assertEquals(null, searchCustomer.getStatusInstance());
  }

  @Test
  public void setStatusInstance() {
    Status status = new Status(1);
    searchCustomer.setStatusInstance(status);
    assertEquals(status, searchCustomer.getStatusInstance());
  }

  @Test
  public void testSearchCustomerById() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    Customer customer = searchCustomer.searchCustomerById("123", connection);
    assertEquals(true, checkCustomerCorrect(customer));
  }

  @Test
  public void testSearchCustomerByIdCustomerNotFound() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    searchCustomer.createMockLoadCustomer(200);
    assertNull(searchCustomer.searchCustomerById("123", connection));
  }

  @Test
  public void testSearchCustomerByIdOtherFieldsNotFound() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    searchCustomer.createMockLoadCustomerAddresses(210);
    searchCustomer.createMockLoadCustomerEmails(220);
    searchCustomer.createMockLoadCustomerEmployer(230);
    searchCustomer.createMockLoadCustomerPhoneNumbers(240);
    searchCustomer.createMockLoadCustomerNotes(250);
    Customer retrievedCustomer = searchCustomer.searchCustomerById("123", connection);
    assertEquals(true, checkCustomerCorrect(retrievedCustomer));
    assertEquals(0, retrievedCustomer.getAddresses().size());
    assertEquals(0, retrievedCustomer.getEmails().size());
    assertFalse(retrievedCustomer.getEmployer().getEmployerName().length() > 0);
    assertEquals(0, retrievedCustomer.getPhones().size());
    assertEquals(0, retrievedCustomer.getNotes().size());
  }

  @Test
  public void testSearchCustomerByIdNoConnection() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    String sql =
        "INSERT INTO customer (customerId, title, name, dateOfBirth, occupation, visa, citizenship"
            + " ) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (java.sql.Connection conn = uoa.lavs.backend.sql.DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, "123");
      pstmt.setString(2, "Mr");
      pstmt.setString(3, "John Doe");
      pstmt.setDate(4, Date.valueOf(LocalDate.of(1945, 3, 12)));
      pstmt.setString(5, "Test dummy");
      pstmt.setString(6, "n/a");
      pstmt.setString(7, "New Zealand");
      pstmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }

    Customer retrievedCustomer = searchCustomer.searchCustomerById("123", connection);
    assertEquals(true, checkCustomerCorrect(retrievedCustomer));
  }

  @Test
  public void testSearchCustomerByIdNoConnectionOrCustomerInLocalDb() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    Customer retrievedCustomer = searchCustomer.searchCustomerById("123", connection);
    assertNotNull(retrievedCustomer);
  }

  @Test
  public void testSearchCustomerByIdError1000NoCustomerInLocalDb() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    searchCustomer.createMockLoadCustomer(1000);
    assertNull(searchCustomer.searchCustomerById("123", connection));
  }

  @Test
  public void testSearchCustomerByIdError1010NoCustomerInLocalDb() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    searchCustomer.createMockLoadCustomer(1010);
    assertNull(searchCustomer.searchCustomerById("123", connection));
  }

  @Test
  public void testSearchCustomerByName() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    List<Customer> customers = searchCustomer.searchCustomerByName("John Doe", connection);
    assertEquals(3, customers.size());
    assertEquals(true, checkCustomerCorrect(customers.get(0)));
  }

  @Test
  public void testSearchCustomerByNameInvalidName() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    List<Customer> customers = searchCustomer.searchCustomerByName("Lark Po", connection);
    assertNull(customers);
  }

  @Test
  public void testSearchCustomerByNameOtherFieldsNotFound() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    searchCustomer.createMockLoadCustomerAddresses(210);
    searchCustomer.createMockLoadCustomerEmails(220);
    searchCustomer.createMockLoadCustomerEmployer(230);
    searchCustomer.createMockLoadCustomerPhoneNumbers(240);
    searchCustomer.createMockLoadCustomerNotes(250);
    List<Customer> retrievedCustomers = searchCustomer.searchCustomerByName("John Doe", connection);
    Customer retrievedCustomer = retrievedCustomers.get(0);
    assertEquals(true, checkCustomerCorrect(retrievedCustomer));
    assertEquals(0, retrievedCustomer.getAddresses().size());
    assertEquals(0, retrievedCustomer.getEmails().size());
    assertFalse(retrievedCustomer.getEmployer().getEmployerName().length() > 0);
    assertEquals(0, retrievedCustomer.getPhones().size());
    assertEquals(0, retrievedCustomer.getNotes().size());
  }

  @Test
  public void testSearchCustomerByNameNoConnection() {
    Connection connection = Instance.getConnection();
    String sql =
        "INSERT INTO customer (customerId, title, name, dateOfBirth, occupation, visa, citizenship"
            + " ) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (java.sql.Connection conn = uoa.lavs.backend.sql.DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, "123");
      pstmt.setString(2, "Mr");
      pstmt.setString(3, "John Doe");
      pstmt.setDate(4, Date.valueOf(LocalDate.of(1945, 3, 12)));
      pstmt.setString(5, "Test dummy");
      pstmt.setString(6, "n/a");
      pstmt.setString(7, "New Zealand");
      pstmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<Customer> customers = searchCustomer.searchCustomerByName("John Doe", connection);
    assertEquals(1, customers.size());
  }

  @Test
  public void testSearchCustomerByNameNoConnectionOrCustomerInLocalDb() {
    Connection connection = Instance.getConnection();
    List<Customer> customers = searchCustomer.searchCustomerByName("John Doe", connection);
    assertNull(customers);
  }

  @Test
  public void testSearchCustomerByNameError1000NoCustomerInLocalDb() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    searchCustomer.createMockFindCustomerAdvanced(1000);
    List<Customer> customers = searchCustomer.searchCustomerByName("John Doe", connection);
    assertNull(customers);
  }

  @Test
  public void testSearchCustomerByNameError1010NoCustomerInLocalDb() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    searchCustomer.createMockFindCustomerAdvanced(1010);
    List<Customer> customers = searchCustomer.searchCustomerByName("John Doe", connection);
    assertNull(customers);
  }

  @Test
  public void testSearchCustomerByNameInvalidNameError1010NoCustomerInLocalDb() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    searchCustomer.createMockFindCustomerAdvanced(1010);
    List<Customer> customers = searchCustomer.searchCustomerByName("Lark Po", connection);
    assertNull(customers);
  }

  @Test
  public void testUpdateCustomerAddresses() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());

    SearchCustomer searchCustomer = new SearchCustomer();
    Customer retrievedCustomer = searchCustomer.searchCustomerById("123", connection);
    ArrayList<Address> addresses = retrievedCustomer.getAddresses();

    assertEquals("123", addresses.get(0).getCustomerId());
    assertEquals("Home", addresses.get(0).getAddressType());
    assertEquals("5 Somewhere Lane", addresses.get(0).getAddressLineOne());
    assertEquals("Nowhere", addresses.get(0).getAddressLineTwo());
    assertEquals("Important", addresses.get(0).getSuburb());
    assertEquals("Auckland", addresses.get(0).getCity());
    assertEquals("New Zealand", addresses.get(0).getCountry());
    assertEquals("1234", addresses.get(0).getPostCode());
    assertEquals(true, addresses.get(0).getIsPrimary());
  }

  @Test
  public void testUpdateCustomerAddressCountZero() {
    // Initialize a mock LoadCustomerAddresses with behavior when server has no addresses
    LoadCustomerAddresses mockLoadCustomerAddresses =
        new LoadCustomerAddresses() {
          @Override
          public Integer getCountFromServer() {
            return 0;
          }
        };

    Customer newCustomer = updateCustomerAddressHelper(mockLoadCustomerAddresses);

    Assertions.assertTrue(
        newCustomer.getAddresses().isEmpty(),
        "Customer should have no addresses after update when none exist on server.");
  }

  @Test
  public void testUpdateCustomerAddressCountNull() {
    // Initialize a mock LoadCustomerAddresses with behavior when server has no addresses
    LoadCustomerAddresses mockLoadCustomerAddresses =
        new LoadCustomerAddresses() {
          @Override
          public Integer getCountFromServer() {
            return null;
          }
        };

    Customer newCustomer = updateCustomerAddressHelper(mockLoadCustomerAddresses);

    Assertions.assertTrue(
        newCustomer.getAddresses().isEmpty(),
        "Customer should have no addresses after update when none exist on server.");
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
    searchCustomer.resetLoadMessages();
    if (!dbFile.delete()) {
      throw new RuntimeException(
          "Failed to delete test database file: " + dbFile.getAbsolutePath());
    }
  }

  private Customer updateCustomerAddressHelper(LoadCustomerAddresses mockLoadCustomerAddresses) {
    // Initialize customer with no initial addresses
    Customer newCustomer =
        new IndividualCustomer(
            "456",
            "Ms",
            "Jane Doe",
            LocalDate.of(1980, 5, 20),
            "Example",
            "n/a",
            "New Zealand",
            null,
            null,
            null,
            null,
            null);

    // Simulate database connection (assuming NitriteConnection is your DB connection wrapper)
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());

    // Perform the update
    searchCustomer.updateCustomerAddresses(mockLoadCustomerAddresses, newCustomer, connection);

    return newCustomer;
  }

  private boolean checkCustomerCorrect(Customer customer) {
    return customer.getCustomerId().equals("123")
        && customer.getTitle().equals("Mr")
        && customer.getName().equals("John Doe")
        && customer.getDateOfBirth().equals(LocalDate.of(1945, 3, 12))
        && customer.getOccupation().equals("Test dummy")
        && customer.getVisa().equals("n/a")
        && customer.getCitizenship().equals("New Zealand");
  }
}
