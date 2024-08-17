package uoa.lavs.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.ControllerHelper;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;

public class CustomerInputDetailsController implements AccessTypeObserver {
  @FXML private Label customerIDLabel;

  @FXML private ComboBox<String> customerTitleComboBox;
  @FXML private TextField customerFirstNameField;
  @FXML private TextField customerMiddleNameField;
  @FXML private TextField customerLastNameField;
  @FXML private DatePicker customerDOBPicker;
  @FXML private TextField customerOccupationField;
  @FXML private ComboBox<String> customerCitizenshipBox;

  @FXML private Button notesButton;
  @FXML private Button addressButton;
  @FXML private Button contactButton;
  @FXML private Button employerButton;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  Random random = new Random();

  @FXML
  private void initialize() {
    DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    customerDOBPicker.setConverter(
        new StringConverter<LocalDate>() {
          @Override
          public String toString(LocalDate date) {
            if (date != null) {
              return displayFormatter.format(date);
            } else {
              return "";
            }
          }

          @Override
          public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
              LocalDate date = LocalDate.parse(string, displayFormatter);
              return LocalDate.parse(date.format(storageFormatter), storageFormatter);
            } else {
              return null;
            }
          }
        });
    AccessTypeNotifier.registerObserver(this);
    updateUIBasedOnAccessType();
  }

  @Override
  @FXML
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessType(
        AppState.customerDetailsAccessType,
        editButton,
        new TextField[] {
          customerFirstNameField,
          customerMiddleNameField,
          customerLastNameField,
          customerOccupationField
        },
        new ComboBox<?>[] {customerTitleComboBox, customerCitizenshipBox},
        new DatePicker[] {customerDOBPicker},
        new RadioButton[] {});
    System.out.println("Details Controller Invoked");
  }

  private void setCustomerDetails() {
    customer.setTitle(customerTitleComboBox.getValue());

    customer.setName(
        customerFirstNameField.getText()
            + " "
            + customerMiddleNameField.getText()
            + " "
            + customerLastNameField.getText());
    customer.setDateOfBirth(customerDOBPicker.getValue());
    customer.setOccupation(customerOccupationField.getText());
    customer.setResidency(customerCitizenshipBox.getValue());
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      // Handle create customer logic
      setCustomerDetails();
      // Save customer to database or perform necessary actions
      AppState.customerDetailsAccessType = "VIEW";
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      // Switch to edit mode
      AppState.customerDetailsAccessType = "EDIT";
    } else if (AppState.customerDetailsAccessType.equals("EDIT")) {
      // Handle confirm changes logic
      setCustomerDetails();
      // Save changes to database or perform necessary actions
      AppState.customerDetailsAccessType = "VIEW";
    }
    AccessTypeNotifier.notifyObservers();
    updateUIBasedOnAccessType();
  }

  @FXML
  private void handleNotesButtonAction() {
    setCustomerDetails();
    Main.setUi(AppUI.CI_NOTES);
  }

  @FXML
  private void handleAddressButtonAction() {
    setCustomerDetails();
    Main.setUi(AppUI.CI_PRIMARY_ADDRESS);
  }

  @FXML
  private void handleContactButtonAction() {
    setCustomerDetails();
    Main.setUi(AppUI.CI_CONTACT);
  }

  @FXML
  private void handleEmployerButtonAction() {
    setCustomerDetails();
    Main.setUi(AppUI.CI_EMPLOYER);
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_MENU);
  }
}
