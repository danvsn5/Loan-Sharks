package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;
import uoa.lavs.utility.CustomerCreationHelper;

public class CustomerInputPrimaryAddressController {
  @FXML private ComboBox<String> customerAddressTypeComboBox;
  @FXML private TextField customerAddressLine1Field;
  @FXML private TextField customerAddressLine2Field;
  @FXML private TextField customerSuburbField;
  @FXML private TextField customerCityField;
  @FXML private TextField customerPostcodeField;
  @FXML private RadioButton mailingAddressRadio;

  @FXML private Button detailsButton;
  @FXML private Button mailingAddressButton;
  @FXML private Button contactButton;
  @FXML private Button employerButton;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  private void setAddressDetails() {
    Address address = customer.getPhysicalAddress();
    address.setAddressLineOne(customerAddressLine1Field.getText());
    address.setAddressLineTwo(customerAddressLine2Field.getText());
    address.setSuburb(customerSuburbField.getText());
    address.setCity(customerCityField.getText());
    address.setPostCode(customerPostcodeField.getText());
    address.setAddressType(customerAddressTypeComboBox.getValue());

    // Autosetting to New Zealand
    address.setCountry("New Zealand");

    // handle mailing address
    if (mailingAddressRadio.isSelected()) {
      customer.setMailingAddress(address);
    }
  }

  @FXML
  private void handleDetailsButtonAction() {
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleMailingAddressButtonAction() {
    Main.setUi(AppUI.CI_MAILING_ADDRESS);
  }

  @FXML
  private void handleContactButtonAction() {
    Main.setUi(AppUI.CI_CONTACT);
  }

  @FXML
  private void handleEmployerButtonAction() {
    Main.setUi(AppUI.CI_EMPLOYER);
  }

  @FXML
  private void handleEditButtonAction() {
    // Add edit button action code here
    if (AppState.customerDetailsAccessType == "CREATE") {
      // send customer to sql database
      setAddressDetails();
      CustomerCreationHelper.createCustomer(customer);
    }
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_MENU);
  }

  @FXML
  private void handleMailingAddressRadioAction() {
    // Add mailing address radio button action code here

  }
}
