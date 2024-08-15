package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class LoanMenuController {
  @FXML private Button createNewLoanButton;

  @FXML private Button findLoanButton;

  @FXML private Button backButton;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleCreateNewLoanButtonAction() {
    Main.setUi(AppUI.LC_INPUT);
  }

  @FXML
  private void handleFindLoanButtonAction() {
    Main.setUi(AppUI.LOAN_SEARCH);
  }

  @FXML
  private void handleBackButtonAction() {
    // Add back button action code here
  }
}
