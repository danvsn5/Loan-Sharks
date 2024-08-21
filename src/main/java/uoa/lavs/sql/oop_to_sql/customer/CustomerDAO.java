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
        "INSERT INTO customer (customerId, title, name, dateOfBirth, occupation, visa, citizenship"
            + " ) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      String customerId = getNextCustomerId();
      pstmt.setString(1, customerId);
      pstmt.setString(2, customer.getTitle());
      pstmt.setString(3, customer.getName());
      pstmt.setDate(4, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(5, customer.getOccupation());
      pstmt.setString(6, customer.getVisa());
      pstmt.setString(7, customer.getCitizenship());
      pstmt.executeUpdate();

      customer.setCustomerId(customerId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private String getNextCustomerId() {
    String sql = "SELECT MIN(CAST(customerId AS INTEGER)) AS smallestId FROM customer";
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
        "UPDATE customer SET title = ?, name = ?, dateOfBirth = ?, occupation = ?, visa = ?, citizenship = ?,"
        + " lastModified = CURRENT_TIMESTAMP WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getTitle());
      pstmt.setString(2, customer.getName());
      pstmt.setDate(3, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(4, customer.getOccupation());
      pstmt.setString(5, customer.getVisa());
      pstmt.setString(6, customer.getCitizenship());
      pstmt.setString(
          7, customer.getCustomerId());
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
        String visa = rs.getString("visa");
        String citizenship = rs.getString("citizenship");

        NotesDAO notesdao = new NotesDAO();
        ArrayList<Note> notes = notesdao.getNotes(customerId);

        AddressDAO addressdao = new AddressDAO();
        ArrayList<Address> addresses = addressdao.getAddresses(customerId);

        PhoneDAO phonedao = new PhoneDAO();
        ArrayList<Phone> phones = phonedao.getPhones(customerId);

        EmailDAO emaildao = new EmailDAO();
        ArrayList<Email> emails = emaildao.getEmails(customerId);

        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        CustomerEmployer employer = employerdao.getCustomerEmployer(customerId);

        return new IndividualCustomer(
            customerId,
            title,
            name,
            dateOfBirth,
            occupation,
            visa,
            citizenship,
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
        String visa = rs.getString("visa");
        String citizenship = rs.getString("citizenship");

        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        NotesDAO notesdao = new NotesDAO();
        ArrayList<Note> notes = notesdao.getNotes(customerId);
        CustomerEmployer employer = employerdao.getCustomerEmployer(customerId);

        AddressDAO addressdao = new AddressDAO();
        ArrayList<Address> addresses = addressdao.getAddresses(customerId);

        PhoneDAO phonedao = new PhoneDAO();
        ArrayList<Phone> phones = phonedao.getPhones(customerId);

        EmailDAO emaildao = new EmailDAO();
        ArrayList<Email> emails = emaildao.getEmails(customerId);

        Customer customer =
            new IndividualCustomer(
                customerId,
                title,
                name,
                dateOfBirth,
                occupation,
                visa,
                citizenship,
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
        String visa = rs.getString("visa");
        String citizenship = rs.getString("citizenship");

        NotesDAO notesdao = new NotesDAO();
        ArrayList<Note> notes = notesdao.getNotes(customerId);

        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        CustomerEmployer employer = employerdao.getCustomerEmployer(customerId);

        AddressDAO addressdao = new AddressDAO();
        ArrayList<Address> addresses = addressdao.getAddresses(customerId);

        PhoneDAO phonedao = new PhoneDAO();
        ArrayList<Phone> phones = phonedao.getPhones(customerId);

        EmailDAO emaildao = new EmailDAO();
        ArrayList<Email> emails = emaildao.getEmails(customerId);

        Customer customer =
            new IndividualCustomer(
                customerId,
                title,
                name,
                date,
                occupation,
                visa,
                citizenship,
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
    customerId = "-1";

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
    phone = new Phone(customerId, "mobile", "027", "1234567890", true, true);
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
            "Guy Shin",
            dateOfBirth,
            "Engineer",
            "B2",
            "Mexico",
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

    for (Address a : addresses) {
      addressdao.addAddress(a);
    }

    for (Phone p : phones) {
      phonedao.addPhone(p);
    }

    for (Email e : emails) {
      emaildao.addEmail(e);
    }

    employerdao.addCustomerEmployer(employer);

    dao.addCustomer(customer);
  }

  // update customer test
  public static void updateCustomerTest(String customerId) {
    Customer customer;
    ArrayList<Address> addresses;
    Address physicalAddress;
    CustomerEmployer employer;
    ArrayList<Phone> phones;
    Phone phone;
    ArrayList<Email> emails;
    Email email;
    ArrayList<Note> notes;
    Note note = null;

    addresses = new ArrayList<>();
    phones = new ArrayList<>();
    emails = new ArrayList<>();

    AddressDAO addressdao = new AddressDAO();
    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();
    EmailDAO emaildao = new EmailDAO();
    PhoneDAO phonedao = new PhoneDAO();


    physicalAddress = addressdao.getAddress(customerId, 1);
    physicalAddress.setAddressType("Commerical");
    
    employer = employerdao.getCustomerEmployer(customerId);
    employer.setEmployerName("New World");

    phones = phonedao.getPhones(customerId);
    phone = new Phone(customerId, "home", "09", "1234567", false, false);
    phones.add(phone);

    emails = emaildao.getEmails(customerId);
    email = emails.get(0);
    email.setEmailAddress("goon@gmail.com");
    emails.set(0, email);

    notes = new ArrayList<>();
    note = new Note(customerId, new String[] {"Smells like burning crayons"});
    notes.add(note);

    physicalAddress.setAddressLineOne("401 Rose St");

    CustomerDAO dao = new CustomerDAO();

    customer = dao.getCustomer(customerId);
    customer.setAddresses(addresses);
    addressdao.updateAddress(physicalAddress);
    employerdao.updateCustomerEmployer(employer);
    phonedao.updatePhone(phone);
    emaildao.updateEmail(email);

    dao.updateCustomer(customer);
  }

  public static void main(String[] args) {
    addCustomerTest();
  }
}
