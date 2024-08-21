package uoa.lavs.controllers;

import java.util.ArrayList;
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
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.loan.PersonalLoanSingleton;

public class LoanCoborrower implements AccessTypeObserver {
  @FXML private TextField coborrowerIDField1;
  @FXML private TextField coborrowerIDField2;
  @FXML private TextField coborrowerIDField3;

  @FXML private Button primaryButton;
  @FXML private Button durationButton;
  @FXML private Button financeButton;
  @FXML private Button summaryButton;
  @FXML private ImageView staticReturnImageView;

  @FXML private Button editButton;

  PersonalLoan personalLoan = PersonalLoanSingleton.getInstance();

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
        new TextField[] {coborrowerIDField1, coborrowerIDField2, coborrowerIDField3},
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {});
  }

  // Add methods for all buttons

  @Override
  public boolean validateData() {
    // Add validation code here
    boolean isValid = true;

    coborrowerIDField1.setStyle("");
    coborrowerIDField2.setStyle("");
    coborrowerIDField3.setStyle("");

    // TODO: Add validation code here (Jamie or Chul). Need to also show Name

    return true;
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.loanDetailsAccessType.equals("CREATE")
        && AccessTypeNotifier.validateLoanObservers()) {
      AppState.loanDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyLoanObservers();
    } else if (AppState.loanDetailsAccessType.equals("VIEW")) {
      AppState.loanDetailsAccessType = "EDIT";
      AccessTypeNotifier.notifyLoanObservers();
    } else if (AppState.loanDetailsAccessType.equals("EDIT")
        && AccessTypeNotifier.validateLoanObservers()) {
      AppState.loanDetailsAccessType = "VIEW";
      AccessTypeNotifier.notifyLoanObservers();
    }
  }

  private void setCoborrowerDetails() {
    if (!validateData()) {
      return;
    }

    ArrayList<String> ids = personalLoan.getCoborrowerIds();
    ids.set(0, coborrowerIDField1.getText());
    ids.set(1, coborrowerIDField2.getText());
    ids.set(2, coborrowerIDField3.getText());
  }

  @FXML
  private void handlePrimaryButtonAction() {
    setCoborrowerDetails();
    Main.setUi(AppUI.LC_PRIMARY);
  }

  @FXML
  private void handleDurationButtonAction() {
    setCoborrowerDetails();
    Main.setUi(AppUI.LC_DURATION);
  }

  @FXML
  private void handleFinanceButtonAction() {
    setCoborrowerDetails();
    Main.setUi(AppUI.LC_FINANCE);
  }

  @FXML
  private void handleSummaryButtonAction() {
    setCoborrowerDetails();
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
