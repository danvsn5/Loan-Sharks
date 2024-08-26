package uoa.lavs.frontend.controllers;

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
import uoa.lavs.backend.oop.customer.Email;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.backend.oop.customer.Phone;
import uoa.lavs.backend.sql.sql_to_mainframe.CustomerCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;

public class CustomerInputContactController extends AbstractCustomerController
    implements AccessTypeObserver {
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

    if (AppState.getIsAccessingFromSearch()) {
      startWithCustomerID();
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
  protected void startWithCustomerID() {
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

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.getCustomerDetailsAccessType(),
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
  @Override
  protected void handleEditButtonAction() throws IOException {
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
      AccessTypeNotifier.notifyCustomerObservers();
      CustomerCreationHelper.createCustomer(customer, false);

    } else if (AppState.getCustomerDetailsAccessType().equals("VIEW")) {

      AppState.setCustomerDetailsAccessType("EDIT");
      AccessTypeNotifier.notifyCustomerObservers();

    } else if (AppState.getCustomerDetailsAccessType().equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {

      setEmailDetails("confirm");
      setPhoneDetails("confirm");

      AppState.setCustomerDetailsAccessType("VIEW");
      AccessTypeNotifier.notifyCustomerObservers();
      CustomerCreationHelper.createCustomer(customer, true);
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

    boolean isPhoneValid = true;
    boolean isEmailValid = true;
    customerPhonePrefixField.setStyle("");
    customerPhoneNumberOne.setStyle("");
    customerEmailTextField.setStyle("");
    customerPhoneTypeBox.setStyle("");
    phonePrimaryRadio.setStyle("");
    emailPrimaryRadio.setStyle("");

    isPhoneValid = validatePhone();
    isEmailValid = validateEmail();

    if (!isPhoneValid || !isEmailValid) return false;

    System.out.println("Phone and email are valid");
    return true;
  }

  // helper method for validating phone details; used in inc/dec and validating
  // final inputs
  private boolean validatePhone() {

    boolean isValid = true;

    if (customerPhoneTypeBox.getValue() == "" || customerPhoneTypeBox.getValue() == null) {
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

    if (!isPrimaryPhoneSet) {
      phonePrimaryRadio.setStyle(
          "-fx-border-color: red; -fx-border-radius: 20px; -fx-border-width: 3px;");
      isValid = false;
    }

    return isValid;
  }

  // helper method for validating email details; used in inc/dec and validating
  // final inputs
  private boolean validateEmail() {

    boolean isValid = true;

    // Regex taken from
    // https://stackoverflow.com/questions/50330109/simple-regex-pattern-for-email
    if (customerEmailTextField.getText().isEmpty()
        || customerEmailTextField.getText().length() > 60
        || !customerEmailTextField.getText().matches("^[^@]+@[^@]+\\.[^@]+$")) {
      customerEmailTextField.setStyle("-fx-border-color: red;");
      isValid = false;
    }

    if (!isPrimaryEmailSet) {
      emailPrimaryRadio.setStyle(
          "-fx-border-color: red; -fx-border-radius: 20px; -fx-border-width: 3px;");
      isValid = false;
    }

    return isValid;
  }

  @Override
  protected void setDetails() {

    if (!validateData()) {
      return;
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
  }

  // sets the phone details for each user if they are valid
  private boolean setPhoneDetails(String location) {

    if (location != "dec") {
      if (!validatePhone()) {
        return false;
      }
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

    if (currentNumberPage == existingCustomerPhones.size()) {
      if (validatePhone()) {

        existingCustomerPhones.add(newPhone);
      }
    } else {
      existingCustomerPhones.set(currentNumberPage, newPhone);
    }

    for (int i = 0; i < existingCustomerPhones.size(); i++) {
      existingCustomerPhones.get(i).setPhoneId(i + 1);
    }

    customer.setPhones(existingCustomerPhones);

    return true;
  }

  // sets the email details for each user if they are valid
  private boolean setEmailDetails(String location) {

    if (location != "dec") {
      if (!validateEmail()) {
        return false;
      }
    }

    // create a new email
    Email newEmail =
        new Email(
            customer.getCustomerId(),
            customerEmailTextField.getText(),
            emailPrimaryRadio.isSelected());

    // sets the details for the current email page for the existing emails;
    if (currentEmailPage == existingCustomerEmails.size()) {
      if (validateEmail()) {
        existingCustomerEmails.add(newEmail);
      }
    } else {
      existingCustomerEmails.set(currentEmailPage, newEmail);
    }

    for (int i = 0; i < existingCustomerEmails.size(); i++) {
      existingCustomerEmails.get(i).setEmailId(i + 1);
    }

    customer.setEmails(existingCustomerEmails);
    return true;
  }

  // select radio buttons such that if they were previously selected, their input
  // is overruled and correctly reassigns the variable
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

  // below methods handle incrementing and decrementing pages for emails and phones

  @FXML
  private void handleIncPhone() {

    if (AppState.getCustomerDetailsAccessType() == "VIEW") {

      if (currentNumberPage < existingCustomerPhones.size() - 1) {

        currentNumberPage++;
        phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));

        setPhoneDetailsUI("value");
      }
    }

    if (AppState.getCustomerDetailsAccessType() == "CREATE") {

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

    if (AppState.getCustomerDetailsAccessType() == "EDIT") {

      if (!setPhoneDetails("incEdit")) {
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

    if (AppState.getCustomerDetailsAccessType() == "VIEW" && currentNumberPage != 0) {
      currentNumberPage--;
      phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      setPhoneDetailsUI("value");
    }

    if (AppState.getCustomerDetailsAccessType() == "CREATE") {

      // if page is decrementing, skip validation but continue to add the fields
      setPhoneDetails("dec");

      if (currentNumberPage != 0) {
        currentNumberPage--;
        phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      }

      // set all the fields to the previous phone number
      setPhoneDetailsUI("value");

      if (validatePhone()) {
        customerPhonePrefixField.setStyle("");
        customerPhoneNumberOne.setStyle("");
        customerPhoneTypeBox.setStyle("");
        phonePrimaryRadio.setStyle("");
      }
    }

    if (AppState.getCustomerDetailsAccessType() == "EDIT") {

      // if page is decrementing, skip validation but continue to add the fields
      setPhoneDetails("decEdit");
      if (currentNumberPage != 0) {
        currentNumberPage--;
        phonePageLabel.setText("Phone Number: " + (currentNumberPage + 1));
      }

      // set all the fields to the previous phone number
      setPhoneDetailsUI("value");

      if (validatePhone()) {
        customerPhonePrefixField.setStyle("");
        customerPhoneNumberOne.setStyle("");
        customerPhoneTypeBox.setStyle("");
        phonePrimaryRadio.setStyle("");
      }
    }
  }

  @FXML
  private void handleIncEmail() {

    if (AppState.getCustomerDetailsAccessType() == "VIEW") {

      if (currentEmailPage < existingCustomerEmails.size() - 1) {
        currentEmailPage++;
        emailPageLabel.setText("Email: " + (currentEmailPage + 1));
        setEmailDetailsUI("value");
      }
    }

    if (AppState.getCustomerDetailsAccessType() == "CREATE") {

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

    if (AppState.getCustomerDetailsAccessType() == "EDIT") {

      if (!setEmailDetails("incEdit")) {
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

    if (AppState.getCustomerDetailsAccessType() == "VIEW" && currentEmailPage != 0) {
      currentEmailPage--;
      emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      setEmailDetailsUI("value");
    }

    if (AppState.getCustomerDetailsAccessType() == "CREATE") {

      // if page is decrementing, skip validation but continue to add the fields
      setEmailDetails("dec");

      if (currentEmailPage != 0) {
        currentEmailPage--;
        emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      }

      // set all the fields to the previous email
      setEmailDetailsUI("value");

      if (validateEmail()) {
        customerEmailTextField.setStyle("");
        emailPrimaryRadio.setStyle("");
      }
    }

    if (AppState.getCustomerDetailsAccessType() == "EDIT") {

      // if page is decrementing, skip validation but continue to add the fields
      setEmailDetails("decEdit");

      if (currentEmailPage != 0) {
        currentEmailPage--;
        emailPageLabel.setText("Email: " + (currentEmailPage + 1));
      }

      setEmailDetailsUI("value");

      if (validateEmail()) {
        customerEmailTextField.setStyle("");
        emailPrimaryRadio.setStyle("");
      }
    }
  }

  @Override
  public Button getButton() {
    return customerContactButton;
  }

  // helper methods to set the UI for filling out fields based on pages
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
      customerEmailTextField.setText(
          existingCustomerEmails.get(currentEmailPage).getEmailAddress());
      emailPrimaryRadio.setSelected(existingCustomerEmails.get(currentEmailPage).getIsPrimary());
    }
  }
}
