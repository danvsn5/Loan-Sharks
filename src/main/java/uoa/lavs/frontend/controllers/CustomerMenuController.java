package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager;
import uoa.lavs.frontend.SceneManager.AppUI;

public class CustomerMenuController {
  @FXML private Button addNewCustomerButton;

  @FXML private Button findCustomerButton;

  @FXML private Button backButton;

  @FXML private ImageView staticReturnImageView;

  @FXML private ImageView connectionSymbol;

  @FXML private Label connectionLabel;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleAddNewCustomerButtonAction() throws IOException {
    // This all been bypassed in order to get to the next screen

    // Connection connection = Instance.getConnection();

    // Request request = new Request(1);

    // Response response = (connection.send(request));
    //  uoa.lavs.mainframe.Status status = response.getStatus();

    // there was an issue connecting to the database
    // if (status.getErrorCode() == 1000
    //    || status.getErrorCode() == 1010
    //    || status.getErrorCode() == 1020) {
    //  setRedSymbol();
    //  connectionLabel.setText(status.getErrorMessage());

    // if the 'unknown' message got a response, it's online
    // } else if (status.getErrorCode() == 100) {
    //  setGreenSymbol();
    //   connectionLabel.setText("");
    AppState.customerDetailsAccessType = null;
    IndividualCustomerSingleton.resetInstance();
    AppState.loadAllCustomerDetails("CREATE");
    Main.setUi(AppUI.CI_DETAILS);
    // catch all for other messages
    // } else {
    //    setOrangeSymbol();
    //   connectionLabel.setText("Unidentified error. Please try again.");
  }

  @FXML
  private void handleFindCustomerButtonAction() {
    AppState.customerDetailsAccessType = null;
    Main.setUi(AppUI.CUSTOMER_SEARCH);
    AppState.setCurrentUiName(AppUI.CUSTOMER_SEARCH);
    AppState.setPreviousUi(AppUI.CUSTOMER_MENU);
  }

  @FXML
  private void handleBackButtonAction() throws IOException {
    AppState.loadMainMenu();
  }

  @FXML
  private void onClickReturnButton() throws IOException {
    AppState.loadMainMenu();
  }

  private void setRedSymbol() {
    ColorAdjust red = new ColorAdjust();
    red.setBrightness(0);
    red.setSaturation(1);
    SceneManager.getScene(AppUI.CUSTOMER_SEARCH).lookup("#connectionSymbol").setEffect(red);
  }

  private void setGreenSymbol() {
    ColorAdjust green = new ColorAdjust();
    green.setBrightness(-0.3);
    green.setSaturation(1);
    green.setHue(0.82);
    SceneManager.getScene(AppUI.CUSTOMER_SEARCH).lookup("#connectionSymbol").setEffect(green);
  }

  private void setOrangeSymbol() {
    ColorAdjust orange = new ColorAdjust();
    orange.setBrightness(0.0);
    orange.setSaturation(1);
    orange.setHue(0.16);
    SceneManager.getScene(AppUI.CUSTOMER_SEARCH).lookup("#connectionSymbol").setEffect(orange);
  }
}
