package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CustomerInputContactController {
  @FXML private TextField customerEmailTextField;
  @FXML private ComboBox<String> customerPhoneTypeComboBox;
  @FXML private TextField customerPhoneNumber;
  @FXML private Button addNewPhoneButton;
  @FXML private Button deletePhoneButton;
  // Both of these fields only appear if the add button was clicked, otherwise keep them hidden.
  @FXML private ComboBox<String> customerPhoneTypeSecondComboBox;
  @FXML private TextField customerPhoneNumberSecond;

  @FXML private ComboBox<String> customerPreffredContactBox;
  @FXML private ComboBox<String> customerAltContactBox;

  @FXML private Button exitButton;
  @FXML private Button nextButton;
  @FXML private Button backButton;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleExitButtonAction() {
    // Add exit button action code here
  }

  @FXML
  private void handleNextButtonAction() {
    // Add next button action code here
  }

  @FXML
  private void handleBackButtonAction() {
    // Add back button action code here
  }
}
