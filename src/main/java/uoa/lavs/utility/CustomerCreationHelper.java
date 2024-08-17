package uoa.lavs.utility;

import okhttp3.Address;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerContactDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerEmployerDAO;
import uoa.lavs.sql.oop_to_sql.customer.NotesDAO;

public class CustomerCreationHelper {
  public static void setAddressDetails(Address address) {}

  public static void createCustomer(IndividualCustomer customer) {
    NotesDAO notesdao = new NotesDAO();
    notesdao.addNotes(customer.getNotes());

    AddressDAO addressdao = new AddressDAO();
    addressdao.addAddress(customer.getPhysicalAddress());
    addressdao.addAddress(customer.getMailingAddress());
    addressdao.addAddress(customer.getEmployer().getEmployerAddress());

    CustomerContactDAO contactdao = new CustomerContactDAO();
    contactdao.addCustomerContact(customer.getContact());

    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();
    employerdao.addCustomerEmployer(customer.getEmployer());

    CustomerDAO customerdao = new CustomerDAO();
    customerdao.addCustomer(customer);
  }
}
