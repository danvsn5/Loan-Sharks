package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class MainMenuController {
  @FXML private Button customerButton;

  @FXML private Button loanButton;

  @FXML private Button logOutButton;

  @FXML private Button instructionsButton;

  @FXML private Label welcomeLabel;

  @FXML
  private void initialize() {
    welcomeLabel.setText("Welcome, " + uoa.lavs.AppState.userName + "!");
  }

  @FXML
  private void onClickCustomerButton() {
    Main.setUi(AppUI.CUSTOMER_MENU);
  }

  @FXML
  private void handleLoanButtonAction() {
    Main.setUi(AppUI.LOAN_MENU);
  }

  @FXML
  private void handleLogOutButtonAction() {
    Main.setUi(AppUI.LOGIN);
  }

  @FXML
  private void handleinstructionsButtonAction() {
    // There is no instruction page, so we will just print a message
    System.out.println("Instructions button clicked");
  }
}
