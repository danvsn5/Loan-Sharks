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
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;

public class CustomerInputEmployerAddressController implements AccessTypeObserver {
  @FXML private TextField employerAddressLine1Field;
  @FXML private TextField employerAddressLine2Field;
  @FXML private TextField employerSuburbField;
  @FXML private TextField employerCityField;
  @FXML private TextField employerPostcodeField;

  @FXML private Button detailsButton;
  @FXML private Button addressButton;
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
          employerAddressLine1Field,
          employerAddressLine2Field,
          employerSuburbField,
          employerCityField,
          employerPostcodeField
        },
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {});
    setAddressDetails();
  }

  private void setAddressDetails() {
    CustomerEmployer employer = customer.getEmployer();
    employer.setLineOne(employerAddressLine1Field.getText());
    employer.setLineTwo(employerAddressLine2Field.getText());
    employer.setSuburb(employerSuburbField.getText());
    employer.setCity(employerCityField.getText());
    employer.setPostCode(employerPostcodeField.getText());

    employer.setCountry("New Zealand");
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
