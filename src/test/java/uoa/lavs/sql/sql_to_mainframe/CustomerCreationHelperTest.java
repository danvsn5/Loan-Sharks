package uoa.lavs.sql.sql_to_mainframe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.oop.customer.Customer;
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
import uoa.lavs.backend.sql.sql_to_mainframe.CustomerCreationHelper;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.FindCustomer;
import uoa.lavs.legacy.mainframe.simulator.NitriteConnection;
import uoa.lavs.mainframe.simulator.nitrite.DatabaseHelper;

public class CustomerCreationHelperTest {
  DatabaseConnection conn;
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

  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    dbFile = DatabaseState.DB_TEST_FILE;
    InitialiseDatabase.createDatabase();

    dateOfBirth = LocalDate.of(2024, 8, 6);
    notes = new ArrayList<>();
    note = new Note("-1", new String[19]);
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
  }

  @Test
  public void testValidateCustomer_NoTitle() {
    customer.setTitle("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoName() {
    customer.setName("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoDateOfBirth() {
    customer.setDateOfBirth(null);
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoOccupation() {
    customer.setOccupation("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_OccupationTooLong() {
    customer.setOccupation("This is a very long occupation that is over 40 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoVisa() {
    customer.setVisa("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoCitizenship() {
    customer.setCitizenship("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoAddresses() {
    customer.setAddresses(new ArrayList<>());
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoAddressType() {
    address.setAddressType("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoAddressLineOne() {
    address.setAddressLineOne("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_AddressLineOneTooLong() {
    address.setAddressLineOne("This is a very long address line that is over 60 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_AddressLineTwoTooLong() {
    address.setAddressLineTwo("This is a very long address line that is over 60 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoSuburb() {
    CustomerCreationHelper customerCreationHelper = new CustomerCreationHelper();
    address.setSuburb("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_SuburbTooLong() {
    address.setSuburb("This is a very long suburb that is over 30 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoPostCode() {
    address.setPostCode("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_PostCodeTooLong() {
    address.setPostCode("This is a very long post code that is over 10 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_PostCodeNotNumeric() {
    address.setPostCode("abc");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoCity() {
    address.setCity("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_CityTooLong() {
    address.setCity("This is a very long city that is over 30 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoCountry() {
    address.setCountry("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoPrimaryAddress() {
    address.setIsPrimary(false);
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoMailingAddress() {
    address.setIsMailing(false);
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmails() {
    customer.setEmails(new ArrayList<>());
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmailAddress() {
    email.setEmailAddress("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoPrimaryEmail() {
    email.setIsPrimary(false);
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoPhones() {
    customer.setPhones(new ArrayList<>());
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoPhoneType() {
    phone.setType("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoPhonePrefix() {
    phone.setPrefix("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_PhonePrefixTooLong() {
    phone.setPrefix("1234567890123456789000001");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_PrefixWrongCharacters() {
    phone.setPrefix("abc");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoPhoneNumber() {
    phone.setPhoneNumber("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_PhoneNumberTooLong() {
    phone.setPhoneNumber("123456789012345678901");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NumberWrongCharacters() {
    phone.setPhoneNumber("abc");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoPrimaryPhone() {
    phone.setIsPrimary(false);
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmployerName() {
    employer.setEmployerName("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerNameTooLong() {
    employer.setEmployerName("This is a very long employer name that is over 60 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmployerEmail() {
    employer.setEmployerEmail("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerEmailWrongCharacters() {
    employer.setEmployerEmail("abc");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerEmailTooLong() {
    employer.setEmployerEmail("This is a very long employer email that is over 60 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmployerWebsite() {
    employer.setEmployerWebsite("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerWebsiteTooLong() {
    employer.setEmployerWebsite(
        "This is a very long employer website that is over 60 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerWebsiteWrongCharacters() {
    employer.setEmployerWebsite("abc");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmployerPhone() {
    employer.setEmployerPhone("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerPhoneTooLong() {
    employer.setEmployerPhone("This is a very long employer phone that is over 60 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerPhoneWrongCharacters() {
    employer.setEmployerPhone("abc");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmployerLineOne() {
    employer.setLineOne("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerLineOneTooLong() {
    employer.setLineOne("This is a very long employer line one that is over 60 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerLineTwoTooLong() {
    employer.setLineTwo("This is a very long employer line two that is over 60 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmployerSuburb() {
    employer.setSuburb("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerSuburbTooLong() {
    employer.setSuburb("This is a very long employer suburb that is over 30 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmployerCity() {
    employer.setCity("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerCityTooLong() {
    employer.setCity("This is a very long employer city that is over 30 characters long");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmployerPostCode() {
    employer.setPostCode("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_EmployerPostCodeWrongCharacters() {
    employer.setPostCode("abc");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testValidateCustomer_NoEmployerCountry() {
    employer.setCountry("");
    assertFalse(CustomerCreationHelper.validateCustomer(customer));
  }

  @Test
  public void testCreateCustomer() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_ValidNotes() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    ArrayList<Note> notes = new ArrayList<>();
    Note note = new Note("-1", new String[19]);
    for (int i = 0; i < 19; i++) {
      note.setLine(i, "This is a note");
    }
    notes.add(note);
    customer.setNotes(notes);

    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_AddressTypeNull() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    address.setAddressType(null);
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_AddressLineOneEmpty() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    address.setAddressLineOne("");
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_SuburbEmpty() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    address.setSuburb("");
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_PostCodeEmpty() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    address.setPostCode("");
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_CityEmpty() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    address.setCity("");
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_CountryEmpty() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    address.setCountry("");
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_PhoneTypeEmpty() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    phone.setType(null);
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_PhonePrefixEmpty() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    phone.setPrefix("");
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_PhoneNumberEmpty() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    phone.setPhoneNumber("");
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_EmailAddressEmpty() throws IOException {
    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    email.setEmailAddress("");
    CustomerCreationHelper.createCustomer(customer, false, connection);
    FindCustomer findCustomer = new FindCustomer();
    findCustomer.setCustomerId(customer.getCustomerId());
    Status status = findCustomer.send(connection);
    connection.close();
    assert (status.getErrorCode() == 0);
  }

  @Test
  public void testCreateCustomer_CurrentlyExists() throws IOException {
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
    for (Address address : addresses) {
      addressDAO.addAddress(address);
    }
    for (Phone phone : phones) {
      phoneDAO.addPhone(phone);
    }
    for (Email email : emails) {
      emailDAO.addEmail(email);
    }
    for (Note note : notes) {
      notesDAO.addNote(note);
    }
    customerDAO.addCustomer(customer);

    NitriteConnection connection = new NitriteConnection(DatabaseHelper.generateDefaultDatabase());
    CustomerCreationHelper.createCustomer(customer, true, connection);
    connection.close();

    Customer retrievedCustomer = customerDAO.getCustomer(customer.getCustomerId());
    assertEquals(customer.getCustomerId(), retrievedCustomer.getCustomerId());
  }
}
