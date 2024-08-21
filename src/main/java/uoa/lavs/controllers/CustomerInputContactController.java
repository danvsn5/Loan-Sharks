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
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;

public class CustomerInputContactController implements AccessTypeObserver {
  @FXML private TextField customerEmailTextField;
  @FXML private TextField customerPhoneNumberOne;

  @FXML private TextField customerPhonePrefixField;
  @FXML private ComboBox<String> customerPhoneTypeBox;
  @FXML private RadioButton sendTextRadio;
  @FXML private RadioButton phonePrimaryRadio;
  @FXML private RadioButton emailPrimaryRadio;

  @FXML private ImageView incPhone;
  @FXML private ImageView incEmail;
  @FXML private ImageView decPhone;
  @FXML private ImageView decEmail;

  @FXML private TextField customerPreferredContactBox;
  @FXML private TextField customerAltContactBox;

  @FXML private Button customerDetailsButton;
  @FXML private Button customerAddressButton;
  @FXML private Button customerEmployerButton;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  @FXML
  private void initialize() {
    customerPhoneTypeBox.getItems().addAll("Home", "Work", "Mobile");
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
          customerEmailTextField,
          customerPhoneNumberOne,
          customerPreferredContactBox,
          customerAltContactBox,
          customerPhonePrefixField
        },
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {});
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.customerDetailsAccessType.equals("CREATE") && setContactDetails()) {
      AppState.customerDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
    } else if (AppState.customerDetailsAccessType.equals("EDIT") && setContactDetails()) {
      AppState.customerDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
    }
  }

  private boolean setContactDetails() {
    // Type is fine
    // Prefix 10 chars, numbers and + char only
    // Number 20 chars, number and dash only
    // Primary and send text need to be checked that there is a selection and that no 2 numbers have
    // it
    // Email 60 chars, email format
    // primary email needs to be checked that there in one set somewhere and no 2 are set

    boolean isValid = true;
    customerPhonePrefixField.setStyle("");
    customerPhoneNumberOne.setStyle("");
    customerEmailTextField.setStyle("");
    customerPreferredContactBox.setStyle("");
    customerAltContactBox.setStyle("");

    if (customerPhoneTypeBox.getValue() == null) {
      customerPhoneTypeBox.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerPhonePrefixField.getText().isEmpty()) {
      customerPhonePrefixField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerPhonePrefixField.getText().length() > 10
        || !customerPhonePrefixField.getText().matches("[0-9\\+]+")) {
      customerPhonePrefixField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerPhoneNumberOne.getText().isEmpty()
        || customerPhoneNumberOne.getText().length() > 20
        || !customerPhoneNumberOne.getText().matches("[0-9\\-]+")) {
      customerPhoneNumberOne.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerEmailTextField.getText().isEmpty()
        || customerEmailTextField.getText().length() > 60) {
      customerEmailTextField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (!isValid) {
      return false;
    }

    // Set customer details

    return true;
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
  private void handleBackButtonAction() {
    if (AppState.isAccessingFromSearch) {
      AppState.isAccessingFromSearch = false;
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    } else {
      Main.setUi(AppUI.CUSTOMER_MENU);
    }
  }

  @FXML
  private void handleIncPhone() {
    // TODO when ting is time to be tung
  }

  @FXML
  private void handleIncEmail() {
    // TODO when ting is time to be tung
  }

  @FXML
  private void handleDecPhone() {
    // TODO when ting is time to be tung
  }

  @FXML
  private void handleDecEmail() {
    // TODO when ting is time to be tung
  }
}
