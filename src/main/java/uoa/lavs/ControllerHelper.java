package uoa.lavs;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ControllerHelper {

  public static void updateUIBasedOnAccessType(
      String accessType,
      Button editButton,
      TextField[] textFields,
      ComboBox<?>[] comboBoxes,
      DatePicker[] datePickers) {

    if (accessType.equals("CREATE")) {
      resetFields(textFields);
      resetComboBoxes(comboBoxes);
      resetDatePickers(datePickers);

      setFieldsEditable(textFields);
      setComboBoxesEditable(comboBoxes);
      setDatePickersEditable(datePickers);

      editButton.setText("Create Customer");
    } else if (accessType.equals("EDIT")) {
      setFieldsEditable(textFields);
      setComboBoxesEditable(comboBoxes);
      setDatePickersEditable(datePickers);

      editButton.setText("Confirm Changes");
    } else if (accessType.equals("VIEW")) {
      setFieldsNonEditable(textFields);
      setComboBoxesNonEditable(comboBoxes);
      setDatePickersNonEditable(datePickers);

      editButton.setText("Edit Customer");
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
}
