package uoa.lavs.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.loan.PersonalLoanSingleton;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.loan.LoadLoanPayments;
import uoa.lavs.utility.AmortizingLoanCalculator;
import uoa.lavs.utility.LoanRepayment;
import uoa.lavs.utility.PaymentFrequency;

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

  // create new loan singleton object to access loan data
  private PersonalLoan personalLoan = PersonalLoanSingleton.getInstance();

  // create new amortising loan calculator object
  private AmortizingLoanCalculator amortizingLoanCalculator = new AmortizingLoanCalculator();
  private PaymentFrequency paymentFrequency;
  private ArrayList<LoanRepayment> loanRepayments = new ArrayList<LoanRepayment>();

  private int listIndex = 0;
  private int currentPage = 1;

  // create new array list to store loan repayments

  @FXML
  private void initialize() {
    uoa.lavs.mainframe.Connection connection = Instance.getConnection();
    LoadLoanPayments loadLoanPayments = new LoadLoanPayments();
    loadLoanPayments.setLoanId(personalLoan.getLoanId());
    loadLoanPayments.setNumber(1);
    Status status = loadLoanPayments.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Successfully received loan payments");
    } else {
      System.out.println("Failed to receive loan payments");
      System.out.println("Error code: " + status.getErrorCode());
      System.out.println("Error message: " + status.getErrorMessage());
      while (status.getErrorCode() == 1020) {
        System.out.println("retrying connection for loan repayments");
        status = loadLoanPayments.send(connection);
        if (status.getErrorCode() == 0) {
          System.out.println("Successfully received loan payments");
          break;
        }
      }
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
      LoanRepayment loanRepayment =
          new LoanRepayment(i, paymentDate, principalAmount, interestAmount, remainingAmount);
      loanRepayments.add(loanRepayment);
    }

    // String frequency = loan.getPayment().getPaymentFrequency();

    // if (frequency == "Weekly") {
    //   PaymentFrequency paymentFrequency = PaymentFrequency.Weekly;
    // } else if (frequency == "Fortnightly") {
    //   PaymentFrequency paymentFrequency = PaymentFrequency.Fortnightly;
    // } else if (frequency == "Monthly") {
    //   PaymentFrequency paymentFrequency = PaymentFrequency.Monthly;
    // }

    // loanRepayments =
    //     amortizingLoanCalculator.generateRepaymentSchedule(
    //         existingCustomerLoan.getPrincipal(),
    //         existingCustomerLoan.getRate(),
    //         // cast payment amount into Double
    //         Double.parseDouble(existingCustomerLoan.getPayment().getPaymentAmount()),
    //         paymentFrequency,
    //         existingCustomerLoan.getDuration().getStartDate());

    // set repayment schedule to the first page
    repaymentsPageLabel.setText("Page: " + currentPage);
    setPaymentPage();
  }

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

  @FXML
  private void setLabelValues(
      int index, String month, String principal, String interest, String remaining) {
    switch (index) {
      case 0:
        monthLabel.setText(month);
        principalLabel.setText(principal);
        interestLabel.setText(interest);
        remLabel.setText(remaining);
        break;
      case 1:
        monthLabel1.setText(month);
        principalLabel1.setText(principal);
        interestLabel1.setText(interest);
        remLabel1.setText(remaining);
        break;
      case 2:
        monthLabel2.setText(month);
        principalLabel2.setText(principal);
        interestLabel2.setText(interest);
        remLabel2.setText(remaining);
        break;
      case 3:
        monthLabel3.setText(month);
        principalLabel3.setText(principal);
        interestLabel3.setText(interest);
        remLabel3.setText(remaining);
        break;
      case 4:
        monthLabel4.setText(month);
        principalLabel4.setText(principal);
        interestLabel4.setText(interest);
        remLabel4.setText(remaining);
        break;
      case 5:
        monthLabel5.setText(month);
        principalLabel5.setText(principal);
        interestLabel5.setText(interest);
        remLabel5.setText(remaining);
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
