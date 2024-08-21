package uoa.lavs.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

public class LoanDurationController implements AccessTypeObserver {
  @FXML private DatePicker startDatePicker;
  @FXML private TextField periodField;
  @FXML private TextField termField;

  @FXML private Button primaryButton;
  @FXML private Button coborrowerButton;
  @FXML private Button financeButton;
  @FXML private Button summaryButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Button editButton;

  @FXML
  private void initialize() {
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();

    DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    startDatePicker.setConverter(
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
  }

  // Add methods for all buttons

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessTypeLoan(
        AppState.loanDetailsAccessType,
        editButton,
        new TextField[] {periodField, termField},
        new ComboBox<?>[] {},
        new DatePicker[] {startDatePicker},
        new RadioButton[] {});
    setDurationDetails();
  }

  @Override
  public boolean validateData() {
    // Add validation code here
    boolean isValid = true;

    startDatePicker.setStyle("");
    periodField.setStyle("");

    if (startDatePicker.getValue() == null) {
      startDatePicker.setStyle("-fx-border-color: red");
      isValid = false;
    }

    if (periodField.getText().isEmpty()
        || !periodField.getText().matches("[0-9]+")
        || periodField.getText().length() > 5) {
      periodField.setStyle("-fx-border-color: red");
      isValid = false;
    }

    return isValid;
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.loanDetailsAccessType.equals("CREATE")
        && AccessTypeNotifier.validateLoanObservers()) {
      AppState.loanDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyLoanObservers();
    } else if (AppState.loanDetailsAccessType.equals("VIEW")) {
      AppState.loanDetailsAccessType = "EDIT";
      AccessTypeNotifier.notifyLoanObservers();
    } else if (AppState.loanDetailsAccessType.equals("EDIT")
        && AccessTypeNotifier.validateLoanObservers()) {
      AppState.loanDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyLoanObservers();
    }
  }

  private void setDurationDetails() {
    // TODO Auto-generated method stub
  }

  @FXML
  private void handlePrimaryButtonAction() {
    setDurationDetails();
    Main.setUi(AppUI.LC_PRIMARY);
  }

  @FXML
  private void handleCoborrowerButtonAction() {
    setDurationDetails();
    Main.setUi(AppUI.LC_COBORROWER);
  }

  @FXML
  private void handleFinanceButtonAction() {
    setDurationDetails();
    Main.setUi(AppUI.LC_FINANCE);
  }

  @FXML
  private void handleSummaryButtonAction() {
    setDurationDetails();
    Main.setUi(AppUI.LC_SUMMARY);
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }
}
