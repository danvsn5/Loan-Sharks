package uoa.lavs.frontend;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ControllerHelper {

  // changes the UI based on the access type, either clearing fields or making
  // them editable/non-editable
  public static void updateUIBasedOnAccessType(
      String accessType,
      Button editButton,
      Label idBanner,
      TextField[] textFields,
      ComboBox<?>[] comboBoxes,
      DatePicker[] datePickers,
      RadioButton[] radioButtons) {

    if (accessType.equals("CREATE")) {
      resetFields(textFields);
      resetComboBoxes(comboBoxes);
      resetDatePickers(datePickers);
      resetRadioButtons(radioButtons);

      setFieldsEditable(textFields);
      setComboBoxesEditable(comboBoxes);
      setDatePickersEditable(datePickers);
      setRadioButtonsEditable(radioButtons);

      editButton.setText("Create Customer");
      idBanner.setText("Creating Customer");
    } else if (accessType.equals("EDIT")) {
      setFieldsEditable(textFields);
      setComboBoxesEditable(comboBoxes);
      setDatePickersEditable(datePickers);
      setRadioButtonsEditable(radioButtons);

      editButton.setText("Confirm Changes");
      idBanner.setText("Editing Customer Details");
    } else if (accessType.equals("VIEW")) {
      setFieldsNonEditable(textFields);
      setComboBoxesNonEditable(comboBoxes);
      setDatePickersNonEditable(datePickers);
      setRadioButtonsNonEditable(radioButtons);

      idBanner.setText("Viewing Customer Details");
      editButton.setText("Edit Details");
    }
  }

  // following logic from above but with all loan tabs
  public static void updateUIBasedOnAccessTypeLoan(
      String accessType,
      Button editButton,
      TextField[] textFields,
      ComboBox<?>[] comboBoxes,
      DatePicker[] datePickers,
      RadioButton[] radioButtons) {

    if (accessType.equals("CREATE")) {
      resetFields(textFields);
      resetComboBoxes(comboBoxes);
      resetDatePickers(datePickers);
      resetRadioButtons(radioButtons);

      setFieldsEditable(textFields);
      setComboBoxesEditable(comboBoxes);
      setDatePickersEditable(datePickers);
      setRadioButtonsEditable(radioButtons);

      editButton.visibleProperty().setValue(false);
    } else if (accessType.equals("EDIT")) {
      setFieldsEditable(textFields);
      setComboBoxesEditable(comboBoxes);
      setDatePickersEditable(datePickers);
      setRadioButtonsEditable(radioButtons);

      editButton.visibleProperty().setValue(false);
    } else if (accessType.equals("VIEW")) {
      setFieldsNonEditable(textFields);
      setComboBoxesNonEditable(comboBoxes);
      setDatePickersNonEditable(datePickers);
      setRadioButtonsNonEditable(radioButtons);

      editButton.visibleProperty().setValue(true);
      editButton.setText("Edit Details");
    }
  }

  // below code is for resetting fields, combo boxes, date pickers, and radio
  // buttons, as well as setting their opacity and disabling them

  public static void resetFields(TextField... textFields) {
    for (TextField textField : textFields) {
      textField.setText("");
    }
  }

  public static void resetComboBoxes(ComboBox<?>... comboBoxes) {
    for (ComboBox<?> comboBox : comboBoxes) {
      comboBox.setValue(null);
    }
  }

  public static void resetDatePickers(DatePicker... datePickers) {
    for (DatePicker datePicker : datePickers) {
      datePicker.setValue(null);
    }
  }

  public static void resetRadioButtons(RadioButton... radioButtons) {
    for (RadioButton radioButton : radioButtons) {
      radioButton.setSelected(false);
    }
  }

  public static void setFieldsEditable(TextField... textFields) {
    for (TextField textField : textFields) {
      textField.setEditable(true);
      textField.setDisable(false);
      textField.setOpacity(1);
    }
  }

  public static void setComboBoxesEditable(ComboBox<?>... comboBoxes) {
    for (ComboBox<?> comboBox : comboBoxes) {
      comboBox.setDisable(false);
      comboBox.setOpacity(1);
    }
  }

  public static void setDatePickersEditable(DatePicker... datePickers) {
    for (DatePicker datePicker : datePickers) {
      datePicker.setDisable(false);
      datePicker.setOpacity(1);
    }
  }

  public static void setRadioButtonsEditable(RadioButton... radioButtons) {
    for (RadioButton radioButton : radioButtons) {
      radioButton.setDisable(false);
      radioButton.setOpacity(1);
    }
  }

  public static void setFieldsNonEditable(TextField... textFields) {
    for (TextField textField : textFields) {
      textField.setEditable(false);
      textField.setDisable(true);
      textField.setOpacity(0.8);
    }
  }

  public static void setComboBoxesNonEditable(ComboBox<?>... comboBoxes) {
    for (ComboBox<?> comboBox : comboBoxes) {
      comboBox.setDisable(true);
      comboBox.setOpacity(0.8);

    }
  }

  public static void setDatePickersNonEditable(DatePicker... datePickers) {
    for (DatePicker datePicker : datePickers) {
      datePicker.setDisable(true);
      datePicker.setOpacity(0.8);
    }
  }

  public static void setRadioButtonsNonEditable(RadioButton... radioButtons) {
    for (RadioButton radioButton : radioButtons) {
      radioButton.setDisable(true);
      radioButton.setOpacity(0.8);
    }
  }
}
