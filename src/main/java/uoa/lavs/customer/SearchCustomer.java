package uoa.lavs.customer;

import java.util.ArrayList;
import java.util.List;
import uoa.lavs.mainframe.Connection;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.customer.FindCustomerAdvanced;
import uoa.lavs.mainframe.messages.customer.LoadCustomer;
import uoa.lavs.mainframe.messages.customer.LoadCustomerAddress;
import uoa.lavs.mainframe.messages.customer.LoadCustomerAddresses;
import uoa.lavs.mainframe.messages.customer.LoadCustomerEmails;
import uoa.lavs.mainframe.messages.customer.LoadCustomerEmployer;
import uoa.lavs.mainframe.messages.customer.LoadCustomerPhoneNumbers;
import uoa.lavs.sql.oop_to_sql.customer.CustomerDAO;

public class SearchCustomer {
  public Status statusInstance;

  public SearchCustomer() {
    this.statusInstance = null;
  }

  public Status getStatusInstance() {
    return statusInstance;
  }

  public void setStatusInstance(Status statusInstance) {
    this.statusInstance = statusInstance;
  }

  public Customer searchCustomerById(String customerId, Connection connection) {
    LoadCustomer loadCustomer = new LoadCustomer();
    LoadCustomerAddresses loadCustomerAddresses = new LoadCustomerAddresses();
    LoadCustomerEmployer loadCustomerEmployer = new LoadCustomerEmployer();
    LoadCustomerPhoneNumbers loadCustomerPhones = new LoadCustomerPhoneNumbers();
    LoadCustomerEmails loadCustomerEmails = new LoadCustomerEmails();

    loadCustomer.setCustomerId(customerId);
    loadCustomerEmployer.setCustomerId(customerId);
    loadCustomerPhones.setCustomerId(customerId);
    loadCustomerEmails.setCustomerId(customerId);
    loadCustomerEmployer.setNumber(1);
    loadCustomerAddresses.setCustomerId(customerId);

    // Send the request for customer

    Status status = loadCustomer.send(connection);
    // Send the request for address
    Status addressStatus = loadCustomerAddresses.send(connection);
    // Send the request for employer
    Status employerStatus = loadCustomerEmployer.send(connection);
    // Send the request for phone
    Status phoneStatus = loadCustomerPhones.send(connection);
    // Send the request for email
    Status emailStatus = loadCustomerEmails.send(connection);

    setStatusInstance(status);

    if (status.getErrorCode() == 1000
        || status.getErrorCode() == 1010
        || status.getErrorCode() == 1020) {
      System.out.println("Error loading customer: " + status.getErrorCode());
      System.out.println(status.getErrorMessage());
      System.out.println("Checking local database...");

      CustomerDAO customerDAO = new CustomerDAO();
      Customer customer = customerDAO.getCustomer(customerId);
      if (customer != null) {
        System.out.println(customer.getName());
        return customer;
      } else {
        System.out.println("Customer not found in local database.");
      }
      return null;
    }

    System.out.println("Searching mainframe...");

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
      System.out.println("Updating addresses...");
      updateCustomerAddresses(loadCustomerAddresses, customer, connection);
    }

    if (employerStatus.getErrorCode() == 0) {
      System.out.println("Updating employer...");
      updateCustomerEmployer(loadCustomerEmployer, customer);
    }

    if (phoneStatus.getErrorCode() == 0) {
      System.out.println("Updating phones...");
      updateCustomerPhones(loadCustomerPhones, customer);
    }

    if (emailStatus.getErrorCode() == 0) {
      System.out.println("Updating emails...");
      updateCustomerEmails(loadCustomerEmails, customer);
    }

    System.out.println("Customer found: " + customer.getName());

    return customer;
  }

  // search customer by name
  public List<Customer> searchCustomerByName(String name, Connection connection) {
    FindCustomerAdvanced findCustomerAdvanced = new FindCustomerAdvanced();
    LoadCustomerAddresses loadCustomerAddresses = new LoadCustomerAddresses();
    LoadCustomerEmployer loadCustomerEmployer = new LoadCustomerEmployer();
    LoadCustomerPhoneNumbers loadCustomerPhones = new LoadCustomerPhoneNumbers();
    LoadCustomerEmails loadCustomerEmails = new LoadCustomerEmails();

    findCustomerAdvanced.setSearchName(name);
    Status status = findCustomerAdvanced.send(connection);

    setStatusInstance(status);

    if (status.getErrorCode() == 1000
        || status.getErrorCode() == 1010
        || status.getErrorCode() == 1020) {
      System.out.println("Error loading customer: " + status.getErrorCode());
      System.out.println(status.getErrorMessage());
      System.out.println("Checking local database...");
      CustomerDAO customerDAO = new CustomerDAO();
      ArrayList<Customer> customers = customerDAO.getCustomersByName(name);
      if (customers != null && customers.size() > 0) {
        for (Customer customer : customers) {
          System.out.println(customer.getName());
        }
        return customers;
      } else {
        System.out.println("Customer not found in local database.");
      }
      return null;
    }

    System.out.println("Searching mainframe...");

    Integer count = findCustomerAdvanced.getCustomerCountFromServer();

    if (count == null || count == 0) {
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

      loadCustomerAddresses.setCustomerId(customerId);
      loadCustomerEmployer.setCustomerId(customerId);
      loadCustomerEmployer.setNumber(1);
      loadCustomerPhones.setCustomerId(customerId);
      loadCustomerEmails.setCustomerId(customerId);

      // Send the request for address
      Status addressStatus = loadCustomerAddresses.send(connection);
      // Send the request for employer
      System.out.println("Sending employer request...");
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
        System.out.println("Error customer: " + status.getErrorCode());
        return null;
      }

      updateCustomerFields(loadCustomer, customer);
      if (addressStatus.getErrorCode() == 0) {
        System.out.println("Updating addresses...");
        updateCustomerAddresses(loadCustomerAddresses, customer, connection);
      }

      if (employerStatus.getErrorCode() == 0) {
        System.out.println("Updating employer...");
        updateCustomerEmployer(loadCustomerEmployer, customer);
      } else {
        System.out.println("Employer error: " + employerStatus.getErrorCode());
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

  private void updateCustomerAddresses(
      LoadCustomerAddresses loadCustomerAddresses, Customer customer, Connection connection) {
    ArrayList<Address> addresses = new ArrayList<>();

    Integer count = loadCustomerAddresses.getCountFromServer();
    int primary = 0;
    int mailing = 0;

    for (int i = 1; i <= count; i++) {
      if (loadCustomerAddresses.getIsPrimaryFromServer(i)) {
        primary = i;
      }
      if (loadCustomerAddresses.getIsMailingFromServer(i)) {
        mailing = i;
      }
    }

    if (count == null || count == 0) {
      customer.setAddresses(addresses);
      return;
    }

    for (int i = 1; i <= count; i++) {
      LoadCustomerAddress loadCustomerAddress = new LoadCustomerAddress();
      loadCustomerAddress.setCustomerId(customer.getCustomerId());
      loadCustomerAddress.setNumber(i);
      Status status = loadCustomerAddress.send(connection);
      System.out.println("Status: " + status.getErrorCode());

      // int attempts = 0;
      // while (true) {
      //   status = loadCustomerAddress.send(Instance.getConnection());
      //   if (status.getErrorCode() == 0) {
      //     break;
      //   }
      //   attempts++;
      //   if (attempts > 5) {
      //     System.out.println("Error loading address: " + status.getErrorCode());
      //     break;
      //   }
      // }

      // if (attempts > 5) {
      //   continue;
      // }

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
              false,
              false);
      if (i == primary) {
        address.setIsPrimary(true);
      }
      if (i == mailing) {
        address.setIsMailing(true);
      }
      address.setAddressId(i);
      addresses.add(address);
      System.out.println("address type: " + address.getAddressType());
      System.out.println("address line 1: " + address.getAddressLineOne());
      System.out.println("address line 2: " + address.getAddressLineTwo());
      System.out.println("address suburb: " + address.getSuburb());
      System.out.println("address city: " + address.getCity());
      System.out.println("address country: " + address.getCountry());
      System.out.println("address post code: " + address.getPostCode());
      System.out.println("address is primary: " + address.getIsPrimary());
      System.out.println("address is mailing: " + address.getIsMailing());
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

  // public static void main(String[] args) {
  //   SearchCustomer searchCustomer = new SearchCustomer();
  //   Connection connection = Instance.getConnection();
  //   Customer customer = searchCustomer.searchCustomerById("1", connection);
  //   System.out.println(customer.getCitizenship());
  // }
}
