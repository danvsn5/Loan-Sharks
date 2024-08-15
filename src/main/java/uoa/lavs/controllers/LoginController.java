package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class LoginController {
  @FXML private Button loginButton;

  @FXML private TextField usernameField;

  @FXML private PasswordField passwordField;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleLoginButtonAction() {
    // Add login button action code here
    if (usernameField.getText().equals("Moana") && passwordField.getText().equals("Password")) {
      System.out.println("Login successful");
      Main.setUi(AppUI.MAIN_MENU);
    } else {
      System.out.println("Login failed");
    }
  }

  @FXML
  private void onEnterPressed(KeyEvent event) {
    if (event.getCode().toString().equals("ENTER")) {
      handleLoginButtonAction();
    }
  }
}
