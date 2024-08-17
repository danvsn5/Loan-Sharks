package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.ControllerHelper;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;

public class CustomerInputPrimaryAddressController implements AccessTypeObserver {
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
    customerAddressTypeComboBox.getItems().addAll("Home", "Work", "PO Box", "Other");
    AccessTypeNotifier.registerCustomerObserver(this);
    updateUIBasedOnAccessType();
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.customerDetailsAccessType,
        editButton,
        new TextField[] {
          customerAddressLine1Field,
          customerAddressLine2Field,
          customerSuburbField,
          customerCityField,
          customerPostcodeField
        },
        new ComboBox<?>[] {customerAddressTypeComboBox},
        new DatePicker[] {},
        new RadioButton[] {mailingAddressRadio});
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
    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      setAddressDetails();
      AppState.customerDetailsAccessType = "VIEW";
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
    } else if (AppState.customerDetailsAccessType.equals("EDIT")) {
      setAddressDetails();
      AppState.customerDetailsAccessType = "VIEW";
    }
    AccessTypeNotifier.notifyCustomerObservers();
    updateUIBasedOnAccessType();
  }

  @FXML
  private void handleBackButtonAction() {
    if (AppState.isAccessingFromSearch) {
      AppState.isAccessingFromSearch = false;
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    } else {
      Main.setUi(AppUI.CUSTOMER_MENU);
    }
  }

  @FXML
  private void handleMailingAddressRadioAction() {
    // Add mailing address radio button action code here

  }
}
