package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.CustomerEmployer;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;
import uoa.lavs.utility.CustomerCreationHelper;

public class CustomerInputEmployerController {
  @FXML private TextField employerNameField;
  @FXML private ComboBox<String> employerCountryField;
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
    // Add initialization code here
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
    // Add edit button action code here
    if (AppState.customerDetailsAccessType == "CREATE") {
      // send customer to sql database
      setEmployerDetails();
      CustomerCreationHelper.createCustomer(customer);
    }
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_MENU);
  }
}
