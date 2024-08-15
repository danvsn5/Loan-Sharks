package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class CustomerInputNotesController {
  @FXML private TextArea customerNotesField;

  @FXML private Button editButton;
  @FXML private Button backButton;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleEditButtonAction() {
    // Add next button action code here
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CI_DETAILS);
  }
}
