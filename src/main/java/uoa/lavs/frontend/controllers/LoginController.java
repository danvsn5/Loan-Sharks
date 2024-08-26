package uoa.lavs.frontend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uoa.lavs.Main;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager;
import uoa.lavs.frontend.SceneManager.AppUI;

public class LoginController {
  @FXML private Button loginButton;

  @FXML private TextField usernameField;

  @FXML private PasswordField passwordField;

  @FXML
  private void initialize() {
    // sets the focus to the username field for improved UX
    usernameField.setText("Moana");
    passwordField.setText("Password");
  }

  @FXML
  private void handleLoginButtonAction() throws Exception {
    // This logic checks if the username and password are Moana. If so, we intilaise
    // the main menu with the proper name and clear all fields
    if (usernameField.getText().equals("Moana") && passwordField.getText().equals("Password")) {
      System.out.println("Login successful");
      AppState.setUserName(usernameField.getText());
      // adds the welcome main menu scene the map and sets the UI to the main menu
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

  // activates log in procedure with enter key press instead of button click for
  // improved UX
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
