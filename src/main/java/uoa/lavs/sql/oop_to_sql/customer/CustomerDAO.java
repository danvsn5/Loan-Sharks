package uoa.lavs.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.Customer;
import uoa.lavs.customer.CustomerContact;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.ICustomer;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.Note;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerDAO {
  public void addCustomer(ICustomer customer) {
    String sql =
        "INSERT INTO customer (customerId, title, name, dateOfBirth,"
            + " occupation, residency, primaryAddressId, mailingAddressId, contactId)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getCustomerId());
      pstmt.setString(2, customer.getTitle());
      pstmt.setString(3, customer.getName());
      pstmt.setDate(4, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(5, customer.getOccupation());
      pstmt.setString(6, customer.getResidency());
      pstmt.setInt(
          7,
          customer.getPhysicalAddress() != null
              ? customer.getPhysicalAddress().getAddressId()
              : null);
      pstmt.setInt(
          8,
          customer.getMailingAddress() != null
              ? customer.getMailingAddress().getAddressId()
              : null);
      pstmt.setInt(9, customer.getContact() != null ? customer.getContact().getContactId() : null);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateCustomer(ICustomer customer) {
    String sql =
        "UPDATE customer SET title = ?, name = ?, dateOfBirth = ?, occupation = ?, residency = ?,"
            + " primaryAddressId = ?, mailingAddressId = ?, contactId = ?,"
            + " lastModified = CURRENT_TIMESTAMP WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getTitle());
      pstmt.setString(2, customer.getName());
      pstmt.setDate(3, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(4, customer.getOccupation());
      pstmt.setString(5, customer.getResidency());
      pstmt.setInt(
          6,
          customer.getPhysicalAddress() != null
              ? customer.getPhysicalAddress().getAddressId()
              : null);
      pstmt.setInt(
          7,
          customer.getMailingAddress() != null
              ? customer.getMailingAddress().getAddressId()
              : null);
      pstmt.setInt(8, customer.getContact() != null ? customer.getContact().getContactId() : null);

      pstmt.setString(9, customer.getCustomerId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public Customer getCustomer(String customerId) {
    String sql = "SELECT * FROM customer WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        String title = rs.getString("title");
        String name = rs.getString("name");
        LocalDate dateOfBirth = rs.getDate("dateOfBirth").toLocalDate();
        String occupation = rs.getString("occupation");
        String residency = rs.getString("residency");
        int primaryAddressId = rs.getInt("primaryAddressId");
        int mailingAddressId = rs.getInt("mailingAddressId");
        int contactId = rs.getInt("contactId");

        NotesDAO notesdao = new NotesDAO();
        ArrayList<Note> notes = notesdao.getNotes(customerId);

        AddressDAO addressdao = new AddressDAO();
        CustomerContactDAO contactdao = new CustomerContactDAO();
        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        Address physicalAddress = addressdao.getAddress(customerId, primaryAddressId);
        Address mailingAddress = addressdao.getAddress(customerId, mailingAddressId);
        CustomerContact contact = contactdao.getCustomerContact(contactId);
        CustomerEmployer employer = employerdao.getCustomerEmployer(customerId);

        return new IndividualCustomer(
            customerId,
            title,
            name,
            dateOfBirth,
            occupation,
            residency,
            notes,
            physicalAddress,
            mailingAddress,
            contact,
            employer);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public ArrayList<Customer> getCustomersByName(String name) {
    ArrayList<Customer> customers = new ArrayList<>();

    String sql = "SELECT * FROM customer WHERE name = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, name);

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String customerId = rs.getString("customerId");
        String title = rs.getString("title");
        LocalDate dateOfBirth = rs.getDate("dateOfBirth").toLocalDate();
        String occupation = rs.getString("occupation");
        String residency = rs.getString("residency");
        int primaryAddressId = rs.getInt("primaryAddressId");
        int mailingAddressId = rs.getInt("mailingAddressId");
        int contactId = rs.getInt("contactId");

        AddressDAO addressdao = new AddressDAO();
        CustomerContactDAO contactdao = new CustomerContactDAO();
        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        NotesDAO notesdao = new NotesDAO();
        ArrayList<Note> notes = notesdao.getNotes(customerId);

        Address physicalAddress = addressdao.getAddress(customerId, primaryAddressId);
        Address mailingAddress = addressdao.getAddress(customerId, mailingAddressId);
        CustomerContact contact = contactdao.getCustomerContact(contactId);
        CustomerEmployer employer = employerdao.getCustomerEmployer(customerId);

        Customer customer =
            new IndividualCustomer(
                customerId,
                title,
                name,
                dateOfBirth,
                occupation,
                residency,
                notes,
                physicalAddress,
                mailingAddress,
                contact,
                employer);
        customers.add(customer);
      }
      return customers;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public ArrayList<Customer> getCustomersByBirth(LocalDate date) {
    ArrayList<Customer> customers = new ArrayList<>();

    String sql = "SELECT * FROM customer WHERE dateOfBirth = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setDate(1, Date.valueOf(date));

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String customerId = rs.getString("customerId");
        String title = rs.getString("title");
        String name = rs.getString("name");
        String occupation = rs.getString("occupation");
        String residency = rs.getString("residency");
        int primaryAddressId = rs.getInt("primaryAddressId");
        int mailingAddressId = rs.getInt("mailingAddressId");
        int contactId = rs.getInt("contactId");

        NotesDAO notesdao = new NotesDAO();
        ArrayList<Note> notes = notesdao.getNotes(customerId);

        AddressDAO addressdao = new AddressDAO();
        CustomerContactDAO contactdao = new CustomerContactDAO();
        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        Address physicalAddress = addressdao.getAddress(customerId, primaryAddressId);
        Address mailingAddress = addressdao.getAddress(customerId, mailingAddressId);
        CustomerContact contact = contactdao.getCustomerContact(contactId);
        CustomerEmployer employer = employerdao.getCustomerEmployer(customerId);

        if (contact == null) {
          System.out.println("Contact is null");
        }

        Customer customer =
            new IndividualCustomer(
                customerId,
                title,
                name,
                date,
                occupation,
                residency,
                notes,
                physicalAddress,
                mailingAddress,
                contact,
                employer);
        customers.add(customer);
      }
      return customers;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  // add customer test
  public static void addCustomerTest() {
    Customer customer;
    LocalDate dateOfBirth;
    Address physicalAddress;
    CustomerContact contact;
    CustomerEmployer employer;
    Phone phoneOne;
    Phone phoneTwo;
    String customerId;
    ArrayList<Note> notes;
    Note note;

    dateOfBirth = LocalDate.of(2024, 8, 6);
    customerId = "000001";

    physicalAddress =
        new Address(
            customerId,
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
            customerId,
            "Countdown",
            "123 Stonesuckle Ct",
            "",
            "Sunnynook",
            "Auckland",
            "12345",
            "Zimbabwe",
            "dog@daniil.com",
            "www.daniil.org.nz",
            "02222222",
            false);

    notes = new ArrayList<>();
    note = new Note(customerId, new String[] {"Smells like burning crayons"});
    notes.add(note);

    customer =
        new IndividualCustomer(
            customerId,
            "Mr",
            "Ting Mun Guy",
            dateOfBirth,
            "Engineer",
            "NZ Citizen",
            notes,
            physicalAddress,
            physicalAddress,
            contact,
            employer);

    NotesDAO notesdao = new NotesDAO();
    AddressDAO addressdao = new AddressDAO();
    CustomerContactDAO contactdao = new CustomerContactDAO();
    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();
    CustomerDAO dao = new CustomerDAO();

    notesdao.addNotes(notes);

    addressdao.addAddress(customer.getPhysicalAddress());
    addressdao.addAddress(customer.getMailingAddress());

    contactdao.addCustomerContact(customer.getContact());

    employerdao.addCustomerEmployer(employer);

    dao.addCustomer(customer);
  }

  // update customer test
  public static void updateCustomerTest(String customerId) {
    Customer customer;
    LocalDate dateOfBirth;
    Address physicalAddress;
    Address mailingAddress;
    CustomerContact contact;
    CustomerEmployer employer;
    ArrayList<Note> notes;
    Note note;

    dateOfBirth = LocalDate.of(2024, 8, 6);

    AddressDAO addressdao = new AddressDAO();
    CustomerContactDAO contactdao = new CustomerContactDAO();
    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

    physicalAddress = addressdao.getAddress(customerId, 1);
    mailingAddress = addressdao.getAddress(customerId, 2);
    physicalAddress.setAddressType("Commerical");
    contact = contactdao.getCustomerContact(Integer.parseInt(customerId));
    employer = employerdao.getCustomerEmployer(customerId);

    employer.setEmployerName("New World");

    notes = new ArrayList<>();
    note = new Note(customerId, new String[] {"Smells like burning crayons"});
    notes.add(note);

    physicalAddress.setAddressLineOne("401 Rose St");

    CustomerDAO dao = new CustomerDAO();

    customer = dao.getCustomer(customerId);
    customer.setPhysicalAddress(physicalAddress);
    customer.setMailingAddress(mailingAddress);
    addressdao.updateAddress(physicalAddress);
    addressdao.updateAddress(mailingAddress);
    employerdao.updateCustomerEmployer(employer);

    dao.updateCustomer(customer);
  }

  public static void main(String[] args) {
    addCustomerTest();
  }
}
