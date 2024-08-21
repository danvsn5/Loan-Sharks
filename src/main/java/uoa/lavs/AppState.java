package uoa.lavs;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import uoa.lavs.SceneManager.AppUI;

public class AppState {
  // This Class will be used for the reinitilasiation of scenes on demand by persisting the current
  // state of the application

  public static String userName;
  public static String customerDetailsAccessType; // CREATE, EDIT, VIEW
  public static Boolean isCreatingLoan = false;
  public static String loanDetailsAccessType; // CREATE, EDIT, VIEW
  public static Boolean isAccessingFromSearch = false;
  public static Boolean isAccessingFromLoanSearch = false;
  public static List<?> searchResultList;

  // Previous and current UI screens for return functionality
  public static AppUI previousUi;
  public static AppUI currentUiName;

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
        AppUI.CI_CONTACT, loadFxml("customer/customer_details/customer_contact_details"));
    SceneManager.addScene(AppUI.CI_NOTES, loadFxml("customer/customer_details/customer_notes"));
    SceneManager.addScene(
        AppUI.CI_EMPLOYER, loadFxml("customer/customer_details/employer_details_details"));
    SceneManager.addScene(
        AppUI.CI_EMPLOYER_ADDRESS, loadFxml("customer/customer_details/employer_address_details"));
  }

  public static void loadLoans(String accessType) throws IOException {
    loanDetailsAccessType = accessType;
    // Add all loan scenes
    SceneManager.addScene(
        AppUI.LC_COBORROWER, loadFxml("loan/loan_details/coborrower_loan_details"));
    SceneManager.addScene(AppUI.LC_DURATION, loadFxml("loan/loan_details/duration_loan_details"));
    SceneManager.addScene(AppUI.LC_FINANCE, loadFxml("loan/loan_details/finance_loan_details"));
    SceneManager.addScene(AppUI.LC_PRIMARY, loadFxml("loan/loan_details/primary_loan_details"));
    SceneManager.addScene(AppUI.LC_SUMMARY, loadFxml("loan/loan_details/loan_summary"));
  }

  public static void loadCustomerSearchResults(List<?> searchResultList) throws IOException {

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

  public static AppUI getCurrentUiName() {
    return currentUiName;
  }

  public static String[] getAllCountries() {
    String[] countries = {
      "New Zealand",
      "Australia",
      "Afghanistan",
      "Albania",
      "Algeria",
      "American Samoa",
      "Andorra",
      "Angola",
      "Anguilla",
      "Antarctica",
      "Antigua and/or Barbuda",
      "Argentina",
      "Armenia",
      "Aruba",
      "Austria",
      "Azerbaijan",
      "Bahamas",
      "Bahrain",
      "Bangladesh",
      "Barbados",
      "Belarus",
      "Belgium",
      "Belize",
      "Benin",
      "Bermuda",
      "Bhutan",
      "Bolivia",
      "Bosnia and Herzegovina",
      "Botswana",
      "Bouvet Island",
      "Brazil",
      "British Indian Ocean Territory",
      "Brunei Darussalam",
      "Bulgaria",
      "Burkina Faso",
      "Burundi",
      "Cambodia",
      "Cameroon",
      "Canada",
      "Cape Verde",
      "Cayman Islands",
      "Central African Republic",
      "Chad",
      "Chile",
      "China",
      "Christmas Island",
      "Cocos (Keeling) Islands",
      "Colombia",
      "Comoros",
      "Congo",
      "Cook Islands",
      "Costa Rica",
      "Croatia (Hrvatska)",
      "Cuba",
      "Cyprus",
      "Czech Republic",
      "Denmark",
      "Djibouti",
      "Dominica",
      "Dominican Republic",
      "East Timor",
      "Ecudaor",
      "Egypt",
      "El Salvador",
      "Equatorial Guinea",
      "Eritrea",
      "Estonia",
      "Ethiopia",
      "Falkland Islands (Malvinas)",
      "Faroe Islands",
      "Fiji",
      "Finland",
      "France",
      "France, Metropolitan",
      "French Guiana",
      "French Polynesia",
      "French Southern Territories",
      "Gabon",
      "Gambia",
      "Georgia",
      "Germany",
      "Ghana",
      "Gibraltar",
      "Greece",
      "Greenland",
      "Grenada",
      "Guadeloupe",
      "Guam",
      "Guatemala",
      "Guinea",
      "Guinea-Bissau",
      "Guyana",
      "Haiti",
      "Heard and Mc Donald Islands",
      "Honduras",
      "Hong Kong",
      "Hungary",
      "Iceland",
      "India",
      "Indonesia",
      "Iran (Islamic Republic of)",
      "Iraq",
      "Ireland",
      "Israel",
      "Italy",
      "Ivory Coast",
      "Jamaica",
      "Japan",
      "Jordan",
      "Kazakhstan",
      "Kenya",
      "Kiribati",
      "Korea, Democratic People's Republic of",
      "Korea, Republic of",
      "Kosovo",
      "Kuwait",
      "Kyrgyzstan",
      "Lao People's Democratic Republic",
      "Latvia",
      "Lebanon",
      "Lesotho",
      "Liberia",
      "Libyan Arab Jamahiriya",
      "Liechtenstein",
      "Lithuania",
      "Luxembourg",
      "Macau",
      "Macedonia",
      "Madagascar",
      "Malawi",
      "Malaysia",
      "Maldives",
      "Mali",
      "Malta",
      "Marshall Islands",
      "Martinique",
      "Mauritania",
      "Mauritius",
      "Mayotte",
      "Mexico",
      "Micronesia, Federated States of",
      "Moldova, Republic of",
      "Monaco",
      "Mongolia",
      "Montserrat",
      "Morocco",
      "Mozambique",
      "Myanmar",
      "Namibia",
      "Nauru",
      "Nepal",
      "Netherlands",
      "Netherlands Antilles",
      "New Caledonia",
      "Nicaragua",
      "Niger",
      "Nigeria",
      "Niue",
      "Norfork Island",
      "Northern Mariana Islands",
      "Norway",
      "Oman",
      "Pakistan",
      "Palau",
      "Panama",
      "Papua New Guinea",
      "Paraguay",
      "Peru",
      "Philippines",
      "Pitcairn",
      "Poland",
      "Portugal",
      "Puerto Rico",
      "Qatar",
      "Reunion",
      "Romania",
      "Russian Federation",
      "Rwanda",
      "Saint Kitts and Nevis",
      "Saint Lucia",
      "Saint Vincent and the Grenadines",
      "Samoa",
      "San Marino",
      "Sao Tome and Principe",
      "Saudi Arabia",
      "Senegal",
      "Seychelles",
      "Sierra Leone",
      "Singapore",
      "Slovakia",
      "Slovenia",
      "Solomon Islands",
      "Somalia",
      "South Africa",
      "South Georgia South Sandwich Islands",
      "South Sudan",
      "Spain",
      "Sri Lanka",
      "St. Helena",
      "St. Pierre and Miquelon",
      "Sudan",
      "Suriname",
      "Svalbarn and Jan Mayen Islands",
      "Swaziland",
      "Sweden",
      "Switzerland",
      "Syrian Arab Republic",
      "Taiwan",
      "Tajikistan",
      "Tanzania, United Republic of",
      "Thailand",
      "Togo",
      "Tokelau",
      "Tonga",
      "Trinidad and Tobago",
      "Tunisia",
      "Turkey",
      "Turkmenistan",
      "Turks and Caicos Islands",
      "Tuvalu",
      "Uganda",
      "Ukraine",
      "United Arab Emirates",
      "United Kingdom",
      "United States",
      "United States minor outlying islands",
      "Uruguay",
      "Uzbekistan",
      "Vanuatu",
      "Vatican City State",
      "Venezuela",
      "Vietnam",
      "Virigan Islands (British)",
      "Virgin Islands (U.S.)",
      "Wallis and Futuna Islands",
      "Western Sahara",
      "Yemen",
      "Yugoslavia",
      "Zaire",
      "Zambia",
      "Zimbabwe"
    };
    return countries;
  }

  public static void setCurrentUiName(AppUI currentUi) {
    AppState.currentUiName = currentUi;
  }

  public static AppUI getPreviousUi() {
    return previousUi;
  }

  public static void setPreviousUi(AppUI previousUi) {
    AppState.previousUi = previousUi;
  }
}
