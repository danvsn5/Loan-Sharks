package uoa.lavs.controllers;

import java.io.IOException;
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
import uoa.lavs.loan.LoanPayment;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.loan.PersonalLoanSingleton;
import uoa.lavs.mainframe.messages.loan.LoadLoanSummary;
import uoa.lavs.sql.sql_to_mainframe.LoanCreationHelper;

public class LoanFinanceDetails implements AccessTypeObserverLoan {
  @FXML private ComboBox<String> compoundingBox;
  @FXML private ComboBox<String> paymentFrequencyBox;
  @FXML private TextField paymentValueField;
  @FXML private RadioButton interestOnlyButton;

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
    compoundingBox.getItems().addAll("Weekly", "Monthly", "Annually");
    paymentFrequencyBox.getItems().addAll("Weekly", "Fortnightly", "Monthly");

    personalLoan.setCustomerId(AppState.getSelectedCustomer().getCustomerId());
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
    // TODO set finance details onto the screen if not create type.
    // setFinanceDetails();
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

  @FXML
  private void handlePrimaryButtonAction() {
    setFinanceDetails();
    Main.setUi(AppUI.LC_PRIMARY);
  }

  @FXML
  private void handleCoborrowerButtonAction() {
    setFinanceDetails();
    Main.setUi(AppUI.LC_COBORROWER);
  }

  @FXML
  private void handleDurationButtonAction() {
    setFinanceDetails();
    Main.setUi(AppUI.LC_DURATION);
  }

  @FXML
  private void handleSummaryButtonAction() throws IOException {
    AccessTypeNotifier.validateLoanObservers();
    setFinanceDetails();
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

  @Override
  public Button getButton() {
    return financeButton;
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
