package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Instance;
import uoa.lavs.legacy.mainframe.Request;
import uoa.lavs.legacy.mainframe.Response;

public class LoanMenuController {
  @FXML private Button createNewLoanButton;

  @FXML private Button findLoanButton;

  @FXML private ImageView staticReturnImageView;

  @FXML private ImageView connectionSymbol;

  @FXML private Label connectionLabel;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleCreateNewLoanButtonAction() {

    Connection connection = Instance.getConnection();

    Request request = new Request(1001);

    Response response = (connection.send(request));
    uoa.lavs.legacy.mainframe.Status status = response.getStatus();

    // there was an issue connecting to the database
    if (status.getErrorCode() == 1000
        || status.getErrorCode() == 1010
        || status.getErrorCode() == 1020) {
      setRedSymbol();
      connectionLabel.setText(status.getErrorMessage());
      return;
    } else {
      // catch all for other messages
      setGreenSymbol();
      // connectionLabel.setText(status.getErrorMessage());
    }
    PersonalLoanSingleton.resetInstance();
    AppState.isCreatingLoan = true;
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }

  @FXML
  private void handleFindLoanButtonAction() {
    // Make it so this does tells the user this is not implemented
    setRedSymbol();
    connectionLabel.setText("This feature is not implemented yet.");
  }

  @FXML
  private void handleBackButtonAction() throws IOException {
    // Add back button action code here
    AppState.isCreatingLoan = false;
    AppState.loadMainMenu();
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
