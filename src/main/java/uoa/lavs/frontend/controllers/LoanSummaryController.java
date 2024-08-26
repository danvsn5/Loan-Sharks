package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.Instance;
import uoa.lavs.legacy.mainframe.LoanStatus;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanSummary;
import uoa.lavs.legacy.mainframe.messages.loan.UpdateLoanStatus;

public class LoanSummaryController implements AccessTypeObserver {
  @FXML
  private TextField principalfField;
  @FXML
  private TextField annualRateField;
  @FXML
  private TextField termField;
  @FXML
  private TextField paymentFrequencyField;
  @FXML
  private TextField paymentValueField;
  @FXML
  private TextField culmuativeInterestField;
  @FXML
  private TextField culumutiveLoanCostField;
  @FXML
  private TextField payOffDateField;

  @FXML
  private Button confirmLoanButton;
  @FXML
  private Button viewPaymentsButton;

  @FXML
  private Button primaryButton;
  @FXML
  private Button coborrowerButton;
  @FXML
  private Button durationButton;
  @FXML
  private Button financeButton;
  @FXML
  private ImageView staticReturnImageView;

  PersonalLoan personalLoan = PersonalLoanSingleton.getInstance();

  @FXML
  private void initialize() {
    System.out.println("Loan Summary Controller Initialized");
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();

    // Set all fields disabled
    principalfField.setDisable(true);
    annualRateField.setDisable(true);
    termField.setDisable(true);
    paymentFrequencyField.setDisable(true);
    paymentValueField.setDisable(true);
    culmuativeInterestField.setDisable(true);
    culumutiveLoanCostField.setDisable(true);
    payOffDateField.setDisable(true);

    if (AppState.getIsOnLoanSummary()) {
      setSummaryDetails();
    }
  }

  // checks if the loan details are valid and confirms the loan based on what mode
  // is set
  @FXML
  private void handleConfirmLoanButtonAction() throws IOException {
    confirmLoanButton.setStyle("");

    if (isCreatingLoan()) {
      handleCreatingLoan();
    } else if (isViewingLoan()) {
      handleViewingLoan();
    } else if (isEditingLoan()) {
      handleEditingLoan();
    } else {
      handleInvalidState();
    }
  }

  // creates the loan summary and sets all the details in the GUI fields
  private boolean isCreatingLoan() {
    return AccessTypeNotifier.validateLoanObservers()
        && AppState.getLoanDetailsAccessType().equals("CREATE");
  }

  private boolean isViewingLoan() {
    return AppState.getLoanDetailsAccessType().equals("VIEW");
  }

  private boolean isEditingLoan() {
    return AppState.getLoanDetailsAccessType().equals("EDIT")
        && AccessTypeNotifier.validateLoanObservers();
  }

  private void handleCreatingLoan() throws IOException {
    AppState.setIsCreatingLoan(false);
    AppState.setLoanDetailsAccessType("VIEW");
    AccessTypeNotifier.notifyLoanObservers();
    confirmLoanButton.setText("Edit Details");
    handleViewPaymentsButtonAction();
  }

  private void handleViewingLoan() {
    AppState.setIsCreatingLoan(false);
    AppState.setLoanDetailsAccessType("EDIT");
    confirmLoanButton.setText("Confirm Loan");
    AccessTypeNotifier.notifyLoanObservers();
  }

  private void handleEditingLoan() {
    confirmLoanButton.setText("Edit Details");
    AppState.setLoanDetailsAccessType("VIEW");
    AccessTypeNotifier.notifyLoanObservers();
  }

  private void handleInvalidState() {
    confirmLoanButton.setStyle("-fx-border-color: red");
  }

  private void setSummaryDetails() {
    LoadLoanSummary summary = AppState.getCurrentLoanSummary();
    System.out.println("summar is null: " + (summary == null));

    principalfField.setText(Double.toString(summary.getPrincipalFromServer()));
    annualRateField.setText(Double.toString(summary.getRateValueFromServer()));
    termField.setText(Integer.toString(summary.getTermFromServer()));
    paymentFrequencyField.setText((summary.getPaymentFrequencyFromServer()).toString());
    paymentValueField.setText(Double.toString(summary.getPaymentAmountFromServer()));
    culmuativeInterestField.setText(Double.toString(summary.getTotalInterestFromServer()));
    culumutiveLoanCostField.setText(Double.toString(summary.getTotalLoanCostFromServer()));
    System.out.println((summary.getPayoffDateFromServer()).toString());
    payOffDateField.setText((summary.getPayoffDateFromServer()).toString());
  }

  // creates a connection to the mainframe to help convert the summary details
  // into payment details and sets the UI to loan repayments
  @FXML
  private void handleViewPaymentsButtonAction() throws IOException {
    uoa.lavs.legacy.mainframe.Connection connection = Instance.getConnection();
    UpdateLoanStatus updateLoanStatus = new UpdateLoanStatus();
    updateLoanStatus.setLoanId(personalLoan.getLoanId());
    updateLoanStatus.setStatus(LoanStatus.Pending);
    Status status = updateLoanStatus.send(connection);
    if (status.getErrorCode() == 0) {
      System.out.println("Loan status updated successfully.");
    } else {
      System.out.println(
          "Error updating loan status: " + status.getErrorCode() + status.getErrorMessage());
    }
    AppState.loanLoanRepayments();
  }

  @FXML
  private void handlePrimaryButtonAction() {
    AppState.setIsOnLoanSummary(false);
    Main.setUi(AppUI.LC_PRIMARY);
  }

  @FXML
  private void handleCoborrowerButtonAction() {
    AppState.setIsOnLoanSummary(false);
    Main.setUi(AppUI.LC_COBORROWER);
  }

  @FXML
  private void handleDurationButtonAction() {
    AppState.setIsOnLoanSummary(false);
    Main.setUi(AppUI.LC_DURATION);
  }

  @FXML
  private void handleFinanceButtonAction() {
    AppState.setIsOnLoanSummary(false);
    Main.setUi(AppUI.LC_FINANCE);
  }

  @FXML
  private void handleBackButtonAction() {
    // Need to add logic if they got here from loan search
    AppState.setIsOnLoanSummary(false);
    if (!AppState.getIsCreatingLoan()) {
      Main.setUi(AppUI.LOAN_MENU);
    } else {
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    }
  }

  @Override
  public void updateUIBasedOnAccessType() {
    // Add logic to update UI based on access type
    if (AppState.getLoanDetailsAccessType().equals("VIEW")) {
      confirmLoanButton.setText("Edit Details");
      viewPaymentsButton.setVisible(true);
    } else {
      confirmLoanButton.setText("Confirm Loan");
      viewPaymentsButton.setVisible(false);
    }
  }

  @Override
  public boolean validateData() {
    // No Data to validate
    return true;
  }

  @Override
  public Button getButton() {
    // Do nothing
    Button button = new Button();
    return button;
  }

  @Override
  public void setInvalidButton(String string) {
    // Do nothing
  }
}
