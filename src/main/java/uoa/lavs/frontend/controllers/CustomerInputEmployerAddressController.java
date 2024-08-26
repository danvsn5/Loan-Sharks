package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.backend.oop.customer.CustomerEmployer;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.backend.sql.sql_to_mainframe.CustomerCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;

public class CustomerInputEmployerAddressController extends AbstractCustomerController
    implements AccessTypeObserver {
  @FXML private TextField employerAddressLine1Field;
  @FXML private TextField employerAddressLine2Field;
  @FXML private TextField employerSuburbField;
  @FXML private TextField employerCityField;
  @FXML private TextField employerPostcodeField;

  @FXML private ComboBox<String> employerCountryBox;

  @FXML private Button customerDetailsButton;
  @FXML private Button customerAddressButton;
  @FXML private Button customerContactButton;
  @FXML private Button customerEmployerButton;

  // TODO: THIS IS AN EXCEPTION, THIS NEEDS TO SHOWN AS RED ONLY TO THE EMPLOYER DETAILS SCREEN AND
  // ITSELF
  @FXML private Button employerAddressButton;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;
  @FXML private Label idBanner;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  @FXML
  private void initialize() {
    employerCountryBox.getItems().addAll(AppState.getAllCountries());
    AccessTypeNotifier.registerCustomerObserver(this);
    updateUIBasedOnAccessType();

    // Set Dummy data
    if (AppState.getCustomerDetailsAccessType() == "CREATE") {
      employerAddressLine1Field.setText("123 Fake Street");
      employerAddressLine2Field.setText("Apt 1");
      employerSuburbField.setText("Fake Suburb");
      employerCityField.setText("Fake City");
      employerPostcodeField.setText("123456");
      employerCountryBox.setValue("New Zealand");
    }

    if (AppState.getIsAccessingFromSearch()) {
      startWithCustomerID();
    }
  }

  @Override
  public boolean validateData() {
    // Address 1 is 60 characters long and is required.
    // Address 2 is 60 chars, optional
    // Suburb is 30 chars, required
    // City is 30 chars, required
    // Postcode is 10 ints
    // Country should not be null

    Boolean isValid = true;
    employerAddressLine1Field.setStyle("");
    employerAddressLine2Field.setStyle("");
    employerSuburbField.setStyle("");
    employerCityField.setStyle("");
    employerPostcodeField.setStyle("");
    employerCountryBox.setStyle("");

    if (employerAddressLine1Field.getText().isEmpty()
        || employerAddressLine1Field.getText().length() > 60) {
      employerAddressLine1Field.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (employerAddressLine2Field.getText().length() > 60) {
      employerAddressLine2Field.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (employerSuburbField.getText().isEmpty() || employerSuburbField.getText().length() > 30) {
      employerSuburbField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (employerCityField.getText().isEmpty() || employerCityField.getText().length() > 30) {
      employerCityField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (employerPostcodeField.getText().isEmpty()
        || !employerPostcodeField.getText().matches("[0-9]{1,10}")) {
      employerPostcodeField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (employerCountryBox.getValue() == null) {
      employerCountryBox.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (!isValid) {

      System.out.println("Invalid Employer Address data");
      return false;
    }

    System.out.println("Valid Employer Address data");
    return true;
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.getCustomerDetailsAccessType(),
        editButton,
        idBanner,
        new TextField[] {
          employerAddressLine1Field,
          employerAddressLine2Field,
          employerSuburbField,
          employerCityField,
          employerPostcodeField
        },
        new ComboBox<?>[] {
          employerCountryBox,
        },
        new DatePicker[] {},
        new RadioButton[] {});
  }

  @Override
  protected void setDetails() {

    if (!validateData()) {
      return;
    }

    CustomerEmployer employer = customer.getEmployer();
    employer.setLineOne(employerAddressLine1Field.getText());
    employer.setLineTwo(employerAddressLine2Field.getText());
    employer.setSuburb(employerSuburbField.getText());
    employer.setCity(employerCityField.getText());
    employer.setPostCode(employerPostcodeField.getText());

    employer.setCountry(employerCountryBox.getValue());
  }

  @FXML
  @Override
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

  @Override
  public Button getButton() {
    return employerAddressButton;
  }

  @Override
  protected void startWithCustomerID() {
    IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
    customer = IndividualCustomerSingleton.getInstance();

    employerAddressLine1Field.setText(customer.getEmployer().getLineOne());
    employerAddressLine2Field.setText(customer.getEmployer().getLineTwo());
    employerSuburbField.setText(customer.getEmployer().getSuburb());
    employerCityField.setText(customer.getEmployer().getCity());
    employerPostcodeField.setText(customer.getEmployer().getPostCode());
    employerCountryBox.setValue(customer.getEmployer().getCountry());
  }

  @FXML
  @Override
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
      } else if (buttonId.equals("employerAddressButton")) {
        employerAddressButton.setStyle(style);
      }
    }
  }
}
