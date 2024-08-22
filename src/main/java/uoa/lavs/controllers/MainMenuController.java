package uoa.lavs.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.SceneManager;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.mainframe.Connection;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Request;
import uoa.lavs.mainframe.Response;
import uoa.lavs.sql.sql_to_mainframe.SyncManager;
import uoa.lavs.AppState;

public class MainMenuController {
  @FXML
  private Button customerButton;

  @FXML
  private Button loanButton;

  @FXML
  private Button logOutButton;

  @FXML
  private Button instructionsButton;

  @FXML
  private Label welcomeLabel;

  @FXML
  private ImageView staticReturnImageView;

  @FXML
  private Button syncButton;

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
  private void onClickSyncButton() throws SQLException, IOException {
    System.out.println("Sync button clicked");
    // check if needs syncing 
    if (SyncManager.checkIfNeedsSyncing()) {
      SyncManager.main(null);
    } else {
      System.out.println("No need to sync");
    }
    
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
