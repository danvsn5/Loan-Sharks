package uoa.lavs.frontend;

import javafx.scene.control.Button;

public interface AccessTypeObserverLoan {
  void updateUIBasedOnAccessType();

  boolean validateData();

  Button getButton();

  void setInvalidButton(String string);
}
