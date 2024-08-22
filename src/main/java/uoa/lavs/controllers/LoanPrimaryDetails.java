package uoa.lavs.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserverLoan;
import uoa.lavs.AppState;
import uoa.lavs.ControllerHelper;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.loan.PersonalLoanSingleton;
import uoa.lavs.mainframe.messages.loan.LoadLoanSummary;
import uoa.lavs.sql.sql_to_mainframe.LoanCreationHelper;

public class LoanPrimaryDetails implements AccessTypeObserverLoan {
  @FXML private TextField borrowerIDField;
  @FXML private TextField principalField;
  @FXML private TextField interestRateField;
  @FXML private ComboBox<String> rateTypeBox;

  @FXML private Button coborrowerButton;
  @FXML private Button durationButton;
  @FXML private Button financeButton;
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
    if (!validateData()) {
      return;
    }
    setPrimaryDetails();
    LoanCreationHelper.createLoan(personalLoan);
    LoanCreationHelper.getLoanSummary(personalLoan);
    LoadLoanSummary loadLoanSummary = LoanCreationHelper.getLoanSummary(personalLoan);
    AppState.setCurrentLoanSummary(loadLoanSummary);

    AppState.loadLoanSummary(AppState.loanDetailsAccessType);
  }

  @FXML
  private void handleBackButtonAction() {
    // Need to add logic if they got here from loan search
    if (AppState.isAccessingFromLoanSearch) {
      AppState.isAccessingFromLoanSearch = false;
      Main.setUi(AppUI.LOAN_RESULTS);
    } else {
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    }
  }
}
