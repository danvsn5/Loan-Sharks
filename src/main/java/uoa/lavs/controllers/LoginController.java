package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.sql.InitialiseDatabase;

public class LoginController {
  @FXML private Button loginButton;

  @FXML private TextField usernameField;

  @FXML private PasswordField passwordField;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleLoginButtonAction() throws Exception {
    // This logic checks if the username and password are Moana. If so, we intilaise the main menu
    // with the proper name and clear all fields
    if (usernameField.getText().equals("Moana") && passwordField.getText().equals("Password")) {
      System.out.println("Login successful");
      AppState.userName = usernameField.getText();
      SceneManager.addScene(
          AppUI.MAIN_MENU,
          new FXMLLoader(Main.class.getResource("/fxml/admin/welcome.fxml")).load());
      Main.setUi(AppUI.MAIN_MENU);

      InitialiseDatabase.createDatabase();

      usernameField.clear();
      passwordField.clear();
    } else {
      System.out.println("Login failed");
    }
  }

  @FXML
  private void onEnterPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      try {
        handleLoginButtonAction();
      } catch (Exception e) {
        System.out.println(
            "Something went wrong - could not login! The message is " + e.getMessage());
      }
    }
  }
}
