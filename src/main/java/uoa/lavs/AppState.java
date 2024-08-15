package uoa.lavs;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import uoa.lavs.SceneManager.AppUI;

public class AppState {
  // This Class will be used for the reinitilasiation of scenes on demand by persisting the current
  // state of the application

  public static String userName;
  public static String customerDetailsAccessType; // CREATE, EDIT, VIEW, LOAN-EDIT, LOAN-VIEW
  public static Boolean isCreatingLoan;
  public static String loanDetailsAccessType; // CREATE, EDIT, VIEW

  public static Parent loadFxml(final String fxml) throws IOException {
    String fxmlPath = "/fxml/" + fxml + ".fxml";
    System.out.println("Loading FXML from path: " + fxmlPath);
    return new FXMLLoader(Main.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  public static void loadAllCustomerDetails(String accessType) throws IOException {
    customerDetailsAccessType = accessType;
    // Add all customer details scenes
    SceneManager.addScene(
        AppUI.CI_DETAILS, loadFxml("customer/customer_details/customer_details_details"));
    SceneManager.addScene(
        AppUI.CI_PRIMARY_ADDRESS,
        loadFxml("customer/customer_details/primary_address_details_details"));
    SceneManager.addScene(
        AppUI.CI_MAILING_ADDRESS,
        loadFxml("customer/customer_details/mailing_address_details_details"));
    SceneManager.addScene(
        AppUI.CI_CONTACT, loadFxml("customer/customer_details/customer_contact_details"));
    SceneManager.addScene(AppUI.CI_NOTES, loadFxml("customer/customer_details/customer_notes"));
    SceneManager.addScene(
        AppUI.CI_EMPLOYER, loadFxml("customer/customer_details/employer_details_details"));
    SceneManager.addScene(
        AppUI.CI_EMPLOYER_ADDRESS, loadFxml("customer/customer_details/employer_address_details"));
  }

  public static void loadLoans() throws IOException {
    // Add all loan scenes
    SceneManager.addScene(AppUI.LC_COBORROWER, loadFxml("loan/coborrower_loan_details"));
    SceneManager.addScene(AppUI.LC_DURATION, loadFxml("loan/duration_loan_details"));
    SceneManager.addScene(AppUI.LC_FINANCE, loadFxml("loan/finance_loan_details"));
    SceneManager.addScene(AppUI.LC_PRIMARY, loadFxml("loan/primary_loan_details"));
    SceneManager.addScene(AppUI.LC_SUMMARY, loadFxml("loan/loan_summary"));
  }

  public static void loadCustomerSearchResults(String searchString) throws IOException {
    // Actual search logic needed here
    SceneManager.addScene(AppUI.CUSTOMER_RESULTS, loadFxml("customer/customer_search_results"));
    Main.setUi(AppUI.CUSTOMER_RESULTS);
  }

  public static void loadLoanSearchResults(String searchString) throws IOException {
    // Actual search logic needed here
    SceneManager.addScene(AppUI.LOAN_RESULTS, loadFxml("loan/loan_search_results"));
    Main.setUi(AppUI.LOAN_RESULTS);
  }
}
