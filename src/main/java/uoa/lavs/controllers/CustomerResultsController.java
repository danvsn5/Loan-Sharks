package uoa.lavs.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import uoa.lavs.AppState;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class CustomerResultsController {
  // make labels for name and ID, 6 each
  @FXML private Label searchResultsNameLabel1;
  @FXML private Label searchResultsNameLabel2;
  @FXML private Label searchResultsNameLabel3;
  @FXML private Label searchResultsNameLabel4;
  @FXML private Label searchResultsNameLabel5;
  @FXML private Label searchResultsNameLabel6;

  @FXML private Label searchResultsIDLabel1;
  @FXML private Label searchResultsIDLabel2;
  @FXML private Label searchResultsIDLabel3;
  @FXML private Label searchResultsIDLabel4;
  @FXML private Label searchResultsIDLabel5;
  @FXML private Label searchResultsIDLabel6;

  // make rectangles, 6 each
  @FXML private Rectangle searchResultsRectangle1;
  @FXML private Rectangle searchResultsRectangle2;
  @FXML private Rectangle searchResultsRectangle3;
  @FXML private Rectangle searchResultsRectangle4;
  @FXML private Rectangle searchResultsRectangle5;
  @FXML private Rectangle searchResultsRectangle6;

  @FXML private Button nextButton;
  @FXML private ImageView staticReturnImageView;

  ArrayList<String> searchResultsNameList = new ArrayList<>();
  ArrayList<String> searchResultsIDList = new ArrayList<>();

  // write for all fxml elements
  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.CUSTOMER_SEARCH);
  }

  @FXML
  private void handleNextButtonAction() {
    // Add logic to cycle between search results
  }

  // add methods for all buttons and rectangles
  @FXML
  private void handleSearchResultsRectangle1Action() throws IOException {
    loadCustomer(1);
  }

  @FXML
  private void handleSearchResultsRectangle2Action() throws IOException {
    loadCustomer(2);
  }

  @FXML
  private void handleSearchResultsRectangle3Action() throws IOException {
    loadCustomer(3);
  }

  @FXML
  private void handleSearchResultsRectangle4Action() throws IOException {
    loadCustomer(4);
  }

  @FXML
  private void handleSearchResultsRectangle5Action() throws IOException {
    loadCustomer(5);
  }

  @FXML
  private void handleSearchResultsRectangle6Action() throws IOException {
    loadCustomer(6);
  }

  private void loadCustomer(int index) throws IOException {
    // String searchId = searchResultsIDList.get(index - 1);
    // searchId method here
    AppState.loadAllCustomerDetails("VIEW");
    Main.setUi(AppUI.CI_DETAILS);
  }
}
