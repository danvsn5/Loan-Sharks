package uoa.lavs.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;

public class LoanPrimaryDetails extends AbstractLoanController implements AccessTypeObserver {
  @FXML private TextField borrowerIDField;
  @FXML private TextField principalField;
  @FXML private TextField interestRateField;
  @FXML private ComboBox<String> rateTypeBox;

  @FXML
  private void initialize() {
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
        AppState.getLoanDetailsAccessType(),
        editButton,
        new TextField[] {principalField, interestRateField},
        new ComboBox<?>[] {
          rateTypeBox,
        },
        new DatePicker[] {},
        new RadioButton[] {});
  }

  // validates data for primary loan details
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

  // if the data is valid, set the details
  @Override
  protected void setLoanDetails() {
    if (!validateData()) {
      return;
    }
    personalLoan.setPrincipal(Double.parseDouble(principalField.getText()));
    personalLoan.setRate(Double.parseDouble(interestRateField.getText()));
    personalLoan.setRateType(rateTypeBox.getValue());
  }

  @Override
  public Button getButton() {
    return primaryButton;
  }
}
