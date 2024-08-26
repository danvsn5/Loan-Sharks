package uoa.lavs.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import uoa.lavs.backend.oop.loan.LoanPayment;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;

public class LoanFinanceDetails extends AbstractLoanController implements AccessTypeObserver {
  @FXML private ComboBox<String> compoundingBox;
  @FXML private ComboBox<String> paymentFrequencyBox;
  @FXML private TextField paymentValueField;
  @FXML private RadioButton interestOnlyButton;

  @FXML
  private void initialize() {
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();
    compoundingBox.getItems().addAll("Weekly", "Monthly", "Annually");
    paymentFrequencyBox.getItems().addAll("Weekly", "Fortnightly", "Monthly");

    personalLoan.setCustomerId(AppState.getSelectedCustomer().getCustomerId());
  }

  // updates the UI
  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessTypeLoan(
        AppState.getLoanDetailsAccessType(),
        editButton,
        new TextField[] {paymentValueField},
        new ComboBox<?>[] {
          compoundingBox, paymentFrequencyBox,
        },
        new DatePicker[] {},
        new RadioButton[] {interestOnlyButton});
  }

  // checks if all the inputs are valid
  @Override
  public boolean validateData() {
    // Add validation code here
    boolean isValid = true;

    paymentValueField.setStyle("");
    compoundingBox.setStyle("");
    paymentFrequencyBox.setStyle("");

    if (paymentValueField.getText().isEmpty()
        || !paymentValueField.getText().matches("^\\d+(\\.\\d+)?$")) {
      paymentValueField.setStyle("-fx-border-color: red");
      isValid = false;
    }

    if (compoundingBox.getValue() == null) {
      compoundingBox.setStyle("-fx-border-color: red");
      isValid = false;
    }

    if (paymentFrequencyBox.getValue() == null) {
      paymentFrequencyBox.setStyle("-fx-border-color: red");
      isValid = false;
    }

    return isValid;
  }

  // if the details are valid, then set the details
  @Override
  protected void setLoanDetails() {
    if (!validateData()) {
      return;
    }

    LoanPayment loanPayment =
        new LoanPayment(
            personalLoan.getLoanId(),
            compoundingBox.getValue(),
            paymentFrequencyBox.getValue(),
            paymentValueField.getText(),
            interestOnlyButton.isSelected());
    personalLoan.setPayment(loanPayment);
  }

  @Override
  public Button getButton() {
    return financeButton;
  }
}
