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
  @FXML private Label searchWithCustomerIDLabel; // When click label, reveal text box.

  @FXML private Label searchWithNameLabel; // When click label, reveal text box.

  @FXML private ImageView staticReturnImageView;

  @FXML private TextField usernameField;

  @FXML private TextField idField;

  @FXML private Button searchButton;

  @FXML private ImageView connectionSymbol;

  @FXML private Label connectionLabel;

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
  }

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

  @FXML
  private void handleSearchWithCustomerIDLabelAction() throws IOException {
    String searchString = idField.getText();
    SearchCustomer searchCustomer = new SearchCustomer();
    try {
      Connection connection = Instance.getConnection();
      Customer customer = searchCustomer.searchCustomerById(searchString, connection);
      searchCustomer.getStatusInstance().getErrorCode();

      if (searchCustomer.getStatusInstance().getErrorCode() == 1000
          || searchCustomer.getStatusInstance().getErrorCode() == 1010
          || searchCustomer.getStatusInstance().getErrorCode() == 1020) {
        setRedSymbol();
      } else if (searchCustomer.getStatusInstance().getErrorCode() == 0) {
        setGreenSymbol();
      } else {
        setOrangeSymbol();
      }
      connectionLabel.setText(searchCustomer.getStatusInstance().getErrorMessage());

      List<Customer> listOfCustomers = new ArrayList<>();
      listOfCustomers.add(customer);
      AppState.loadCustomerSearchResults(listOfCustomers);
      idField.setStyle("");
      idField.setText("");
    } catch (Exception e) {
      idField.setStyle("-fx-border-color: red;");
      e.printStackTrace();
    }
  }

  @FXML
  private void handleSearchWithNameLabelAction() throws IOException {
    String searchString = usernameField.getText();
    SearchCustomer searchCustomer = new SearchCustomer();
    try {
      Connection connection = Instance.getConnection();
      List<Customer> listOfCustomers =
          searchCustomer.searchCustomerByName(searchString, connection);
      searchCustomer.getStatusInstance().getErrorCode();

      if (searchCustomer.getStatusInstance().getErrorCode() == 1000
          || searchCustomer.getStatusInstance().getErrorCode() == 1010
          || searchCustomer.getStatusInstance().getErrorCode() == 1020) {
        setRedSymbol();
      } else if (searchCustomer.getStatusInstance().getErrorCode() == 0) {
        setGreenSymbol();
      } else {
        setOrangeSymbol();
      }
      connectionLabel.setText(searchCustomer.getStatusInstance().getErrorMessage());
      if (listOfCustomers == null) {
        connectionLabel.setText("No customers found");
        setOrangeSymbol();
      }
      AppState.loadCustomerSearchResults(listOfCustomers);
      usernameField.setStyle("");
      usernameField.setText("");
    } catch (Exception e) {
      usernameField.setStyle("-fx-border-color: red;");
      e.printStackTrace();
    }
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
      // connectionLabel.setText("Unidentified error. Please try again.");
    }
  }

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
