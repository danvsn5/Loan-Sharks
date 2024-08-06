package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CustomerInputEmployerController {
  @FXML private TextField employerNameField;
  @FXML private TextField employerAddress1Field;
  @FXML private TextField employerAddress2Field;
  @FXML private TextField employerSuburbField;
  @FXML private TextField employerPostCodeField;
  @FXML private ComboBox<String> employerCountryField;
  @FXML private TextField employerEmailField;
  @FXML private TextField employerWebsiteField;
  @FXML private TextField employerPhoneField;
  @FXML private CheckBox customerIsEmployerCheckbox;

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
