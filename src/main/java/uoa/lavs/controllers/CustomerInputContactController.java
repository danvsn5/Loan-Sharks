package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.CustomerContact;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;
import uoa.lavs.utility.CustomerCreationHelper;

public class CustomerInputContactController {
  @FXML private TextField customerEmailTextField;
  @FXML private TextField customerPhoneNumberOne;
  @FXML private TextField customerPhoneNumberTwo;

  @FXML private TextField customerPreferredContactBox;
  @FXML private TextField customerAltContactBox;

  @FXML private Button customerDetailsButton;
  @FXML private Button customerAddressButton;
  @FXML private Button customerEmployerButton;

  @FXML private Button editButton;
  @FXML private Button backButton;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  private void setContactDetails() {
    CustomerContact contact = customer.getContact();
    contact.setCustomerEmail(customerEmailTextField.getText());
    // TODO Uncomment once types are implemented
    // contact.getPhoneOne().setType();
    contact.getPhoneOne().setPhoneNumber(customerPhoneNumberOne.getText());
    // contact.getPhoneTwo().setType();
    contact.getPhoneTwo().setPhoneNumber(customerPhoneNumberTwo.getText());
    contact.setPreferredContact(customerPreferredContactBox.getText());
    contact.setAlternateContact(customerAltContactBox.getText());
  }

  // add handlers for all buttons
  @FXML
  private void handleCustomerDetailsButtonAction() {
    setContactDetails();
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleCustomerAddressButtonAction() {
    setContactDetails();
    Main.setUi(AppUI.CI_PRIMARY_ADDRESS);
  }

  @FXML
  private void handleCustomerEmployerButtonAction() {
    setContactDetails();
    Main.setUi(AppUI.CI_EMPLOYER);
  }

  @FXML
  private void handleEditButtonAction() {
    // Add edit button action code here
    if (AppState.customerDetailsAccessType == "CREATE") {
      // send customer to sql database
      setContactDetails();
      CustomerCreationHelper.createCustomer(customer);
    }
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_MENU);
  }
}
