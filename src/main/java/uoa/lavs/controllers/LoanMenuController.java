package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class LoanMenuController {
  @FXML private Button createNewLoanButton;

  @FXML private Button findLoanButton;

  @FXML private ImageView staticReturnImageView;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleCreateNewLoanButtonAction() {
    AppState.isCreatingLoan = true;
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }

  @FXML
  private void handleFindLoanButtonAction() {
    Main.setUi(AppUI.LOAN_SEARCH);
  }

  @FXML
  private void handleBackButtonAction() {
    // Add back button action code here
    AppState.isCreatingLoan = false;
    Main.setUi(AppUI.MAIN_MENU);
  }
}
