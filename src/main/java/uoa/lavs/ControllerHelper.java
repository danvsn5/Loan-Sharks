package uoa.lavs;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ControllerHelper {

  public static void updateUIBasedOnAccessType(
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

      editButton.setText("Create Customer");
    } else if (accessType.equals("EDIT")) {
      setFieldsEditable(textFields);
      setComboBoxesEditable(comboBoxes);
      setDatePickersEditable(datePickers);
      setRadioButtonsEditable(radioButtons);

      editButton.setText("Confirm Changes");
    } else if (accessType.equals("VIEW")) {
      setFieldsNonEditable(textFields);
      setComboBoxesNonEditable(comboBoxes);
      setDatePickersNonEditable(datePickers);
      setRadioButtonsNonEditable(radioButtons);

      editButton.setText("Edit Details");
    }
  }

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
    }
  }

  public static void setComboBoxesEditable(ComboBox<?>... comboBoxes) {
    for (ComboBox<?> comboBox : comboBoxes) {
      comboBox.setDisable(false);
    }
  }

  public static void setDatePickersEditable(DatePicker... datePickers) {
    for (DatePicker datePicker : datePickers) {
      datePicker.setDisable(false);
    }
  }

  public static void setRadioButtonsEditable(RadioButton... radioButtons) {
    for (RadioButton radioButton : radioButtons) {
      radioButton.setDisable(false);
    }
  }

  public static void setFieldsNonEditable(TextField... textFields) {
    for (TextField textField : textFields) {
      textField.setEditable(false);
      textField.setDisable(true);
    }
  }

  public static void setComboBoxesNonEditable(ComboBox<?>... comboBoxes) {
    for (ComboBox<?> comboBox : comboBoxes) {
      comboBox.setDisable(true);
    }
  }

  public static void setDatePickersNonEditable(DatePicker... datePickers) {
    for (DatePicker datePicker : datePickers) {
      datePicker.setDisable(true);
    }
  }

  public static void setRadioButtonsNonEditable(RadioButton... radioButtons) {
    for (RadioButton radioButton : radioButtons) {
      radioButton.setDisable(true);
    }
  }
}
