package uoa.lavs.frontend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import uoa.lavs.Main;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager.AppUI;

public class LoanResultsController {
  // Make 6 fxml labels for name and id
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

  @FXML private ImageView staticReturnImageView;

  ArrayList<String> searchResultsNameList = new ArrayList<>();
  ArrayList<String> searchResultsLoanIDList = new ArrayList<>();

  // write for all fxml elements
  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleBackButtonAction() {
    AppState.isAccessingFromLoanSearch = false;
    Main.setUi(AppUI.LOAN_SEARCH);
  }

  @FXML
  private void handleSearchResultsRectangle1Action() throws IOException {
    loadLoan(1);
  }

  @FXML
  private void handleSearchResultsRectangle2Action() throws IOException {
    loadLoan(2);
  }

  @FXML
  private void handleSearchResultsRectangle3Action() throws IOException {
    loadLoan(3);
  }

  @FXML
  private void handleSearchResultsRectangle4Action() throws IOException {
    loadLoan(4);
  }

  @FXML
  private void handleSearchResultsRectangle5Action() throws IOException {
    loadLoan(5);
  }

  @FXML
  private void handleSearchResultsRectangle6Action() throws IOException {
    loadLoan(6);
  }

  private void loadLoan(int index) throws IOException {
    // String searchId = searchResultsLoanIDList.get(index - 1);
    // searchId method here
    AppState.isAccessingFromLoanSearch = true;
    AppState.loadLoans("VIEW");
    Main.setUi(AppUI.LC_SUMMARY);
  }
}
