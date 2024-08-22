package uoa.lavs.controllers;

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
  @FXML private RadioButton primaryAddressRadio;

  @FXML private ImageView incAddress;
  @FXML private ImageView decAddress;
  @FXML private Label addressPageLabel;

  @FXML private Button detailsButton;
  @FXML private Button mailingAddressButton;
  @FXML private Button contactButton;
  @FXML private Button employerButton;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Label idBanner;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  // private ArrayList<uoa.lavs.customer.Address> addresses =
  // customer.getAddresses();
  private ArrayList<uoa.lavs.customer.Address> addresses = new ArrayList<>();
  private int currentAddress = 0;
  private boolean isMailingSelected = false;
  private boolean isPrimarySelected = false;

  private ArrayList<uoa.lavs.customer.Address> existingCustomerAddresses = customer.getAddresses();

  @FXML
  private void initialize() {
    customerAddressTypeComboBox.getItems().addAll("Home", "Work", "PO Box", "Other");
    AccessTypeNotifier.registerCustomerObserver(this);
    updateUIBasedOnAccessType();
    addressPageLabel.setText("Address: " + (currentAddress + 1));

    if (AppState.isAccessingFromSearch) {
      IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
      customer = IndividualCustomerSingleton.getInstance();

      if (customer.getAddresses().size() > 0) {
        customerAddressTypeComboBox.setValue(customer.getAddresses().get(0).getAddressType());
        customerAddressLine1Field.setText(customer.getAddresses().get(0).getAddressLineOne());
        customerAddressLine2Field.setText(customer.getAddresses().get(0).getAddressLineTwo());
        customerSuburbField.setText(customer.getAddresses().get(0).getSuburb());
        customerCityField.setText(customer.getAddresses().get(0).getCity());
        customerPostcodeField.setText(customer.getAddresses().get(0).getPostCode());
        mailingAddressRadio.setSelected(customer.getAddresses().get(0).getIsMailing());
        primaryAddressRadio.setSelected(customer.getAddresses().get(0).getIsPrimary());
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

  private boolean setAddressDetails() {

    if (!validateData()) {
      return false;
    }

    customer.setAddresses(addresses);
    return true;
  }

  @FXML
  private void handleDetailsButtonAction() {
    setAddressDetails();
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleMailingAddressButtonAction() {
    Main.setUi(AppUI.CI_MAILING_ADDRESS);
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
    if (AppState.customerDetailsAccessType.equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      AppState.customerDetailsAccessType = "VIEW";
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
    // overrule strategy: if the mailing button is selected, the isMailing is set to false
    // for all other addresses and the current address is set to true
    if (mailingAddressRadio.isSelected() && addresses.size() > 0) {
      for (int i = 0; i < addresses.size(); i++) {
        addresses.get(i).setIsMailing(false);
        isMailingSelected = true;
      }
      addresses.get(currentAddress).setIsMailing(true);
    } else {
      isMailingSelected = false;
    }
  }

  @FXML
  private void handlePrimaryAddressRadioAction() {
    // overrule strategy: if the primary button is selected, the isPrimary is set to false
    // for all other addresses and the current address is set to true
    if (primaryAddressRadio.isSelected() && addresses.size() > 0) {
      for (int i = 0; i < addresses.size(); i++) {
        addresses.get(i).setIsPrimary(false);
        isPrimarySelected = true;
      }
      addresses.get(currentAddress).setIsPrimary(true);
    } else {
      isPrimarySelected = false;
    }
  }

  @FXML
  private void handleIncAddress() {
    if (AppState.customerDetailsAccessType == "CREATE") {
      if (currentAddress == 9) {
        return;
      }

      // if the current address is in the final 'page' of the section, then create a
      // new address and add to list
      if (currentAddress == addresses.size()) {
        // gets all the input fields and adds them to an address instance
        uoa.lavs.customer.Address address =
            new uoa.lavs.customer.Address(
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

      // conditional statements to check if the primary or mailing address is selected
      if (primaryAddressRadio.isSelected()) {
        isPrimarySelected = true;
        // TODO disable the primary address radio button if it has been selected once,
        // but have it deselectable ONLY WHEN THAT address is being displayed
        // ALTERNATIVELY!!!!!!!! if they select primary address in a different address,
        // then the OLD ADDRESS gets its primary address radio button deselected.
        // FOLLOW ALONG FOR MAILING ADDRESS
      }
      if (mailingAddressRadio.isSelected()) {
        isMailingSelected = true;
      }

      // current address counter increment for displaying the cached results in the
      // temp array
      currentAddress++;
      addressPageLabel.setText("Address: " + (currentAddress + 1));

      // if a new address was created, then set the fields to empty values, otherwise
      // set them to the
      // next address in the arraylist
      if (currentAddress == addresses.size()) {
        // clear all fields
        customerAddressTypeComboBox.setValue("");
        customerAddressLine1Field.setText("");
        customerAddressLine2Field.setText("");
        customerSuburbField.setText("");
        customerCityField.setText("");
        customerPostcodeField.setText("");
        mailingAddressRadio.setSelected(false);
        primaryAddressRadio.setSelected(false);
      } else {
        // set all the fields to the new currentAddress
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

    if (AppState.customerDetailsAccessType == "VIEW") {

      // increment the current address counter
      currentAddress++;
      addressPageLabel.setText("Address: " + (currentAddress + 1));

      // set all the fields to the new currentAddress
      customerAddressTypeComboBox.setValue(addresses.get(currentAddress).getAddressType());
      customerAddressLine1Field.setText(addresses.get(currentAddress).getAddressLineOne());
      customerAddressLine2Field.setText(addresses.get(currentAddress).getAddressLineTwo());
      customerSuburbField.setText(addresses.get(currentAddress).getSuburb());
      customerCityField.setText(addresses.get(currentAddress).getCity());
      customerPostcodeField.setText(addresses.get(currentAddress).getPostCode());
      mailingAddressRadio.setSelected(addresses.get(currentAddress).getIsMailing());
      primaryAddressRadio.setSelected(addresses.get(currentAddress).getIsPrimary());
    }

    if (AppState.customerDetailsAccessType == "EDIT") {

      currentAddress++;
      addressPageLabel.setText("Address: " + (currentAddress + 1));

      // set all the fields to the new currentAddress
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

  @FXML
  private void handleDecAddress() {
    if (currentAddress != 0) {
      if (currentAddress == 0) {
        return;
      }
      if (AppState.customerDetailsAccessType == "CREATE") {
        if (currentAddress == addresses.size()) {
          // gets all the input fields and adds them to an address instance
          uoa.lavs.customer.Address address =
              new uoa.lavs.customer.Address(
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
        customerAddressTypeComboBox.setValue(addresses.get(currentAddress).getAddressType());
        customerAddressLine1Field.setText(addresses.get(currentAddress).getAddressLineOne());
        customerAddressLine2Field.setText(addresses.get(currentAddress).getAddressLineTwo());
        customerSuburbField.setText(addresses.get(currentAddress).getSuburb());
        customerCityField.setText(addresses.get(currentAddress).getCity());
        customerPostcodeField.setText(addresses.get(currentAddress).getPostCode());
        mailingAddressRadio.setSelected(addresses.get(currentAddress).getIsMailing());
        primaryAddressRadio.setSelected(addresses.get(currentAddress).getIsPrimary());
      }

      if (AppState.customerDetailsAccessType == "VIEW") {

        // decrement the current address counter
        currentAddress--;
        addressPageLabel.setText("Address: " + (currentAddress + 1));

        // set all the fields to the new currentAddress
        customerAddressTypeComboBox.setValue(addresses.get(currentAddress).getAddressType());
        customerAddressLine1Field.setText(addresses.get(currentAddress).getAddressLineOne());
        customerAddressLine2Field.setText(addresses.get(currentAddress).getAddressLineTwo());
        customerSuburbField.setText(addresses.get(currentAddress).getSuburb());
        customerCityField.setText(addresses.get(currentAddress).getCity());
        customerPostcodeField.setText(addresses.get(currentAddress).getPostCode());
        mailingAddressRadio.setSelected(addresses.get(currentAddress).getIsMailing());
        primaryAddressRadio.setSelected(addresses.get(currentAddress).getIsPrimary());
      }

      if (AppState.customerDetailsAccessType == "EDIT") {

        if (currentAddress != 0) {
          // decrement the current address counter
          currentAddress--;
          addressPageLabel.setText("Address: " + (currentAddress + 1));

          // set all the fields to the new currentAddress
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
  }
}
