package uoa.lavs.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.util.Pair;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.customer.Customer;
import uoa.lavs.backend.oop.loan.Loan;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanSummary;

public class AppState {
  // This Class will be used for the reinitilasiation of scenes on demand by
  // persisting the current
  // state of the application

  private static String userName;
  private static String customerDetailsAccessType; // CREATE, EDIT, VIEW
  private static Boolean isCreatingLoan = false;
  private static String loanDetailsAccessType; // CREATE, EDIT, VIEW
  private static Boolean isAccessingFromSearch = false;
  private static Boolean isAccessingFromLoanSearch = false;
  private static List<Customer> searchResultList;
  private static List<Loan> loanSearchResultList;
  private static Customer selectedCustomer;
  private static boolean isOnLoanSummary = false;
  private static LoadLoanSummary currentLoanSummary;
  private static Button currentButton;

  // Previous and current UI screens for return functionality
  private static AppUI previousUi;
  private static AppUI currentUiName;

  // Getters and Setters
  public static String getUserName() {
    return userName;
  }

  public static void setUserName(String userName) {
    AppState.userName = userName;
  }

  public static String getCustomerDetailsAccessType() {
    return customerDetailsAccessType;
  }

  public static void setCustomerDetailsAccessType(String customerDetailsAccessType) {
    AppState.customerDetailsAccessType = customerDetailsAccessType;
  }

  public static Boolean getIsCreatingLoan() {
    return isCreatingLoan;
  }

  public static void setIsCreatingLoan(Boolean isCreatingLoan) {
    AppState.isCreatingLoan = isCreatingLoan;
  }

  public static String getLoanDetailsAccessType() {
    return loanDetailsAccessType;
  }

  public static void setLoanDetailsAccessType(String loanDetailsAccessType) {
    AppState.loanDetailsAccessType = loanDetailsAccessType;
  }

  public static Boolean getIsAccessingFromSearch() {
    return isAccessingFromSearch;
  }

  public static void setIsAccessingFromSearch(Boolean isAccessingFromSearch) {
    AppState.isAccessingFromSearch = isAccessingFromSearch;
  }

  public static Boolean getIsAccessingFromLoanSearch() {
    return isAccessingFromLoanSearch;
  }

  public static void setIsAccessingFromLoanSearch(Boolean isAccessingFromLoanSearch) {
    AppState.isAccessingFromLoanSearch = isAccessingFromLoanSearch;
  }

  public static List<Customer> getSearchResultList() {
    return searchResultList;
  }

  public static void setSearchResultList(List<Customer> searchResultList) {
    AppState.searchResultList = searchResultList;
  }

  public static List<Loan> getLoanSearchResultList() {
    return loanSearchResultList;
  }

  public static void setLoanSearchResultList(List<Loan> loanSearchResultList) {
    AppState.loanSearchResultList = loanSearchResultList;
  }

  public static Customer getSelectedCustomer() {
    return selectedCustomer;
  }

  public static void setSelectedCustomer(Customer selectedCustomer) {
    AppState.selectedCustomer = selectedCustomer;
  }

  public static boolean getIsOnLoanSummary() {
    return isOnLoanSummary;
  }

  public static void setIsOnLoanSummary(boolean isOnLoanSummary) {
    AppState.isOnLoanSummary = isOnLoanSummary;
  }

  public static LoadLoanSummary getCurrentLoanSummary() {
    return currentLoanSummary;
  }

  public static void setCurrentLoanSummary(LoadLoanSummary currentLoanSummary) {
    AppState.currentLoanSummary = currentLoanSummary;
  }

  public static Button getCurrentButton() {
    return currentButton;
  }

  public static void setCurrentButton(Button currentButton) {
    AppState.currentButton = currentButton;
  }

  public static AppUI getPreviousUi() {
    return previousUi;
  }

  public static void setPreviousUi(AppUI previousUi) {
    AppState.previousUi = previousUi;
  }

  public static AppUI getCurrentUiName() {
    return currentUiName;
  }

  public static void setCurrentUiName(AppUI currentUiName) {
    AppState.currentUiName = currentUiName;
  }

  public static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(Main.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  public static void loadMainMenu() throws IOException {
    SceneManager.addScene(AppUI.MAIN_MENU, loadFxml("admin/welcome"));
    Main.setUi(AppUI.MAIN_MENU);
  }

  public static Pair<Parent, Object> loadFxmlDynamic(final String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/" + fxml + ".fxml"));
    Parent parent = loader.load();
    Object controller = loader.getController();
    System.out.println("Controller: " + controller);
    return new Pair<>(parent, controller);
  }

  public static void loadAllCustomerDetails(String accessType) throws IOException {
    customerDetailsAccessType = accessType;
    currentButton = null;

    AccessTypeNotifier.clearObservers();
    SceneManager.removeCustomerScenes();

    // Add all customer details scenes
    Pair<Parent, Object> pair;

    pair = loadFxmlDynamic("customer/customer_details/customer_details_details");
    SceneManager.addSceneWithController(AppUI.CI_DETAILS, pair.getKey(), pair.getValue());

    pair = loadFxmlDynamic("customer/customer_details/primary_address_details_details");
    SceneManager.addSceneWithController(AppUI.CI_PRIMARY_ADDRESS, pair.getKey(), pair.getValue());

    pair = loadFxmlDynamic("customer/customer_details/customer_contact_details");
    SceneManager.addSceneWithController(AppUI.CI_CONTACT, pair.getKey(), pair.getValue());

    pair = loadFxmlDynamic("customer/customer_details/customer_notes");
    SceneManager.addSceneWithController(AppUI.CI_NOTES, pair.getKey(), pair.getValue());

    pair = loadFxmlDynamic("customer/customer_details/employer_details_details");
    SceneManager.addSceneWithController(AppUI.CI_EMPLOYER, pair.getKey(), pair.getValue());

    pair = loadFxmlDynamic("customer/customer_details/employer_address_details");
    SceneManager.addSceneWithController(AppUI.CI_EMPLOYER_ADDRESS, pair.getKey(), pair.getValue());
  }

  public static void loadLoans(String accessType) throws IOException {
    loanDetailsAccessType = accessType;
    currentButton = null;
    AccessTypeNotifier.clearObservers();
    SceneManager.removeLoanScenes(); // Assuming there's a method to remove existing loan scenes

    // Add all loan details scenes
    Pair<Parent, Object> pair;

    pair = loadFxmlDynamic("loan/loan_details/coborrower_loan_details");
    SceneManager.addSceneWithController(AppUI.LC_COBORROWER, pair.getKey(), pair.getValue());

    pair = loadFxmlDynamic("loan/loan_details/duration_loan_details");
    SceneManager.addSceneWithController(AppUI.LC_DURATION, pair.getKey(), pair.getValue());

    pair = loadFxmlDynamic("loan/loan_details/finance_loan_details");
    SceneManager.addSceneWithController(AppUI.LC_FINANCE, pair.getKey(), pair.getValue());

    pair = loadFxmlDynamic("loan/loan_details/primary_loan_details");
    SceneManager.addSceneWithController(AppUI.LC_PRIMARY, pair.getKey(), pair.getValue());

    loadLoanSummary(accessType);
  }

  public static void loadLoanSummary(String accessType) throws IOException {
    loanDetailsAccessType = accessType;
    SceneManager.addScene(AppUI.LC_SUMMARY, loadFxml("loan/loan_details/loan_summary"));
    Main.setUi(AppUI.LC_SUMMARY);
  }

  public static void loadCustomerSearchResults(List<Customer> searchResultList) throws IOException {
    AppState.searchResultList = searchResultList;
    SceneManager.addScene(AppUI.CUSTOMER_RESULTS, loadFxml("customer/customer_search_results"));
    Main.setUi(AppUI.CUSTOMER_RESULTS);
  }

  public static void loadLoanSearchResults(String searchString) throws IOException {
    // Actual search logic needed here
    SceneManager.addScene(AppUI.LOAN_RESULTS, loadFxml("loan/loan_search_results"));
    Main.setUi(AppUI.LOAN_RESULTS);
  }

  public static void loanLoanRepayments() throws IOException {
    SceneManager.addScene(AppUI.PAYMENT_MENU, loadFxml("loan/loan_repayments"));
    Main.setUi(AppUI.PAYMENT_MENU);
  }

  // Credit for country list: https://gist.github.com/rogargon/5534902
  public static String[] getAllCountries() {
    List<String> countries = new ArrayList<>();
    try (BufferedReader br =
        new BufferedReader(
            new InputStreamReader(AppState.class.getResourceAsStream("/country.txt")))) {
      String line;
      while ((line = br.readLine()) != null) {
        countries.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return countries.toArray(new String[0]);
  }
}
