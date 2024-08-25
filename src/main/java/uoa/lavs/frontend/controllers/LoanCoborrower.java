package uoa.lavs.frontend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.customer.SearchCustomer;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.backend.sql.sql_to_mainframe.LoanCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Instance;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanSummary;

public class LoanCoborrower implements AccessTypeObserver {
  @FXML private TextField coborrowerIDField1;
  @FXML private TextField coborrowerIDField2;
  @FXML private TextField coborrowerIDField3;

  @FXML private Button coborrowerButton;
  @FXML private Button durationButton;
  @FXML private Button financeButton;
  @FXML private Button primaryButton;

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
        AppState.getLoanDetailsAccessType(),
        editButton,
        new TextField[] {coborrowerIDField1, coborrowerIDField2, coborrowerIDField3},
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {});
  }

  // Add methods for all buttons

  @Override
  public boolean validateData() {
    boolean isValid = true;

    // Searches if mainframe/localdb has these ids and sets the style of the textfield accordingly
    SearchCustomer searchCustomer = new SearchCustomer();
    Connection connection = Instance.getConnection();

    if (!coborrowerIDField1.getText().isEmpty()) {
      if (!coborrowerIDField1.getText().equals(personalLoan.getCustomerId())
          && searchCustomer.searchCustomerById(coborrowerIDField1.getText(), connection) != null) {
        coborrowerIDField1.setStyle("");
      } else {
        coborrowerIDField1.setStyle("-fx-border-color: red");
        isValid = false;
      }
    }
    if (!coborrowerIDField2.getText().isEmpty()) {
      if (!coborrowerIDField2.getText().equals(personalLoan.getCustomerId())
          && searchCustomer.searchCustomerById(coborrowerIDField2.getText(), connection) != null) {
        coborrowerIDField2.setStyle("");
      } else {
        coborrowerIDField2.setStyle("-fx-border-color: red");
        isValid = false;
      }
    }
    if (!coborrowerIDField3.getText().isEmpty()) {
      if (!coborrowerIDField3.getText().equals(personalLoan.getCustomerId())
          && searchCustomer.searchCustomerById(coborrowerIDField3.getText(), connection) != null) {
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
    if (AppState.getLoanDetailsAccessType().equals("CREATE")
        && AccessTypeNotifier.validateLoanObservers()) {
      AppState.setLoanDetailsAccessType("VIEW");
      AccessTypeNotifier.notifyLoanObservers();
    } else if (AppState.getLoanDetailsAccessType().equals("VIEW")) {
      AppState.setLoanDetailsAccessType("EDIT");
      AccessTypeNotifier.notifyLoanObservers();
    } else if (AppState.getLoanDetailsAccessType().equals("EDIT")
        && AccessTypeNotifier.validateLoanObservers()) {
      AppState.setLoanDetailsAccessType("VIEW");
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

    setCoborrowerDetails();
    if (AccessTypeNotifier.validateLoanObservers()) {
      boolean loanIsValid = LoanCreationHelper.validateLoan(personalLoan);
      if (!loanIsValid) {
        System.out.println("Loan is not valid and thus will not be created");
        summaryButton.setStyle("-fx-border-color: red");
        return;
      }
      LoanCreationHelper.createLoan(personalLoan);
      LoanCreationHelper.getLoanSummary(personalLoan);
      LoadLoanSummary loadLoanSummary = LoanCreationHelper.getLoanSummary(personalLoan);
      AppState.setCurrentLoanSummary(loadLoanSummary);

      AppState.loadLoanSummary(AppState.getLoanDetailsAccessType());
    }
  }

  @FXML
  private void handleBackButtonAction() {
    if (AppState.getIsCreatingLoan()) {
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    } else {
      Main.setUi(AppUI.LC_SUMMARY);
    }
  }

  @Override
  public Button getButton() {
    return coborrowerButton;
  }

  @FXML
  public void setInvalidButton(String style) {
    Button currentButton = AppState.getCurrentButton();

    String buttonId = currentButton.getId();

    if (buttonId != null) {
      if (buttonId.equals("coborrowerButton")) {
        coborrowerButton.setStyle(style);
      } else if (buttonId.equals("durationButton")) {
        durationButton.setStyle(style);
      } else if (buttonId.equals("financeButton")) {
        financeButton.setStyle(style);
      } else if (buttonId.equals("primaryButton")) {
        primaryButton.setStyle(style);
      } else if (buttonId.equals("summaryButton")) {
        summaryButton.setStyle(style);
      }
    }
  }
}
