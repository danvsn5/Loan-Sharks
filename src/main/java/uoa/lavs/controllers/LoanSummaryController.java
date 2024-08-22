package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.loan.LoanDuration;
import uoa.lavs.loan.LoanPayment;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.loan.PersonalLoanSingleton;

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
    // Add initialization code here
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();
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
    LoanDuration duration = personalLoan.getDuration();
    LoanPayment payment = personalLoan.getPayment();

    principalfField.setText(Double.toString(personalLoan.getPrincipal()));
    annualRateField.setText(Double.toString(personalLoan.getRate()));
    termField.setText(Integer.toString(duration.getLoanTerm()));
    paymentFrequencyField.setText(payment.getPaymentFrequency());
    paymentValueField.setText(payment.getPaymentAmount());
    // culmuativeInterestField.setText(payment.)
    // culumutiveLoanCostField;
    // payOffDateField;
  }

  @FXML
  private void handleViewPaymentsButtonAction() {
    // Add action code here
  }

  @FXML
  private void handlePrimaryButtonAction() {
    Main.setUi(AppUI.LC_PRIMARY);
  }

  @FXML
  private void handleCoborrowerButtonAction() {
    Main.setUi(AppUI.LC_COBORROWER);
  }

  @FXML
  private void handleDurationButtonAction() {
    Main.setUi(AppUI.LC_DURATION);
  }

  @FXML
  private void handleFinanceButtonAction() {
    Main.setUi(AppUI.LC_FINANCE);
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
