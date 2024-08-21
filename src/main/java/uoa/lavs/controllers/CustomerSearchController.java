package uoa.lavs.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.Customer;
import uoa.lavs.customer.SearchCustomer;

public class CustomerSearchController {
  @FXML private Label searchWithCustomerIDLabel; // When click label, reveal text box.

  @FXML private Label searchWithNameLabel; // When click label, reveal text box.

  @FXML private ImageView staticReturnImageView;

  @FXML private TextField usernameField;

  @FXML private TextField idField;

  @FXML private Button searchButton;

  String state;

  @FXML
  private void initialize() {}

  // When enter key is pressed, perform search.
  @FXML
  private void onEnterPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      try {
        onClickSearchButton();
      } catch (Exception e) {
        System.out.println("Something went wrong " + e.getMessage());
      }
    }
  }

  @FXML
  private void onClickSearchButton() throws IOException {
    if (idField.getText().isEmpty() && usernameField.getText().isEmpty()) {
      return;
    }
    if (state.equals("id")) {
      handleSearchWithCustomerIDLabelAction();
    } else {
      handleSearchWithNameLabelAction();
    }
    usernameField.setText("");
    idField.setText("");
  }

  @FXML
  private void enableIDSearch() {
    usernameField.setText("");
    state = "id";
  }

  @FXML
  private void enableNameSearch() {
    idField.setText("");
    state = "name";
  }

  @FXML
  private void handleSearchWithCustomerIDLabelAction() throws IOException {
    String searchString = idField.getText();
    SearchCustomer searchCustomer = new SearchCustomer();
    Customer customer = searchCustomer.searchCustomerById(searchString);
    List<Customer> listOfCustomers = new ArrayList<>();
    listOfCustomers.add(customer);
    AppState.loadCustomerSearchResults(searchString);
  }

  @FXML
  private void handleSearchWithNameLabelAction() throws IOException {
    String searchString = usernameField.getText();
    SearchCustomer searchCustomer = new SearchCustomer();
    List<Customer> listOfCustomers = searchCustomer.searchCustomerByName(searchString);
    AppState.loadCustomerSearchResults(searchString);
  }

  @FXML
  private void handleBackButtonAction() {
    if (AppState.isCreatingLoan) {
      AppState.isCreatingLoan = false;
      Main.setUi(AppUI.LOAN_MENU);
    } else {
      Main.setUi(AppUI.CUSTOMER_MENU);
    }
  }
}
