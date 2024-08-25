package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.backend.oop.customer.CustomerEmployer;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.backend.sql.sql_to_mainframe.CustomerCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;

public class CustomerInputEmployerController extends AbstractCustomerController
    implements AccessTypeObserver {
  @FXML private TextField employerNameField;
  @FXML private TextField employerEmailField;
  @FXML private TextField employerWebsiteField;
  @FXML private TextField employerPhoneField;
  @FXML private RadioButton customerIsEmployerCheckbox;

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
    AccessTypeNotifier.registerCustomerObserver(this);
    updateUIBasedOnAccessType();

    // Set dummy values
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")) {
      employerNameField.setText("Fake Company");
      employerEmailField.setText("dada@dada.com");
      employerWebsiteField.setText("www.fake.com");
      employerPhoneField.setText("1234567890");
      customerIsEmployerCheckbox.setSelected(true);
    }

    if (AppState.getIsAccessingFromSearch()) {
      startWithCustomerID();
    }
  }

  @Override
  public boolean validateData() {
    // employer name is 60 characters
    // email is 60 characters and must be email format
    // website is 60 characters and must be a valid URL
    // phone is 30 characters, numbers and dash only, and potentially could start with +
    // Country has to be not null

    boolean isValid = true;
    employerNameField.setStyle("");
    employerEmailField.setStyle("");
    employerWebsiteField.setStyle("");
    employerPhoneField.setStyle("");

    if (employerNameField.getText().isEmpty() || employerNameField.getText().length() > 60) {
      employerNameField.setStyle("-fx-border-color: red");
      isValid = false;
    }

    // Regex taken from https://stackoverflow.com/questions/50330109/simple-regex-pattern-for-email
    if (employerEmailField.getText().isEmpty()
        || employerEmailField.getText().length() > 60
        || !employerEmailField.getText().matches("^[^@]+@[^@]+\\.[^@]+$")) {
      employerEmailField.setStyle("-fx-border-color: red");
      isValid = false;
    }

    // Regex taken from
    // https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url
    if (employerWebsiteField.getText().isEmpty()
        || employerWebsiteField.getText().length() > 60
        || !employerWebsiteField
            .getText()
            .matches(
                "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")) {
      employerWebsiteField.setStyle("-fx-border-color: red");
      isValid = false;
    }

    if (employerPhoneField.getText().isEmpty()
        || employerPhoneField.getText().length() > 30
        || !employerPhoneField.getText().matches("^\\+?[0-9\\-]{1,30}$")) {
      employerPhoneField.setStyle("-fx-border-color: red");
      isValid = false;
    }

    if (!isValid) {
      System.out.println("Employer details are invalid");
      return false;
    }

    System.out.println("Employer details are valid");
    return true;
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.getCustomerDetailsAccessType(),
        editButton,
        idBanner,
        new TextField[] {
          employerNameField, employerEmailField, employerWebsiteField, employerPhoneField
        },
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {customerIsEmployerCheckbox});
  }

  @Override
  protected void setDetails() {

    if (!validateData()) {
      return;
    }

    CustomerEmployer employer = customer.getEmployer();
    employer.setEmployerName(employerNameField.getText());
    employer.setEmployerEmail(employerEmailField.getText());
    employer.setEmployerWebsite(employerWebsiteField.getText());
    employer.setEmployerPhone(employerPhoneField.getText());
    employer.setOwnerOfCompany(customerIsEmployerCheckbox.isSelected());
  }

  @FXML
  private void handleEditButtonAction() throws IOException {
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      setDetails();

      boolean customerIsValid = CustomerCreationHelper.validateCustomer(customer);
      if (!customerIsValid) {
        System.out.println("Customer is not valid and thus will not be created");
        editButton.setStyle("-fx-border-color: red");
        return;
      }

      AppState.setCustomerDetailsAccessType("VIEW");
      CustomerCreationHelper.createCustomer(customer, false);
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
    } else if (AppState.getCustomerDetailsAccessType().equals("VIEW")) {
      AppState.setCustomerDetailsAccessType("EDIT");
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
    } else if (AppState.getCustomerDetailsAccessType().equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {
      AppState.setCustomerDetailsAccessType("VIEW");
      AccessTypeNotifier.notifyCustomerObservers();
      updateUIBasedOnAccessType();
      setDetails();
      CustomerCreationHelper.createCustomer(customer, true);
    }
  }

  @Override
  public Button getButton() {
    return customerEmployerButton;
  }

  @Override
  protected void startWithCustomerID() {
    IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
    customer = IndividualCustomerSingleton.getInstance();

    employerNameField.setText(customer.getEmployer().getEmployerName());
    employerEmailField.setText(customer.getEmployer().getEmployerEmail());
    employerWebsiteField.setText(customer.getEmployer().getEmployerWebsite());
    employerPhoneField.setText(customer.getEmployer().getEmployerPhone());
    customerIsEmployerCheckbox.setSelected(customer.getEmployer().getOwnerOfCompany());
  }

  @FXML
  @Override
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
