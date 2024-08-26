package uoa.lavs.frontend.controllers;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import uoa.lavs.backend.oop.customer.SearchCustomer;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;
import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Instance;

public class LoanCoborrower extends AbstractLoanController implements AccessTypeObserver {
  @FXML private TextField coborrowerIDField1;
  @FXML private TextField coborrowerIDField2;
  @FXML private TextField coborrowerIDField3;

  @FXML
  private void initialize() {
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();

    personalLoan.setCustomerId(AppState.getSelectedCustomer().getCustomerId());
  }

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessTypeLoan(
        AppState.getLoanDetailsAccessType(),
        editButton,
        new TextField[] {coborrowerIDField1, coborrowerIDField2, coborrowerIDField3},
        new ComboBox<?>[] {},
        new DatePicker[] {},
        new RadioButton[] {});
  }

  // Add methods for all buttons

  @Override
  public boolean validateData() {
    boolean isValid = true;

    // Searches if mainframe/localdb has these ids and sets the style of the textfield accordingly
    SearchCustomer searchCustomer = new SearchCustomer();
    Connection connection = Instance.getConnection();

    if (!coborrowerIDField1.getText().isEmpty()) {
      if (!coborrowerIDField1.getText().equals(personalLoan.getCustomerId())
          && searchCustomer.searchCustomerById(coborrowerIDField1.getText(), connection) != null) {
        coborrowerIDField1.setStyle("");
      } else {
        coborrowerIDField1.setStyle("-fx-border-color: red");
        isValid = false;
      }
    }
    if (!coborrowerIDField2.getText().isEmpty()) {
      if (!coborrowerIDField2.getText().equals(personalLoan.getCustomerId())
          && searchCustomer.searchCustomerById(coborrowerIDField2.getText(), connection) != null) {
        coborrowerIDField2.setStyle("");
      } else {
        coborrowerIDField2.setStyle("-fx-border-color: red");
        isValid = false;
      }
    }
    if (!coborrowerIDField3.getText().isEmpty()) {
      if (!coborrowerIDField3.getText().equals(personalLoan.getCustomerId())
          && searchCustomer.searchCustomerById(coborrowerIDField3.getText(), connection) != null) {
        coborrowerIDField3.setStyle("");
      } else {
        coborrowerIDField3.setStyle("-fx-border-color: red");
        isValid = false;
      }
    }

    return isValid;
  }

  @Override
  protected void setLoanDetails() {
    if (!validateData()) {
      return;
    }

    ArrayList<String> ids = personalLoan.getCoborrowerIds();
    if (!coborrowerIDField1.getText().isEmpty()) {
      ids.set(0, coborrowerIDField1.getText());
    }
    if (!coborrowerIDField2.getText().isEmpty()) {
      ids.set(1, coborrowerIDField2.getText());
    }
    if (!coborrowerIDField3.getText().isEmpty()) {
      ids.set(2, coborrowerIDField3.getText());
    }
  }

  @Override
  public Button getButton() {
    return coborrowerButton;
  }
}
