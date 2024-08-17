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

public class LoanDurationController implements AccessTypeObserver {
  @FXML private TextField startDateField;
  @FXML private TextField periodField;
  @FXML private TextField termField;

  @FXML private Button primaryButton;
  @FXML private Button coborrowerButton;
  @FXML private Button financeButton;
  @FXML private Button summaryButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Button editButton;

  @FXML
  private void initialize() {
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();
  }

  // Add methods for all buttons

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessTypeLoan(
        AppState.loanDetailsAccessType,
        editButton,
        new TextField[] {startDateField, periodField, termField},
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {});
    setDurationDetails();
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

  private void setDurationDetails() {
    // TODO Auto-generated method stub
  }

  @FXML
  private void handlePrimaryButtonAction() {
    setDurationDetails();
    Main.setUi(AppUI.LC_PRIMARY);
  }

  @FXML
  private void handleCoborrowerButtonAction() {
    setDurationDetails();
    Main.setUi(AppUI.LC_COBORROWER);
  }

  @FXML
  private void handleFinanceButtonAction() {
    setDurationDetails();
    Main.setUi(AppUI.LC_FINANCE);
  }

  @FXML
  private void handleSummaryButtonAction() {
    setDurationDetails();
    Main.setUi(AppUI.LC_SUMMARY);
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }
}
