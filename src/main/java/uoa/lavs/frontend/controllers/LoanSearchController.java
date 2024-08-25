package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uoa.lavs.Main;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;

public class LoanSearchController {
  @FXML private Label searchWithLoanIDLabel; // When click label, reveal text box.

  @FXML private Label searchWithNameLabel; // When click label, reveal text box.

  @FXML private ImageView staticReturnImageView;

  @FXML private Button searchButton;

  @FXML private TextField nameField;

  @FXML private TextField idField;

  String state;

  @FXML
  private void initialize() {}

  // When enter key is pressed, perform search.
  @FXML
  private void onEnterPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      try {
        onClickSearchButton();
      } catch (Exception e) {
        System.out.println("Something went wrong" + e.getMessage());
      }
    }
  }

  @FXML
  private void onClickSearchButton() throws IOException {
    if (idField.getText().isEmpty() && nameField.getText().isEmpty()) {
      return;
    }
    if (state.equals("id")) {
      handleSearchWithLoanIDLabelAction();
    } else {
      handleSearchWithNameLabelAction();
    }
    nameField.setText("");
    idField.setText("");
  }

  @FXML
  private void handleSearchWithLoanIDLabelAction() throws IOException {
    String searchString = idField.getText();
    AppState.loadLoanSearchResults(searchString);
  }

  @FXML
  private void handleSearchWithNameLabelAction() throws IOException {
    String searchString = nameField.getText();
    AppState.loadLoanSearchResults(searchString);
  }

  @FXML
  private void handleBackButtonAction() {
    AppState.isCreatingLoan = false;
    Main.setUi(AppUI.LOAN_MENU);
  }

  @FXML
  private void enableIDSearch() {
    nameField.setText("");
    state = "id";
  }

  @FXML
  private void enableNameSearch() {
    idField.setText("");
    state = "name";
  }
}
