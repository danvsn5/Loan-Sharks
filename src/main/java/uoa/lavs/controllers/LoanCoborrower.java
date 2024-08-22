package uoa.lavs.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserverLoan;
import uoa.lavs.AppState;
import uoa.lavs.ControllerHelper;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.SearchCustomer;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.loan.PersonalLoanSingleton;
import uoa.lavs.mainframe.messages.loan.LoadLoanSummary;
import uoa.lavs.sql.sql_to_mainframe.LoanCreationHelper;

public class LoanCoborrower implements AccessTypeObserverLoan {
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

    personalLoan.setCustomerId(AppState.getSelectedCustomer().getCustomerId());
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
    // TODO set coborrower details onto the screen if not create type.
    // setCoborrowerDetails();
  }

  // Add methods for all buttons

  @Override
  public boolean validateData() {
    boolean isValid = true;

    // Searches if mainframe/localdb has these ids and sets the style of the textfield accordingly
    SearchCustomer searchCustomer = new SearchCustomer();
    if (!coborrowerIDField1.getText().isEmpty()) {
      if (!coborrowerIDField1.getText().equals(personalLoan.getCustomerId())
          && searchCustomer.searchCustomerById(coborrowerIDField1.getText()) != null) {
        coborrowerIDField1.setStyle("");
      } else {
        coborrowerIDField1.setStyle("-fx-border-color: red");
        isValid = false;
      }
    }
    if (!coborrowerIDField2.getText().isEmpty()) {
      if (!coborrowerIDField2.getText().equals(personalLoan.getCustomerId())
          && searchCustomer.searchCustomerById(coborrowerIDField2.getText()) != null) {
        coborrowerIDField2.setStyle("");
      } else {
        coborrowerIDField2.setStyle("-fx-border-color: red");
        isValid = false;
      }
    }
    if (!coborrowerIDField3.getText().isEmpty()) {
      if (!coborrowerIDField3.getText().equals(personalLoan.getCustomerId())
          && searchCustomer.searchCustomerById(coborrowerIDField3.getText()) != null) {
        coborrowerIDField3.setStyle("");
      } else {
        coborrowerIDField3.setStyle("-fx-border-color: red");
        isValid = false;
      }
    }

    return isValid;
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
    if (!coborrowerIDField1.getText().isEmpty()) {
      ids.set(0, coborrowerIDField1.getText());
    }
    if (!coborrowerIDField2.getText().isEmpty()) {
      ids.set(1, coborrowerIDField2.getText());
    }
    if (!coborrowerIDField3.getText().isEmpty()) {
      ids.set(2, coborrowerIDField3.getText());
    }
  }

  @FXML
  private void handlePrimaryButtonAction() {
    if (!validateData()) {
      return;
    }
    setCoborrowerDetails();
    Main.setUi(AppUI.LC_PRIMARY);
  }

  @FXML
  private void handleDurationButtonAction() {
    if (!validateData()) {
      return;
    }
    setCoborrowerDetails();
    Main.setUi(AppUI.LC_DURATION);
  }

  @FXML
  private void handleFinanceButtonAction() {
    if (!validateData()) {
      return;
    }
    setCoborrowerDetails();
    Main.setUi(AppUI.LC_FINANCE);
  }

  @FXML
  private void handleSummaryButtonAction() throws IOException {
    if (!validateData()) {
      return;
    }
    setCoborrowerDetails();
    LoanCreationHelper.createLoan(personalLoan);
    LoanCreationHelper.getLoanSummary(personalLoan);
    LoadLoanSummary loadLoanSummary = LoanCreationHelper.getLoanSummary(personalLoan);
    AppState.setCurrentLoanSummary(loadLoanSummary);

    AppState.loadLoanSummary(AppState.loanDetailsAccessType);
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
