package uoa.lavs.frontend;

import java.util.ArrayList;
import java.util.List;

public class AccessTypeNotifier {
  private static List<AccessTypeObserver> customerObservers = new ArrayList<>();
  private static List<AccessTypeObserverLoan> loanObservers = new ArrayList<>();

  public static void registerCustomerObserver(AccessTypeObserver observer) {
    customerObservers.add(observer);
  }

  public static void registerLoanObserver(AccessTypeObserverLoan observer) {
    loanObservers.add(observer);
  }

  public static boolean validateCustomerObservers() {
    boolean isValid = true;
    for (AccessTypeObserver observer : customerObservers) {
      if (!observer.validateData()) {
        AppState.setCurrentButton(observer.getButton());
        for (AccessTypeObserver disableObserer : customerObservers) {
          disableObserer.setInvalidButton("-fx-border-color: red;");
        }
        isValid = false;
      } else {
        AppState.setCurrentButton(observer.getButton());
        for (AccessTypeObserver disableObserer : customerObservers) {
          disableObserer.setInvalidButton("");
        }
      }
    }
    return isValid;
  }

  public static boolean validateLoanObservers() {
    boolean isValid = true;
    for (AccessTypeObserverLoan observer : loanObservers) {
      if (!observer.validateData()) {
        AppState.setCurrentButton(observer.getButton());
        for (AccessTypeObserverLoan disableObserer : loanObservers) {
          disableObserer.setInvalidButton("-fx-border-color: red;");
        }
        isValid = false;
      } else {
        AppState.setCurrentButton(observer.getButton());
        for (AccessTypeObserverLoan disableObserer : loanObservers) {
          disableObserer.setInvalidButton("");
        }
      }
    }
    return isValid;
  }

  public static void notifyCustomerObservers() {
    for (AccessTypeObserver observer : customerObservers) {
      observer.updateUIBasedOnAccessType();
    }
  }

  public static void notifyLoanObservers() {
    for (AccessTypeObserverLoan observer : loanObservers) {
      observer.updateUIBasedOnAccessType();
    }
  }

  public static void clearObservers() {
    customerObservers.clear();
    loanObservers.clear();
  }
}
