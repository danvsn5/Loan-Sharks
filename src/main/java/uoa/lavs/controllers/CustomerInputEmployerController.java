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

public class CustomerInputEmployerController implements AccessTypeObserver {
  @FXML private TextField employerNameField;
  @FXML private TextField employerEmailField;
  @FXML private TextField employerWebsiteField;
  @FXML private TextField employerPhoneField;
  @FXML private RadioButton customerIsEmployerCheckbox;

  @FXML private Button detailsButton;
  @FXML private Button addressButton;
  @FXML private Button contactButton;
  @FXML private Button employerAddressButton;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  @FXML
  private void initialize() {
    AccessTypeNotifier.registerObserver(this);
    updateUIBasedOnAccessType();
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.customerDetailsAccessType,
        editButton,
        new TextField[] {
          employerNameField, employerEmailField, employerWebsiteField, employerPhoneField
        },
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {customerIsEmployerCheckbox});

    System.out.println("Contact Controller Invoked");
  }

  private void setEmployerDetails() {
    CustomerEmployer employer = customer.getEmployer();
    employer.setEmployerName(employerNameField.getText());
    employer.setEmployerEmail(employerEmailField.getText());
    employer.setEmployerWebsite(employerWebsiteField.getText());
    employer.setEmployerPhone(employerPhoneField.getText());
    employer.setOwnerOfCompany(customerIsEmployerCheckbox.isSelected());
  }

  @FXML
  private void handleDetailsButtonAction() {
    setEmployerDetails();
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleAddressButtonAction() {
    setEmployerDetails();
    Main.setUi(AppUI.CI_PRIMARY_ADDRESS);
  }

  @FXML
  private void handleContactButtonAction() {
    setEmployerDetails();
    Main.setUi(AppUI.CI_CONTACT);
  }

  @FXML
  private void handleEmployerAddressButtonAction() {
    setEmployerDetails();
    Main.setUi(AppUI.CI_EMPLOYER_ADDRESS);
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      setEmployerDetails();
      AppState.customerDetailsAccessType = "VIEW";
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
    } else if (AppState.customerDetailsAccessType.equals("EDIT")) {
      setEmployerDetails();
      AppState.customerDetailsAccessType = "VIEW";
    }
    AccessTypeNotifier.notifyObservers();
    updateUIBasedOnAccessType();
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_MENU);
  }
}
