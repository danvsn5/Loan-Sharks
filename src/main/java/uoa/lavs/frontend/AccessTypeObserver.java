package uoa.lavs.frontend;

import javafx.scene.control.Button;

public interface AccessTypeObserver {
  void updateUIBasedOnAccessType();

  boolean validateData();

  Button getButton();

  void setInvalidButton(String style);
}
