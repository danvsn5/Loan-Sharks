package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.loan.PersonalLoanSingleton;
import uoa.lavs.mainframe.Connection;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Request;
import uoa.lavs.mainframe.Response;

public class LoanMenuController {
  @FXML
  private Button createNewLoanButton;

  @FXML
  private Button findLoanButton;

  @FXML
  private ImageView staticReturnImageView;

  @FXML
  private ImageView connectionSymbol;

  @FXML
  private Label connectionLabel;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleCreateNewLoanButtonAction() {

    Connection connection = Instance.getConnection();

    Request request = new Request(1);

    Response response = (connection.send(request));
    uoa.lavs.mainframe.Status status = response.getStatus();

    // there was an issue connecting to the database
    if (status.getErrorCode() == 1000
        || status.getErrorCode() == 1010
        || status.getErrorCode() == 1020) {
      setRedSymbol();
      connectionLabel.setText(status.getErrorMessage());
      // if the 'unknown' message got a response, it's online
    } else if (status.getErrorCode() == 100) {
      setGreenSymbol();
      connectionLabel.setText("Connection is successful");
      PersonalLoanSingleton.resetInstance();
      AppState.isCreatingLoan = true;
      Main.setUi(AppUI.CUSTOMER_SEARCH);
      // catch all for other messages
    } else {
      setOrangeSymbol();
      connectionLabel.setText("Unidentified error. Please try again.");
    }

  }

  @FXML
  private void handleFindLoanButtonAction() {
    Main.setUi(AppUI.LOAN_SEARCH);
  }

  @FXML
  private void handleBackButtonAction() {
    // Add back button action code here
    AppState.isCreatingLoan = false;
    Main.setUi(AppUI.MAIN_MENU);
  }

  private void setRedSymbol() {
    ColorAdjust red = new ColorAdjust();
    red.setBrightness(0);
    red.setSaturation(1);
    connectionSymbol.setEffect(red);
  }

  private void setGreenSymbol() {
    ColorAdjust green = new ColorAdjust();
    green.setBrightness(-0.3);
    green.setSaturation(1);
    green.setHue(0.82);
    connectionSymbol.setEffect(green);
  }

  private void setOrangeSymbol() {
    ColorAdjust orange = new ColorAdjust();
    orange.setBrightness(0.0);
    orange.setSaturation(1);
    orange.setHue(0.16);
    connectionSymbol.setEffect(orange);
  }
}
