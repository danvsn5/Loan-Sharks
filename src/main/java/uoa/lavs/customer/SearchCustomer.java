package uoa.lavs.customer;

import java.util.ArrayList;
import java.util.List;

import uoa.lavs.mainframe.Connection;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.customer.FindCustomerAddress;
import uoa.lavs.mainframe.messages.customer.FindCustomerAdvanced;
import uoa.lavs.mainframe.messages.customer.LoadCustomer;
import uoa.lavs.mainframe.messages.customer.LoadCustomerAddress;
import uoa.lavs.mainframe.messages.customer.LoadCustomerEmails;
import uoa.lavs.mainframe.messages.customer.LoadCustomerEmployer;
import uoa.lavs.mainframe.messages.customer.LoadCustomerPhoneNumbers;

public class SearchCustomer {

  public SearchCustomer() {}

  public Customer searchCustomerById(String customerId) {
    LoadCustomer loadCustomer = new LoadCustomer();
    LoadCustomerAddress loadCustomerAddress = new LoadCustomerAddress();
    LoadCustomerEmployer loadCustomerEmployer = new LoadCustomerEmployer();
    LoadCustomerPhoneNumbers loadCustomerPhones = new LoadCustomerPhoneNumbers();
    LoadCustomerEmails loadCustomerEmails = new LoadCustomerEmails();

    loadCustomer.setCustomerId(customerId);
    loadCustomerEmployer.setCustomerId(customerId);
    loadCustomerPhones.setCustomerId(customerId);
    loadCustomerEmails.setCustomerId(customerId);
    loadCustomerEmployer.setNumber(1);
    loadCustomerAddress.setCustomerId(customerId);

    Connection connection = Instance.getConnection();

    // Send the request for customer
    

    Status status = loadCustomer.send(connection);
    // Send the request for address
    Status addressStatus = loadCustomerAddress.send(connection);
    // Send the request for employer
    Status employerStatus = loadCustomerEmployer.send(connection);
    // Send the request for phone
    Status phoneStatus = loadCustomerPhones.send(connection);
    // Send the request for email
    Status emailStatus = loadCustomerEmails.send(connection);

    if (status.getErrorCode() != 0) {
      System.out.println("Error loading customer: " + status.getErrorCode());
      return null;
    }

    Customer customer =
        new IndividualCustomer(
            "",
            "",
            "",
            null,
            "",
            "",
            "",
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new CustomerEmployer("", "", "", "", "", "", "", "", "", "", "", false));
    customer.setCustomerId(customerId);

    // Update customer fields
    updateCustomerFields(loadCustomer, customer);

    if (addressStatus.getErrorCode() == 0) {

      FindCustomerAddress findCustomerAddress = new FindCustomerAddress();
      findCustomerAddress.setCustomerId(customerId);
      findCustomerAddress.send(connection);
      int count = findCustomerAddress.getCountFromServer();
      updateCustomerAddress(loadCustomerAddress, customer, count);
    }

    if (employerStatus.getErrorCode() == 0) {
      updateCustomerEmployer(loadCustomerEmployer, customer);
    }

    if (phoneStatus.getErrorCode() == 0) {
      updateCustomerPhones(loadCustomerPhones, customer);
    }

    if (emailStatus.getErrorCode() == 0) {
      updateCustomerEmails(loadCustomerEmails, customer);
    }

    return customer;
  }

  // search customer by name
  public List<Customer> searchCustomerByName(String name) {
    FindCustomerAdvanced findCustomerAdvanced = new FindCustomerAdvanced();
    LoadCustomerAddress loadCustomerAddress = new LoadCustomerAddress();
    LoadCustomerEmployer loadCustomerEmployer = new LoadCustomerEmployer();
    LoadCustomerPhoneNumbers loadCustomerPhones = new LoadCustomerPhoneNumbers();
    LoadCustomerEmails loadCustomerEmails = new LoadCustomerEmails();
    Connection connection = Instance.getConnection();

    findCustomerAdvanced.setSearchName(name);
    Status status = findCustomerAdvanced.send(connection);

    int count = findCustomerAdvanced.getCustomerCountFromServer();

    if (count == 0) {
      System.out.println("No customers found.");
      return null;
    }

    List<Customer> customers = new ArrayList<>();

    for (int i = 1; i <= count; i++) {
      String customerId = findCustomerAdvanced.getIdFromServer(i);
  
      if (status.getErrorCode() != 0) {
        System.out.println("Error loading customer: " + status.getErrorCode());
        return null;
      }
  
      loadCustomerAddress.setCustomerId(customerId);
      loadCustomerEmployer.setCustomerId(customerId);
      loadCustomerPhones.setCustomerId(customerId);
      loadCustomerEmails.setCustomerId(customerId);
  
      // Send the request for address
      Status addressStatus = loadCustomerAddress.send(connection);
      // Send the request for employer
      Status employerStatus = loadCustomerEmployer.send(connection);
      // Send the request for phone
      Status phoneStatus = loadCustomerPhones.send(connection);
      // Send the request for email
      Status emailStatus = loadCustomerEmails.send(connection);
  
      if (status.getErrorCode() != 0) {
        System.out.println("Error loading customer: " + status.getErrorCode());
        return null;
      }
  
      Customer customer =
          new IndividualCustomer(
              "",
              "",
              "",
              null,
              "",
              "",
              "",
              new ArrayList<>(),
              new ArrayList<>(),
              new ArrayList<>(),
              new ArrayList<>(),
              new CustomerEmployer("", "", "", "", "", "", "", "", "", "", "", false));
      customer.setCustomerId(customerId);
  
      LoadCustomer loadCustomer = new LoadCustomer();
  
      loadCustomer.setCustomerId(customerId);
      status = loadCustomer.send(connection);
  
      if (status.getErrorCode() != 0) {
        System.out.println("Error loading customer: " + status.getErrorCode());
        return null;
      }
  
      updateCustomerFields(loadCustomer, customer);
      if (addressStatus.getErrorCode() == 0) {
  
        FindCustomerAddress findCustomerAddress = new FindCustomerAddress();
        findCustomerAddress.setCustomerId(customerId);
        findCustomerAddress.send(connection);
        int num = findCustomerAddress.getCountFromServer();
        updateCustomerAddress(loadCustomerAddress, customer, num);
      }
  
      if (employerStatus.getErrorCode() == 0) {
        updateCustomerEmployer(loadCustomerEmployer, customer);
      }
  
      if (phoneStatus.getErrorCode() == 0) {
        updateCustomerPhones(loadCustomerPhones, customer);
      }
  
      if (emailStatus.getErrorCode() == 0) {
        updateCustomerEmails(loadCustomerEmails, customer);
      }

      customers.add(customer);
  
    }

    for (Customer customer : customers) {
      System.out.println(customer.getName());
    }

    return customers;
  }

  private void updateCustomerFields(LoadCustomer loadCustomer, Customer customer) {
    customer.setTitle(loadCustomer.getTitleFromServer());
    customer.setName(loadCustomer.getNameFromServer());
    customer.setDateOfBirth(loadCustomer.getDateofBirthFromServer());
    customer.setOccupation(loadCustomer.getOccupationFromServer());
    customer.setCitizenship(loadCustomer.getCitizenshipFromServer());
    customer.setVisa(loadCustomer.getVisaFromServer());
  }

  private void updateCustomerAddress(
      LoadCustomerAddress loadCustomerAddress, Customer customer, int count) {
    ArrayList<Address> addresses = new ArrayList<>();

    for (int i = 1; i <= count; i++) {
      loadCustomerAddress.setNumber(i);
      Address address =
          new Address(
              customer.getCustomerId(),
              loadCustomerAddress.getTypeFromServer(),
              loadCustomerAddress.getLine1FromServer(),
              loadCustomerAddress.getLine2FromServer(),
              loadCustomerAddress.getSuburbFromServer(),
              loadCustomerAddress.getPostCodeFromServer(),
              loadCustomerAddress.getCityFromServer(),
              loadCustomerAddress.getCountryFromServer(),
              loadCustomerAddress.getIsPrimaryFromServer(),
              loadCustomerAddress.getIsMailingFromServer());
      address.setAddressId(i);
      addresses.add(address);
    }
    customer.setAddresses(addresses);
  }

  private void updateCustomerEmployer(
      LoadCustomerEmployer loadCustomerEmployer, Customer customer) {
    CustomerEmployer employer = customer.getEmployer();
    employer.setCustomerId(customer.getCustomerId());
    employer.setEmployerName(loadCustomerEmployer.getNameFromServer());
    employer.setLineOne(loadCustomerEmployer.getLine1FromServer());
    employer.setLineTwo(loadCustomerEmployer.getLine2FromServer());
    employer.setSuburb(loadCustomerEmployer.getSuburbFromServer());
    employer.setCity(loadCustomerEmployer.getCityFromServer());
    employer.setPostCode(loadCustomerEmployer.getPostCodeFromServer());
    employer.setCountry(loadCustomerEmployer.getCountryFromServer());
    employer.setEmployerEmail(loadCustomerEmployer.getEmailAddressFromServer());
    employer.setEmployerWebsite(loadCustomerEmployer.getWebsiteFromServer());
    employer.setEmployerPhone(loadCustomerEmployer.getPhoneNumberFromServer());
    employer.setOwnerOfCompany(loadCustomerEmployer.getIsOwnerFromServer());
    customer.setEmployer(employer);
  }

  private void updateCustomerPhones(
      LoadCustomerPhoneNumbers loadCustomerPhones, Customer customer) {
    ArrayList<Phone> phoneNumbers = new ArrayList<>();
    int count = loadCustomerPhones.getCountFromServer();
    for (int i = 1; i <= count; i++) {
      Phone phoneNumber =
          new Phone(
              customer.getCustomerId(),
              loadCustomerPhones.getTypeFromServer(i),
              loadCustomerPhones.getPrefixFromServer(i),
              loadCustomerPhones.getPhoneNumberFromServer(i),
              loadCustomerPhones.getIsPrimaryFromServer(i),
              loadCustomerPhones.getCanSendTxtFromServer(i));
      phoneNumbers.add(phoneNumber);
    }
    customer.setPhones(phoneNumbers);
  }

  private void updateCustomerEmails(LoadCustomerEmails loadCustomerEmails, Customer customer) {
    ArrayList<Email> emails = new ArrayList<>();

    int count = loadCustomerEmails.getCountFromServer();
    for (int i = 1; i <= count; i++) {
      Email email =
          new Email(
              customer.getCustomerId(),
              loadCustomerEmails.getAddressFromServer(i),
              loadCustomerEmails.getIsPrimaryFromServer(i));
      emails.add(email);
    }
    customer.setEmails(emails);
  }

  public static void main(String[] args) {
    SearchCustomer searchCustomer = new SearchCustomer();
    Customer customer = searchCustomer.searchCustomerById("1");
    System.out.println(customer.getCitizenship());
  }
}
