package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import uoa.lavs.AccessTypeNotifier;
import uoa.lavs.AccessTypeObserver;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.IndividualCustomerSingleton;

public class CustomerInputNotesController implements AccessTypeObserver {
  @FXML private TextArea customerNotesField;

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;
  @FXML private Label notesPageNumber;
  @FXML private ImageView incNotes;
  @FXML private ImageView decNotes;

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  @FXML
  private void initialize() {
    AccessTypeNotifier.registerCustomerObserver(this);
    updateUIBasedOnAccessType();
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      customerNotesField.setEditable(true);
      editButton.setText("Create Customer");
    }
    if (AppState.customerDetailsAccessType.equals("VIEW")) {
      customerNotesField.setEditable(false);
      editButton.setText("Edit Details");
    }
    if (AppState.customerDetailsAccessType.equals("EDIT")) {
      customerNotesField.setEditable(true);
      editButton.setText("Confirm Changes");
    }
    setNotes();
  }

  private void setNotes() {
    // TODO once the notes gui is implemented.

    // if (customerNotesField.getText() != null) {
    //   customer.setNotes(customerNotesField.getText());
    // } else {
    //   customer.setNotes("");
    // }
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.customerDetailsAccessType.equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      setNotes();
      AppState.customerDetailsAccessType = "VIEW";
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
    } else if (AppState.customerDetailsAccessType.equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {
      setNotes();
      AppState.customerDetailsAccessType = "VIEW";
    }
    AccessTypeNotifier.notifyCustomerObservers();
    updateUIBasedOnAccessType();
  }

  @FXML
  private void handleBackButtonAction() {
    setNotes();
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleIncNotes() {
    // Pagination TODO
  }

  @FXML
  private void handleDecNotes() {
    // Pagination TODO
  }

  // Daniil Moment
  @Override
  public boolean validateData() {
    // TODO Auto-generated method stub
    return true;
  }
}
