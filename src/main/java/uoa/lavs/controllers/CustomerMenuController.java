package uoa.lavs.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.IndividualCustomerSingleton;
import uoa.lavs.mainframe.Connection;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Request;
import uoa.lavs.mainframe.Response;

public class CustomerMenuController {
  @FXML
  private Button addNewCustomerButton;

  @FXML
  private Button findCustomerButton;

  @FXML
  private Button backButton;

  @FXML
  private ImageView staticReturnImageView;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleAddNewCustomerButtonAction() throws IOException {
    IndividualCustomerSingleton.resetInstance();
    AppState.loadAllCustomerDetails("CREATE");
    Main.setUi(AppUI.CI_DETAILS);
    AppState.setCurrentUiName(AppUI.CI_DETAILS);
    AppState.setPreviousUi(AppUI.CUSTOMER_MENU);
  }

  @FXML
  private void handleFindCustomerButtonAction() {

    Connection connection = Instance.getConnection();

    Request request = new Request(1);

    Response response = (connection.send(request));
    uoa.lavs.mainframe.Status status = response.getStatus();

    // there was an issue connecting to the database
    if (status.getErrorCode() == 1000
        || status.getErrorCode() == 1010
        || status.getErrorCode() == 1020) {
      setRedSymbol();
      // if the 'unknown' message got a response, it's online
    } else if (status.getErrorCode() == 100) {
      setGreenSymbol();
      // catch all for other messages
    } else {
      setOrangeSymbol();
    }

    Main.setUi(AppUI.CUSTOMER_SEARCH);
    AppState.setCurrentUiName(AppUI.CUSTOMER_SEARCH);
    AppState.setPreviousUi(AppUI.CUSTOMER_MENU);
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.MAIN_MENU);
  }

  @FXML
  private void onClickReturnButton() {
    Main.setUi(AppUI.MAIN_MENU);
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
