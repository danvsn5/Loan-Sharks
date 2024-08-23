package uoa.lavs;

import javafx.scene.control.Button;

public interface AccessTypeObserverLoan {
  void updateUIBasedOnAccessType();

  boolean validateData();

  Button getButton();

  void setInvalidButton(String string);
}
