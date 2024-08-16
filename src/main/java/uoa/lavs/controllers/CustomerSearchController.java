package uoa.lavs.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uoa.lavs.AppState;

public class CustomerSearchController {
  @FXML private Label searchWithCustomerIDLabel; // When click label, reveal text box.

  @FXML private Label searchWithNameLabel; // When click label, reveal text box.

  @FXML private ImageView staticReturnImageView;

  @FXML private TextField usernameField;

  @FXML private TextField idField;

  @FXML private Button searchButton;

  String state;

  @FXML
  private void initialize() {
    usernameField.setVisible(false);
    idField.setVisible(false);
  }

  // When enter key is pressed, perform search.
  @FXML
  private void onEnterPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      try {
        onClickSearchButton();
      } catch (Exception e) {
        System.out.println("Something went wrong " + e.getMessage());
      }
    }
  }

  @FXML
  private void onClickSearchButton() throws IOException {
    if (idField.getText().isEmpty() && usernameField.getText().isEmpty()) {
      return;
    }
    if (state.equals("id")) {
      handleSearchWithCustomerIDLabelAction();
    } else {
      handleSearchWithNameLabelAction();
    }
  }

  @FXML
  private void enableIDSearch() {
    usernameField.setVisible(false);
    idField.setVisible(true);
    state = "id";
  }

  @FXML
  private void enableNameSearch() {
    idField.setVisible(false);
    usernameField.setVisible(true);
    state = "name";
  }

  @FXML
  private void handleSearchWithCustomerIDLabelAction() throws IOException {
    // implement that jamie
    String searchString = idField.getText();
    AppState.loadCustomerSearchResults(searchString);
  }

  @FXML
  private void handleSearchWithNameLabelAction() throws IOException {
    String searchString = usernameField.getText();
    AppState.loadCustomerSearchResults(searchString);
  }

  @FXML
  private void handleBackButtonAction() {
    // Add back button action code here
  }
}
