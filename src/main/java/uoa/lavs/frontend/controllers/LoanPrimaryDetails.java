package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.backend.sql.sql_to_mainframe.LoanCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserverLoan;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanSummary;

public class LoanPrimaryDetails implements AccessTypeObserverLoan {
  @FXML private TextField borrowerIDField;
  @FXML private TextField principalField;
  @FXML private TextField interestRateField;
  @FXML private ComboBox<String> rateTypeBox;

  @FXML private Button coborrowerButton;
  @FXML private Button durationButton;
  @FXML private Button financeButton;
  @FXML private Button primaryButton;

  @FXML private Button summaryButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Button editButton;

  private PersonalLoan personalLoan = PersonalLoanSingleton.getInstance();

  @FXML
  private void initialize() {
    // TODO intialise the borrowerIDField
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();
    rateTypeBox.getItems().addAll("Floating", "Fixed");
    borrowerIDField.setDisable(true);

    personalLoan.setCustomerId(AppState.getSelectedCustomer().getCustomerId());
    borrowerIDField.setText(personalLoan.getCustomerId());

    // Set dummy values
    if (AppState.loanDetailsAccessType.equals("CREATE")) {
      principalField.setText("100");
      interestRateField.setText("10");
      rateTypeBox.setValue("Floating");
    }
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessTypeLoan(
        AppState.loanDetailsAccessType,
        editButton,
        new TextField[] {borrowerIDField, principalField, interestRateField},
        new ComboBox<?>[] {
          rateTypeBox,
        },
        new DatePicker[] {},
        new RadioButton[] {});
    // TODO set primary details onto the screen if not create type.
    // setPrimaryDetails();

  }

  @Override
  public boolean validateData() {
    // Add validation code here
    boolean isValid = true;

    principalField.setStyle("");
    interestRateField.setStyle("");
    rateTypeBox.setStyle("");

    if (principalField.getText().isEmpty()
        || !principalField.getText().matches("^\\d+(\\.\\d+)?$")
        || principalField.getText().length() > 15) {
      principalField.setStyle("-fx-border-color: red");
      isValid = false;
    }

    if (interestRateField.getText().isEmpty()
        || !interestRateField.getText().matches("^\\d+(\\.\\d+)?$")
        || interestRateField.getText().length() > 5) {
      interestRateField.setStyle("-fx-border-color: red");
      isValid = false;
    }

    if (rateTypeBox.getValue() == null) {
      rateTypeBox.setStyle("-fx-border-color: red");
      isValid = false;
    }

    return isValid;
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.loanDetailsAccessType.equals("CREATE")) {
      AppState.loanDetailsAccessType = "VIEW";
    } else if (AppState.loanDetailsAccessType.equals("VIEW")) {
      AppState.loanDetailsAccessType = "EDIT";
    } else if (AppState.loanDetailsAccessType.equals("EDIT")) {
      AppState.loanDetailsAccessType = "VIEW";
    }
    AccessTypeNotifier.notifyLoanObservers();
    updateUIBasedOnAccessType();
  }

  private void setPrimaryDetails() {
    if (!validateData()) {
      return;
    }
    // need to set the text field of primary borrower id in the gui

    personalLoan.setPrincipal(Double.parseDouble(principalField.getText()));
    personalLoan.setRate(Double.parseDouble(interestRateField.getText()));
    personalLoan.setRateType(rateTypeBox.getValue());
  }

  // Add methods for all buttons
  @FXML
  private void handleCoborrowerButtonAction() {
    setPrimaryDetails();
    Main.setUi(AppUI.LC_COBORROWER);
  }

  @FXML
  private void handleDurationButtonAction() {
    setPrimaryDetails();
    Main.setUi(AppUI.LC_DURATION);
  }

  @FXML
  private void handleFinanceButtonAction() {
    setPrimaryDetails();
    Main.setUi(AppUI.LC_FINANCE);
  }

  @FXML
  private void handleSummaryButtonAction() throws IOException {

    setPrimaryDetails();
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

      AppState.loadLoanSummary(AppState.loanDetailsAccessType);
    }
  }

  @FXML
  private void handleBackButtonAction() {
    if (AppState.isAccessingFromLoanSearch) {
      AppState.isAccessingFromLoanSearch = false;
      Main.setUi(AppUI.LOAN_RESULTS);
    } else if (AppState.isCreatingLoan) {
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    } else {
      Main.setUi(AppUI.LC_SUMMARY);
    }
  }

  @Override
  public Button getButton() {
    return primaryButton;
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
