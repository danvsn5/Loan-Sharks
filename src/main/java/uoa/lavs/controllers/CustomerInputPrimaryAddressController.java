package uoa.lavs.controllers;

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
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.ControllerHelper;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;
import uoa.lavs.sql.sql_to_mainframe.CustomerCreationHelper;

public class CustomerInputPrimaryAddressController implements AccessTypeObserver {
  @FXML
  private ComboBox<String> customerAddressTypeComboBox;
  @FXML
  private TextField customerAddressLine1Field;
  @FXML
  private TextField customerAddressLine2Field;
  @FXML
  private TextField customerSuburbField;
  @FXML
  private TextField customerCityField;
  @FXML
  private TextField customerPostcodeField;
  @FXML
  private RadioButton mailingAddressRadio;
  @FXML
  private RadioButton primaryAddressRadio;

  @FXML
  private ImageView incAddress;
  @FXML
  private ImageView decAddress;
  @FXML
  private Label addressPageLabel;

  @FXML
  private Button customerDetailsButton;
  @FXML
  private Button customerAddressButton;
  @FXML
  private Button customerContactButton;
  @FXML
  private Button customerEmployerButton;

  @FXML
  private Button editButton;
  @FXML
  private ImageView staticReturnImageView;

  @FXML
  private Label idBanner;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  // private ArrayList<uoa.lavs.customer.Address> addresses =
  // customer.getAddresses();
  private ArrayList<uoa.lavs.customer.Address> addresses = new ArrayList<>();
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

    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      customerAddressTypeComboBox.setValue("Home");
      customerAddressLine1Field.setText("123 Fake Street");
      customerAddressLine2Field.setText("Apt 1");
      customerSuburbField.setText("Fake Suburb");
      customerCityField.setText("Fake City");
      customerPostcodeField.setText("123456");
      mailingAddressRadio.setSelected(false);
      primaryAddressRadio.setSelected(false);
    }

    if (AppState.isAccessingFromSearch) {
      IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
      customer = IndividualCustomerSingleton.getInstance();

      if (customer.getAddresses().size() > 0) {
        addresses = customer.getAddresses();
        customerAddressTypeComboBox.setValue(customer.getAddresses().get(0).getAddressType());
        customerAddressLine1Field.setText(customer.getAddresses().get(0).getAddressLineOne());
        customerAddressLine2Field.setText(customer.getAddresses().get(0).getAddressLineTwo());
        customerSuburbField.setText(customer.getAddresses().get(0).getSuburb());
        customerCityField.setText(customer.getAddresses().get(0).getCity());
        customerPostcodeField.setText(addresses.get(0).getPostCode());
        mailingAddressRadio.setSelected(addresses.get(0).getIsMailing());
        primaryAddressRadio.setSelected(addresses.get(0).getIsPrimary());
        if (mailingAddressRadio.isSelected()) {
          isMailingSelected = true;
        }
        if (primaryAddressRadio.isSelected()) {
          isPrimarySelected = true;
        }
      }
    }
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.customerDetailsAccessType,
        editButton,
        idBanner,
        new TextField[] {
            customerAddressLine1Field,
            customerAddressLine2Field,
            customerSuburbField,
            customerCityField,
            customerPostcodeField
        },
        new ComboBox<?>[] { customerAddressTypeComboBox },
        new DatePicker[] {},
        new RadioButton[] { mailingAddressRadio, primaryAddressRadio });
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

    // Primary and mailing address check, only one address can have primary and
    // mailing

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

    if (!isValid) {
      return false;
    }

    return true;
  }

  private boolean setAddressDetails(String location) {

    if (location != "dec") {
      if (!validateData())
        return false;
    }

    // create a new address with input details

    Address address = new Address(
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
      if (validateData())
        existingCustomerAddresses.add(address);
    } else {
      existingCustomerAddresses.set(currentAddress, address);
    }

    customer.setAddresses(existingCustomerAddresses);
    return true;
  }

  @FXML
  private void handleDetailsButtonAction() {
    setAddressDetails("edit");
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleMailingAddressButtonAction() {
    setAddressDetails("edit");
    Main.setUi(AppUI.CI_MAILING_ADDRESS);
  }

  @FXML
  private void handleContactButtonAction() {
    setAddressDetails("edit");
    Main.setUi(AppUI.CI_CONTACT);
  }

  @FXML
  private void handleEmployerButtonAction() {
    setAddressDetails("edit");
    Main.setUi(AppUI.CI_EMPLOYER);
  }

  @FXML
  private void handleEditButtonAction() throws IOException {
    if (AppState.customerDetailsAccessType.equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      setAddressDetails("edit");

      boolean customerIsValid = CustomerCreationHelper.validateCustomer(customer);
      if (!customerIsValid) {
        System.out.println("Customer is not valid and thus will not be created");
        editButton.setStyle("-fx-border-color: red");
        return;
      }

      AppState.customerDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
      CustomerCreationHelper.createCustomer(customer, false);

    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
    } else if (AppState.customerDetailsAccessType.equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {
      AppState.customerDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
      setAddressDetails("edit");
      CustomerCreationHelper.createCustomer(customer, true);
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
    if (AppState.customerDetailsAccessType == "CREATE") {

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

    if (AppState.customerDetailsAccessType == "VIEW" && currentAddress < addresses.size() - 1) {

      // increment the current address counter
      currentAddress++;
      addressPageLabel.setText("Address: " + (currentAddress + 1));

      // set all the fields to the new currentAddress
      setAddressDetailsUI("value");

    }

    if (AppState.customerDetailsAccessType == "EDIT") {

      currentAddress++;
      addressPageLabel.setText("Address: " + (currentAddress + 1));

      // set all the fields to the new currentAddress
      setAddressDetailsUI("value");

    }
  }

  @FXML
  private void handleDecAddress() {
    if (currentAddress != 0) {
      if (currentAddress == 0) {
        return;
      }
      if (AppState.customerDetailsAccessType == "CREATE") {
        if (currentAddress == addresses.size()) {
          // gets all the input fields and adds them to an address instance
          uoa.lavs.customer.Address address = new uoa.lavs.customer.Address(
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
          addresses.add(address);

        } else {
          addresses.get(currentAddress).setAddressType(customerAddressTypeComboBox.getValue());
          addresses.get(currentAddress).setAddressLineOne(customerAddressLine1Field.getText());
          addresses.get(currentAddress).setAddressLineTwo(customerAddressLine2Field.getText());
          addresses.get(currentAddress).setSuburb(customerSuburbField.getText());
          addresses.get(currentAddress).setPostCode(customerPostcodeField.getText());
          addresses.get(currentAddress).setCity(customerCityField.getText());
          addresses.get(currentAddress).setCountry("New Zealand");
          addresses.get(currentAddress).setIsPrimary(primaryAddressRadio.isSelected());
          addresses.get(currentAddress).setIsMailing(mailingAddressRadio.isSelected());
        }

        // decrement the current address counter
        currentAddress--;
        addressPageLabel.setText("Address: " + (currentAddress + 1));

        // set all the fields to the new currentAddress
        setAddressDetailsUI("value");

      }

      if (AppState.customerDetailsAccessType == "VIEW" && currentAddress > 0) {

        // decrement the current address counter
        currentAddress--;
        addressPageLabel.setText("Address: " + (currentAddress + 1));

        // set all the fields to the new currentAddress
        setAddressDetailsUI("value");

      }

      if (AppState.customerDetailsAccessType == "EDIT") {

        if (currentAddress != 0) {
          // decrement the current address counter
          currentAddress--;
          addressPageLabel.setText("Address: " + (currentAddress + 1));

          // set all the fields to the new currentAddress
          setAddressDetailsUI("value");
        }
      }
    }
  }

  @Override
  public Button getButton() {
    return customerAddressButton;
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
      customerAddressTypeComboBox.setValue(addresses.get(currentAddress).getAddressType());
      customerAddressLine1Field.setText(addresses.get(currentAddress).getAddressLineOne());
      customerAddressLine2Field.setText(addresses.get(currentAddress).getAddressLineTwo());
      customerSuburbField.setText(addresses.get(currentAddress).getSuburb());
      customerCityField.setText(addresses.get(currentAddress).getCity());
      customerPostcodeField.setText(addresses.get(currentAddress).getPostCode());
      mailingAddressRadio.setSelected(addresses.get(currentAddress).getIsMailing());
      primaryAddressRadio.setSelected(addresses.get(currentAddress).getIsPrimary());
    }

  }
}
