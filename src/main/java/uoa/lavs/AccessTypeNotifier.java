package uoa.lavs;

import java.util.ArrayList;
import java.util.List;

public class AccessTypeNotifier {
  private static List<AccessTypeObserver> customerObservers = new ArrayList<>();

  public static void registerCustomerObserver(AccessTypeObserver observer) {
    System.out.println("Registering observer");
    customerObservers.add(observer);
  }

  public static void notifyCustomerObservers() {
    for (AccessTypeObserver observer : customerObservers) {
      System.out.println("Notifying observer");
      observer.updateUIBasedOnAccessType();
    }
  }
}
