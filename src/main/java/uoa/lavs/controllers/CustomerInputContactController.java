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
  @FXML
  private TextField customerEmailTextField;
  @FXML
  private TextField customerPhoneNumberOne;

  @FXML
  private TextField customerPhonePrefixField;
  @FXML
  private ComboBox<String> customerPhoneTypeBox;
  @FXML
  private RadioButton sendTextRadio;
  @FXML
  private RadioButton phonePrimaryRadio;
  @FXML
  private RadioButton emailPrimaryRadio;

  @FXML
  private ImageView incPhone;
  @FXML
  private ImageView incEmail;
  @FXML
  private ImageView decPhone;
  @FXML
  private ImageView decEmail;
  @FXML
  private Label emailPageLabel;
  @FXML
  private Label phonePageLabel;

  @FXML
  private TextField customerPreferredContactBox;
  @FXML
  private TextField customerAltContactBox;

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

    // Set dummy data
    if (AppState.customerDetailsAccessType.equals("CREATE")) {

      customerPhoneTypeBox.setValue("Mobile");
      customerPhonePrefixField.setText("+1");
      customerPhoneNumberOne.setText("1234567890");
      sendTextRadio.setSelected(true);
      phonePrimaryRadio.setSelected(true);
      isPrimaryPhoneSet = true;

      customerEmailTextField.setText("dummy@example.com");
      emailPrimaryRadio.setSelected(true);
      isPrimaryEmailSet = true;

      customerPreferredContactBox.setText("Preferred Contact");
      customerAltContactBox.setText("Alternate Contact");
    }

    if (AppState.isAccessingFromSearch) {
      IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
      customer = IndividualCustomerSingleton.getInstance();

      existingCustomerPhones = customer.getPhones();
      existingCustomerEmails = customer.getEmails();
      if (existingCustomerPhones.size() > 0) {

        customerPhoneTypeBox.setValue(existingCustomerPhones.get(0).getType());
        customerPhonePrefixField.setText(existingCustomerPhones.get(0).getPrefix());
        customerPhoneNumberOne.setText(existingCustomerPhones.get(0).getPhoneNumber());
        sendTextRadio.setSelected(existingCustomerPhones.get(0).getCanSendText());
        phonePrimaryRadio.setSelected(existingCustomerPhones.get(0).getIsPrimary());
        if (phonePrimaryRadio.isSelected()) {
          isPrimaryPhoneSet = true;
        } else {
          isPrimaryPhoneSet = false;
        }
      }

      if (existingCustomerEmails.size() > 0) {
        customerEmailTextField.setText(existingCustomerEmails.get(0).getEmailAddress());
        emailPrimaryRadio.setSelected(existingCustomerEmails.get(0).getIsPrimary());
        if (emailPrimaryRadio.isSelected()) {
          isPrimaryEmailSet = true;
        } else {
          isPrimaryEmailSet = false;
        }
      }
    }

    // iterate through all existing phone numbers and emails; if ANY of of them have
    // a getIspPrimary for phones or emails set to true, set the isPrimaryPhoneSet
    // or isPrimaryEmailSet to true
    for (Phone phone : existingCustomerPhones) {
      if (phone.getIsPrimary()) {
        isPrimaryPhoneSet = true;
      }
    }
    for (Email email : existingCustomerEmails) {
      if (email.getIsPrimary()) {
        isPrimaryEmailSet = true;
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
            customerEmailTextField,
            customerPhoneNumberOne,
            customerPreferredContactBox,
            customerAltContactBox,
            customerPhonePrefixField
        },
        new ComboBox<?>[] { customerPhoneTypeBox },
        new DatePicker[] {},
        new RadioButton[] { sendTextRadio, phonePrimaryRadio, emailPrimaryRadio });
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
    Phone newPhone = new Phone(
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
    Email newEmail = new Email(
        customer.getCustomerId(),
        customerEmailTextField.getText(),
        emailPrimaryRadio.isSelected());

    // if the current email page is the same as the amount of valid emails
    existingCustomerEmails.set(currentEmailPage, newEmail);
    customer.setEmails(existingCustomerEmails);

    return true;
  }

  private boolean setPhoneDetails(String location) {

    if (location != "dec" && location != "decEdit") {
      if (!validateData()) {
        return false;
      }
    }

    // create a new phone number
    Phone newPhone = new Phone(
        customer.getCustomerId(),
        customerPhoneTypeBox.getValue(),
        customerPhonePrefixField.getText(),
        customerPhoneNumberOne.getText(),
        phonePrimaryRadio.isSelected(),
        sendTextRadio.isSelected());

    // sets the details for the current number page for the existing phones;
    if (location != "decEdit") {
      existingCustomerPhones.set(currentNumberPage, newPhone);
    } else {
      existingCustomerPhones.add(newPhone);
    }
    customer.setPhones(existingCustomerPhones);

    return true;
  }

  private boolean setEmailDetails(String location) {

    if (location != "dec" && location != "decEdit") {
      if (!validateData()) {
        return false;
      }
    }

    // create a new email
    Email newEmail = new Email(
        customer.getCustomerId(),
        customerEmailTextField.getText(),
        emailPrimaryRadio.isSelected());

    // sets the details for the current email page for the existing emails;
    if (location != "decEdit") {
      existingCustomerEmails.set(currentEmailPage, newEmail);
    } else {
      existingCustomerEmails.add(newEmail);
    }
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

      if (currentNumberPage < existingCustomerPhones.size() - 1) {

        currentNumberPage++;
        phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));

        setPhoneDetailsUI("value");

      }

    }

    if (AppState.customerDetailsAccessType == "CREATE") {

      if (!setPhoneDetails("inc")) {
        // if a field is invalid, return
        return;
      }
      currentNumberPage++;
      phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      amountOfValidNumbers++;
      // set all the fields to empty

      if (currentNumberPage == amountOfValidNumbers) {
        setPhoneDetailsUI("empty");

      } else {
        setPhoneDetailsUI("value");
      }
    }

    if (AppState.customerDetailsAccessType == "EDIT") {

      if (!setPhoneDetails("inc")) {
        // if a field is invalid, return
        return;
      }

      currentNumberPage++;
      phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));

      if (currentNumberPage == existingCustomerPhones.size()) {

        // set all the fields to the next phone number
        setPhoneDetailsUI("empty");
      } else {
        setPhoneDetailsUI("value");
      }
    }
  }

  @FXML
  private void handleDecPhone() {
    if (AppState.customerDetailsAccessType == "VIEW" && currentNumberPage != 0) {
      currentNumberPage--;
      phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      setPhoneDetailsUI("value");
    }

    if (AppState.customerDetailsAccessType == "CREATE") {

      // if page is decrementing, skip validation but continue to add the fields
      setPhoneDetails("dec");

      if (currentNumberPage != 0) {
        currentNumberPage--;
        phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      }

      // set all the fields to the previous phone number
      setPhoneDetailsUI("value");

      if (validateData()) {
        customerPhonePrefixField.setStyle("");
        customerPhoneNumberOne.setStyle("");
        customerPhoneTypeBox.setStyle("");
        phonePrimaryRadio.setStyle("");
      }
    }

    if (AppState.customerDetailsAccessType == "EDIT") {

      // if page is decrementing, skip validation but continue to add the fields
      setPhoneDetails("decEdit");
      if (currentNumberPage != 0) {
        currentNumberPage--;
        phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      }

      // set all the fields to the previous phone number
      setPhoneDetailsUI("value");

      if (validateData()) {
        customerPhonePrefixField.setStyle("");
        customerPhoneNumberOne.setStyle("");
        customerEmailTextField.setStyle("");
        customerPhoneTypeBox.setStyle("");
        phonePrimaryRadio.setStyle("");
        emailPrimaryRadio.setStyle("");
      }
    }
  }

  @FXML
  private void handleIncEmail() {
    if (AppState.customerDetailsAccessType == "VIEW") {

      if (currentEmailPage < existingCustomerEmails.size() - 1) {
        currentEmailPage++;
        emailPageLabel.setText("Email: " + (currentEmailPage + 1));
        setEmailDetailsUI("value");
      }
    }

    if (AppState.customerDetailsAccessType == "CREATE") {

      if (!setEmailDetails("inc")) {
        // if a field is invalid, return; suggesting to the user that the first email
        // that you enter is the primary email
        return;
      }
      currentEmailPage++;
      emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      amountOfValidEmails++;
      // set all the fields to empty
      if (currentEmailPage == amountOfValidEmails) {
        setEmailDetailsUI("empty");
      } else {
        setEmailDetailsUI("value");
      }
    }

    if (AppState.customerDetailsAccessType == "EDIT") {

      if (!setEmailDetails("inc")) {
        // if a field is invalid, return
        return;
      }

      currentEmailPage++;
      emailPageLabel.setText("Email: " + (currentEmailPage + 1));

      if (currentEmailPage == existingCustomerEmails.size()) {
        // set all the fields to the next email
        setEmailDetailsUI("empty");
      } else {
        setEmailDetailsUI("value");
      }

    }
  }

  @FXML
  private void handleDecEmail() {
    if (AppState.customerDetailsAccessType == "VIEW" && currentEmailPage != 0) {
      currentEmailPage--;
      emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      setEmailDetailsUI("value");
    }

    if (AppState.customerDetailsAccessType == "CREATE") {

      // if page is decrementing, skip validation but continue to add the fields
      setEmailDetails("dec");

      if (currentEmailPage != 0) {
        currentEmailPage--;
        emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      }

      // set all the fields to the previous email
      setEmailDetailsUI("value");

      if (validateData()) {
        customerEmailTextField.setStyle("");
        emailPrimaryRadio.setStyle("");
      }
    }

    if (AppState.customerDetailsAccessType == "EDIT") {
      // get the current fields and replace the current email page with the email
      // fields
      Email newEmail = new Email(
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

  @Override
  public Button getButton() {
    return customerContactButton;
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

  public void setPhoneDetailsUI(String setting) {

    if (setting == "empty") {
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

  public void setEmailDetailsUI(String setting) {

    if (setting == "empty") {
      customerEmailTextField.setText("");
      emailPrimaryRadio.setSelected(false);
    } else {
      customerEmailTextField.setText(existingCustomerEmails.get(currentEmailPage).getEmailAddress());
      emailPrimaryRadio.setSelected(existingCustomerEmails.get(currentEmailPage).getIsPrimary());
    }
  }
}
