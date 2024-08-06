package uoa.lavs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CustomerSummaryController {
  @FXML private Label customerTitle;
  @FXML private Label customerFirstName;
  @FXML private Label customerMiddleName;
  @FXML private Label customerLastName;
  @FXML private Label customerDOB;
  @FXML private Label customerOccupation;
  @FXML private Label customerResidencyStatus;

  @FXML private Label customerAddressType;
  @FXML private Label customerAddress1;
  @FXML private Label customerAddress2;
  @FXML private Label customerSuburb;
  @FXML private Label customerCity;
  @FXML private Label customerPostCode;
  @FXML private CheckBox customerPrimaryAddress;
  @FXML private CheckBox customerMailingAddress;

  @FXML private Label customerEmail;
  @FXML private Label customerPhoneNumType;
  @FXML private Label customerPhoneNum;
  @FXML private Label customerPreferredContact;
  @FXML private Label customerAlternateContact;

  @FXML private Label employerName;
  @FXML private Label employerAddress1;
  @FXML private Label employerAddress2;
  @FXML private Label employerSuburb;
  @FXML private Label employerCity;
  @FXML private Label employerPostCode;
  @FXML private Label employerCountry;
  @FXML private Label employerEmail;
  @FXML private Label employerWebsite;
  @FXML private Label employerPhoneNum;
  @FXML private CheckBox isCustomerOwner;

  @FXML private TextField customerNotes;

  @FXML private Button editDetailsButton;

  @FXML
  private void initialize() {
    // Add initialization code here
  }

  @FXML
  private void handleEditDetailsButtonAction() {
    // Add edit details button action code here
  }
}
