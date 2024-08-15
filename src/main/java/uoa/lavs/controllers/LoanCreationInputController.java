package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoanCreationInputController {
  @FXML private Label customerIdLabel;
  @FXML private TextField loanPrincipalField;
  @FXML private TextField loanRateField;
  @FXML private DatePicker loanStartDateField;
  @FXML private TextField loanPeriodField;
  @FXML private TextField loanTermField;
  @FXML private ComboBox<String> loanCompoundingComboBox;
  @FXML private ComboBox<String> loanPaymentFrequencyComboBox;
  @FXML private Label paymentAmountsLabel; // This is calculated after inputs
  @FXML private CheckBox loanInterestOnlyCheckBox;

  @FXML private Button createLoanButton;
  @FXML private Button backButton;
  @FXML private Button exitButton;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleBackButtonAction() {
    // Add back button action code here
  }

  @FXML
  private void handleExitButtonAction() {
    // Add exit button action code here
  }

  @FXML
  private void handleCreateLoanButtonAction() {
    // Add create loan button action code here
  }
}
