package uoa.lavs.frontend.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.Instance;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanPayments;
import uoa.lavs.legacy.utility.LoanRepayment;

public class LoanRepaymentsController {

  @FXML
  private Label monthLabel;
  @FXML
  private Label monthLabel1;
  @FXML
  private Label monthLabel2;
  @FXML
  private Label monthLabel3;
  @FXML
  private Label monthLabel4;
  @FXML
  private Label monthLabel5;

  @FXML
  private Label principalLabel;
  @FXML
  private Label principalLabel1;
  @FXML
  private Label principalLabel2;
  @FXML
  private Label principalLabel3;
  @FXML
  private Label principalLabel4;
  @FXML
  private Label principalLabel5;

  @FXML
  private Label interestLabel;
  @FXML
  private Label interestLabel1;
  @FXML
  private Label interestLabel2;
  @FXML
  private Label interestLabel3;
  @FXML
  private Label interestLabel4;
  @FXML
  private Label interestLabel5;

  @FXML
  private Label remLabel;
  @FXML
  private Label remLabel1;
  @FXML
  private Label remLabel2;
  @FXML
  private Label remLabel3;
  @FXML
  private Label remLabel4;
  @FXML
  private Label remLabel5;

  @FXML
  private Label repaymentsPageLabel;
  @FXML
  private ImageView staticReturnImageView;
  @FXML
  private ImageView incPage;
  @FXML
  private ImageView decPage;

  @FXML
  private TextField loanStatusField;

  // create new loan singleton object to access loan data
  private PersonalLoan personalLoan = PersonalLoanSingleton.getInstance();

  private ArrayList<LoanRepayment> loanRepayments = new ArrayList<LoanRepayment>();

  private int currentPage = 1;

  // create new array list to store loan repayments

  @FXML
  private void initialize() {
    uoa.lavs.legacy.mainframe.Connection connection = Instance.getConnection();
    LoadLoanPayments loadLoanPayments = new LoadLoanPayments();
    loadLoanPayments.setLoanId(personalLoan.getLoanId());
    loadLoanPayments.setNumber(1);
    Status status = loadLoanPayments.send(connection);

    loanStatusField.setDisable(true);

    if (status.getErrorCode() == 0) {
      System.out.println("Successfully received loan payments");
    } else {
      System.out.println("Failed to receive loan payments");
      System.out.println("Error code: " + status.getErrorCode());
      System.out.println("Error message: " + status.getErrorMessage());
    }
    // set payment number as parameter in each method call.
    for (int i = 1; i < loadLoanPayments.getPaymentCountFromServer() + 1; i++) {
      LocalDate paymentDate = loadLoanPayments.getPaymentDateFromServer(i);
      Double principalAmount = loadLoanPayments.getPaymentPrincipalFromServer(i);
      Double interestAmount = loadLoanPayments.getPaymentInterestFromServer(i);
      Double remainingAmount = loadLoanPayments.getPaymentRemainingFromServer(i);
      // print these fields
      System.out.println("Payment Number: " + i);
      System.out.println("Payment Date: " + paymentDate);
      System.out.println("Principal Amount: " + principalAmount);
      System.out.println("Interest Amount: " + interestAmount);
      System.out.println("Remaining Amount: " + remainingAmount);
      LoanRepayment loanRepayment = new LoanRepayment(i, paymentDate, principalAmount, interestAmount, remainingAmount);
      loanRepayments.add(loanRepayment);
    }

    // set repayment schedule to the first page
    repaymentsPageLabel.setText("Page: " + currentPage);
    setPaymentPage();
  }

  // sets the payment page by iterating through each row element depending on the
  // page number
  @FXML
  private void setPaymentPage() {
    int startIndex = (currentPage - 1) * 6;
    for (int i = 0; i < 6; i++) {
      int index = startIndex + i;
      if (index < loanRepayments.size() && loanRepayments.get(index) != null) {
        LoanRepayment repayment = loanRepayments.get(index);
        setLabelValues(
            i,
            repayment.getRepaymentDate().toString(),
            repayment.getPrincipalAmount().toString(),
            repayment.getInterestAmount().toString(),
            repayment.getRemainingAmount().toString());
      } else {
        setLabelValues(i, "", "", "", "");
      }
    }
  }

  // sets all the label values depending on which row needs to be set in the
  // current page of repayments
  @FXML
  private void setLabelValues(
      int index, String month, String principal, String interest, String remaining) {
    // Format the remaining amount to 2 decimal places
    String formattedRemaining = "";
    String formattedPrincipal = "";

    if (!principal.isBlank()) {
      formattedRemaining = String.format("%.2f", Double.parseDouble(remaining));
    }

    if (!principal.isBlank()) {
      formattedPrincipal = String.format("%.2f", Double.parseDouble(principal));
    }

    switch (index) {
      case 0:
        monthLabel.setText(month);
        principalLabel.setText(formattedPrincipal);
        interestLabel.setText(interest);
        remLabel.setText(formattedRemaining);
        break;
      case 1:
        monthLabel1.setText(month);
        principalLabel1.setText(formattedPrincipal);
        interestLabel1.setText(interest);
        remLabel1.setText(formattedRemaining);
        break;
      case 2:
        monthLabel2.setText(month);
        principalLabel2.setText(formattedPrincipal);
        interestLabel2.setText(interest);
        remLabel2.setText(formattedRemaining);
        break;
      case 3:
        monthLabel3.setText(month);
        principalLabel3.setText(formattedPrincipal);
        interestLabel3.setText(interest);
        remLabel3.setText(formattedRemaining);
        break;
      case 4:
        monthLabel4.setText(month);
        principalLabel4.setText(formattedPrincipal);
        interestLabel4.setText(interest);
        remLabel4.setText(formattedRemaining);
        break;
      case 5:
        monthLabel5.setText(month);
        principalLabel5.setText(formattedPrincipal);
        interestLabel5.setText(interest);
        remLabel5.setText(formattedRemaining);
        break;
    }
  }

  @FXML
  private void handleIncPage() {
    if (currentPage < Math.ceil((double) loanRepayments.size() / 6)) {
      currentPage++;
      setPaymentPage();
      repaymentsPageLabel.setText("Page: " + currentPage);
    }
  }

  @FXML
  private void handleDecPage() {
    if (currentPage > 1) {
      currentPage--;
      setPaymentPage();
      repaymentsPageLabel.setText("Page: " + currentPage);
    }
  }

  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.LC_SUMMARY);
  }
}
