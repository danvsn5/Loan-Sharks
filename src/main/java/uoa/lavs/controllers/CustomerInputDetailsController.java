package uoa.lavs.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.ControllerHelper;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;
import uoa.lavs.sql.sql_to_mainframe.CustomerCreationHelper;

public class CustomerInputDetailsController implements AccessTypeObserver {
  @FXML private Label idBanner;

  @FXML private ComboBox<String> customerTitleComboBox;
  @FXML private TextField customerNameField;
  @FXML private DatePicker customerDOBPicker;
  @FXML private TextField customerOccupationField;
  @FXML private ComboBox<String> customerVisaBox;

  @FXML private ComboBox<String> customerCitizenshipBox;

  @FXML private Button notesButton;
  @FXML private Button customerDetailsButton;
  @FXML private Button customerAddressButton;
  @FXML private Button customerContactButton;
  @FXML private Button customerEmployerButton;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  Random random = new Random();

  @FXML
  private void initialize() {
    customerTitleComboBox.getItems().addAll("Mr", "Mrs", "Ms", "Master");
    customerVisaBox
        .getItems()
        .addAll("NZ Citizen", "NZ Permanent Resident", "AUS Citizen", "NZ Work Visa", "Other");
    customerCitizenshipBox.getItems().addAll(AppState.getAllCountries());
    DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    customerDOBPicker.setConverter(
        new StringConverter<LocalDate>() {
          @Override
          public String toString(LocalDate date) {
            if (date != null) {
              return displayFormatter.format(date);
            } else {
              return "";
            }
          }

          @Override
          public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
              LocalDate date = LocalDate.parse(string, displayFormatter);
              return LocalDate.parse(date.format(storageFormatter), storageFormatter);
            } else {
              return null;
            }
          }
        });
    AccessTypeNotifier.registerCustomerObserver(this);
    updateUIBasedOnAccessType();

    // Set dummy values
    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      customerTitleComboBox.setValue("Mr");
      customerNameField.setText("John Doe Bingus");
      customerDOBPicker.setValue(LocalDate.now());
      customerOccupationField.setText("Software Developer");
      customerVisaBox.setValue("NZ Citizen");
      customerCitizenshipBox.setValue("New Zealand");
    }

    if (AppState.isAccessingFromSearch) {
      IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
      customer = IndividualCustomerSingleton.getInstance();

      customerNameField.setText(customer.getName());
      customerDOBPicker.setValue(customer.getDateOfBirth());
      customerOccupationField.setText(customer.getOccupation());
      customerVisaBox.setValue(customer.getVisa());
      customerTitleComboBox.setValue(customer.getTitle());
      customerCitizenshipBox.setValue(customer.getCitizenship());
    }
  }

  @Override
  @FXML
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.customerDetailsAccessType,
        editButton,
        idBanner,
        new TextField[] {customerNameField, customerOccupationField},
        new ComboBox<?>[] {customerTitleComboBox, customerVisaBox, customerCitizenshipBox},
        new DatePicker[] {customerDOBPicker},
        new RadioButton[] {});
  }

  @Override
  public boolean validateData() {
    boolean isValid = true;

    // Clear previous error styles
    customerTitleComboBox.setStyle("");
    customerNameField.setStyle("");
    customerDOBPicker.setStyle("");
    customerOccupationField.setStyle("");
    customerVisaBox.setStyle("");
    customerCitizenshipBox.setStyle("");

    // Validate fields and apply error styles if necessary
    if (customerTitleComboBox.getValue() == null) {
      customerTitleComboBox.setStyle("-fx-border-color: red;");
      isValid = false;
    }
    if (customerNameField.getText().isEmpty() || customerNameField.getText().length() > 60) {
      customerNameField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerDOBPicker.getValue() == null) {
      customerDOBPicker.setStyle("-fx-border-color: red;");
      isValid = false;
    }
    if (customerOccupationField.getText().isEmpty()
        || customerOccupationField.getText().length() > 40) {
      customerOccupationField.setStyle("-fx-border-color: red;");
      isValid = false;
    }
    if (customerVisaBox.getValue() == null) {
      customerVisaBox.setStyle("-fx-border-color: red;");
      isValid = false;
    }
    if (customerCitizenshipBox.getValue() == null) {
      customerCitizenshipBox.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (!isValid) {
      return false;
    }

    return true;
  }

  private boolean setCustomerDetails() {

    if (!validateData()) {
      return false;
    }

    // Set customer details
    customer.setTitle(customerTitleComboBox.getValue());
    customer.setName(customerNameField.getText());
    customer.setDateOfBirth(customerDOBPicker.getValue());
    customer.setOccupation(customerOccupationField.getText());
    customer.setVisa(customerVisaBox.getValue());
    customer.setCitizenship(customerCitizenshipBox.getValue());
    return true;
  }

  @FXML
  private void handleEditButtonAction() throws IOException {
    if (AppState.customerDetailsAccessType.equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      // Handle create customer logic
      // Save customer to database or perform necessary actions
      AppState.customerDetailsAccessType = "VIEW";
      setCustomerDetails();
      AccessTypeNotifier.notifyCustomerObservers();
      CustomerCreationHelper.createCustomer(customer, false);

    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      // Switch to edit mode
      AppState.customerDetailsAccessType = "EDIT";
      AccessTypeNotifier.notifyCustomerObservers();
    } else if (AppState.customerDetailsAccessType.equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {
      // Handle confirm changes logic
      // Save changes to database or perform necessary actions
      AppState.customerDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyCustomerObservers();
      setCustomerDetails();
      CustomerCreationHelper.createCustomer(customer, true);
    }
  }

  @FXML
  private void handleNotesButtonAction() {
    setCustomerDetails();
    Main.setUi(AppUI.CI_NOTES);
  }

  @FXML
  private void handleAddressButtonAction() {
    setCustomerDetails();
    Main.setUi(AppUI.CI_PRIMARY_ADDRESS);
  }

  @FXML
  private void handleContactButtonAction() {
    setCustomerDetails();
    Main.setUi(AppUI.CI_CONTACT);
  }

  @FXML
  private void handleEmployerButtonAction() {
    setCustomerDetails();
    Main.setUi(AppUI.CI_EMPLOYER);
  }

  @FXML
  private void handleBackButtonAction() {
    if (AppState.isAccessingFromSearch) {
      AppState.isAccessingFromSearch = false;
      Main.setUi(AppUI.CUSTOMER_SEARCH);
    } else {
      Main.setUi(AppUI.CUSTOMER_MENU);
    }
  }

  @Override
  public Button getButton() {
    return customerDetailsButton;
  }

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
