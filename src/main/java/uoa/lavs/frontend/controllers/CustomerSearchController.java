package uoa.lavs.frontend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.customer.Customer;
import uoa.lavs.backend.oop.customer.SearchCustomer;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Instance;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;

public class CustomerSearchController {
  @FXML
  private Label searchWithCustomerIDLabel; // When click label, reveal text box.

  @FXML
  private Label searchWithNameLabel; // When click label, reveal text box.

  @FXML
  private ImageView staticReturnImageView;

  @FXML
  private TextField usernameField;

  @FXML
  private TextField idField;

  @FXML
  private Button searchButton;

  @FXML
  private ImageView connectionSymbol;

  @FXML
  private Label connectionLabel;

  String state;

  @FXML
  private void initialize() {
  }

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
  }

  // sets which search bar is currently active, which is used when choosing which
  // method to employ for customer searching
  @FXML
  private void enableIDSearch() {
    usernameField.setText("");
    usernameField.setStyle("");
    state = "id";
  }

  @FXML
  private void enableNameSearch() {
    idField.setText("");
    idField.setStyle("");
    state = "name";
  }

  // handles search with ID; follows same logic as below method
  @FXML
  private void handleSearchWithCustomerIDLabelAction() throws IOException {
    String searchString = idField.getText();
    SearchCustomer searchCustomer = new SearchCustomer();
    List<Customer> listOfCustomers = new ArrayList<>();
    try {
      Connection connection = Instance.getConnection();
      Customer customer = searchCustomer.searchCustomerById(searchString, connection);

      // if a customer is found, then move to next page, set the fields to empty,
      // symbol to green, and label to ""

      System.out.println("CUSTOMER ID RETRIEVED = " + customer.getCustomerId());

      listOfCustomers.add(customer);
      if (listOfCustomers.size() > 0) {
        connectionLabel.setText("Search successful.");
        setGreenSymbol();
        AppState.loadCustomerSearchResults(listOfCustomers);
        idField.setStyle("");
        idField.setText("");
        return;
      }

    } catch (Exception e) {
      // connection somehow failed and errors are shown
      System.out.println("----------");
      System.out.println(listOfCustomers.size());
      System.out.println(searchCustomer.getStatusInstance().getErrorCode());
      System.out.println("----------");

      if (searchCustomer.getStatusInstance().getErrorCode() == 1000
          || searchCustomer.getStatusInstance().getErrorCode() == 1010
          || searchCustomer.getStatusInstance().getErrorCode() == 1020) {
        setRedSymbol();
        connectionLabel.setText("Remote host is not available. No customers found in local system.");
      } else {
        setOrangeSymbol();
        connectionLabel.setText("No customers found in mainframe or local system.");
      }

      idField.setStyle("-fx-border-color: red;");
      System.out.println("No customers found with ID: " + searchString);
    }
  }

  // handles the search while using name as the input string; if search was
  // successful, move to next page, otherwise change the wifi symbol visual and
  // display message in GUI
  @FXML
  private void handleSearchWithNameLabelAction() throws IOException {
    String searchString = usernameField.getText();
    SearchCustomer searchCustomer = new SearchCustomer();
    try {
      Connection connection = Instance.getConnection();
      List<Customer> listOfCustomers = searchCustomer.searchCustomerByName(searchString, connection);

      if (searchCustomer.getStatusInstance().getErrorCode() == 1000
          || searchCustomer.getStatusInstance().getErrorCode() == 1010
          || searchCustomer.getStatusInstance().getErrorCode() == 1020) {
        if (listOfCustomers == null) {
          connectionLabel.setText("Remote host is not available. No customers found in local system.");
        }
        setRedSymbol();
      } else if (searchCustomer.getStatusInstance().getErrorCode() == 0) {
        setGreenSymbol();
      } else {
        if (listOfCustomers == null) {
          connectionLabel.setText("No customers found in mainframe or local system.");
        } else {
          connectionLabel.setText(searchCustomer.getStatusInstance().getErrorMessage());
        }
        setOrangeSymbol();
      }

      if (listOfCustomers != null) {
        AppState.loadCustomerSearchResults(listOfCustomers);
        setGreenSymbol();
        connectionLabel.setText("Search successful.");
      }
      usernameField.setStyle("");
      usernameField.setText("");
    } catch (Exception e) {
      usernameField.setStyle("-fx-border-color: red;");
      System.out.println("No customers found with name: " + searchString);
    }
  }

  // returns based on the how the scene was entered
  @FXML
  private void handleBackButtonAction() {
    if (AppState.getIsCreatingLoan()) {
      AppState.setIsCreatingLoan(false);
      Main.setUi(AppUI.LOAN_MENU);
    } else {
      Main.setUi(AppUI.CUSTOMER_MENU);
    }
  }

  // sends a sample connection to test whether or not the mainframe is connected
  @FXML
  private void getConnectionSample() {

    Connection connection = Instance.getConnection();

    Request request = new Request(1001);

    Response response = (connection.send(request));
    uoa.lavs.legacy.mainframe.Status status = response.getStatus();

    // there was an issue connecting to the database
    if (status.getErrorCode() == 1000
        || status.getErrorCode() == 1010
        || status.getErrorCode() == 1020) {
      setRedSymbol();
      connectionLabel.setText(status.getErrorMessage());
      // if the 'unknown' message got a response, it's online
    } else if (status.getErrorCode() == 100) {
      setGreenSymbol();
      connectionLabel.setText("Connection is successful");
      // catch all for other messages
    } else {
      setOrangeSymbol();
    }
  }

  // sets wifi symbols based on the success of the search query
  private void setRedSymbol() {
    ColorAdjust red = new ColorAdjust();
    red.setBrightness(0);
    red.setSaturation(1);
    connectionSymbol.setEffect(red);
  }

  private void setGreenSymbol() {
    ColorAdjust green = new ColorAdjust();
    green.setBrightness(-0.3);
    green.setSaturation(1);
    green.setHue(0.82);
    connectionSymbol.setEffect(green);
  }

  private void setOrangeSymbol() {
    ColorAdjust orange = new ColorAdjust();
    orange.setBrightness(0.0);
    orange.setSaturation(1);
    orange.setHue(0.16);
    connectionSymbol.setEffect(orange);
  }
}
