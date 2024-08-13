package uoa.lavs.sql.customer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
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
        "INSERT INTO customer (customerId, title, firstName, middleName, lastName, dateOfBirth,"
            + " occupation, residency, notes, physicalAddressId, mailingAddressId, contactId,"
            + " employerId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getCustomerId());
      pstmt.setString(2, customer.getTitle());
      pstmt.setString(3, customer.getFirstName());
      pstmt.setString(4, customer.getMiddleName());
      pstmt.setString(5, customer.getLastName());
      pstmt.setDate(6, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(7, customer.getOccupation());
      pstmt.setString(8, customer.getResidency());
      pstmt.setString(9, customer.getNotes());
      pstmt.setInt(
          10,
          customer.getPhysicalAddress() != null
              ? customer.getPhysicalAddress().getAddressId()
              : null);
      pstmt.setInt(
          11,
          customer.getMailingAddress() != null
              ? customer.getMailingAddress().getAddressId()
              : null);
      pstmt.setInt(12, customer.getContact() != null ? customer.getContact().getContactId() : null);
      pstmt.setInt(
          13, customer.getEmployer() != null ? customer.getEmployer().getEmployerId() : null);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateCustomer(ICustomer customer) {
    String sql =
        "UPDATE customer SET title = ?, firstName = ?, middleName = ?, lastName = ?, dateOfBirth ="
            + " ?, occupation = ?, residency = ?, notes = ?, physicalAddressId = ?,"
            + " mailingAddressId = ?, contactId = ?, employerId = ? WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getTitle());
      pstmt.setString(2, customer.getFirstName());
      pstmt.setString(3, customer.getMiddleName());
      pstmt.setString(4, customer.getLastName());
      pstmt.setDate(5, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(6, customer.getOccupation());
      pstmt.setString(7, customer.getResidency());
      pstmt.setString(8, customer.getNotes());
      pstmt.setInt(
          9,
          customer.getPhysicalAddress() != null
              ? customer.getPhysicalAddress().getAddressId()
              : null);
      pstmt.setInt(
          10,
          customer.getMailingAddress() != null
              ? customer.getMailingAddress().getAddressId()
              : null);
      pstmt.setInt(11, customer.getContact() != null ? customer.getContact().getContactId() : null);
      pstmt.setInt(
          12, customer.getEmployer() != null ? customer.getEmployer().getEmployerId() : null);
      pstmt.setString(13, customer.getCustomerId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
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
            "Ting",
            "Mun",
            "Guy",
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
