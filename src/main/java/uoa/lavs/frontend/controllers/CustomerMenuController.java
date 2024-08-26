package uoa.lavs.frontend.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;

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
  private ImageView connectionSymbol;

  @FXML
  private Label connectionLabel;

  // enters UI for customer creation or customer search
  @FXML
  private void handleAddNewCustomerButtonAction() throws IOException {
    AppState.setCustomerDetailsAccessType(null);
    IndividualCustomerSingleton.resetInstance();
    AppState.loadAllCustomerDetails("CREATE");
    Main.setUi(AppUI.CI_DETAILS);

  }

  @FXML
  private void handleFindCustomerButtonAction() {
    AppState.setCustomerDetailsAccessType(null);
    Main.setUi(AppUI.CUSTOMER_SEARCH);
    AppState.setCurrentUiName(AppUI.CUSTOMER_SEARCH);
    AppState.setPreviousUi(AppUI.CUSTOMER_MENU);
  }

  @FXML
  private void onClickReturnButton() throws IOException {
    AppState.loadMainMenu();
  }
}
