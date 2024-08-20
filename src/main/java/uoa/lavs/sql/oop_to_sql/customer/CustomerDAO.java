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
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.Email;
import uoa.lavs.customer.ICustomer;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.Note;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerDAO {
  public void addCustomer(ICustomer customer) {
    String sql =
        "INSERT INTO customer (customerId, title, name, dateOfBirth, occupation, residency,"
            + " employerId) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      String customerId = getNextCustomerId();
      pstmt.setString(1, customerId);
      pstmt.setString(2, customer.getTitle());
      pstmt.setString(3, customer.getName());
      pstmt.setDate(4, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(5, customer.getOccupation());
      pstmt.setString(6, customer.getResidency());
      pstmt.setInt(
          7, customer.getEmployer() != null ? customer.getEmployer().getEmployerId() : null);

      pstmt.executeUpdate();

      customer.setCustomerId(customerId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private String getNextCustomerId() {
    String sql = "SELECT MIN(customerId) AS smallestId FROM customer";
    String previousId = "-1";

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        String smallestId = rs.getString("smallestId");

        if (smallestId != null) {
          try {
            int idNumber = Integer.parseInt(smallestId);
            previousId = String.valueOf(idNumber - 1);
          } catch (NumberFormatException e) {
            System.out.println("Error parsing customerId: " + e.getMessage());
          }
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return previousId;
  }

  public void updateCustomer(ICustomer customer) {
    String sql =
        "UPDATE customer SET title = ?, name = ?, dateOfBirth = ?, occupation = ?, residency = ?,"
            + " employerId = ?, lastModified = CURRENT_TIMESTAMP WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getTitle());
      pstmt.setString(2, customer.getName());
      pstmt.setDate(3, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(4, customer.getOccupation());
      pstmt.setString(5, customer.getResidency());
      pstmt.setInt(
          6, customer.getEmployer() != null ? customer.getEmployer().getEmployerId() : null);
      pstmt.setString(7, customer.getCustomerId());

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
        int employerId = rs.getInt("employerId");

        NotesDAO notesdao = new NotesDAO();
        ArrayList<Note> notes = notesdao.getNotes(customerId);

        AddressDAO addressdao = new AddressDAO();
        ArrayList<Address> addresses = addressdao.getAddresses(customerId);

        PhoneDAO phonedao = new PhoneDAO();
        ArrayList<Phone> phones = phonedao.getPhones(customerId);

        EmailDAO emaildao = new EmailDAO();
        ArrayList<Email> emails = emaildao.getEmails(customerId);

        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        CustomerEmployer employer = employerdao.getCustomerEmployer(employerId);

        return new IndividualCustomer(
            customerId,
            title,
            name,
            dateOfBirth,
            occupation,
            residency,
            notes,
            addresses,
            phones,
            emails,
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
        int employerId = rs.getInt("employerId");

        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        NotesDAO notesdao = new NotesDAO();
        ArrayList<Note> notes = notesdao.getNotes(customerId);

        AddressDAO addressdao = new AddressDAO();
        ArrayList<Address> addresses = addressdao.getAddresses(customerId);

        PhoneDAO phonedao = new PhoneDAO();
        ArrayList<Phone> phones = phonedao.getPhones(customerId);

        EmailDAO emaildao = new EmailDAO();
        ArrayList<Email> emails = emaildao.getEmails(customerId);

        CustomerEmployer employer = employerdao.getCustomerEmployer(employerId);

        Customer customer =
            new IndividualCustomer(
                customerId,
                title,
                name,
                dateOfBirth,
                occupation,
                residency,
                notes,
                addresses,
                phones,
                emails,
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
        int employerId = rs.getInt("employerId");

        NotesDAO notesdao = new NotesDAO();
        ArrayList<Note> notes = notesdao.getNotes(customerId);

        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        AddressDAO addressdao = new AddressDAO();
        ArrayList<Address> addresses = addressdao.getAddresses(customerId);

        PhoneDAO phonedao = new PhoneDAO();
        ArrayList<Phone> phones = phonedao.getPhones(customerId);

        EmailDAO emaildao = new EmailDAO();
        ArrayList<Email> emails = emaildao.getEmails(customerId);

        CustomerEmployer employer = employerdao.getCustomerEmployer(employerId);

        Customer customer =
            new IndividualCustomer(
                customerId,
                title,
                name,
                date,
                occupation,
                residency,
                notes,
                addresses,
                phones,
                emails,
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
    CustomerEmployer employer;
    ArrayList<Phone> phones;
    Phone phone;
    ArrayList<Email> emails;
    Email email;
    String customerId;
    ArrayList<Note> notes;
    Note note;
    ArrayList<Address> addresses;

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

    phones = new ArrayList<>();
    emails = new ArrayList<>();
    phone = new Phone(customerId, "mobile", "1234567890", true, true);
    email = new Email(customerId, "abc@gmail.com", true);
    phones.add(phone);
    emails.add(email);

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

    addresses = new ArrayList<>();
    addresses.add(physicalAddress);

    customer =
        new IndividualCustomer(
            customerId,
            "Mr",
            "Ting Mun Guy",
            dateOfBirth,
            "Engineer",
            "NZ Citizen",
            notes,
            addresses,
            phones,
            emails,
            employer);

    NotesDAO notesdao = new NotesDAO();
    AddressDAO addressdao = new AddressDAO();
    PhoneDAO phonedao = new PhoneDAO();
    EmailDAO emaildao = new EmailDAO();
    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();
    CustomerDAO dao = new CustomerDAO();

    for (Note n : notes) {
      notesdao.addNote(n);
    }

    addressdao.addAddress(physicalAddress);

    for (Phone p : phones) {
      phonedao.addPhone(p);
    }

    for (Email e : emails) {
      emaildao.addEmail(e);
    }

    employerdao.addCustomerEmployer(customer.getEmployer());

    dao.addCustomer(customer);
  }

  // update customer test
  public static void updateCustomerTest(String customerId) {
    Customer customer;
    LocalDate dateOfBirth;
    ArrayList<Address> addresses;
    Address physicalAddress;
    CustomerEmployer employer;
    ArrayList<Phone> phones;
    Phone phone;
    ArrayList<Email> emails;
    Email email;
    ArrayList<Note> notes;
    Note note;

    dateOfBirth = LocalDate.of(2024, 8, 6);
    physicalAddress =
        new Address(
            customerId,
            "Commercial",
            "304 Rose St",
            "46",
            "Sunnynook",
            "12345",
            "Auckland",
            "Zimbabwe",
            true,
            false);

    addresses = new ArrayList<>();
    addresses.add(physicalAddress);

    phones = new ArrayList<>();
    emails = new ArrayList<>();
    phone = new Phone(customerId, "mobile", "1234567890", true, true);
    email = new Email(customerId, "abc@gmail.com", true);
    phones.add(phone);
    emails.add(email);

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
            "1",
            "Mr",
            "Guy Goon Ting",
            dateOfBirth,
            "Engineer",
            "NZ Citizen",
            notes,
            addresses,
            phones,
            emails,
            employer);

    NotesDAO notesdao = new NotesDAO();
    notesdao.addNote(note);
    CustomerDAO dao = new CustomerDAO();
    dao.updateCustomer(customer);
  }

  public static void main(String[] args) {
    updateCustomerTest("1");
  }
}
