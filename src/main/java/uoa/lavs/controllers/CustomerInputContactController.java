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
import uoa.lavs.customer.CustomerContact;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;

public class CustomerInputContactController implements AccessTypeObserver {
  @FXML private TextField customerEmailTextField;
  @FXML private TextField customerPhoneNumberOne;
  @FXML private TextField customerPhoneNumberTwo;

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
          customerPhoneNumberTwo,
          customerPreferredContactBox,
          customerAltContactBox
        },
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {});
    setContactDetails();
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      AppState.customerDetailsAccessType = "VIEW";
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
    } else if (AppState.customerDetailsAccessType.equals("EDIT")) {
      AppState.customerDetailsAccessType = "VIEW";
    }
    AccessTypeNotifier.notifyCustomerObservers();
    updateUIBasedOnAccessType();
  }

  private void setContactDetails() {
    CustomerContact contact = customer.getContact();
    contact.setCustomerEmail(customerEmailTextField.getText());
    // TODO Uncomment once types are implemented
    // contact.getPhoneOne().setType();
    contact.getPhoneOne().setPhoneNumber(customerPhoneNumberOne.getText());
    // contact.getPhoneTwo().setType();
    contact.getPhoneTwo().setPhoneNumber(customerPhoneNumberTwo.getText());
    contact.setPreferredContact(customerPreferredContactBox.getText());
    contact.setAlternateContact(customerAltContactBox.getText());
  }

  // add handlers for all buttons
  @FXML
  private void handleCustomerDetailsButtonAction() {
    setContactDetails();
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleCustomerAddressButtonAction() {
    setContactDetails();
    Main.setUi(AppUI.CI_PRIMARY_ADDRESS);
  }

  @FXML
  private void handleCustomerEmployerButtonAction() {
    setContactDetails();
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
}
