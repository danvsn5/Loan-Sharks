package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class CustomerMenuController {
  @FXML private Button addNewCustomerButton;

  @FXML private Button findCustomerButton;

  @FXML private Button backButton;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleAddNewCustomerButtonAction() {
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleFindCustomerButtonAction() {
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.MAIN_MENU);
  }
}
