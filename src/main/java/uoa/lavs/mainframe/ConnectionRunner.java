package uoa.lavs.mainframe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import uoa.lavs.mainframe.messages.customer.FindCustomerAddress;
import uoa.lavs.mainframe.messages.customer.FindCustomerAdvanced;
import uoa.lavs.mainframe.messages.customer.LoadCustomer;
import uoa.lavs.mainframe.messages.customer.LoadCustomerEmployer;
import uoa.lavs.mainframe.messages.customer.UpdateCustomer;
import uoa.lavs.mainframe.messages.customer.UpdateCustomerAddress;

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

    System.out.print("Enter customer ID: ");
    String customerId = scanner.nextLine();
    loadCustomer.setCustomerId(customerId);
    loadCustomerEmployer.setCustomerId(customerId);
    loadCustomerEmployer.setNumber(1);
    findCustomerAddress.setCustomerId(customerId);

    // Send the request for customer
    Status status = loadCustomer.send(connection);
    // Send the request for address
    Status addressStatus = findCustomerAddress.send(connection);

    // Send the request for employer
    Status employerStatus = loadCustomerEmployer.send(connection);

    if (status.getErrorCode() != 0 || addressStatus.getErrorCode() != 0 || employerStatus.getErrorCode() != 0) {
      System.out.println("Error fetching customer details: " + status.getErrorMessage());
      return;
    }

    // Print customer details
    printCustomerDetails(loadCustomer);
    printCustomerAddressDetails(findCustomerAddress);
    printCustomerEmployerDetails(loadCustomerEmployer);
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
    System.out.println("Owner of Company: " + (loadCustomerEmployer.getIsOwnerFromServer() ? "Yes" : "No"));
    System.out.println("--------------------------");
}

  private static void closeConnection(Connection connection) {
    try {
      connection.close();
    } catch (IOException e) {
      System.err.println("Error closing connection: " + e.getMessage());
    }
  }
}
