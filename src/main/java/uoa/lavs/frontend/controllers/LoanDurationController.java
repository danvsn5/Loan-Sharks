package uoa.lavs.frontend.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import uoa.lavs.backend.oop.loan.LoanDuration;
import uoa.lavs.frontend.AccessTypeNotifier;
import uoa.lavs.frontend.AccessTypeObserver;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.ControllerHelper;

public class LoanDurationController extends AbstractLoanController implements AccessTypeObserver {
  @FXML private DatePicker startDatePicker;
  @FXML private TextField periodField;
  @FXML private TextField termField;

  @FXML
  private void initialize() {
    AccessTypeNotifier.registerLoanObserver(this);
    updateUIBasedOnAccessType();

    DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    startDatePicker.setConverter(
        new StringConverter<LocalDate>() {
          @Override
          public String toString(LocalDate date) {
            if (date != null) {
              return displayFormatter.format(date);
            } else {
              return "";
            }
          }

          @Override
          public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
              LocalDate date = LocalDate.parse(string, displayFormatter);
              return LocalDate.parse(date.format(storageFormatter), storageFormatter);
            } else {
              return null;
            }
          }
        });

    termField.setDisable(true);

    personalLoan.setCustomerId(AppState.getSelectedCustomer().getCustomerId());
    termField.setText("30");

    // Set dummy values
    if (AppState.getLoanDetailsAccessType().equals("CREATE")) {
      startDatePicker.setValue(LocalDate.now());
      periodField.setText("30");
    }
  }

  // Add methods for all buttons

  @FXML
  @Override
  public void updateUIBasedOnAccessType() {
    ControllerHelper.updateUIBasedOnAccessTypeLoan(
        AppState.getLoanDetailsAccessType(),
        editButton,
        new TextField[] {periodField, termField},
        new ComboBox<?>[] {},
        new DatePicker[] {startDatePicker},
        new RadioButton[] {});
    // TODO set dration details onto the screen if not create type.
    // setLoanDetails();
  }

  @Override
  public boolean validateData() {
    // Add validation code here
    boolean isValid = true;

    startDatePicker.setStyle("");
    periodField.setStyle("");

    if (startDatePicker.getValue() == null) {
      startDatePicker.setStyle("-fx-border-color: red");
      isValid = false;
    }

    if (periodField.getText().isEmpty()
        || !periodField.getText().matches("[0-9]+")
        || periodField.getText().length() > 5) {
      periodField.setStyle("-fx-border-color: red");
      isValid = false;
    }

    return isValid;
  }

  @Override
  protected void setLoanDetails() {
    if (!validateData()) {
      return;
    }

    LoanDuration loanDuration = personalLoan.getDuration();
    loanDuration.setStartDate(startDatePicker.getValue());
    loanDuration.setPeriod(Integer.parseInt(periodField.getText()));
    loanDuration.setLoanTerm(30);
  }

  @Override
  public Button getButton() {
    return durationButton;
  }
}
