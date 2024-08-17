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
import uoa.lavs.customer.Address;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;

public class CustomerInputMailingAddressController implements AccessTypeObserver {
  @FXML private ComboBox<String> customerAddressTypeComboBox;
  @FXML private TextField customerAddressLine1Field;
  @FXML private TextField customerAddressLine2Field;
  @FXML private TextField customerSuburbField;
  @FXML private TextField customerCityField;
  @FXML private TextField customerPostcodeField;

  @FXML private Button detailsButton;
  @FXML private Button primaryAddressButton;
  @FXML private Button contactButton;
  @FXML private Button employerButton;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  @FXML
  private void initialize() {
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
        new ComboBox<?>[] {customerAddressTypeComboBox},
        new DatePicker[] {},
        new RadioButton[] {});
    System.out.println("Contact Controller Invoked");
  }

  private void setAddressDetails() {
    Address address = customer.getMailingAddress();
    address.setAddressLineOne(customerAddressLine1Field.getText());
    address.setAddressLineTwo(customerAddressLine2Field.getText());
    address.setSuburb(customerSuburbField.getText());
    address.setCity(customerCityField.getText());
    address.setPostCode(customerPostcodeField.getText());
    address.setAddressType(customerAddressTypeComboBox.getValue());

    // Autosetting to New Zealand
    address.setCountry("New Zealand");
  }

  @FXML
  private void handleDetailsButtonAction() {
    setAddressDetails();
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handlePrimaryAddressButtonAction() {
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
  private void handleEditButtonAction() {
    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      setAddressDetails();
      AppState.customerDetailsAccessType = "VIEW";
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
    } else if (AppState.customerDetailsAccessType.equals("EDIT")) {
      setAddressDetails();
      AppState.customerDetailsAccessType = "VIEW";
    }
    AccessTypeNotifier.notifyCustomerObservers();
    updateUIBasedOnAccessType();
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_MENU);
  }
}
