package uoa.lavs.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import uoa.lavs.Main;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;

public abstract class AbstractCustomerController {
  @FXML protected Button customerDetailsButton;
  @FXML protected Button customerAddressButton;
  @FXML protected Button customerContactButton;
  @FXML protected Button customerEmployerButton;

  protected abstract void startWithCustomerID();

  protected abstract void setDetails();

  @FXML
  protected void handleDetailsButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  protected void handleAddressButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_PRIMARY_ADDRESS);
  }

  @FXML
  protected void handleContactButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_CONTACT);
  }

  @FXML
  protected void handleEmployerButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_EMPLOYER);
  }

  @FXML
  protected void handleEmployerAddressButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_EMPLOYER_ADDRESS);
  }

  @FXML
  protected void handleNotesButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_NOTES);
  }

  @FXML
  protected void handleBackButtonAction() {
    if (AppState.getIsAccessingFromSearch()) {
      AppState.setIsAccessingFromLoanSearch(false);
      Main.setUi(AppUI.CUSTOMER_SEARCH);
    } else {
      Main.setUi(AppUI.CUSTOMER_MENU);
    }
  }

  @FXML
  public void setInvalidButton(String style) {
    Button currentButton = AppState.getCurrentButton();

    String buttonId = currentButton.getId();

    if (buttonId != null) {
      if (buttonId.equals("customerDetailsButton")) {
        customerDetailsButton.setStyle(style);
      } else if (buttonId.equals("customerAddressButton")) {
        customerAddressButton.setStyle(style);
      } else if (buttonId.equals("customerContactButton")) {
        customerContactButton.setStyle(style);
      } else if (buttonId.equals("customerEmployerButton")) {
        customerEmployerButton.setStyle(style);
      }
    }
  }
}
