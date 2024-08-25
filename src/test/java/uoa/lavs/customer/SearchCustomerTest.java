package uoa.lavs.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.mainframe.Connection;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.simulator.NitriteConnection;
import uoa.lavs.mainframe.simulator.nitrite.DatabaseHelper;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;
import uoa.lavs.sql.oop_to_sql.customer.AddressDAO;

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
  }

  @Test
  public void testSearchCustomerByIdNoConnection() {
    Connection connection = Instance.getConnection();
    String sql =
        "INSERT INTO customer (customerId, title, name, dateOfBirth, occupation, visa, citizenship"
            + " ) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (java.sql.Connection conn = uoa.lavs.sql.DatabaseConnection.connect();
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
  }

  @Test
  public void testSearchCustomerByIdNoConnectionOrCustomerInLocalDb() {
    Connection connection = Instance.getConnection();
    Customer retrievedCustomer = searchCustomer.searchCustomerById("123", connection);
  }

  @Test
  public void testSearchCustomerByName() {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    List<Customer> customers = searchCustomer.searchCustomerByName("John Doe", connection);
  }

  @Test
  public void testSearchCustomerByNameNoConnection() {
    Connection connection = Instance.getConnection();
    String sql =
        "INSERT INTO customer (customerId, title, name, dateOfBirth, occupation, visa, citizenship"
            + " ) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (java.sql.Connection conn = uoa.lavs.sql.DatabaseConnection.connect();
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
  }

  @Test
  public void testSearchCustomerByNameNoConnectionOrCustomerInLocalDb() {
    Connection connection = Instance.getConnection();
    List<Customer> customers = searchCustomer.searchCustomerByName("John Doe", connection);
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

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
    if (!dbFile.delete()) {
      throw new RuntimeException(
          "Failed to delete test database file: " + dbFile.getAbsolutePath());
    }
  }
}
