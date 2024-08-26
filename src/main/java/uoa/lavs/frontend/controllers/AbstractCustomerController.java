package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.backend.sql.sql_to_mainframe.CustomerCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;

public abstract class AbstractCustomerController {
  @FXML protected Button customerDetailsButton;
  @FXML protected Button customerAddressButton;
  @FXML protected Button customerContactButton;
  @FXML protected Button customerEmployerButton;

  @FXML protected Button editButton;
  @FXML protected ImageView staticReturnImageView;
  @FXML protected Label idBanner;

  protected IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  // sets the initial values for the customer after a search
  protected abstract void startWithCustomerID();

  // sets the details for each tab
  protected abstract void setDetails();

  // handles the clicking of the edit button based on the mode the user is in
  @FXML
  protected void handleEditButtonAction() throws IOException {
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      setDetails();

      boolean customerIsValid = CustomerCreationHelper.validateCustomer(customer);
      if (!customerIsValid) {
        System.out.println("Customer is not valid and thus will not be created");
        editButton.setStyle("-fx-border-color: red");
        return;
      }

      AppState.setCustomerDetailsAccessType("VIEW");
      AccessTypeNotifier.notifyCustomerObservers();
      CustomerCreationHelper.createCustomer(customer, false);

    } else if (AppState.getCustomerDetailsAccessType().equals("VIEW")) {

      AppState.setCustomerDetailsAccessType("EDIT");
      AccessTypeNotifier.notifyCustomerObservers();

    } else if (AppState.getCustomerDetailsAccessType().equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {

      setDetails();

      AppState.setCustomerDetailsAccessType("VIEW");
      AccessTypeNotifier.notifyCustomerObservers();
      CustomerCreationHelper.createCustomer(customer, true);
    }
  }

  // sets the tab to customer details
  @FXML
  protected void handleDetailsButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_DETAILS);
  }

  // sets the tab to address details
  @FXML
  protected void handleAddressButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_PRIMARY_ADDRESS);
  }

  // sets the tab to contact details
  @FXML
  protected void handleContactButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_CONTACT);
  }

  // sets the tab to employer details
  @FXML
  protected void handleEmployerButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_EMPLOYER);
  }

  // sets the tab for employer address details
  @FXML
  protected void handleEmployerAddressButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_EMPLOYER_ADDRESS);
  }

  // sets the tab to notes details
  @FXML
  protected void handleNotesButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_NOTES);
  }

  // returns based on how the user accessed the customer details tab
  @FXML
  protected void handleBackButtonAction() {
    if (AppState.getIsAccessingFromSearch()) {
      AppState.setIsAccessingFromLoanSearch(false);
      Main.setUi(AppUI.CUSTOMER_SEARCH);
    } else {
      Main.setUi(AppUI.CUSTOMER_MENU);
    }
  }

  // sets invalid buttons
  @FXML
  public void setInvalidButton(String style) {
    Button currentButton = AppState.getCurrentButton();

    String buttonId = currentButton.getId();

    if (buttonId != null) {
      if (buttonId.equals("customerDetailsButton")) {
        customerDetailsButton.setStyle(style);
      } else if (buttonId.equals("customerAddressButton")) {
        customerAddressButton.setStyle(style);
      } else if (buttonId.equals("customerContactButton")) {
        customerContactButton.setStyle(style);
      } else if (buttonId.equals("customerEmployerButton")) {
        customerEmployerButton.setStyle(style);
      }
    }
  }
}
