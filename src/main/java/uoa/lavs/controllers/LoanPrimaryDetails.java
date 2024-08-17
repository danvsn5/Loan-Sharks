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

  @FXML private Button coborrowerButton;
  @FXML private Button durationButton;
  @FXML private Button financeButton;
  @FXML private Button summaryButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Button editButton;

  @FXML
  private void initialize() {
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessTypeLoan(
        AppState.loanDetailsAccessType,
        editButton,
        new TextField[] {borrowerIDField, principalField, interestRateField},
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {});
    setPrimaryDetails();
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
    setPrimaryDetails();
    Main.setUi(AppUI.LC_COBORROWER);
  }

  @FXML
  private void handleDurationButtonAction() {
    setPrimaryDetails();
    Main.setUi(AppUI.LC_DURATION);
  }

  @FXML
  private void handleFinanceButtonAction() {
    setPrimaryDetails();
    Main.setUi(AppUI.LC_FINANCE);
  }

  @FXML
  private void handleSummaryButtonAction() {
    setPrimaryDetails();
    Main.setUi(AppUI.LC_SUMMARY);
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }
}
