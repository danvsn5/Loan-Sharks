package uoa.lavs.backend.oop.customer;

import java.util.ArrayList;
import java.util.List;
import uoa.lavs.backend.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Instance;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.FindCustomerAdvanced;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomer;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerAddress;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerAddresses;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerEmails;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerEmployer;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerNote;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerPhoneNumbers;

public class SearchCustomer {
  public Status statusInstance;
  public FindCustomerAdvanced findCustomerAdvanced;
  public LoadCustomer loadCustomer;
  public LoadCustomerAddresses loadCustomerAddresses;
  public LoadCustomerEmployer loadCustomerEmployer;
  public LoadCustomerPhoneNumbers loadCustomerPhones;
  public LoadCustomerEmails loadCustomerEmails;
  public LoadCustomerNote loadCustomerNotes;

  public SearchCustomer() {
    this.statusInstance = null;
    this.findCustomerAdvanced = new FindCustomerAdvanced();
    this.loadCustomer = new LoadCustomer();
    this.loadCustomerAddresses = new LoadCustomerAddresses();
    this.loadCustomerEmployer = new LoadCustomerEmployer();
    this.loadCustomerPhones = new LoadCustomerPhoneNumbers();
    this.loadCustomerEmails = new LoadCustomerEmails();
    this.loadCustomerNotes = new LoadCustomerNote();
  }

  public Status getStatusInstance() {
    return statusInstance;
  }

  public void setStatusInstance(Status statusInstance) {
    this.statusInstance = statusInstance;
  }

  public Customer searchCustomerById(String customerId, Connection connection) {
    loadCustomer.setCustomerId(customerId);
    loadCustomerEmployer.setCustomerId(customerId);
    loadCustomerPhones.setCustomerId(customerId);
    loadCustomerEmails.setCustomerId(customerId);
    loadCustomerEmployer.setNumber(1);
    loadCustomerAddresses.setCustomerId(customerId);
    loadCustomerNotes.setCustomerId(customerId);
    loadCustomerNotes.setNumber(1);

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

    Status noteStatus = loadCustomerNotes.send(connection);

    setStatusInstance(status);

    // print all the error codes
    System.out.println("Status: " + status.getErrorCode());
    System.out.println("Address Status: " + addressStatus.getErrorCode());
    System.out.println("Employer Status: " + employerStatus.getErrorCode());
    System.out.println("Phone Status: " + phoneStatus.getErrorCode());

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
      }
      System.out.println("Customer not found in local database.");
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

    if (noteStatus.getErrorCode() == 0) {
      System.out.println("Updating notes...");
      updateCustomerNotes(loadCustomerNotes, customer);
    }

    System.out.println("Customer found: " + customer.getName());

    return customer;
  }

  // search customer by name
  public List<Customer> searchCustomerByName(String name, Connection connection) {
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
      loadCustomerNotes.setCustomerId(customerId);
      loadCustomerNotes.setNumber(1);

      // Send the request for address
      Status addressStatus = loadCustomerAddresses.send(connection);
      // Send the request for employer
      System.out.println("Sending employer request...");
      Status employerStatus = loadCustomerEmployer.send(connection);
      // Send the request for phone
      Status phoneStatus = loadCustomerPhones.send(connection);
      // Send the request for email
      Status emailStatus = loadCustomerEmails.send(connection);

      Status noteStatus = loadCustomerNotes.send(connection);

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
      } 

      if (phoneStatus.getErrorCode() == 0) {
        updateCustomerPhones(loadCustomerPhones, customer);
      }

      if (emailStatus.getErrorCode() == 0) {
        updateCustomerEmails(loadCustomerEmails, customer);
      }

      if (noteStatus.getErrorCode() == 0) {
        System.out.println("Updating notes...");
        updateCustomerNotes(loadCustomerNotes, customer);
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

  public void updateCustomerAddresses(
      LoadCustomerAddresses loadCustomerAddresses, Customer customer, Connection connection) {
    ArrayList<Address> addresses = new ArrayList<>();

    Integer count = loadCustomerAddresses.getCountFromServer();
    int primary = 0;
    int mailing = 0;

    if (count == null || count == 0) {
      customer.setAddresses(addresses);
      return;
    }

    for (int i = 1; i <= count; i++) {
      if (loadCustomerAddresses.getIsPrimaryFromServer(i)) {
        primary = i;
      }
      if (loadCustomerAddresses.getIsMailingFromServer(i)) {
        mailing = i;
      }
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

  private void updateCustomerNotes(LoadCustomerNote loadCustomerNotes, Customer customer) {
    ArrayList<Note> notes = new ArrayList<>();
    int count = loadCustomerNotes.getPageCountFromServer();
    for (int i = 1; i <= count; i++) {
      loadCustomerNotes.setNumber(i);
      loadCustomerNotes.send(Instance.getConnection());
      Note note = new Note(customer.getCustomerId(), new String[19]);
      note.setNoteId(i);
      Integer lineCount = loadCustomerNotes.getLineCountFromServer();
      if (lineCount == null) {
        continue;
      }
      for (int j = 1; j <= lineCount; j++) {
        note.setLine(j - 1, loadCustomerNotes.getLineFromServer(j));
      }
      notes.add(note);
    }
    customer.setNotes(notes);
  }

  // mock method to test statuses for find customer advanced
  public void createMockFindCustomerAdvanced(Integer errorCode) {
    this.findCustomerAdvanced =
        new FindCustomerAdvanced() {
          @Override
          public Status send(Connection connection) {
            return new Status(errorCode, "Error finding customer", 1);
          }
        };
  }

  // mock method to test statuses for load customer
  public void createMockLoadCustomer(Integer errorCode) {
    this.loadCustomer =
        new LoadCustomer() {
          @Override
          public Status send(Connection connection) {
            return new Status(errorCode, "Error loading customer", 1);
          }
        };
  }

  // mock method to test statuses for load customer addresses
  public void createMockLoadCustomerAddresses(Integer errorCode) {
    this.loadCustomerAddresses =
        new LoadCustomerAddresses() {
          @Override
          public Status send(Connection connection) {
            return new Status(errorCode, "Error loading customer addresses", 1);
          }
        };
  }

  // mock method to test statuses for load customer employer
  public void createMockLoadCustomerEmployer(Integer errorCode) {
    this.loadCustomerEmployer =
        new LoadCustomerEmployer() {
          @Override
          public Status send(Connection connection) {
            return new Status(errorCode, "Error loading customer employer", 1);
          }
        };
  }

  // mock method to test statuses for load customer phone numbers
  public void createMockLoadCustomerPhoneNumbers(Integer errorCode) {
    this.loadCustomerPhones =
        new LoadCustomerPhoneNumbers() {
          @Override
          public Status send(Connection connection) {
            return new Status(errorCode, "Error loading customer phone numbers", 1);
          }
        };
  }

  // mock method to test statuses for load customer emails
  public void createMockLoadCustomerEmails(Integer errorCode) {
    this.loadCustomerEmails =
        new LoadCustomerEmails() {
          @Override
          public Status send(Connection connection) {
            return new Status(errorCode, "Error loading customer emails", 1);
          }
        };
  }

  // mock method to test statuses for load customer notes
  public void createMockLoadCustomerNotes(Integer errorCode) {
    this.loadCustomerNotes =
        new LoadCustomerNote() {
          @Override
          public Status send(Connection connection) {
            return new Status(errorCode, "Error loading customer notes", 1);
          }
        };
  }

  // method to reset load messages
  public void resetLoadMessages() {
    this.findCustomerAdvanced = new FindCustomerAdvanced();
    this.loadCustomer = new LoadCustomer();
    this.loadCustomerAddresses = new LoadCustomerAddresses();
    this.loadCustomerEmployer = new LoadCustomerEmployer();
    this.loadCustomerPhones = new LoadCustomerPhoneNumbers();
    this.loadCustomerEmails = new LoadCustomerEmails();
    this.loadCustomerNotes = new LoadCustomerNote();
  }
}
