package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class CustomerResultsController {
  // Table will need to be populated with data from the database
  @FXML private TableView<?> customerResultsTable;

  @FXML private Button backButton;

  @FXML private Button leaveButton;

  // write for all fxml elements
  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleBackButtonAction() {
    // Add back button action code here
  }

  @FXML
  private void handleLeaveButtonAction() {
    // Add leave button action code here
  }

  // Need to implement method when clicking on a row in the table
  // to view the customer's details

}
