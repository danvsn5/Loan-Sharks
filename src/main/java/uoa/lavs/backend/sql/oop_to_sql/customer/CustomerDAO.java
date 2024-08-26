package uoa.lavs.backend.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.oop.customer.Customer;
import uoa.lavs.backend.oop.customer.CustomerEmployer;
import uoa.lavs.backend.oop.customer.Email;
import uoa.lavs.backend.oop.customer.ICustomer;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.Note;
import uoa.lavs.backend.oop.customer.Phone;
import uoa.lavs.backend.sql.DatabaseConnection;

public class CustomerDAO {

  // Adds a customer to the database
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

  // Finds the next customerId for generating a customer
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
            // Previous id already -1, so only changes previd if idNumber is less than that
            if (idNumber < 0) {
              previousId = String.valueOf(idNumber - 1);
            }
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

  // Updates a customer in the database with the new customer parameters
  public void updateCustomer(ICustomer customer) {
    String sql =
        "UPDATE customer SET title = ?, name = ?, dateOfBirth = ?, occupation = ?, visa = ?,"
            + " citizenship = ?, lastModified = CURRENT_TIMESTAMP WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customer.getTitle());
      pstmt.setString(2, customer.getName());
      pstmt.setDate(3, Date.valueOf(customer.getDateOfBirth()));
      pstmt.setString(4, customer.getOccupation());
      pstmt.setString(5, customer.getVisa());
      pstmt.setString(6, customer.getCitizenship());
      pstmt.setString(7, customer.getCustomerId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Returns a customer from the database with the given customerId
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

  // Returns all customers with name similar to the given name
  public ArrayList<Customer> getCustomersByName(String name) {
    ArrayList<Customer> customers = new ArrayList<>();

    String sql = "SELECT * FROM customer WHERE name LIKE ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, "%" + name + "%");

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String customerId = rs.getString("customerId");
        String customerName = rs.getString("name");
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
                customerName,
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
}
