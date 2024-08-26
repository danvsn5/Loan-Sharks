package uoa.lavs.frontend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.backend.sql.sql_to_mainframe.CustomerCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;

public class CustomerInputPrimaryAddressController extends AbstractCustomerController
    implements AccessTypeObserver {
  @FXML private ComboBox<String> customerAddressTypeComboBox;
  @FXML private TextField customerAddressLine1Field;
  @FXML private TextField customerAddressLine2Field;
  @FXML private TextField customerSuburbField;
  @FXML private TextField customerCityField;
  @FXML private TextField customerPostcodeField;
  @FXML private RadioButton mailingAddressRadio;
  @FXML private RadioButton primaryAddressRadio;

  @FXML private ImageView incAddress;
  @FXML private ImageView decAddress;
  @FXML private Label addressPageLabel;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  // private ArrayList<uoa.lavs.customer.Address> addresses =
  // customer.getAddresses();

  private int currentAddress = 0;
  private int amountofValidAddresses = 0;

  private boolean isMailingSelected = false;
  private boolean isPrimarySelected = false;

  private ArrayList<Address> existingCustomerAddresses = customer.getAddresses();

  @FXML
  private void initialize() {
    customerAddressTypeComboBox.getItems().addAll("Home", "Work", "PO Box", "Other");
    AccessTypeNotifier.registerCustomerObserver(this);
    updateUIBasedOnAccessType();
    addressPageLabel.setText("Address: " + (currentAddress + 1));

    // Set dummy values
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")) {
      customerAddressTypeComboBox.setValue("Home");
      customerAddressLine1Field.setText("123 Fake Street");
      customerAddressLine2Field.setText("Apt 1");
      customerSuburbField.setText("Fake Suburb");
      customerCityField.setText("Fake City");
      customerPostcodeField.setText("123456");
      mailingAddressRadio.setSelected(false);
      primaryAddressRadio.setSelected(false);
    }

    if (AppState.getIsAccessingFromSearch()) {
      startWithCustomerID();
    }
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.getCustomerDetailsAccessType(),
        editButton,
        idBanner,
        new TextField[] {
          customerAddressLine1Field,
          customerAddressLine2Field,
          customerSuburbField,
          customerCityField,
          customerPostcodeField
        },
        new ComboBox<?>[] {customerAddressTypeComboBox},
        new DatePicker[] {},
        new RadioButton[] {mailingAddressRadio, primaryAddressRadio});
  }

  @Override
  public boolean validateData() {
    // Address type only check not null
    // Address 1 60 chars
    // Address 2 60 chars (optional)
    // Suburb 30 chars
    // City 30 chars
    // Postcode 10 ints (no string)
    // Primary check null
    // Mail check null

    boolean isValid = true;

    customerAddressTypeComboBox.setStyle("");
    customerAddressLine1Field.setStyle("");
    customerAddressLine2Field.setStyle("");
    customerSuburbField.setStyle("");
    customerCityField.setStyle("");
    customerPostcodeField.setStyle("");
    mailingAddressRadio.setStyle("");
    primaryAddressRadio.setStyle("");

    checkFields();

    // Primary and mailing address check, only one address can have primary and
    // mailing

    if (!checkFields() || !checkIfPrimaryAndMailingSelected()) {
      isValid = false;
    }

    return isValid;
  }

  private boolean checkFields() {

    boolean isValid = true;

    if (customerAddressTypeComboBox.getValue() == null) {
      customerAddressTypeComboBox.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerAddressLine1Field.getText().isEmpty()
        || customerAddressLine1Field.getText().length() > 60) {
      customerAddressLine1Field.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerAddressLine2Field.getText().length() > 60) {
      customerAddressLine2Field.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerSuburbField.getText().isEmpty() || customerSuburbField.getText().length() > 30) {
      customerSuburbField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerCityField.getText().isEmpty() || customerCityField.getText().length() > 30) {
      customerCityField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerPostcodeField.getText().isEmpty()
        || customerPostcodeField.getText().length() > 10
        || !customerPostcodeField.getText().matches("[0-9]+")) {
      customerPostcodeField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    return isValid;
  }

  private boolean checkIfPrimaryAndMailingSelected() {

    boolean isValid = true;

    if (!isPrimarySelected) {
      primaryAddressRadio.setStyle(
          "-fx-border-color: red; -fx-border-radius: 20px; -fx-border-width: 3px;");
      isValid = false;
    }

    if (!isMailingSelected) {
      mailingAddressRadio.setStyle(
          "-fx-border-color: red; -fx-border-radius: 20px; -fx-border-width: 3px;");
      isValid = false;
    }

    return isValid;
  }

  private boolean setAddressDetails(String location) {

    if (location != "dec") {
      if (!checkFields()) return false;
    }

    // create a new address with input details
    Address address =
        new Address(
            customer.getCustomerId(),
            customerAddressTypeComboBox.getValue(),
            customerAddressLine1Field.getText(),
            customerAddressLine2Field.getText(),
            customerSuburbField.getText(),
            customerPostcodeField.getText(),
            customerCityField.getText(),
            "New Zealand",
            primaryAddressRadio.isSelected(),
            mailingAddressRadio.isSelected());

    if (currentAddress == existingCustomerAddresses.size()) {
      if (validateData()) existingCustomerAddresses.add(address);
    } else {
      existingCustomerAddresses.set(currentAddress, address);
    }

    for (int i = 0; i < existingCustomerAddresses.size(); i++) {
      existingCustomerAddresses.get(i).setAddressId(i + 1);
    }

    customer.setAddresses(existingCustomerAddresses);
    return true;
  }

  @FXML
  @Override
  protected void handleEditButtonAction() throws IOException {
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      setAddressDetails("edit");

      // check if primary and mailing address are set
      if (!isPrimarySelected) {
        primaryAddressRadio.setStyle(
            "-fx-border-color: red; -fx-border-radius: 20px; -fx-border-width: 3px;");
        return;
      }

      if (!isMailingSelected) {
        mailingAddressRadio.setStyle(
            "-fx-border-color: red; -fx-border-radius: 20px; -fx-border-width: 3px;");
        return;
      }

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

      setAddressDetails("edit");

      AppState.setCustomerDetailsAccessType("VIEW");
      AccessTypeNotifier.notifyCustomerObservers();
      CustomerCreationHelper.createCustomer(customer, true);
    }
  }

  @FXML
  private void handleMailingAddressRadioAction() {
    if (mailingAddressRadio.isSelected() && existingCustomerAddresses.size() > 0) {
      for (int i = 0; i < existingCustomerAddresses.size(); i++) {
        existingCustomerAddresses.get(i).setIsMailing(false);
        isMailingSelected = true;
      }
      existingCustomerAddresses.get(currentAddress).setIsMailing(true);
    } else {
      isMailingSelected = false;
    }
  }

  @FXML
  private void handlePrimaryAddressRadioAction() {
    if (primaryAddressRadio.isSelected() && existingCustomerAddresses.size() > 0) {
      for (int i = 0; i < existingCustomerAddresses.size(); i++) {
        existingCustomerAddresses.get(i).setIsPrimary(false);
        isPrimarySelected = true;
      }
      existingCustomerAddresses.get(currentAddress).setIsPrimary(true);
    } else {
      isPrimarySelected = false;
    }
  }

  @FXML
  private void handleIncAddress() {
    if (AppState.getCustomerDetailsAccessType() == "CREATE") {

      // cannot increment without having filled in the current address
      if (!setAddressDetails("inc")) {
        System.out.println("data is invalid and cannot increment");
        return;
      }

      currentAddress++;
      addressPageLabel.setText("Address: " + (currentAddress + 1));
      amountofValidAddresses++;

      // set all fields to empty if user is on the final page, otherwise set to next
      // value
      if (currentAddress == amountofValidAddresses) {
        setAddressDetailsUI("empty");
      } else {
        setAddressDetailsUI("value");
      }
    }

    if (AppState.getCustomerDetailsAccessType() == "VIEW") {

      if (currentAddress < existingCustomerAddresses.size() - 1) {
        currentAddress++;
        addressPageLabel.setText("Address: " + (currentAddress + 1));
        setAddressDetailsUI("value");
      }
    }

    if (AppState.getCustomerDetailsAccessType() == "EDIT") {

      if (!setAddressDetails("incEdit")) {
        // if a field is invalid, then do not increment and return
        return;
      }

      currentAddress++;
      addressPageLabel.setText("Address: " + (currentAddress + 1));

      // set all the fields to the new currentAddress
      if (currentAddress == existingCustomerAddresses.size()) {
        setAddressDetailsUI("empty");
      } else {
        setAddressDetailsUI("value");
      }
    }
  }

  @FXML
  private void handleDecAddress() {
    if (AppState.getCustomerDetailsAccessType() == "CREATE") {

      setAddressDetails("dec");

      if (currentAddress != 0) {
        currentAddress--;
        addressPageLabel.setText("Address: " + (currentAddress + 1));
      }

      setAddressDetailsUI("value");

      if (validateData()) {
        customerAddressTypeComboBox.setStyle("");
        customerAddressLine1Field.setStyle("");
        customerAddressLine2Field.setStyle("");
        customerSuburbField.setStyle("");
        customerCityField.setStyle("");
        customerPostcodeField.setStyle("");
        mailingAddressRadio.setStyle("");
        primaryAddressRadio.setStyle("");
      }
    }

    if (AppState.getCustomerDetailsAccessType() == "VIEW" && currentAddress > 0) {
      currentAddress--;
      addressPageLabel.setText("Address: " + (currentAddress + 1));
      setAddressDetailsUI("value");
    }

    if (AppState.getCustomerDetailsAccessType() == "EDIT") {

      setAddressDetails("dec");
      if (currentAddress != 0) {
        currentAddress--;
        addressPageLabel.setText("Address: " + (currentAddress + 1));
      }

      setAddressDetailsUI("value");

      if (validateData()) {
        customerAddressTypeComboBox.setStyle("");
        customerAddressLine1Field.setStyle("");
        customerAddressLine2Field.setStyle("");
        customerSuburbField.setStyle("");
        customerCityField.setStyle("");
        customerPostcodeField.setStyle("");
        mailingAddressRadio.setStyle("");
        primaryAddressRadio.setStyle("");
      }
    }
  }

  @Override
  public Button getButton() {
    return customerAddressButton;
  }

  public void setAddressDetailsUI(String setting) {

    if (setting == "empty") {
      customerAddressTypeComboBox.setValue("");
      customerAddressLine1Field.setText("");
      customerAddressLine2Field.setText("");
      customerSuburbField.setText("");
      customerCityField.setText("");
      customerPostcodeField.setText("");
      mailingAddressRadio.setSelected(false);
      primaryAddressRadio.setSelected(false);

    } else {
      customerAddressTypeComboBox.setValue(
          existingCustomerAddresses.get(currentAddress).getAddressType());
      customerAddressLine1Field.setText(
          existingCustomerAddresses.get(currentAddress).getAddressLineOne());
      customerAddressLine2Field.setText(
          existingCustomerAddresses.get(currentAddress).getAddressLineTwo());
      customerSuburbField.setText(existingCustomerAddresses.get(currentAddress).getSuburb());
      customerCityField.setText(existingCustomerAddresses.get(currentAddress).getCity());
      customerPostcodeField.setText(existingCustomerAddresses.get(currentAddress).getPostCode());
      mailingAddressRadio.setSelected(existingCustomerAddresses.get(currentAddress).getIsMailing());
      primaryAddressRadio.setSelected(existingCustomerAddresses.get(currentAddress).getIsPrimary());
    }
    // set styles to empty
    customerAddressTypeComboBox.setStyle("");
    customerAddressLine1Field.setStyle("");
    customerAddressLine2Field.setStyle("");
    customerSuburbField.setStyle("");
    customerCityField.setStyle("");
    customerPostcodeField.setStyle("");
    mailingAddressRadio.setStyle("");
    primaryAddressRadio.setStyle("");
  }

  @Override
  protected void startWithCustomerID() {
    IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
    customer = IndividualCustomerSingleton.getInstance();

    if (existingCustomerAddresses.size() > 0) {
      customerAddressTypeComboBox.setValue(existingCustomerAddresses.get(0).getAddressType());
      customerAddressLine1Field.setText(existingCustomerAddresses.get(0).getAddressLineOne());
      customerAddressLine2Field.setText(existingCustomerAddresses.get(0).getAddressLineTwo());
      customerSuburbField.setText(existingCustomerAddresses.get(0).getSuburb());
      customerCityField.setText(existingCustomerAddresses.get(0).getCity());
      customerPostcodeField.setText(existingCustomerAddresses.get(0).getPostCode());
      mailingAddressRadio.setSelected(existingCustomerAddresses.get(0).getIsMailing());
      primaryAddressRadio.setSelected(existingCustomerAddresses.get(0).getIsPrimary());

      for (Address address : existingCustomerAddresses) {
        if (address.getIsPrimary()) {
          isPrimarySelected = true;
        }
        if (address.getIsMailing()) {
          isMailingSelected = true;
        }
      }
    }
  }

  @Override
  protected void setDetails() {
    // Overriden by setAddressDetails
  }
}
