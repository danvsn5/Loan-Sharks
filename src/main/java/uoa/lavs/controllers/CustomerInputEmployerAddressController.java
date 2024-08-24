package uoa.lavs.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.ControllerHelper;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;
import uoa.lavs.sql.sql_to_mainframe.CustomerCreationHelper;

public class CustomerInputEmployerAddressController implements AccessTypeObserver {
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
    if (AppState.customerDetailsAccessType == "CREATE") {
      employerAddressLine1Field.setText("123 Fake Street");
      employerAddressLine2Field.setText("Apt 1");
      employerSuburbField.setText("Fake Suburb");
      employerCityField.setText("Fake City");
      employerPostcodeField.setText("123456");
      employerCountryBox.setValue("New Zealand");
    }

    if (AppState.isAccessingFromSearch) {
      IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
      customer = IndividualCustomerSingleton.getInstance();

      employerAddressLine1Field.setText(customer.getEmployer().getLineOne());
      employerAddressLine2Field.setText(customer.getEmployer().getLineTwo());
      employerSuburbField.setText(customer.getEmployer().getSuburb());
      employerCityField.setText(customer.getEmployer().getCity());
      employerPostcodeField.setText(customer.getEmployer().getPostCode());
      employerCountryBox.setValue(customer.getEmployer().getCountry());
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
      return false;
    }

    return true;
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.customerDetailsAccessType,
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

  private boolean setAddressDetails() {

    if (!validateData()) {
      return false;
    }

    CustomerEmployer employer = customer.getEmployer();
    employer.setLineOne(employerAddressLine1Field.getText());
    employer.setLineTwo(employerAddressLine2Field.getText());
    employer.setSuburb(employerSuburbField.getText());
    employer.setCity(employerCityField.getText());
    employer.setPostCode(employerPostcodeField.getText());

    employer.setCountry(employerCountryBox.getValue());

    return true;
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
  private void handleEditButtonAction() throws IOException {
    if (AppState.customerDetailsAccessType.equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      AppState.customerDetailsAccessType = "VIEW";
      setAddressDetails();
      CustomerCreationHelper.createCustomer(customer);
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
    } else if (AppState.customerDetailsAccessType.equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {
      AppState.customerDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
      setAddressDetails();
    }
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

  @Override
  public Button getButton() {
    return employerAddressButton;
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
      } else if (buttonId.equals("employerAddressButton")) {
        employerAddressButton.setStyle(style);
      }
    }
  }
}
