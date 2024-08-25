package uoa.lavs.frontend.controllers;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.IndividualCustomerSingleton;
import uoa.lavs.backend.oop.customer.Note;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;

public class CustomerInputNotesController implements AccessTypeObserver {

  @FXML private Button editButton;
  @FXML private ImageView staticReturnImageView;
  @FXML private Label notesPageNumber;
  @FXML private ImageView incNotes;
  @FXML private ImageView decNotes;

  @FXML private Pane notesPane;

  @FXML private TextField noteField1;
  @FXML private TextField noteField2;
  @FXML private TextField noteField3;
  @FXML private TextField noteField4;
  @FXML private TextField noteField5;
  @FXML private TextField noteField6;
  @FXML private TextField noteField7;
  @FXML private TextField noteField8;
  @FXML private TextField noteField9;
  @FXML private TextField noteField10;
  @FXML private TextField noteField11;
  @FXML private TextField noteField12;
  @FXML private TextField noteField13;
  @FXML private TextField noteField14;
  @FXML private TextField noteField15;
  @FXML private TextField noteField16;
  @FXML private TextField noteField17;
  @FXML private TextField noteField18;
  @FXML private TextField noteField19;

  @FXML private Button fakeButton;

  private ArrayList<Note> notes = new ArrayList<>();

  private IndividualCustomer customer = IndividualCustomerSingleton.getInstance();

  private int currentPage = 1;

  @FXML
  private void initialize() {
    limitTextFieldLength();
    AccessTypeNotifier.registerCustomerObserver(this);
    updateUIBasedOnAccessType();
    // Initialize notes with 100 notes, with all lines empty
    for (int i = 0; i < 100; i++) {
      notes.add(new Note("", new String[19]));
    }

    // Set dummy values
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")) {
      noteField1.setText("Note 1");
      noteField2.setText("Note 2");
      noteField3.setText("Note 3");
      noteField4.setText("Note 4");
      noteField5.setText("Note 5");
      noteField6.setText("Note 6");
      noteField7.setText("Note 7");
      noteField8.setText("Note 8");
      noteField9.setText("Note 9");
      noteField10.setText("Note 10");
      noteField11.setText("Note 11");
      noteField12.setText("Note 12");
      noteField13.setText("Note 13");
      noteField14.setText("Note 14");
      noteField15.setText("Note 15");
      noteField16.setText("Note 16");
      noteField17.setText("Note 17");
      noteField18.setText("Note 18");
      noteField19.setText("Note 19");
    }

    if (AppState.getIsAccessingFromSearch()) {
      IndividualCustomerSingleton.setInstanceCustomer(AppState.getSelectedCustomer());
      customer = IndividualCustomerSingleton.getInstance();

      notes = customer.getNotes();
      // For all notes, set it based on the index of the page notes and also the line number
      loadPageNotes(currentPage);
    }
  }

  private void limitTextFieldLength() {
    addTextLimiter(noteField1, 70);
    addTextLimiter(noteField2, 70);
    addTextLimiter(noteField3, 70);
    addTextLimiter(noteField4, 70);
    addTextLimiter(noteField5, 70);
    addTextLimiter(noteField6, 70);
    addTextLimiter(noteField7, 70);
    addTextLimiter(noteField8, 70);
    addTextLimiter(noteField9, 70);
    addTextLimiter(noteField10, 70);
    addTextLimiter(noteField11, 70);
    addTextLimiter(noteField12, 70);
    addTextLimiter(noteField13, 70);
    addTextLimiter(noteField14, 70);
    addTextLimiter(noteField15, 70);
    addTextLimiter(noteField16, 70);
    addTextLimiter(noteField17, 70);
    addTextLimiter(noteField18, 70);
    addTextLimiter(noteField19, 70);
  }

  private void addTextLimiter(TextField textField, int maxLength) {
    textField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.length() > maxLength) {
                textField.setText(newValue.substring(0, maxLength));
              }
            });
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")) {
      setTextFieldsEditable(true);
      editButton.setText("Create Customer");
      setDetails();
    }
    if (AppState.getCustomerDetailsAccessType().equals("VIEW")) {
      setTextFieldsEditable(false);
      editButton.setText("Edit Details");
    }
    if (AppState.getCustomerDetailsAccessType().equals("EDIT")) {
      setTextFieldsEditable(false);
      editButton.setText("Confirm Changes");
      setDetails();
    }
  }

  private void setTextFieldsEditable(boolean editable) {
    noteField1.setEditable(editable);
    noteField2.setEditable(editable);
    noteField3.setEditable(editable);
    noteField4.setEditable(editable);
    noteField5.setEditable(editable);
    noteField6.setEditable(editable);
    noteField7.setEditable(editable);
    noteField8.setEditable(editable);
    noteField9.setEditable(editable);
    noteField10.setEditable(editable);
    noteField11.setEditable(editable);
    noteField12.setEditable(editable);
    noteField13.setEditable(editable);
    noteField14.setEditable(editable);
    noteField15.setEditable(editable);
    noteField16.setEditable(editable);
    noteField17.setEditable(editable);
    noteField18.setEditable(editable);
    noteField19.setEditable(editable);
  }

  private void setDetails() {
    ArrayList<Note> notes = new ArrayList<>();
    for (int page = 1; page <= 100; page++) {
      String[] pageNotes = new String[19];
      pageNotes[0] = noteField1.getText();
      pageNotes[1] = noteField2.getText();
      pageNotes[2] = noteField3.getText();
      pageNotes[3] = noteField4.getText();
      pageNotes[4] = noteField5.getText();
      pageNotes[5] = noteField6.getText();
      pageNotes[6] = noteField7.getText();
      pageNotes[7] = noteField8.getText();
      pageNotes[8] = noteField9.getText();
      pageNotes[9] = noteField10.getText();
      pageNotes[10] = noteField11.getText();
      pageNotes[11] = noteField12.getText();
      pageNotes[12] = noteField13.getText();
      pageNotes[13] = noteField14.getText();
      pageNotes[14] = noteField15.getText();
      pageNotes[15] = noteField16.getText();
      pageNotes[16] = noteField17.getText();
      pageNotes[17] = noteField18.getText();
      pageNotes[18] = noteField19.getText();

      notes.add(new Note("", pageNotes));
    }
    customer.setNotes(notes);
  }

  @FXML
  private void handleEditButtonAction() {
    if (AppState.getCustomerDetailsAccessType().equals("CREATE")
        && AccessTypeNotifier.validateCustomerObservers()) {
      setDetails();
      AppState.setCustomerDetailsAccessType("VIEW");
    } else if (AppState.getCustomerDetailsAccessType().equals("VIEW")) {
      AppState.setCustomerDetailsAccessType("EDIT");
    } else if (AppState.getCustomerDetailsAccessType().equals("EDIT")
        && AccessTypeNotifier.validateCustomerObservers()) {
      setDetails();
      AppState.setCustomerDetailsAccessType("VIEW");
    }
    AccessTypeNotifier.notifyCustomerObservers();
    updateUIBasedOnAccessType();
  }

  @FXML
  private void handleBackButtonAction() {
    setDetails();
    Main.setUi(AppUI.CI_DETAILS);
  }

  @FXML
  private void handleIncNotes() {
    if (currentPage < 100) {
      saveCurrentPageNotes(currentPage);
      currentPage++;
      notesPageNumber.setText("Page: " + String.valueOf(currentPage));
      loadPageNotes(currentPage);
    }
  }

  @FXML
  private void handleDecNotes() {
    if (currentPage > 1) {
      saveCurrentPageNotes(currentPage);
      currentPage--;
      notesPageNumber.setText("Page: " + String.valueOf(currentPage));
      loadPageNotes(currentPage);
    }
  }

  private void saveCurrentPageNotes(int page) {
    String[] pageNotes = new String[19];
    pageNotes[0] = noteField1.getText();
    pageNotes[1] = noteField2.getText();
    pageNotes[2] = noteField3.getText();
    pageNotes[3] = noteField4.getText();
    pageNotes[4] = noteField5.getText();
    pageNotes[5] = noteField6.getText();
    pageNotes[6] = noteField7.getText();
    pageNotes[7] = noteField8.getText();
    pageNotes[8] = noteField9.getText();
    pageNotes[9] = noteField10.getText();
    pageNotes[10] = noteField11.getText();
    pageNotes[11] = noteField12.getText();
    pageNotes[12] = noteField13.getText();
    pageNotes[13] = noteField14.getText();
    pageNotes[14] = noteField15.getText();
    pageNotes[15] = noteField16.getText();
    pageNotes[16] = noteField17.getText();
    pageNotes[17] = noteField18.getText();
    pageNotes[18] = noteField19.getText();

    if (notes.size() >= page) {
      notes.set(page - 1, new Note("", pageNotes));
    } else {
      notes.add(new Note("", pageNotes));
    }
    customer.setNotes(notes);
  }

  private void loadPageNotes(int page) {
    if (notes.size() >= page) {
      Note note = notes.get(page - 1);
      String[] pageNotes = note.getLines();
      noteField1.setText(pageNotes[0]);
      noteField2.setText(pageNotes[1]);
      noteField3.setText(pageNotes[2]);
      noteField4.setText(pageNotes[3]);
      noteField5.setText(pageNotes[4]);
      noteField6.setText(pageNotes[5]);
      noteField7.setText(pageNotes[6]);
      noteField8.setText(pageNotes[7]);
      noteField9.setText(pageNotes[8]);
      noteField10.setText(pageNotes[9]);
      noteField11.setText(pageNotes[10]);
      noteField12.setText(pageNotes[11]);
      noteField13.setText(pageNotes[12]);
      noteField14.setText(pageNotes[13]);
      noteField15.setText(pageNotes[14]);
      noteField16.setText(pageNotes[15]);
      noteField17.setText(pageNotes[16]);
      noteField18.setText(pageNotes[17]);
      noteField19.setText(pageNotes[18]);
    } else {
      noteField1.setText("");
      noteField2.setText("");
      noteField3.setText("");
      noteField4.setText("");
      noteField5.setText("");
      noteField6.setText("");
      noteField7.setText("");
      noteField8.setText("");
      noteField9.setText("");
      noteField10.setText("");
      noteField11.setText("");
      noteField12.setText("");
      noteField13.setText("");
      noteField14.setText("");
      noteField15.setText("");
      noteField16.setText("");
      noteField17.setText("");
      noteField18.setText("");
      noteField19.setText("");
    }
  }

  @Override
  public boolean validateData() {
    // No validation needs to occur for notes
    return true;
  }

  @Override
  public Button getButton() {
    return fakeButton = new Button();
  }

  @Override
  public void setInvalidButton(String style) {
    // Do nothing
  }
}
