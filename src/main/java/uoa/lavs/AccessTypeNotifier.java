package uoa.lavs;

import java.util.ArrayList;
import java.util.List;

public class AccessTypeNotifier {
  private static List<AccessTypeObserver> observers = new ArrayList<>();

  public static void registerObserver(AccessTypeObserver observer) {
    System.out.println("Registering observer");
    observers.add(observer);
  }

  public static void notifyObservers() {
    for (AccessTypeObserver observer : observers) {
      System.out.println("Notifying observer");
      observer.updateUIBasedOnAccessType();
    }
  }
}
