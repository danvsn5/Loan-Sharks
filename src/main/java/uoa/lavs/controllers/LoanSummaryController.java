package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class LoanSummaryController {
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
  @FXML private Button summaryButton;
  @FXML private ImageView staticReturnImageView;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleConfirmLoanButtonAction() {
    // Add action code here
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
  private void handleSummaryButtonAction() {
    Main.setUi(AppUI.LC_SUMMARY);
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }
}
