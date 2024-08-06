package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class LoanDetailsController {
  @FXML private Label loanIDLabel;
  @FXML private Label loanStatusLabel;
  @FXML private Label loanCustomerNamelLabel;
  @FXML private Label loanCustomerIDLabel;
  @FXML private Label startDateLabel;
  @FXML private Label coborrowersLabel;
  @FXML private Label loanTermLabel;
  @FXML private Label loanCompoundingLabel;
  @FXML private Label loanPrincipalLabel;
  @FXML private Label loanFrequencyLabel;
  @FXML private Label paymentAmountLabel;
  @FXML private CheckBox interestOnlyCheckBox;

  @FXML
  private void initialize() {
    // Add initialization code here
  }
}
