package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CustomerInputEmployerAddressController {
  @FXML private ComboBox<String> customerAddressTypeComboBox;
  @FXML private TextField customerAddressLine1Field;
  @FXML private TextField customerAddressLine2Field;
  @FXML private TextField customerSuburbField;
  @FXML private TextField customerCityField;
  @FXML private TextField customerPostcodeField;
  @FXML private CheckBox customerPrimaryAddressCheckBox;
  @FXML private CheckBox customerMailingAddressCheckBox;

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
