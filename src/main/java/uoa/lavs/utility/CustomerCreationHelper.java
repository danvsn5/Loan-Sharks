package uoa.lavs.utility;

import java.util.ArrayList;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.Email;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerEmployerDAO;
import uoa.lavs.sql.oop_to_sql.customer.EmailDAO;
import uoa.lavs.sql.oop_to_sql.customer.NotesDAO;
import uoa.lavs.sql.oop_to_sql.customer.PhoneDAO;

public class CustomerCreationHelper {
  public static void setAddressDetails(Address address) {}

  public static void createCustomer(IndividualCustomer customer) {
    NotesDAO notesdao = new NotesDAO();
    notesdao.addNotes(customer.getNotes());

    AddressDAO addressdao = new AddressDAO();
    ArrayList<Address> addresses = customer.getAddresses();
    for (Address address : addresses) {
      addressdao.addAddress(address);
    }

    PhoneDAO phonedao = new PhoneDAO();
    ArrayList<Phone> phones = customer.getPhones();
    for (Phone phone : phones) {
      phonedao.addPhone(phone);
    }

    EmailDAO emaildao = new EmailDAO();
    ArrayList<Email> emails = customer.getEmails();
    for (Email email : emails) {
      emaildao.addEmail(email);
    }

    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();
    employerdao.addCustomerEmployer(customer.getEmployer());

    CustomerDAO customerdao = new CustomerDAO();
    customerdao.addCustomer(customer);
  }
}
