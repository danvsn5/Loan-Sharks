package uoa.lavs;

import java.util.ArrayList;
import java.util.List;

public class AccessTypeNotifier {
  private static List<AccessTypeObserver> customerObservers = new ArrayList<>();
  private static List<AccessTypeObserver> loanObservers = new ArrayList<>();

  public static void registerCustomerObserver(AccessTypeObserver observer) {
    customerObservers.add(observer);
  }

  public static void registerLoanObserver(AccessTypeObserver observer) {
    loanObservers.add(observer);
  }

  public static boolean validateCustomerObservers() {
    boolean isValid = true;
    for (AccessTypeObserver observer : customerObservers) {
      if (!observer.validateData()) {
        isValid = false;
      }
    }
    return isValid;
  }

  public static boolean validateLoanObservers() {
    boolean isValid = true;
    for (AccessTypeObserver observer : loanObservers) {
      if (!observer.validateData()) {
        isValid = false;
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
    for (AccessTypeObserver observer : loanObservers) {
      observer.updateUIBasedOnAccessType();
    }
  }
}
