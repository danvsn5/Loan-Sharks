package uoa.lavs.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.IndividualCustomerSingleton;

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

    SceneManager.getScene(AppUI.CUSTOMER_SEARCH).lookup("#connectionSymbol").setStyle("-fx-opacity: 0.9;");

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
}
