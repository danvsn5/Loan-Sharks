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
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.DatabaseConnection;

public class CustomerDAO {
  public void addCustomer(ICustomer customer) {
    String sql =
        "INSERT INTO customer (customerId, title, name, dateOfBirth,"
            + " occupation, residency, notes, physicalAddressId, mailingAddressId, contactId,"
            + " employerId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getCustomerId());
      pstmt.setString(2, customer.getTitle());
      pstmt.setString(3, customer.getName());
      pstmt.setDate(4, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(5, customer.getOccupation());
      pstmt.setString(6, customer.getResidency());
      pstmt.setString(7, customer.getNotes());
      pstmt.setInt(
          8,
          customer.getPhysicalAddress() != null
              ? customer.getPhysicalAddress().getAddressId()
              : null);
      pstmt.setInt(
          9,
          customer.getMailingAddress() != null
              ? customer.getMailingAddress().getAddressId()
              : null);
      pstmt.setInt(10, customer.getContact() != null ? customer.getContact().getContactId() : null);
      pstmt.setInt(
          11, customer.getEmployer() != null ? customer.getEmployer().getEmployerId() : null);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateCustomer(ICustomer customer) {
    String sql =
        "UPDATE customer SET title = ?, name = ?, dateOfBirth ="
            + " ?, occupation = ?, residency = ?, notes = ?, physicalAddressId = ?,"
            + " mailingAddressId = ?, contactId = ?, employerId = ? WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getTitle());
      pstmt.setString(2, customer.getName());
      pstmt.setDate(3, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(4, customer.getOccupation());
      pstmt.setString(5, customer.getResidency());
      pstmt.setString(6, customer.getNotes());
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
      pstmt.setInt(
          10, customer.getEmployer() != null ? customer.getEmployer().getEmployerId() : null);
      pstmt.setString(11, customer.getCustomerId());

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
        String notes = rs.getString("notes");
        int physicalAddressId = rs.getInt("physicalAddressId");
        int mailingAddressId = rs.getInt("mailingAddressId");
        int contactId = rs.getInt("contactId");
        int employerId = rs.getInt("employerId");

        AddressDAO addressdao = new AddressDAO();
        CustomerContactDAO contactdao = new CustomerContactDAO();
        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        Address physicalAddress = addressdao.getAddress(physicalAddressId);
        Address mailingAddress = addressdao.getAddress(mailingAddressId);
        CustomerContact contact = contactdao.getCustomerContact(contactId);
        CustomerEmployer employer = employerdao.getCustomerEmployer(employerId);

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

  public ArrayList<Customer> getCustomersByName(
      String name) {
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
        String notes = rs.getString("notes");
        int physicalAddressId = rs.getInt("physicalAddressId");
        int mailingAddressId = rs.getInt("mailingAddressId");
        int contactId = rs.getInt("contactId");
        int employerId = rs.getInt("employerId");

        AddressDAO addressdao = new AddressDAO();
        CustomerContactDAO contactdao = new CustomerContactDAO();
        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        Address physicalAddress = addressdao.getAddress(physicalAddressId);
        Address mailingAddress = addressdao.getAddress(mailingAddressId);
        CustomerContact contact = contactdao.getCustomerContact(contactId);
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
        String notes = rs.getString("notes");
        int physicalAddressId = rs.getInt("physicalAddressId");
        int mailingAddressId = rs.getInt("mailingAddressId");
        int contactId = rs.getInt("contactId");
        int employerId = rs.getInt("employerId");

        AddressDAO addressdao = new AddressDAO();
        CustomerContactDAO contactdao = new CustomerContactDAO();
        CustomerEmployerDAO employerdao = new CustomerEmployerDAO();

        Address physicalAddress = addressdao.getAddress(physicalAddressId);
        Address mailingAddress = addressdao.getAddress(mailingAddressId);
        CustomerContact contact = contactdao.getCustomerContact(contactId);
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

  public static void main(String[] args) {
    Customer customer;
    LocalDate dateOfBirth;
    Address physicalAddress;
    CustomerContact contact;
    Address employerAddress;
    CustomerEmployer employer;
    Phone phoneOne;
    Phone phoneTwo;

    dateOfBirth = LocalDate.of(2024, 8, 6);
    physicalAddress =
        new Address("Rural", "304 Rose St", "46", "Sunnynook", "12345", "Auckland", "Zimbabwe");
    phoneOne = new Phone("mobile", "1234567890");
    phoneTwo = new Phone("home", "0987654321");
    contact = new CustomerContact("abc@gmail.com", phoneOne, phoneTwo, "mobile sms", "email");
    employerAddress =
        new Address(
            "Commercial", "123 Stonesuckle Ct", "", "Sunnynook", "12345", "Auckland", "Zimbabwe");
    employer =
        new CustomerEmployer(
            "Countdown", employerAddress, "dog@daniil.com", "www.daniil.org.nz", "02222222", false);

    customer =
        new IndividualCustomer(
            "000001",
            "Mr",
            "Ting Mun Guy",
            dateOfBirth,
            "Engineer",
            "NZ Citizen",
            "Smells like burning crayons",
            physicalAddress,
            physicalAddress,
            contact,
            employer);

    AddressDAO addressdao = new AddressDAO();
    CustomerContactDAO contactdao = new CustomerContactDAO();
    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();
    CustomerDAO dao = new CustomerDAO();

    addressdao.addAddress(customer.getPhysicalAddress());
    addressdao.addAddress(customer.getMailingAddress());
    addressdao.addAddress(customer.getEmployer().getEmployerAddress());

    contactdao.addCustomerContact(customer.getContact());

    employerdao.addCustomerEmployer(customer.getEmployer());

    dao.addCustomer(customer);
  }
}
