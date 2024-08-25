package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserverLoan;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.Instance;
import uoa.lavs.legacy.mainframe.LoanStatus;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanSummary;
import uoa.lavs.legacy.mainframe.messages.loan.UpdateLoanStatus;

public class LoanSummaryController implements AccessTypeObserverLoan {
  @FXML private TextField principalfField;
  @FXML private TextField annualRateField;
  @FXML private TextField termField;
  @FXML private TextField paymentFrequencyField;
  @FXML private TextField paymentValueField;
  @FXML private TextField culmuativeInterestField;
  @FXML private TextField culumutiveLoanCostField;
  @FXML private TextField payOffDateField;

  @FXML private Button confirmLoanButton;
  @FXML private Button viewPaymentsButton;

  @FXML private Button primaryButton;
  @FXML private Button coborrowerButton;
  @FXML private Button durationButton;
  @FXML private Button financeButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Label loanStatusLabel;
  @FXML private TextField loanStatusField;

  PersonalLoan personalLoan = PersonalLoanSingleton.getInstance();

  @FXML
  private void initialize() {
    System.out.println("Loan Summary Controller Initialized");
    // Add initialization code here
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();
    if (AppState.isOnLoanSummary) {
      setSummaryDetails();
      // TODO summary becomes null when you go back, change something, and then forwards to summary
      // details and it doesn't update in the mainframe because there are no new rows in the sql db,
      // although they have been updated
    }
  }

  @FXML
  private void handleConfirmLoanButtonAction() {
    confirmLoanButton.setStyle("");
    if (AccessTypeNotifier.validateLoanObservers()
        && !AppState.loanDetailsAccessType.equals("VIEW")) {
      AppState.isCreatingLoan = false;
      AppState.loanDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyLoanObservers();
      confirmLoanButton.setText("Edit Details");

    } else if (!AppState.loanDetailsAccessType.equals("VIEW")) {
      confirmLoanButton.setStyle("-fx-border-color: red");
    } else if (AppState.loanDetailsAccessType.equals("VIEW")) {
      AppState.isCreatingLoan = false;
      AppState.loanDetailsAccessType = "EDIT";
      confirmLoanButton.setText("Confirm Loan");
      AccessTypeNotifier.notifyLoanObservers();
    }
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
    AppState.isOnLoanSummary = false;
    Main.setUi(AppUI.LC_PRIMARY);
  }

  @FXML
  private void handleCoborrowerButtonAction() {
    AppState.isOnLoanSummary = false;
    Main.setUi(AppUI.LC_COBORROWER);
  }

  @FXML
  private void handleDurationButtonAction() {
    AppState.isOnLoanSummary = false;
    Main.setUi(AppUI.LC_DURATION);
  }

  @FXML
  private void handleFinanceButtonAction() {
    AppState.isOnLoanSummary = false;
    Main.setUi(AppUI.LC_FINANCE);
  }

  @FXML
  private void handleBackButtonAction() {
    // Need to add logic if they got here from loan search
    AppState.isOnLoanSummary = false;
    if (AppState.isAccessingFromLoanSearch) {
      AppState.isAccessingFromLoanSearch = false;
      Main.setUi(AppUI.LOAN_RESULTS);
    } else if (!AppState.isCreatingLoan) {
      Main.setUi(AppUI.LOAN_MENU);
    } else {
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    }
  }

  @Override
  public void updateUIBasedOnAccessType() {
    // Add logic to update UI based on access type
    if (AppState.loanDetailsAccessType.equals("VIEW")) {
      confirmLoanButton.setText("Edit Details");
      viewPaymentsButton.setVisible(true);
      loanStatusField.setVisible(true);
      loanStatusLabel.setVisible(true);
    } else {
      confirmLoanButton.setText("Confirm Loan");
      viewPaymentsButton.setVisible(false);
      loanStatusField.setVisible(false);
      loanStatusLabel.setVisible(false);
    }
  }

  @Override
  public boolean validateData() {
    // No Data to validate
    return true;
  }

  @Override
  public Button getButton() {
    // Fake Button
    Button button = new Button();
    return button;
  }

  @Override
  public void setInvalidButton(String string) {
    // Do nothing
  }
}
