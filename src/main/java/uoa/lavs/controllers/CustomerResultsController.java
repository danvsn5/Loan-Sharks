package uoa.lavs.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.Customer;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.Email;
import uoa.lavs.customer.Phone;
import uoa.lavs.loan.PersonalLoanSingleton;

public class CustomerResultsController {
  // make labels for name and ID, 6 each
  @FXML private Label searchResultsNameLabel1;
  @FXML private Label searchResultsNameLabel2;
  @FXML private Label searchResultsNameLabel3;
  @FXML private Label searchResultsNameLabel4;
  @FXML private Label searchResultsNameLabel5;
  @FXML private Label searchResultsNameLabel6;

  @FXML private Label searchResultsIDLabel1;
  @FXML private Label searchResultsIDLabel2;
  @FXML private Label searchResultsIDLabel3;
  @FXML private Label searchResultsIDLabel4;
  @FXML private Label searchResultsIDLabel5;
  @FXML private Label searchResultsIDLabel6;

  // make rectangles, 6 each
  @FXML private Rectangle searchResultsRectangle1;
  @FXML private Rectangle searchResultsRectangle2;
  @FXML private Rectangle searchResultsRectangle3;
  @FXML private Rectangle searchResultsRectangle4;
  @FXML private Rectangle searchResultsRectangle5;
  @FXML private Rectangle searchResultsRectangle6;

  @FXML private Button nextButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Label nullLabel;

  int currentPage = 1;
  ArrayList<String> searchResultsNameList;
  ArrayList<String> searchResultsIDList;
  List<Customer> searchResultList;

  // write for all fxml elements
  @FXML
  private void initialize() {
    searchResultsNameList = new ArrayList<>();
    searchResultsIDList = new ArrayList<>();
    searchResultList = (List<Customer>) AppState.getSearchResultList();
    nullLabel.setVisible(false);

    populateLabels();
  }

  private void populateLabels() {
    int startIndex = (currentPage - 1) * 6;

    for (int i = 0; i < 6; i++) {
      int resultIndex = startIndex + i;
      if (resultIndex < searchResultList.size()) {
        Customer customer = searchResultList.get(resultIndex);
        getLabelByIndex(i + 1, "name").setText(customer.getName());
        getLabelByIndex(i + 1, "id").setText(customer.getCustomerId());
        // Enable the rectangle
        getRectangleByIndex(i + 1).setDisable(false);
      } else {
        getLabelByIndex(i + 1, "name").setText("");
        getLabelByIndex(i + 1, "id").setText("");
        // Disable the rectangle too
        getRectangleByIndex(i + 1).setDisable(true);
      }
    }
  }

  private Node getRectangleByIndex(int i) {
    switch (i) {
      case 1:
        return searchResultsRectangle1;
      case 2:
        return searchResultsRectangle2;
      case 3:
        return searchResultsRectangle3;
      case 4:
        return searchResultsRectangle4;
      case 5:
        return searchResultsRectangle5;
      case 6:
        return searchResultsRectangle6;
    }
    return null;
  }

  private Label getLabelByIndex(int index, String type) {
    switch (type) {
      case "name":
        switch (index) {
          case 1:
            return searchResultsNameLabel1;
          case 2:
            return searchResultsNameLabel2;
          case 3:
            return searchResultsNameLabel3;
          case 4:
            return searchResultsNameLabel4;
          case 5:
            return searchResultsNameLabel5;
          case 6:
            return searchResultsNameLabel6;
        }
      case "id":
        switch (index) {
          case 1:
            return searchResultsIDLabel1;
          case 2:
            return searchResultsIDLabel2;
          case 3:
            return searchResultsIDLabel3;
          case 4:
            return searchResultsIDLabel4;
          case 5:
            return searchResultsIDLabel5;
          case 6:
            return searchResultsIDLabel6;
        }
    }
    return null;
  }

  @FXML
  private void handleBackButtonAction() {
    AppState.isAccessingFromSearch = false;
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }

  @FXML
  private void handleNextButtonAction() {
    // Add logic to cycle between search results
  }

  // add methods for all buttons and rectangles
  @FXML
  private void handleSearchResultsRectangle1Action() throws IOException {
    loadCustomer(1);
  }

  @FXML
  private void handleSearchResultsRectangle2Action() throws IOException {
    loadCustomer(2);
  }

  @FXML
  private void handleSearchResultsRectangle3Action() throws IOException {
    loadCustomer(3);
  }

  @FXML
  private void handleSearchResultsRectangle4Action() throws IOException {
    loadCustomer(4);
  }

  @FXML
  private void handleSearchResultsRectangle5Action() throws IOException {
    loadCustomer(5);
  }

  @FXML
  private void handleSearchResultsRectangle6Action() throws IOException {
    loadCustomer(6);
  }

  private void loadCustomer(int index) throws IOException {
    Customer customer = searchResultList.get((currentPage - 1) * 6 + index - 1);
    if (AppState.isCreatingLoan && validateCustomerForLoan(customer)) {
      if (validateCustomerForLoan(customer)) {
        AppState.selectedCustomer = customer;
        PersonalLoanSingleton.resetInstance();
        AppState.loadLoans("CREATE");
        Main.setUi(AppUI.LC_PRIMARY);
      } else {
        nullLabel.setVisible(true);
        nullLabel.setText("Customer does not have a mailing address and/or contact method");
      }
    } else {
      nullFields(customer);
      AppState.selectedCustomer = customer;
      AppState.isAccessingFromSearch = true;
      AppState.loadAllCustomerDetails("VIEW");
      Main.setUi(AppUI.CI_DETAILS);
    }
  }

  private boolean validateCustomerForLoan(Customer customer) {
    /**
     * Check that the customer has at least one mailing address (see Screen 06: Customer Address.)
     * If the customer does not have a mailing address, the loan cannot proceed. Check that the
     * customer has at least one alternate contact method (phone or email.)
     */
    System.out.println("Validating customer");
    boolean hasAddress = false;
    boolean hasMailingAddress = false;
    boolean hasPrimaryAddress = false;

    boolean hasContact = false;
    if (customer.getAddresses().size() > 0) {
      System.out.println("Customer has addresses");
      hasAddress = true;
      for (int i = 0; i < customer.getAddresses().size(); i++) {
        if (customer.getAddresses().get(i).getIsMailing()) {
          System.out.println("Customer has mailing address");
          hasMailingAddress = true;
        }
        if (customer.getAddresses().get(i).getIsPrimary()) {
          System.out.println("Customer has primary address");
          hasPrimaryAddress = true;
        }
      }
    }
    if (customer.getPhones().size() > 0 || customer.getEmails().size() > 0) {
      System.out.println("Customer has contact");
      hasContact = true;
    }

    return hasAddress && hasMailingAddress && hasPrimaryAddress && hasContact;
  }

  private void nullFields(Customer customer) {
    // This method checks for any null / empty fields in the customer object. If any are found, it
    // will set the values within as empty strings.
    System.out.println("Checking for null fields");
    if (customer.getCustomerId() == null) {
      System.out.println("Customer ID is null");
      customer.setCustomerId("");
    }
    if (customer.getAddresses() == null) {
      System.out.println("Customer addresses is null");
      ArrayList<Address> addresses = new ArrayList<>();
      addresses.add(new Address("", "", "", "", "", "", "", "", false, false));
      customer.setAddresses(addresses);
    }
    if (customer.getEmails() == null) {
      System.out.println("Customer emails is null");
      ArrayList<Email> emails = new ArrayList<>();
      emails.add(new Email("", "", false));
      customer.setEmails(emails);
    }
    if (customer.getEmployer() == null) {
      System.out.println("Customer employer is null");
      CustomerEmployer employer =
          new CustomerEmployer("", "", "", "", "", "", "", "", "", "", "", false);
      customer.setEmployer(employer);
    }

    if (customer.getName() == null) {
      System.out.println("Customer name is null");
      customer.setName("");
    }

    if (customer.getPhones() == null) {
      System.out.println("Customer phones is null");
      ArrayList<Phone> phones = new ArrayList<>();
      phones.add(new Phone("", "", "", "", false, false));
      customer.setPhones(phones);
    }

    if (customer.getNotes() == null) {
      System.out.println("Customer notes is null");
      customer.setNotes(new ArrayList<>());
    }

    if (customer.getOccupation() == null) {
      System.out.println("Customer occupation is null");
      customer.setOccupation("");
    }
    if (customer.getVisa() == null) {
      System.out.println("Customer visa is null");
      customer.setVisa("");
    }
    if (customer.getCitizenship() == null) {
      System.out.println("Customer citizenship is null");
      customer.setCitizenship("");
    }
    if (customer.getDateOfBirth() == null) {
      System.out.println("Customer date of birth is null");
      customer.setDateOfBirth(null);
    }
    if (customer.getTitle() == null) {
      System.out.println("Customer title is null");
      customer.setTitle("");
    }
  }
}
