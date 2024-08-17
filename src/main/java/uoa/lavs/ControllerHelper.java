package uoa.lavs;

import java.util.Random;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ControllerHelper {
  private static final Random random = new Random();

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

  public static int generateUniqueCustomerId() {
    return random.nextInt(1000000); // Generates a random ID between 0 and 999999
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
