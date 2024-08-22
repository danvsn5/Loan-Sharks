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
import uoa.lavs.customer.Email;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.sql_to_mainframe.CustomerCreationHelper;

public class CustomerInputContactController implements AccessTypeObserver {
  @FXML private TextField customerEmailTextField;
  @FXML private TextField customerPhoneNumberOne;

  @FXML private TextField customerPhonePrefixField;
  @FXML private ComboBox<String> customerPhoneTypeBox;
  @FXML private RadioButton sendTextRadio;
  @FXML private RadioButton phonePrimaryRadio;
  @FXML private RadioButton emailPrimaryRadio;

  @FXML private ImageView incPhone;
  @FXML private ImageView incEmail;
  @FXML private ImageView decPhone;
  @FXML private ImageView decEmail;
  @FXML private Label emailPageLabel;
  @FXML private Label phonePageLabel;

  @FXML private TextField customerPreferredContactBox;
  @FXML private TextField customerAltContactBox;

  @FXML private Button customerDetailsButton;
  @FXML private Button customerAddressButton;
  @FXML private Button customerEmployerButton;
  @FXML private Button customerContactButton;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Label idBanner;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  private ArrayList<Phone> existingCustomerPhones = customer.getPhones();
  private int currentNumberPage = 0;
  private int amountOfValidNumbers = 0;

  private ArrayList<Email> existingCustomerEmails = customer.getEmails();
  private int currentEmailPage = 0;
  private int amountOfValidEmails = 0;

  private boolean isPrimaryEmailSet = false;
  private boolean isPrimaryPhoneSet = false;

  @FXML
  private void initialize() {
    customerPhoneTypeBox.getItems().addAll("Home", "Work", "Mobile");
    AccessTypeNotifier.registerCustomerObserver(this);
    updateUIBasedOnAccessType();
    phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
    emailPageLabel.setText("Email: " + (currentEmailPage + 1));

    if (AppState.isAccessingFromSearch) {
      IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
      customer = IndividualCustomerSingleton.getInstance();

      existingCustomerPhones = customer.getPhones();
      existingCustomerEmails = customer.getEmails();
      customerPhoneTypeBox.setValue(customer.getPhones().get(0).getType());
      customerPhonePrefixField.setText(customer.getPhones().get(0).getPrefix());
      customerPhoneNumberOne.setText(customer.getPhones().get(0).getPhoneNumber());
      sendTextRadio.setSelected(customer.getPhones().get(0).getCanSendText());
      phonePrimaryRadio.setSelected(customer.getPhones().get(0).getIsPrimary());

      customerEmailTextField.setText(customer.getEmails().get(0).getEmailAddress());
      emailPrimaryRadio.setSelected(customer.getEmails().get(0).getIsPrimary());
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
          customerEmailTextField,
          customerPhoneNumberOne,
          customerPreferredContactBox,
          customerAltContactBox,
          customerPhonePrefixField
        },
        new ComboBox<?>[] {customerPhoneTypeBox},
        new DatePicker[] {},
        new RadioButton[] {sendTextRadio, phonePrimaryRadio, emailPrimaryRadio});
  }

  @FXML
  private void handleEditButtonAction() throws IOException {
    if (AppState.customerDetailsAccessType.equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      AppState.customerDetailsAccessType = "VIEW";

      setContactDetails();
      CustomerCreationHelper.createCustomer(customer);

      AccessTypeNotifier.notifyCustomerObservers();
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
      AccessTypeNotifier.notifyCustomerObservers();
    } else if (AppState.customerDetailsAccessType.equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {
      AppState.customerDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyCustomerObservers();
      setContactDetails();
    }
  }

  @Override
  public boolean validateData() {
    // Type is fine
    // Prefix 10 chars, numbers and + char only
    // Number 20 chars, number and dash only
    // Primary and send text need to be checked that there is a selection and that
    // no 2 numbers have
    // it
    // Email 60 chars, email format
    // primary email needs to be checked that there in one set somewhere and no 2
    // are set

    boolean isValid = true;
    customerPhonePrefixField.setStyle("");
    customerPhoneNumberOne.setStyle("");
    customerEmailTextField.setStyle("");
    customerPhoneTypeBox.setStyle("");
    phonePrimaryRadio.setStyle("");
    emailPrimaryRadio.setStyle("");

    if (customerPhoneTypeBox.getValue() == null) {
      customerPhoneTypeBox.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerPhonePrefixField.getText().isEmpty()) {
      customerPhonePrefixField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerPhonePrefixField.getText().length() > 10
        || !customerPhonePrefixField.getText().matches("[0-9\\+]+")) {
      customerPhonePrefixField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (customerPhoneNumberOne.getText().isEmpty()
        || customerPhoneNumberOne.getText().length() > 20
        || !customerPhoneNumberOne.getText().matches("[0-9\\-]+")) {
      customerPhoneNumberOne.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    // Regex taken from
    // https://stackoverflow.com/questions/50330109/simple-regex-pattern-for-email
    if (customerEmailTextField.getText().isEmpty()
        || customerEmailTextField.getText().length() > 60
        || !customerEmailTextField.getText().matches("^[^@]+@[^@]+\\.[^@]+$")) {
      customerEmailTextField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (!isPrimaryPhoneSet) {
      phonePrimaryRadio.setStyle(
          "-fx-border-color: red; -fx-border-radius: 20px; -fx-border-width: 3px;");
      isValid = false;
    }

    if (!isPrimaryEmailSet) {
      emailPrimaryRadio.setStyle(
          "-fx-border-color: red; -fx-border-radius: 20px; -fx-border-width: 3px;");
      isValid = false;
    }

    if (!isValid) {
      return false;
    }

    return true;
  }

  private boolean setContactDetails() {

    if (!validateData()) {
      return false;
    }
    // create a new phone number
    Phone newPhone =
        new Phone(
            customer.getCustomerId(),
            customerPhoneTypeBox.getValue(),
            customerPhonePrefixField.getText(),
            customerPhoneNumberOne.getText(),
            phonePrimaryRadio.isSelected(),
            sendTextRadio.isSelected());

    // if the current number page is the same as the amount of valid numbers
    existingCustomerPhones.set(currentNumberPage, newPhone);
    customer.setPhones(existingCustomerPhones);

    // create a new email
    Email newEmail =
        new Email(
            customer.getCustomerId(),
            customerEmailTextField.getText(),
            emailPrimaryRadio.isSelected());

    // if the current email page is the same as the amount of valid emails
    existingCustomerEmails.set(currentEmailPage, newEmail);
    customer.setEmails(existingCustomerEmails);

    return true;
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
  private void phonePrimaryRadioClick() {
    if (phonePrimaryRadio.isSelected()) {
      for (Phone phone : existingCustomerPhones) {
        phone.setIsPrimary(false);
      }
      existingCustomerPhones.get(currentNumberPage).setIsPrimary(true);
      isPrimaryPhoneSet = true;
    } else {
      isPrimaryPhoneSet = false;
    }
  }

  @FXML
  private void emailPrimaryRadioClick() {
    if (emailPrimaryRadio.isSelected()) {
      for (Email email : existingCustomerEmails) {
        email.setIsPrimary(false);
      }
      existingCustomerEmails.get(currentEmailPage).setIsPrimary(true);
      isPrimaryEmailSet = true;
    } else {
      isPrimaryEmailSet = false;
    }
  }

  @FXML
  private void sendTypeBox() {
    if (!customerPhoneTypeBox.getValue().equals("Mobile")) {
      sendTextRadio.setSelected(false);
      sendTextRadio.setDisable(true);
    } else {
      sendTextRadio.setDisable(false);
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
  private void handleIncPhone() {

    if (AppState.customerDetailsAccessType == "VIEW") {
      currentNumberPage++;
      phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));

      customerPhoneNumberOne.setText(
          existingCustomerPhones.get(currentNumberPage).getPhoneNumber());
      customerPhonePrefixField.setText(existingCustomerPhones.get(currentNumberPage).getPrefix());
      customerPhoneTypeBox.setValue(existingCustomerPhones.get(currentNumberPage).getType());
      sendTextRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getCanSendText());
      phonePrimaryRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getIsPrimary());
    }
    if (AppState.customerDetailsAccessType == "CREATE") {

      setContactDetails();
      currentNumberPage++;
      phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      amountOfValidNumbers++;
      // set all the fields to empty

      if (currentNumberPage == amountOfValidNumbers) {
        customerPhoneNumberOne.setText("");
        customerPhonePrefixField.setText("");
        customerPhoneTypeBox.setValue("");
        sendTextRadio.setSelected(false);
        phonePrimaryRadio.setSelected(false);
      } else {
        customerPhoneNumberOne.setText(
            existingCustomerPhones.get(currentNumberPage).getPhoneNumber());
        customerPhonePrefixField.setText(existingCustomerPhones.get(currentNumberPage).getPrefix());
        customerPhoneTypeBox.setValue(existingCustomerPhones.get(currentNumberPage).getType());
        sendTextRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getCanSendText());
        phonePrimaryRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getIsPrimary());
      }
    }

    if (AppState.customerDetailsAccessType == "EDIT") {

      // get the current fields and replace the current number page with the phone
      // fields
      Phone newPhone =
          new Phone(
              customer.getCustomerId(),
              customerPhoneTypeBox.getValue(),
              customerPhonePrefixField.getText(),
              customerPhoneNumberOne.getText(),
              phonePrimaryRadio.isSelected(),
              sendTextRadio.isSelected());

      existingCustomerPhones.set(currentNumberPage, newPhone);

      currentNumberPage++;
      phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));

      // set all the fields to the next phone number
      customerPhoneNumberOne.setText(
          existingCustomerPhones.get(currentNumberPage).getPhoneNumber());
      customerPhonePrefixField.setText(existingCustomerPhones.get(currentNumberPage).getPrefix());
      customerPhoneTypeBox.setValue(existingCustomerPhones.get(currentNumberPage).getType());
      sendTextRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getCanSendText());
      phonePrimaryRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getIsPrimary());
    }
  }

  @FXML
  private void handleDecPhone() {
    if (AppState.customerDetailsAccessType == "VIEW" && currentNumberPage != 0) {
      currentNumberPage--;
      phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));

      customerPhoneNumberOne.setText(
          existingCustomerPhones.get(currentNumberPage).getPhoneNumber());
      customerPhonePrefixField.setText(existingCustomerPhones.get(currentNumberPage).getPrefix());
      customerPhoneTypeBox.setValue(existingCustomerPhones.get(currentNumberPage).getType());
      sendTextRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getCanSendText());
      phonePrimaryRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getIsPrimary());
    }

    if (AppState.customerDetailsAccessType == "CREATE") {
      setContactDetails();

      if (currentNumberPage != 0) {
        currentNumberPage--;
        phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      }

      // set all the fields to the previous phone number
      customerPhoneNumberOne.setText(
          existingCustomerPhones.get(currentNumberPage).getPhoneNumber());
      customerPhonePrefixField.setText(existingCustomerPhones.get(currentNumberPage).getPrefix());
      customerPhoneTypeBox.setValue(existingCustomerPhones.get(currentNumberPage).getType());
      sendTextRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getCanSendText());
      phonePrimaryRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getIsPrimary());
    }

    if (AppState.customerDetailsAccessType == "EDIT") {
      // get the current fields and replace the current number page with the phone
      // fields
      Phone newPhone =
          new Phone(
              customer.getCustomerId(),
              customerPhoneTypeBox.getValue(),
              customerPhonePrefixField.getText(),
              customerPhoneNumberOne.getText(),
              phonePrimaryRadio.isSelected(),
              sendTextRadio.isSelected());

      existingCustomerPhones.set(currentNumberPage, newPhone);

      if (currentNumberPage != 0) {
        currentNumberPage--;
        phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      }

      // set all the fields to the previous phone number
      customerPhoneNumberOne.setText(
          existingCustomerPhones.get(currentNumberPage).getPhoneNumber());
      customerPhonePrefixField.setText(existingCustomerPhones.get(currentNumberPage).getPrefix());
      customerPhoneTypeBox.setValue(existingCustomerPhones.get(currentNumberPage).getType());
      sendTextRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getCanSendText());
      phonePrimaryRadio.setSelected(existingCustomerPhones.get(currentNumberPage).getIsPrimary());
    }
  }

  @FXML
  private void handleIncEmail() {
    if (AppState.customerDetailsAccessType == "VIEW") {

      currentEmailPage++;
      emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      customerEmailTextField.setText(
          existingCustomerEmails.get(currentEmailPage).getEmailAddress());
      emailPrimaryRadio.setSelected(existingCustomerEmails.get(currentEmailPage).getIsPrimary());
    }

    if (AppState.customerDetailsAccessType == "CREATE") {
      setContactDetails();
      currentEmailPage++;
      emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      amountOfValidEmails++;
      // set all the fields to empty
      if (currentEmailPage == amountOfValidEmails) {
        customerEmailTextField.setText("");
        emailPrimaryRadio.setSelected(false);
      } else {
        customerEmailTextField.setText(
            existingCustomerEmails.get(currentEmailPage).getEmailAddress());
        emailPrimaryRadio.setSelected(existingCustomerEmails.get(currentEmailPage).getIsPrimary());
      }
    }

    if (AppState.customerDetailsAccessType == "EDIT") {
      setContactDetails();

      currentEmailPage++;
      emailPageLabel.setText("Email: " + (currentEmailPage + 1));

      // if the current email page is the same as the amount of valid emails
      if (currentEmailPage == amountOfValidEmails) {
        currentEmailPage++;
        amountOfValidEmails++;
      }

      // set all the fields to the next email
      customerEmailTextField.setText(
          existingCustomerEmails.get(currentEmailPage).getEmailAddress());
      emailPrimaryRadio.setSelected(existingCustomerEmails.get(currentEmailPage).getIsPrimary());
    }
  }

  @FXML
  private void handleDecEmail() {
    if (AppState.customerDetailsAccessType == "VIEW" && currentEmailPage != 0) {
      currentEmailPage--;
      emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      customerEmailTextField.setText(
          existingCustomerEmails.get(currentEmailPage).getEmailAddress());
      emailPrimaryRadio.setSelected(existingCustomerEmails.get(currentEmailPage).getIsPrimary());
    }

    if (AppState.customerDetailsAccessType == "CREATE") {
      // get the current fields and replace the current email page with the email
      // fields
      Email newEmail =
          new Email(
              customer.getCustomerId(),
              customerEmailTextField.getText(),
              emailPrimaryRadio.isSelected());

      existingCustomerEmails.set(currentEmailPage, newEmail);
      setContactDetails();

      if (currentEmailPage != 0) {
        currentEmailPage--;
        emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      }

      // set all the fields to the previous email
      customerEmailTextField.setText(
          existingCustomerEmails.get(currentEmailPage).getEmailAddress());
      emailPrimaryRadio.setSelected(existingCustomerEmails.get(currentEmailPage).getIsPrimary());
    }

    if (AppState.customerDetailsAccessType == "EDIT") {
      // get the current fields and replace the current email page with the email
      // fields
      Email newEmail =
          new Email(
              customer.getCustomerId(),
              customerEmailTextField.getText(),
              emailPrimaryRadio.isSelected());

      existingCustomerEmails.set(currentEmailPage, newEmail);

      if (currentEmailPage != 0) {
        currentEmailPage--;
        emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      }

      // set all the fields to the previous email
      customerEmailTextField.setText(
          existingCustomerEmails.get(currentEmailPage).getEmailAddress());
      emailPrimaryRadio.setSelected(existingCustomerEmails.get(currentEmailPage).getIsPrimary());
    }
  }
}
