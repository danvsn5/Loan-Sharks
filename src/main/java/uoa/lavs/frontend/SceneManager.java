package uoa.lavs.frontend;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {
  // enum for the different scenes in the application
  public enum AppUI {
    CI_PRIMARY_ADDRESS,
    CI_MAILING_ADDRESS,
    CI_CONFIRM,
    CI_CONTACT,
    CI_DETAILS,
    CI_EMPLOYER,
    CI_EMPLOYER_ADDRESS,
    CI_NOTES,
    CUSTOMER_MENU,
    CUSTOMER_RESULTS,
    CUSTOMER_SEARCH,
    CUSTOMER_SUMMARY,
    LC_SEARCH,
    LC_SUMMARY,
    LC_COBORROWER,
    LC_DURATION,
    LC_FINANCE,
    LC_PRIMARY,
    LOAN_MENU,
    LOAN_RESULTS,
    LOAN_SEARCH,
    LOGIN,
    MAIN_MENU,
    PAYMENT_MENU,
    INFORMATION
  }

  // maps to store the scenes and controllers for when they are instantiated in
  // the heap
  private static HashMap<AppUI, Parent> sceneMap = new HashMap<AppUI, Parent>();

  private static HashMap<AppUI, Object> controllerMap = new HashMap<>();

  // add a scene to the scene map
  public static void addScene(AppUI scene, Parent parent) {
    sceneMap.put(scene, parent);
  }

  // add a scene with a controller to the scene map; used for tab scenes so that
  // controller overlapping does not occur
  public static void addSceneWithController(AppUI scene, Parent parent, Object controller) {
    sceneMap.put(scene, parent);
    controllerMap.put(scene, controller);
  }

  public static Parent getScene(AppUI scene) {
    return sceneMap.get(scene);
  }

  public static void removeCustomerScenes() {
    System.out.println("Removing customer scenes");
    for (AppUI ui : AppUI.values()) {
      if (ui.name().startsWith("CI_")) {
        System.out.println("Removing scene: " + ui.name());
        sceneMap.remove(ui);
        controllerMap.remove(ui);
      }
    }
    System.out.println("Customer scenes removed: " + sceneMap.size());
    System.out.println("Customer controllers removed: " + controllerMap.size());
    System.gc();
  }

  public static void removeLoanScenes() {
    System.out.println("Removing loan scenes");
    for (AppUI ui : AppUI.values()) {
      if (ui.name().startsWith("LC_")) {
        System.out.println("Removing scene: " + ui.name());
        sceneMap.remove(ui);
        System.out.println("Removing controller: " + ui.name());
        controllerMap.remove(ui);
      }
    }
    System.out.println("Loan scenes removed: " + sceneMap.size());
    System.out.println("Loan controllers removed: " + controllerMap.size());
    System.gc();
  }
}
