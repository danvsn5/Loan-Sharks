package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;

public class CustomerInputNotesController {
  @FXML private TextArea customerNotesField;

  @FXML private Button editButton;
  @FXML private Button backButton;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  private void setNotes() {
    customer.setNotes(customerNotesField.getText());
  }

  @FXML
  private void handleEditButtonAction() {
    setNotes();
    // Add next button action code here
  }

  @FXML
  private void handleBackButtonAction() {
    setNotes();
    Main.setUi(AppUI.CI_DETAILS);
  }
}
