package uoa.lavs.frontend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.oop.customer.Customer;
import uoa.lavs.backend.oop.customer.CustomerEmployer;
import uoa.lavs.backend.oop.customer.Email;
import uoa.lavs.backend.oop.customer.Phone;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;

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

  @FXML private ImageView incResults;
  @FXML private ImageView decResults;
  @FXML private Label pageLabel;

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
    AppState.setIsAccessingFromSearch(false);
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }

  @FXML
  private void handleNextButtonAction() {
    if (searchResultList.size() > currentPage * 6) {
      currentPage++;
      populateLabels();
      pageLabel.setText("Page " + currentPage);
    }
  }

  @FXML
  private void handlePreviousButtonAction() {
    if (currentPage > 1) {
      currentPage--;
      populateLabels();
      pageLabel.setText("Page " + currentPage);
    }
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
    if (AppState.getIsCreatingLoan()) {
      if (AppState.validateCustomerForLoan(customer)) {
        nullLabel.setVisible(false);
        AppState.setSelectedCustomer(customer);
        PersonalLoanSingleton.resetInstance();
        AppState.loadLoans("CREATE");
        Main.setUi(AppUI.LC_PRIMARY);
      } else {
        nullLabel.setVisible(true);
        nullLabel.setText("Customer does not have a valid mailing address and/or contact method");
        return;
      }
    } else {
      nullFields(customer);
      AppState.setSelectedCustomer(customer);
      AppState.setIsAccessingFromSearch(true);
      AppState.loadAllCustomerDetails("VIEW");
      Main.setUi(AppUI.CI_DETAILS);
    }
  }

  // set null fields to empty strings when viewing customer
  private void nullFields(Customer customer) {
    // This method checks for any null / empty fields in the customer object. If any are found, it
    // will set the values within as empty strings.
    System.out.println("Checking for null fields");
    if (customer.getCustomerId() == null || customer.getCustomerId().isEmpty()) {
      System.out.println("Customer ID is null");
      customer.setCustomerId("");
    }

    if (customer.getAddresses() == null) {
      System.out.println("Customer addresses is null");
      ArrayList<Address> addresses = new ArrayList<>();
      addresses.add(new Address("", "", "", "", "", "", "", "", false, false));
      customer.setAddresses(addresses);
    } else {
      for (Address address : customer.getAddresses()) {
        if (address.getAddressLineOne() == null) {
          System.out.println("Customer address street is null");
          address.setAddressLineOne("");
        }
        if (address.getCity() == null) {
          System.out.println("Customer address city is null");
          address.setCity("");
        }
        if (address.getPostCode() == null) {
          System.out.println("Customer address state is null");
          address.setPostCode("");
        }
        if (address.getSuburb() == null) {
          System.out.println("Customer address zip is null");
          address.setSuburb("");
        }
        if (address.getCountry() == null) {
          System.out.println("Customer address country is null");
          address.setCountry("");
        }
        if (address.getAddressType() == null) {
          System.out.println("Customer address type is null");
          address.setAddressType("");
        }
      }
    }

    if (customer.getEmails() == null) {
      System.out.println("Customer emails is null");
      ArrayList<Email> emails = new ArrayList<>();
      emails.add(new Email("", "", false));
      customer.setEmails(emails);
    } else {
      for (Email email : customer.getEmails()) {
        if (email.getEmailAddress() == null) {
          System.out.println("Customer email is null");
          email.setEmailAddress("");
        }
      }
    }

    if (customer.getEmployer() == null) {
      System.out.println("Customer employer is null");
      CustomerEmployer employer =
          new CustomerEmployer("", "", "", "", "", "", "", "", "", "", "", false);
      customer.setEmployer(employer);
    } else {
      CustomerEmployer employer = customer.getEmployer();
      if (employer.getCity() == null) {
        System.out.println("Customer employer city is null");
        employer.setCity("");
      }
      if (employer.getCountry() == null) {
        System.out.println("Customer employer country is null");
        employer.setCountry("");
      }
      if (employer.getEmployerEmail() == null) {
        System.out.println("Customer employer state is null");
        employer.setEmployerEmail("");
      }
      if (employer.getLineOne() == null) {
        System.out.println("Customer employer address line one is null");
        employer.setLineOne("");
      }
      if (employer.getEmployerName() == null) {
        System.out.println("Customer employer name is null");
        employer.setEmployerName("");
      }
      if (employer.getEmployerPhone() == null) {
        System.out.println("Customer employer phone is null");
        employer.setEmployerPhone("");
      }
      if (employer.getPostCode() == null) {
        System.out.println("Customer employer post code is null");
        employer.setPostCode("");
      }

      if (employer.getEmployerWebsite() == null) {
        System.out.println("Customer employer street is null");
        employer.setEmployerWebsite("");
      }
      if (employer.getSuburb() == null) {
        System.out.println("Customer employer suburb is null");
        employer.setSuburb("");
      }
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
    } else {
      for (Phone phone : customer.getPhones()) {
        if (phone.getPhoneNumber() == null) {
          System.out.println("Customer phone number is null");
          phone.setPhoneNumber("");
        }
        if (phone.getType() == null) {
          System.out.println("Customer phone type is null");
          phone.setType("");
        }
        if (phone.getPrefix() == null) {
          System.out.println("Customer phone prefix is null");
          phone.setPrefix("");
        }
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
}
