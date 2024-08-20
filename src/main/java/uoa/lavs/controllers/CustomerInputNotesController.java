package uoa.lavs.controllers;

import java.util.ArrayList;
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
import uoa.lavs.customer.Note;

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
    // TODO once the notes gui is fully implemented as this is all on the first page of notes
    // also creates a new note every time so not ideal.
    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      String[] pageOne = new String[19];
      for (int i = 0; i < pageOne.length; i++) {
        pageOne[i] = "";
      }

      if (customerNotesField.getText() != null) {
        String[] combinedNotes = customerNotesField.getText().split("\n");

        for (int i = 0; i < combinedNotes.length; i++) {
          pageOne[i] = combinedNotes[i];
        }
      }

      ArrayList<Note> notes = new ArrayList<>();
      notes.add(new Note("", pageOne));
      customer.setNotes(notes);
    }
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.customerDetailsAccessType.equals("CREATE")) {
      setNotes();
      AppState.customerDetailsAccessType = "VIEW";
    } else if (AppState.customerDetailsAccessType.equals("VIEW")) {
      AppState.customerDetailsAccessType = "EDIT";
    } else if (AppState.customerDetailsAccessType.equals("EDIT")) {
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
  private void handleIncNotes(){
    // Pagination TODO
  }

  @FXML
  private void handleDecNotes(){
    // Pagination TODO
  }
}
