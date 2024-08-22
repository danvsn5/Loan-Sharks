package uoa.lavs.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.Customer;

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
      } else {
        getLabelByIndex(i + 1, "name").setText("");
        getLabelByIndex(i + 1, "id").setText("");
      }
    }
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
    boolean nullFields = nullFields(customer);
    if (AppState.isCreatingLoan) {
      AppState.selectedCustomer = customer;
      AppState.loadLoans("CREATE");
      Main.setUi(AppUI.LC_PRIMARY);
    } else {
      if (nullFields) {
        nullLabel.setVisible(true);
      } else {
        AppState.selectedCustomer = customer;
        AppState.isAccessingFromSearch = true;
        AppState.loadAllCustomerDetails("VIEW");
        Main.setUi(AppUI.CI_DETAILS);
      }
    }
  }

  private boolean nullFields(Customer customer) {
    System.out.println("Null Field check");
    return (customer.getAddresses().equals(null)
        || customer.getCitizenship().equals(null)
        || customer.getCustomerId().equals(null)
        || customer.getDateOfBirth().equals(null)
        || customer.getEmails().equals(null)
        || customer.getEmployer().equals(null)
        || customer.getName().equals(null)
        || customer.getNotes().equals(null)
        || customer.getOccupation().equals(null)
        || customer.getPhones().equals(null)
        || customer.getTitle().equals(null)
        || customer.getVisa().equals(null));
  }
}
