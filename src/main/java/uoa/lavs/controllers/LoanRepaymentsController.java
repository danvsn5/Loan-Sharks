package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;

public class LoanRepaymentsController {

  @FXML private Label monthLabel;
  @FXML private Label monthLabel1;
  @FXML private Label monthLabel2;
  @FXML private Label monthLabel3;
  @FXML private Label monthLabel4;
  @FXML private Label monthLabel5;

  @FXML private Label principalLabel;
  @FXML private Label principalLabel1;
  @FXML private Label principalLabel2;
  @FXML private Label principalLabel3;
  @FXML private Label principalLabel4;
  @FXML private Label principalLabel5;

  @FXML private Label interestLabel;
  @FXML private Label interestLabel1;
  @FXML private Label interestLabel2;
  @FXML private Label interestLabel3;
  @FXML private Label interestLabel4;
  @FXML private Label interestLabel5;

  @FXML private Label remLabel;
  @FXML private Label remLabel1;
  @FXML private Label remLabel2;
  @FXML private Label remLabel3;
  @FXML private Label remLabel4;
  @FXML private Label remLabel5;

  @FXML private Label repaymentsPageLabel;
  @FXML private ImageView staticReturnImageView;
  @FXML private ImageView incPage;
  @FXML private ImageView decPage;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleIncPage() {
    // Add code here
  }

  @FXML
  private void handleDecPage() {
    // Add code here
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.LC_SUMMARY);
  }
}
