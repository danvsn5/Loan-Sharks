package uoa.lavs.frontend.controllers;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import uoa.lavs.Main;
import uoa.lavs.backend.sql.sql_to_mainframe.SyncManager;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.Status;

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
  private Button syncButton;

  @FXML
  private Label syncLabel;

  @FXML
  private void initialize() throws SQLException {
    if (SyncManager.checkIfNeedsSyncing()) {
      syncLabel.setText("Local changes detected. Sync is required.");
    } else {
      syncLabel.setText("No local changes detected. Sync is not required.");
    }
    welcomeLabel.setText("Welcome!");
  }

  // navigates to customer menu
  @FXML
  private void onClickCustomerButton() {
    Main.setUi(AppUI.CUSTOMER_MENU);
    AppState.setCurrentUiName(AppUI.CUSTOMER_MENU);
    AppState.setPreviousUi(AppUI.MAIN_MENU);
  }

  // activates syncing; if local data has not been pushed to the mainframe,
  // syncing is required. Syncing result is displayed in the GUI
  @FXML
  private void onClickSyncButton() throws SQLException, IOException {
    System.out.println("Sync button clicked");
    // check if needs syncing
    if (SyncManager.checkIfNeedsSyncing()) {
      System.out.println("Needs to sync");
      Status status = SyncManager.masterSync();
      if (status.getErrorCode() == 0) {
        System.out.println("Sync successful");
        syncLabel.setText("Sync successful");
      } else {
        System.out.println("Sync failed: " + status.getErrorMessage());
        syncLabel.setText("Sync failed: Could not connect to mainframe. Local changes were not saved.");
      }
    } else {
      System.out.println("No need to sync");
      syncLabel.setText("No need to sync");
    }
  }

  // navigates to loan menu
  @FXML
  private void handleLoanButtonAction() {
    Main.setUi(AppUI.LOAN_MENU);
    AppState.setCurrentUiName(AppUI.LOAN_MENU);
    AppState.setPreviousUi(AppUI.MAIN_MENU);
  }

  // logs out and returns to the sign in menu
  @FXML
  private void handleLogOutButtonAction() {
    Main.setUi(AppUI.LOGIN);
    AppState.setCurrentUiName(AppUI.LOGIN);
  }

  @FXML
  private void handleinstructionsButtonAction() {
    Main.setUi(AppUI.INFORMATION);
  }

}
