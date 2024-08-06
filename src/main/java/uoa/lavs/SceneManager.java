package uoa.lavs;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {
  public enum AppUI {
    CI_ADDRESS,
    CI_CONFIRM,
    CI_CONTACT,
    CI_DETAILS,
    CI_EMPLOYER,
    CI_NOTES,
    CUSTOMER_MENU,
    CUSTOMER_RESULTS,
    CUSTOMER_SEARCH,
    CUSTOMER_SUMMARY,
    LC_CUSTOMER_DETAILS,
    LC_CUSTOMER_RESULTS,
    LC_SEARCH,
    LC_INPUT,
    LOAN_DETAILS,
    LOAN_MENU,
    LOAN_RESULTS,
    LOAN_SEARCH,
    LOGIN,
    MAIN_MENU,
    PAYMENT_MENU,
  }

  private static HashMap<AppUI, Parent> sceneMap = new HashMap<AppUI, Parent>();

  public static void addScene(AppUI scene, Parent parent) {
    sceneMap.put(scene, parent);
  }

  public static Parent getScene(AppUI scene) {
    return sceneMap.get(scene);
  }
}
