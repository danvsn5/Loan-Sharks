package uoa.lavs.frontend.controllers;

import java.io.IOException;
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
import uoa.lavs.Main;
import uoa.lavs.backend.oop.loan.LoanDuration;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.backend.sql.sql_to_mainframe.LoanCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanSummary;

public class LoanDurationController implements AccessTypeObserver {
  @FXML private DatePicker startDatePicker;
  @FXML private TextField periodField;
  @FXML private TextField termField;

  @FXML private Button coborrowerButton;
  @FXML private Button durationButton;
  @FXML private Button financeButton;
  @FXML private Button primaryButton;

  @FXML private Button summaryButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Button editButton;

  PersonalLoan personalLoan = PersonalLoanSingleton.getInstance();

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

    termField.setDisable(true);

    personalLoan.setCustomerId(AppState.getSelectedCustomer().getCustomerId());
    termField.setText("30");

    // Set dummy values
    if (AppState.getLoanDetailsAccessType().equals("CREATE")) {
      startDatePicker.setValue(LocalDate.now());
      periodField.setText("30");
    }
  }

  // Add methods for all buttons

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessTypeLoan(
        AppState.getLoanDetailsAccessType(),
        editButton,
        new TextField[] {periodField, termField},
        new ComboBox<?>[] {},
        new DatePicker[] {startDatePicker},
        new RadioButton[] {});
    // TODO set dration details onto the screen if not create type.
    // setDurationDetails();
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
    if (AppState.getLoanDetailsAccessType().equals("CREATE")
        && AccessTypeNotifier.validateLoanObservers()) {
      AppState.setLoanDetailsAccessType("VIEW");
      AccessTypeNotifier.notifyLoanObservers();
    } else if (AppState.getLoanDetailsAccessType().equals("VIEW")) {
      AppState.setLoanDetailsAccessType("EDIT");
      AccessTypeNotifier.notifyLoanObservers();
    } else if (AppState.getLoanDetailsAccessType().equals("EDIT")
        && AccessTypeNotifier.validateLoanObservers()) {
      AppState.setLoanDetailsAccessType("VIEW");
      AccessTypeNotifier.notifyLoanObservers();
    }
  }

  private void setDurationDetails() {
    if (!validateData()) {
      return;
    }

    LoanDuration loanDuration = personalLoan.getDuration();
    loanDuration.setStartDate(startDatePicker.getValue());
    loanDuration.setPeriod(Integer.parseInt(periodField.getText()));
    loanDuration.setLoanTerm(30);
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
  private void handleSummaryButtonAction() throws IOException {

    setDurationDetails();

    if (AccessTypeNotifier.validateLoanObservers()) {
      boolean loanIsValid = LoanCreationHelper.validateLoan(personalLoan);
      if (!loanIsValid) {
        System.out.println("Loan is not valid and thus will not be created");
        summaryButton.setStyle("-fx-border-color: red");
        return;
      }
      LoanCreationHelper.createLoan(personalLoan);
      LoanCreationHelper.getLoanSummary(personalLoan);
      LoadLoanSummary loadLoanSummary = LoanCreationHelper.getLoanSummary(personalLoan);
      AppState.setCurrentLoanSummary(loadLoanSummary);

      AppState.loadLoanSummary(AppState.getLoanDetailsAccessType());
    }
  }

  @FXML
  private void handleBackButtonAction() {
    if (AppState.getIsCreatingLoan()) {
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    } else {
      Main.setUi(AppUI.LC_SUMMARY);
    }
  }

  @Override
  public Button getButton() {
    return durationButton;
  }

  @FXML
  public void setInvalidButton(String style) {
    Button currentButton = AppState.getCurrentButton();

    String buttonId = currentButton.getId();

    if (buttonId != null) {
      if (buttonId.equals("coborrowerButton")) {
        coborrowerButton.setStyle(style);
      } else if (buttonId.equals("durationButton")) {
        durationButton.setStyle(style);
      } else if (buttonId.equals("financeButton")) {
        financeButton.setStyle(style);
      } else if (buttonId.equals("primaryButton")) {
        primaryButton.setStyle(style);
      } else if (buttonId.equals("summaryButton")) {
        summaryButton.setStyle(style);
      }
    }
  }
}
