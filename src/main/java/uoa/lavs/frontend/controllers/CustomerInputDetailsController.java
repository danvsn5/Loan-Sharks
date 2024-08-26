package uoa.lavs.frontend.controllers;

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
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.backend.sql.sql_to_mainframe.CustomerCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;

public class CustomerInputDetailsController extends AbstractCustomerController
    implements AccessTypeObserver {
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
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")) {
      customerTitleComboBox.setValue("Mr");
      customerNameField.setText("John Doe Bingus");
      customerDOBPicker.setValue(LocalDate.now());
      customerOccupationField.setText("Software Developer");
      customerVisaBox.setValue("NZ Citizen");
      customerCitizenshipBox.setValue("New Zealand");
    }

    if (AppState.getIsAccessingFromSearch()) {
      startWithCustomerID();
    }
  }

  @Override
  @FXML
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.getCustomerDetailsAccessType(),
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
      System.out.println("Invalid Details Data");
      return false;
    }

    System.out.println("Valid Details Data");
    return true;
  }

  @Override
  protected void setDetails() {

    if (!validateData()) {
      return;
    }

    // Set customer details
    customer.setTitle(customerTitleComboBox.getValue());
    customer.setName(customerNameField.getText());
    customer.setDateOfBirth(customerDOBPicker.getValue());
    customer.setOccupation(customerOccupationField.getText());
    customer.setVisa(customerVisaBox.getValue());
    customer.setCitizenship(customerCitizenshipBox.getValue());
  }

  @FXML
  @Override
  protected void handleEditButtonAction() throws IOException {
    System.out.println("Edit button clicked");
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      // Handle create customer logic
      // Save customer to database or perform necessary actions
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

      // Switch to edit mode
      AppState.setCustomerDetailsAccessType("EDIT");
      AccessTypeNotifier.notifyCustomerObservers();

    } else if (AppState.getCustomerDetailsAccessType().equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {

      // Handle confirm changes logic
      // Save changes to database or perform necessary actions
      setDetails();
      AppState.setCustomerDetailsAccessType("VIEW");
      AccessTypeNotifier.notifyCustomerObservers();
      CustomerCreationHelper.createCustomer(customer, true);

    } else if (AppState.getCustomerDetailsAccessType().equals("EDIT")
        && !AccessTypeNotifier.validateCustomerObservers()) {
      System.out.println("Invalid data");
    }
  }

  @Override
  public Button getButton() {
    return customerDetailsButton;
  }

  @FXML
  @Override
  protected void startWithCustomerID() {
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
