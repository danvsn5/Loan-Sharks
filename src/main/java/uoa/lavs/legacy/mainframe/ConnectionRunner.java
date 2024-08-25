package uoa.lavs.legacy.mainframe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import uoa.lavs.legacy.mainframe.messages.customer.FindCustomerAddress;
import uoa.lavs.legacy.mainframe.messages.customer.FindCustomerAdvanced;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomer;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerEmails;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerEmployer;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerPhoneNumbers;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomer;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomerAddress;

public class ConnectionRunner {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Connection connection = Instance.getConnection();

    while (true) {
      System.out.println("Choose an option:");
      System.out.println("1. Add a new customer");
      System.out.println("2. View customer details");
      System.out.println("3. Exit");
      int choice = scanner.nextInt();
      scanner.nextLine(); // consume the newline

      switch (choice) {
        case 1:
          addCustomer(scanner, connection);
          break;
        case 2:
          viewCustomerDetails(scanner, connection);
          break;
        case 3:
          System.out.println("Exiting...");
          closeConnection(connection);
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  private static void addCustomer(Scanner scanner, Connection connection) {
    UpdateCustomer updateCustomer = new UpdateCustomer();
    updateCustomer.setCustomerId(null); // Setting ID to null for adding a new customer

    System.out.print("Enter title: ");
    updateCustomer.setTitle(scanner.nextLine());

    System.out.print("Enter name: ");
    updateCustomer.setName(scanner.nextLine());

    System.out.print("Enter date of birth (yyyy-mm-dd): ");
    updateCustomer.setDateofBirth(LocalDate.parse(scanner.nextLine()));

    System.out.print("Enter occupation: ");
    updateCustomer.setOccupation(scanner.nextLine());

    System.out.print("Enter citizenship: ");
    updateCustomer.setCitizenship(scanner.nextLine());

    System.out.print("Enter visa: ");
    updateCustomer.setVisa(scanner.nextLine());

    // Send the update request for customer
    Status status = updateCustomer.send(connection);

    // Add address details
    UpdateCustomerAddress updateAddress = new UpdateCustomerAddress();
    updateAddress.setCustomerId(
        updateCustomer.getCustomerIdFromServer()); // Set customer ID after creation

    System.out.print("Enter address number: ");
    updateAddress.setNumber(scanner.nextInt());
    scanner.nextLine(); // consume the newline

    System.out.print("Enter address type: ");
    updateAddress.setType(scanner.nextLine());

    System.out.print("Enter address line 1: ");
    updateAddress.setLine1(scanner.nextLine());

    System.out.print("Enter address line 2 (optional): ");
    updateAddress.setLine2(scanner.nextLine());

    System.out.print("Enter suburb: ");
    updateAddress.setSuburb(scanner.nextLine());

    System.out.print("Enter city: ");
    updateAddress.setCity(scanner.nextLine());

    System.out.print("Enter post code: ");
    updateAddress.setPostCode(scanner.nextLine());

    System.out.print("Enter country: ");
    updateAddress.setCountry(scanner.nextLine());

    // Send the address update request
    Status addressStatus = updateAddress.send(connection);

    // Print status for customer
    System.out.println("Update Status:");
    System.out.println("Status Code: " + status.getErrorCode());
    System.out.println("Status Message: " + status.getErrorMessage());
    System.out.println("Transaction ID: " + status.getTransactionId());
    System.out.println("Customer ID: " + updateCustomer.getCustomerIdFromServer());

    // Print status for address
    System.out.println("Address Update Status:");
    System.out.println("Status Code: " + addressStatus.getErrorCode());
    System.out.println("Status Message: " + addressStatus.getErrorMessage());
  }

  private static void viewCustomerDetails(Scanner scanner, Connection connection) {
    System.out.println("Search by:");
    System.out.println("1. Customer ID");
    System.out.println("2. Name");
    int searchChoice = scanner.nextInt();
    scanner.nextLine(); // consume the newline

    switch (searchChoice) {
      case 1:
        searchById(scanner, connection);
        break;
      case 2:
        searchByName(scanner, connection);
        break;
      default:
        System.out.println("Invalid choice. Please try again.");
    }
  }

  private static void searchById(Scanner scanner, Connection connection) {
    LoadCustomer loadCustomer = new LoadCustomer();
    FindCustomerAddress findCustomerAddress = new FindCustomerAddress();
    LoadCustomerEmployer loadCustomerEmployer = new LoadCustomerEmployer();
    LoadCustomerPhoneNumbers loadCustomerPhones = new LoadCustomerPhoneNumbers();
    LoadCustomerEmails loadCustomerEmails = new LoadCustomerEmails();


    System.out.print("Enter customer ID: ");
    String customerId = scanner.nextLine();
    loadCustomer.setCustomerId(customerId);
    loadCustomerEmployer.setCustomerId(customerId);
    loadCustomerPhones.setCustomerId(customerId);
    loadCustomerEmails.setCustomerId(customerId);
    loadCustomerEmployer.setNumber(1);
    findCustomerAddress.setCustomerId(customerId);

    // Send the request for customer
    Status status = loadCustomer.send(connection);
    // Send the request for address
    Status addressStatus = findCustomerAddress.send(connection);

    // Send the request for employer
    Status employerStatus = loadCustomerEmployer.send(connection);

    // Send the request for phone
    Status phoneStatus = loadCustomerPhones.send(connection);

    // Send the request for email
    Status emailStatus = loadCustomerEmails.send(connection);

    if (status.getErrorCode() != 0) {
      System.out.println("Customer not found.");
      return;
    }

    if (addressStatus.getErrorCode() != 0) {
      System.out.println("Address not found for this customer.");
      return;
    }

    if (employerStatus.getErrorCode() != 0) {
      System.out.println("Employer not found for this customer.");
      return;
    }

    if (phoneStatus.getErrorCode() != 0) {
      System.out.println("Phone numbers not found for this customer.");
      return;
    }

    if (emailStatus.getErrorCode() != 0) {
      System.out.println("Email addresses not found for this customer.");
      return;
    }

    // Print customer details
    printCustomerDetails(loadCustomer);
    printCustomerAddressDetails(findCustomerAddress);
    printCustomerEmployerDetails(loadCustomerEmployer);
    printCustomerPhoneDetails(loadCustomerPhones);
    printCustomerEmailDetails(loadCustomerEmails);
  }

  private static void searchByName(Scanner scanner, Connection connection) {
    FindCustomerAdvanced findCustomerAdvanced = new FindCustomerAdvanced();
    FindCustomerAddress findCustomerAddress = new FindCustomerAddress();

    System.out.print("Enter customer name: ");
    findCustomerAdvanced.setSearchName(scanner.nextLine());

    // Send the request
    Status status = findCustomerAdvanced.send(connection);

    // get customer id from server
    findCustomerAddress.setCustomerId(findCustomerAdvanced.getIdFromServer(1));

    // Send the request for address
    Status addressStatus = findCustomerAddress.send(connection);

    if (findCustomerAdvanced.getCustomerCountFromServer() == 0) {
      System.out.println("No customers found.");
      return;
    }

    // Print customer details
    printCustomerDetails(findCustomerAdvanced);
    printCustomerAddressDetails(findCustomerAddress);
  }

  private static void printCustomerDetails(FindCustomerAdvanced findCustomerAdvanced) {
    int count = findCustomerAdvanced.getCustomerCountFromServer();
    if (count == 0) {
      System.out.println("No customers found.");
      return;
    }

    for (int i = 1; i <= count; i++) {
      System.out.println("Customer " + i + ":");
      System.out.println("ID: " + findCustomerAdvanced.getIdFromServer(i));
      System.out.println("Name: " + findCustomerAdvanced.getNameFromServer(i));
      System.out.println("Date of Birth: " + findCustomerAdvanced.getDateofBirthFromServer(i));
      System.out.println();
    }
  }

  private static void printCustomerDetails(LoadCustomer loadCustomer) {
    System.out.println("Customer Details:");
    System.out.println("Title: " + loadCustomer.getTitleFromServer());
    System.out.println("Name: " + loadCustomer.getNameFromServer());
    System.out.println("Date of Birth: " + loadCustomer.getDateofBirthFromServer());
    System.out.println("Occupation: " + loadCustomer.getOccupationFromServer());
    System.out.println("Citizenship: " + loadCustomer.getCitizenshipFromServer());
    System.out.println("Visa: " + loadCustomer.getVisaFromServer());
    System.out.println("Status: " + loadCustomer.getStatusFromServer());
  }

  private static void printCustomerAddressDetails(FindCustomerAddress findCustomerAddress) {
    int count = findCustomerAddress.getCountFromServer();
    if (count == 0) {
      System.out.println("No address found for this customer.");
      return;
    }

    for (int i = 1; i <= count; i++) {
      System.out.println("Address " + i + ":");
      System.out.println("Number: " + findCustomerAddress.getNumberFromServer(i));
      System.out.println("Type: " + findCustomerAddress.getTypeFromServer(i));
      System.out.println("Is Primary: " + findCustomerAddress.getIsPrimaryFromServer(i));
      System.out.println("Is Mailing: " + findCustomerAddress.getIsMailingFromServer(i));
      System.out.println();
    }
  }

  private static void printCustomerEmployerDetails(LoadCustomerEmployer loadCustomerEmployer) {
    System.out.println("Customer Employer Details:");
    System.out.println("--------------------------");
    System.out.println("Employer Name: " + loadCustomerEmployer.getNameFromServer());
    System.out.println("Address Line One: " + loadCustomerEmployer.getLine1FromServer());
    System.out.println("Address Line Two: " + loadCustomerEmployer.getLine2FromServer());
    System.out.println("Suburb: " + loadCustomerEmployer.getSuburbFromServer());
    System.out.println("City: " + loadCustomerEmployer.getCityFromServer());
    System.out.println("Post Code: " + loadCustomerEmployer.getPostCodeFromServer());
    System.out.println("Country: " + loadCustomerEmployer.getCountryFromServer());
    System.out.println("Employer Email: " + loadCustomerEmployer.getEmailAddressFromServer());
    System.out.println("Employer Website: " + loadCustomerEmployer.getWebsiteFromServer());
    System.out.println("Employer Phone: " + loadCustomerEmployer.getPhoneNumberFromServer());
    System.out.println(
        "Owner of Company: " + (loadCustomerEmployer.getIsOwnerFromServer() ? "Yes" : "No"));
    System.out.println("--------------------------");
  }

  private static void printCustomerPhoneDetails(LoadCustomerPhoneNumbers loadCustomerPhones) {
    int count = loadCustomerPhones.getCountFromServer();
    if (count == 0) {
      System.out.println("No phone numbers found for this customer.");
      return;
    }
    for (int i = 1; i <= count; i++) {
      System.out.println("Phone Number " + (i) + ":");
      System.out.println("Type: " + loadCustomerPhones.getTypeFromServer(i));
      System.out.println("Prefix: " + loadCustomerPhones.getPrefixFromServer(i));
      System.out.println("Number: " + loadCustomerPhones.getPhoneNumberFromServer(i));
      System.out.println("Is Primary: " + loadCustomerPhones.getIsPrimaryFromServer(i));
      System.out.println("Can Send Text: " + loadCustomerPhones.getCanSendTxtFromServer(i));
      System.out.println();
    }
  }

  // emails
  private static void printCustomerEmailDetails(LoadCustomerEmails loadCustomerEmails) {
    int count = loadCustomerEmails.getCountFromServer();
    if (count == 0) {
      System.out.println("No email addresses found for this customer.");
      return;
    }
    for (int i = 1; i <= count; i++) {
      System.out.println("Email Address " + (i) + ":");
      System.out.println("Email: " + loadCustomerEmails.getAddressFromServer(i));
      System.out.println("Is Primary: " + loadCustomerEmails.getIsPrimaryFromServer(i));
      System.out.println();
    }
  }

  private static void closeConnection(Connection connection) {
    try {
      connection.close();
    } catch (IOException e) {
      System.err.println("Error closing connection: " + e.getMessage());
    }
  }
}
