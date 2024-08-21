package uoa.lavs.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import okhttp3.Address;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.ControllerHelper;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;

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
  private Button detailsButton;
  @FXML
  private Button mailingAddressButton;
  @FXML
  private Button contactButton;
  @FXML
  private Button employerButton;

  @FXML
  private Button editButton;
  @FXML
  private ImageView staticReturnImageView;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  // private ArrayList<uoa.lavs.customer.Address> addresses =
  // customer.getAddresses();
  private List<uoa.lavs.customer.Address> addresses = new ArrayList<>();
  private int currentAddress = 0;
  private boolean isMailingSelected = false;
  private boolean isPrimarySelected = false;

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

    if (!isValid) {
      return false;
    }

    return true;
  }

  private boolean setAddressDetails() {

    if (!validateData()) {
      return false;
    }

    // TODO: Set customer details (Chulshin or Jamie)

    /**
     * ArrayList<Address> addresses = customer.getAddresses();
     *
     * <p>
     * address.setAddressLineOne(customerAddressLine1Field.getText());
     * address.setAddressLineTwo(customerAddressLine2Field.getText());
     * address.setSuburb(customerSuburbField.getText());
     * address.setCity(customerCityField.getText());
     * address.setPostCode(customerPostcodeField.getText());
     * address.setAddressType(customerAddressTypeComboBox.getValue());
     *
     * <p>
     * // Autosetting to New Zealand address.setCountry("New Zealand");
     *
     * <p>
     * // handle mailing address if (mailingAddressRadio.isSelected()) {
     * customer.setMailingAddress(address); }
     */
    return true;
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
    // Add mailing address radio button action code here

  }

  @FXML
  private void handlePrimaryAddressRadioAction() {
    // Add mailing address radio button action code here

  }

  @FXML
  private void handleIncAddress() {
    if (AppState.customerDetailsAccessType == "CREATE") {
      // gets all the input fields and adds them to an address instance
      uoa.lavs.customer.Address address = new uoa.lavs.customer.Address(customer.getCustomerId(),
          customerAddressTypeComboBox.getValue(), customerAddressLine1Field.getText(),
          customerAddressLine2Field.getText(), customerSuburbField.getText(), customerPostcodeField.getText(),
          customerCityField.getText(), "New Zealand", primaryAddressRadio.isSelected(),
          mailingAddressRadio.isSelected());
      addresses.add(address);

      // conditional statements to check if the primary or mailing address is selected
      if (primaryAddressRadio.isSelected()) {
        isPrimarySelected = true;
      }
      if (mailingAddressRadio.isSelected()) {
        isMailingSelected = true;
      }


      // current address counter increment for displaying the cached results in the
      // temp array
      currentAddress++;

      // clear all fields
      customerAddressTypeComboBox.setValue("");
      customerAddressLine1Field.setText(null);
      customerAddressLine2Field.setText(null);
      customerSuburbField.setText(null);
      customerCityField.setText(null);
      customerPostcodeField.setText(null);
      mailingAddressRadio.setSelected(false);
      primaryAddressRadio.setSelected(false);

      // print out all fields from first address
      System.out.println(customerAddressLine1Field.getText());
      System.out.println(addresses.get(currentAddress - 1).getAddressType());
      System.out.println(addresses.get(currentAddress - 1).getAddressLineOne());
      System.out.println(addresses.get(currentAddress - 1).getAddressLineTwo());
      System.out.println(addresses.get(currentAddress - 1).getSuburb());
      System.out.println(addresses.get(currentAddress - 1).getPostCode());
      System.out.println(addresses.get(currentAddress - 1).getCity());
      System.out.println(addresses.get(currentAddress - 1).getCountry());
      System.out.println(addresses.get(currentAddress - 1).getIsPrimary());
      System.out.println(addresses.get(currentAddress - 1).getIsMailing());
      System.out.println(addresses.get(currentAddress - 1).getCustomerId());
    }

  }

  @FXML
  private void handleDecAddress() {
    // Add decrement address button action code here

  }
}
