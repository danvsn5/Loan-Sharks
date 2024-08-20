package uoa.lavs.utility;

import java.util.ArrayList;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.Email;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.Note;
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
    CustomerDAO customerdao = new CustomerDAO();
    customerdao.addCustomer(customer);

    NotesDAO notesdao = new NotesDAO();
    ArrayList<Note> notes = customer.getNotes();
    for (Note note : notes) {
      note.setCustomerId(customer.getCustomerId());
      notesdao.addNote(note);
    }

    AddressDAO addressdao = new AddressDAO();
    ArrayList<Address> addresses = customer.getAddresses();
    for (Address address : addresses) {
      address.setCustomerId(customer.getCustomerId());
      addressdao.addAddress(address);
    }

    PhoneDAO phonedao = new PhoneDAO();
    ArrayList<Phone> phones = customer.getPhones();
    for (Phone phone : phones) {
      phone.setCustomerId(customer.getCustomerId());
      phonedao.addPhone(phone);
    }

    EmailDAO emaildao = new EmailDAO();
    ArrayList<Email> emails = customer.getEmails();
    for (Email email : emails) {
      email.setCustomerId(customer.getCustomerId());
      emaildao.addEmail(email);
    }

    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();
    customer.getEmployer().setCustomerId(customer.getCustomerId());
    employerdao.addCustomerEmployer(customer.getEmployer());
  }
}
