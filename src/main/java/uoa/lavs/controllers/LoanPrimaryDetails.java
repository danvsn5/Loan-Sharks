package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.ControllerHelper;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class LoanPrimaryDetails implements AccessTypeObserver {
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

  @FXML
  private void initialize() {
    // TODO intialise the borrowerIDField
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();
    rateTypeBox.getItems().addAll("Floating", "Fixed");
    borrowerIDField.setDisable(true);
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
    setPrimaryDetails();
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
    // TODO Auto-generated method stub
  }

  // Add methods for all buttons
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
    // Need to add logic if they got here from loan search
    if (AppState.isAccessingFromLoanSearch) {
      AppState.isAccessingFromLoanSearch = false;
      Main.setUi(AppUI.LOAN_RESULTS);
    } else {
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    }
  }
}
