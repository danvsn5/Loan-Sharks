package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.SceneManager;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.mainframe.Connection;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Request;
import uoa.lavs.mainframe.Response;
import uoa.lavs.AppState;

public class MainMenuController {
  @FXML
  private Button customerButton;

  @FXML
  private Button loanButton;

  @FXML
  private Button logOutButton;

  @FXML
  private Button instructionsButton;

  @FXML
  private Label welcomeLabel;

  @FXML
  private ImageView staticReturnImageView;

  @FXML
  private Button syncButton;

  @FXML
  private void initialize() {
    welcomeLabel.setText("Welcome!");
    staticReturnImageView.setDisable(true);
    staticReturnImageView.setVisible(false);
  }

  @FXML
  private void onClickCustomerButton() {

    Connection connection = Instance.getConnection();

    Request request = new Request(1);

    Response response = (connection.send(request));
    uoa.lavs.mainframe.Status status = response.getStatus();

    // there was an issue connecting to the database
    if (status.getErrorCode() == 1000
        || status.getErrorCode() == 1010
        || status.getErrorCode() == 1020) {
      setRedSymbol(AppUI.CUSTOMER_MENU);
      ((Labeled) SceneManager.getScene(AppUI.CUSTOMER_MENU).lookup("#connectionLabel"))
          .setText(status.getErrorMessage());
      // if the 'unknown' message got a response, it's online
    } else if (status.getErrorCode() == 100) {
      setGreenSymbol(AppUI.CUSTOMER_MENU);
      ((Labeled) SceneManager.getScene(AppUI.CUSTOMER_MENU).lookup("#connectionLabel"))
          .setText("Connection is successful");
      // catch all for other messages
    } else {
      setOrangeSymbol(AppUI.CUSTOMER_MENU);
      ((Labeled) SceneManager.getScene(AppUI.CUSTOMER_MENU).lookup("#connectionLabel"))
          .setText("Unidentified error. Please try again.");
    }

    Main.setUi(AppUI.CUSTOMER_MENU);
    AppState.setCurrentUiName(AppUI.CUSTOMER_MENU);
    AppState.setPreviousUi(AppUI.MAIN_MENU);
  }

  @FXML
  private void onClickSyncButton() {
    System.out.println("Sync button clicked");
  }

  @FXML
  private void handleLoanButtonAction() {

    Connection connection = Instance.getConnection();

    Request request = new Request(1);

    Response response = (connection.send(request));
    uoa.lavs.mainframe.Status status = response.getStatus();

    // there was an issue connecting to the database
    if (status.getErrorCode() == 1000
        || status.getErrorCode() == 1010
        || status.getErrorCode() == 1020) {
      setRedSymbol(AppUI.LOAN_MENU);
      ((Labeled) SceneManager.getScene(AppUI.LOAN_MENU).lookup("#connectionLabel"))
          .setText(status.getErrorMessage());
      // if the 'unknown' message got a response, it's online
    } else if (status.getErrorCode() == 100) {
      setGreenSymbol(AppUI.LOAN_MENU);
      ((Labeled) SceneManager.getScene(AppUI.LOAN_MENU).lookup("#connectionLabel"))
          .setText("Connection is successful");
      // catch all for other messages
    } else {
      setOrangeSymbol(AppUI.LOAN_MENU);
      ((Labeled) SceneManager.getScene(AppUI.LOAN_MENU).lookup("#connectionLabel"))
          .setText("Unidentified error. Please try again.");
    }

    Main.setUi(AppUI.LOAN_MENU);
    AppState.setCurrentUiName(AppUI.LOAN_MENU);
    AppState.setPreviousUi(AppUI.MAIN_MENU);
  }

  @FXML
  private void handleLogOutButtonAction() {
    Main.setUi(AppUI.LOGIN);
    AppState.setCurrentUiName(AppUI.LOGIN);
  }

  @FXML
  private void handleinstructionsButtonAction() {
    // There is no instruction page, so we will just print a message
    System.out.println("Instructions button clicked");
  }

  @FXML
  private void onClickReturnButton() {
    // There is no return button, so we will just print a message
  }

  private void setRedSymbol(AppUI ui) {
    ColorAdjust red = new ColorAdjust();
    red.setBrightness(0);
    red.setSaturation(1);
    SceneManager.getScene(ui).lookup("#connectionSymbol").setEffect(red);
  }

  private void setGreenSymbol(AppUI ui) {
    ColorAdjust green = new ColorAdjust();
    green.setBrightness(-0.3);
    green.setSaturation(1);
    green.setHue(0.82);
    SceneManager.getScene(ui).lookup("#connectionSymbol").setEffect(green);
  }

  private void setOrangeSymbol(AppUI ui) {
    ColorAdjust orange = new ColorAdjust();
    orange.setBrightness(0.0);
    orange.setSaturation(1);
    orange.setHue(0.16);
    SceneManager.getScene(ui).lookup("#connectionSymbol").setEffect(orange);
  }
}
