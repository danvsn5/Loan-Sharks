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

public class LoanFinanceDetails implements AccessTypeObserver {
  @FXML private ComboBox<String> compoundingBox;
  @FXML private ComboBox<String> paymentFrequencyBox;
  @FXML private TextField paymentValueField;
  @FXML private RadioButton interestOnlyButton;

  @FXML private Button primaryButton;
  @FXML private Button coborrowerButton;
  @FXML private Button durationButton;
  @FXML private Button summaryButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Button editButton;

  @FXML
  private void initialize() {
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();
    compoundingBox.getItems().addAll("Weekly", "Monthly", "Annually");
    paymentFrequencyBox.getItems().addAll("Weekly", "Fortnightly", "Monthly");
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessTypeLoan(
        AppState.loanDetailsAccessType,
        editButton,
        new TextField[] {paymentValueField},
        new ComboBox<?>[] {
          compoundingBox, paymentFrequencyBox,
        },
        new DatePicker[] {},
        new RadioButton[] {interestOnlyButton});
    setFinanceDetails();
  }

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

  private void setFinanceDetails() {
    // TODO Auto-generated method stub
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
  private void handleSummaryButtonAction() {
    Main.setUi(AppUI.LC_SUMMARY);
  }

  @FXML
  private void handleBackButtonAction() {
    if (AppState.isAccessingFromLoanSearch) {
      AppState.isAccessingFromLoanSearch = false;
      Main.setUi(AppUI.LOAN_RESULTS);
    } else {
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    }
  }
}
