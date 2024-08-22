package uoa.lavs.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.loan.PersonalLoanSingleton;
import uoa.lavs.mainframe.messages.loan.LoadLoanSummary;

public class LoanSummaryController implements AccessTypeObserver {
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
    if (AccessTypeNotifier.validateLoanObservers()) {
      AppState.isCreatingLoan = false;
      AppState.loanDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyLoanObservers();

    } else {
      confirmLoanButton.setStyle("-fx-border-color: red");
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
}
