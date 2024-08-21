package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.AppState;

public class MainMenuController {
  @FXML private Button customerButton;

  @FXML private Button loanButton;

  @FXML private Button logOutButton;

  @FXML private Button instructionsButton;

  @FXML private Label welcomeLabel;

  @FXML private ImageView staticReturnImageView;

  @FXML private Button syncButton;

  @FXML
  private void initialize() {
    welcomeLabel.setText("Welcome!");
    staticReturnImageView.setDisable(true);
    staticReturnImageView.setVisible(false);
  }

  @FXML
  private void onClickCustomerButton() {
    Main.setUi(AppUI.CUSTOMER_MENU);
    AppState.setCurrentUiName(AppUI.CUSTOMER_MENU);
    AppState.setPreviousUi(AppUI.MAIN_MENU);
  }

  @FXML
  private void onClickSyncButton() {
    System.out.println("Sync button clicked");
  }

  @FXML
  private void handleLoanButtonAction() {
    Main.setUi(AppUI.LOAN_MENU);
    AppState.setCurrentUiName(AppUI.LOAN_MENU);
    AppState.setPreviousUi(AppUI.MAIN_MENU);
  }

  @FXML
  private void handleLogOutButtonAction() {
    Main.setUi(AppUI.LOGIN);
    AppState.setCurrentUiName(AppUI.LOGIN);
  }

  @FXML
  private void handleinstructionsButtonAction() {
    // There is no instruction page, so we will just print a message
    System.out.println("Instructions button clicked");
  }

  @FXML
  private void onClickReturnButton() {
    // There is no return button, so we will just print a message
  }
}
