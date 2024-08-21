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
        new ComboBox<?>[] {customerPhoneTypeBox},
        new DatePicker[] {},
        new RadioButton[] {sendTextRadio, phonePrimaryRadio, emailPrimaryRadio});
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.customerDetailsAccessType.equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      AppState.customerDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyCustomerObservers();
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
      AccessTypeNotifier.notifyCustomerObservers();
    } else if (AppState.customerDetailsAccessType.equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {
      AppState.customerDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyCustomerObservers();
    }
  }

  @Override
  public boolean validateData() {
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
    customerPhoneTypeBox.setStyle("");

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

    // Regex taken from https://stackoverflow.com/questions/50330109/simple-regex-pattern-for-email
    if (customerEmailTextField.getText().isEmpty()
        || customerEmailTextField.getText().length() > 60
        || !customerEmailTextField.getText().matches("^[^@]+@[^@]+\\.[^@]+$")) {
      customerEmailTextField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (!isValid) {
      return false;
    }

    return true;
  }

  private boolean setContactDetails() {

    if (!validateData()) {
      return false;
    }

    // TODO: Set customer details (Chulshin or Jamie)

    return true;
  }

  // add handlers for all buttons
  @FXML
  private void handleCustomerDetailsButtonAction() {
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleCustomerAddressButtonAction() {
    Main.setUi(AppUI.CI_PRIMARY_ADDRESS);
  }

  @FXML
  private void handleCustomerEmployerButtonAction() {
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
