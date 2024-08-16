package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;
import uoa.lavs.utility.CustomerCreationHelper;

public class CustomerInputEmployerAddressController {
  @FXML private ComboBox<String> employerAddressTypeComboBox;
  @FXML private TextField employerAddressLine1Field;
  @FXML private TextField employerAddressLine2Field;
  @FXML private TextField employerSuburbField;
  @FXML private TextField employerCityField;
  @FXML private TextField employerPostcodeField;

  @FXML private Button detailsButton;
  @FXML private Button addressButton;
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
    Address address = customer.getEmployer().getEmployerAddress();

    address.setAddressLineOne(employerAddressLine1Field.getText());
    address.setAddressLineTwo(employerAddressLine2Field.getText());
    address.setSuburb(employerSuburbField.getText());
    address.setCity(employerCityField.getText());
    address.setPostCode(employerPostcodeField.getText());
    address.setAddressType(employerAddressTypeComboBox.getValue());

    // Autosetting to New Zealand
    address.setCountry("New Zealand");
  }

  @FXML
  private void handleDetailsButtonAction() {
    setAddressDetails();
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleAddressButtonAction() {
    setAddressDetails();
    Main.setUi(AppUI.CI_PRIMARY_ADDRESS);
  }

  @FXML
  private void handleContactButtonAction() {
    setAddressDetails();
    Main.setUi(AppUI.CI_CONTACT);
  }

  @FXML
  private void handleEmployerButtonAction() {
    setAddressDetails();
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
}
