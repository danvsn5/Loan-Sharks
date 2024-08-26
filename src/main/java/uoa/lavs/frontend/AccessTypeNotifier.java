package uoa.lavs.frontend;

import java.util.ArrayList;
import java.util.List;

public class AccessTypeNotifier {
  private static List<AccessTypeObserver> customerObservers = new ArrayList<>();
  private static List<AccessTypeObserver> loanObservers = new ArrayList<>();

  // static observer methods run through all controller classes to check if the data
  // if valid; used during creating/editing customers and loans to change tabs visuals
  // if invalid data is submitted

  public static void registerCustomerObserver(AccessTypeObserver observer) {
    customerObservers.add(observer);
  }

  public static void registerLoanObserver(AccessTypeObserver observer) {
    loanObservers.add(observer);
  }

  private static boolean validateObservers(List<AccessTypeObserver> observers) {
    boolean isValid = true;
    for (AccessTypeObserver observer : observers) {
      if (!observer.validateData()) {
        AppState.setCurrentButton(observer.getButton());
        for (AccessTypeObserver disableObserver : observers) {
          disableObserver.setInvalidButton("-fx-border-color: red;");
        }
        isValid = false;
      } else {
        AppState.setCurrentButton(observer.getButton());
        for (AccessTypeObserver disableObserver : observers) {
          disableObserver.setInvalidButton("");
        }
      }
    }
    return isValid;
  }

  public static boolean validateCustomerObservers() {
    return validateObservers(customerObservers);
  }

  public static boolean validateLoanObservers() {
    return validateObservers(loanObservers);
  }

  public static void notifyCustomerObservers() {
    for (AccessTypeObserver observer : customerObservers) {
      observer.updateUIBasedOnAccessType();
    }
  }

  public static void notifyLoanObservers() {
    for (AccessTypeObserver observer : loanObservers) {
      observer.updateUIBasedOnAccessType();
    }
  }

  public static void clearObservers() {
    customerObservers.clear();
    loanObservers.clear();
  }
}
