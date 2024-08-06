package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class LoanSearchController {
  @FXML private Label searchWithLoanIDLabel; // When click label, reveal text box.

  @FXML private Label searchWithNameLabel; // When click label, reveal text box.

  @FXML private Button backButton;

  @FXML private Button leaveButton;

  @FXML private TextField usernameField;

  @FXML private TextField idField;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  // When enter key is pressed, perform search.
  @FXML
  private void onEnterPressed(KeyEvent event) {
    // Add code here
  }

  @FXML
  private void handleSearchWithLoanIDLabelAction() {
    // Add search with ID label action code here
  }

  @FXML
  private void handleSearchWithNameLabelAction() {
    // Add search with name label action code here
  }

  @FXML
  private void handleBackButtonAction() {
    // Add back button action code here
  }

  @FXML
  private void handleLeaveButtonAction() {
    // Add leave button action code here
  }
}
