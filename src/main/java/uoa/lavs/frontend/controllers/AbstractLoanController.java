package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.backend.sql.sql_to_mainframe.LoanCreationHelper;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanSummary;

public abstract class AbstractLoanController {

  @FXML
  protected Button coborrowerButton;
  @FXML
  protected Button durationButton;
  @FXML
  protected Button financeButton;
  @FXML
  protected Button primaryButton;

  @FXML
  protected Button summaryButton;
  @FXML
  protected ImageView staticReturnImageView;

  @FXML
  protected Button editButton;

  protected PersonalLoan personalLoan = PersonalLoanSingleton.getInstance();

  // sets the details for each tab
  protected abstract void setLoanDetails();

  // validates the data for each tab
  protected abstract boolean validateData();

  // sets the tab to primary
  @FXML
  private void handlePrimaryButtonAction() {
    if (!validateData()) {
      return;
    }
    setLoanDetails();
    Main.setUi(AppUI.LC_PRIMARY);
  }

  // sets the tab to duration
  @FXML
  private void handleDurationButtonAction() {
    if (!validateData()) {
      return;
    }
    setLoanDetails();
    Main.setUi(AppUI.LC_DURATION);
  }

  // sets the tab to finance
  @FXML
  private void handleFinanceButtonAction() {
    if (!validateData()) {
      return;
    }
    setLoanDetails();
    Main.setUi(AppUI.LC_FINANCE);
  }

  // sets the tab to coborrower
  @FXML
  private void handleCoborrowerButtonAction() {
    setLoanDetails();
    Main.setUi(AppUI.LC_COBORROWER);
  }

  // determines functionality based on the current mode the user is in while in
  // the loan screen
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

  // if the details are valid, enter the view summary page, otherwise display red
  // for tabs and fields that are invalid
  @FXML
  private void handleSummaryButtonAction() throws IOException {

    setLoanDetails();
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

  // returns to the previous page based on where the user has entered the scene
  @FXML
  private void handleBackButtonAction() {
    if (AppState.getIsCreatingLoan()) {
      Main.setUi(AppUI.CUSTOMER_RESULTS);
    } else {
      Main.setUi(AppUI.LC_SUMMARY);
    }
  }

  // sets the style for each button
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
