package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import uoa.lavs.backend.oop.loan.LoanDuration;
import uoa.lavs.backend.oop.loan.LoanPayment;
import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanCoborrowersDAO;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanDAO;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanDurationDAO;
import uoa.lavs.backend.sql.oop_to_sql.loan.LoanPaymentDAO;
import uoa.lavs.frontend.AppState;
import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanSummary;

public class LoanCreationHelper {
  // Validates the loan data before creating it
  public static boolean validateLoan(PersonalLoan loan) {
    LoanDuration loanDuration = loan.getDuration();
    LoanPayment loanPayment = loan.getPayment();

    // CHECK PRIMARY DETAILS
    if (loan.getPrincipal() == 0
        || loan.getRate() == 0
        || loan.getRateType() == null
        || loan.getRateType().equals("")) {
      return false;
    }

    // CHECK DURATION DETAILS
    if (loanDuration.getStartDate() == null
        || loanDuration.getPeriod() == 0
        || loanDuration.getLoanTerm() == 0) {
      return false;
    }

    // CHECK PAYMENT DETAILS
    if (loanPayment.getCompounding().equals("")
        || loanPayment.getPaymentFrequency().equals("")
        || loanPayment.getPaymentAmount().equals("")
        || !loanPayment.getPaymentAmount().matches("^\\d+(\\.\\d+)?$")) {
      return false;
    }

    return true;
  }

  public static void createLoan(PersonalLoan loan, Connection connection) throws IOException {
    LoanDAO loanDAO = new LoanDAO();
    LoanDurationDAO loanDurationDAO = new LoanDurationDAO();
    LoanPaymentDAO loanPaymentDAO = new LoanPaymentDAO();
    LoanCoborrowersDAO loanCoborrowersDAO = new LoanCoborrowersDAO();

    // Creates if loanid not in mainframe and updates if it is
    if (loanDAO.getLoan(loan.getLoanId()) == null) {
      loanDAO.addLoan(loan);
      System.out.println("loan duration start date: " + loan.getDuration().getStartDate());
      loanDurationDAO.addLoanDuration(loan.getDuration());
      loanPaymentDAO.addLoanPayment(loan.getPayment());

      ArrayList<String> coborrowerIds = new ArrayList<>();
      boolean coborrowerExists = false;
      // Add coborrowers if they exist in the database
      for (int i = 0; i < 3; i++) {
        if (loan.getCoborrowerIds().get(i) != "") {
          coborrowerIds.add(loan.getCoborrowerIds().get(i));
          coborrowerExists = true;
        }
      }
      if (coborrowerExists) {
        loanCoborrowersDAO.addCoborrowers(loan.getLoanId(), coborrowerIds);
      }
    }
    // Update loan if it exists in the mainframe
    else {
      System.out.println("loan in sql database. updating");
      loanDAO.updateLoan(loan);
      loanDurationDAO.updateLoanDuration(loan.getDuration());
      loanPaymentDAO.updateLoanPayment(loan.getPayment());
      ArrayList<String> coborrowerIds = new ArrayList<>();
      System.out.println("printing coborrower ids from creation hel[er]");
      for (int i = 0; i < 3; i++) {
        if (!loan.getCoborrowerIds().get(i).equals("") && loan.getCoborrowerIds().get(i) != null) {
          coborrowerIds.add(loan.getCoborrowerIds().get(i));
          System.out.println("coborrower id: " + loan.getCoborrowerIds().get(i));
        }
      }
      loanCoborrowersDAO.updateCoborrowers(loan.getLoanId(), coborrowerIds);
    }

    // Sync the loan and coborrowers with the mainframe
    SyncLoan syncLoan = new SyncLoan();
    SyncLoanCoborrower syncLoanCoborrower = new SyncLoanCoborrower();
    LocalDateTime lastSyncTime = syncLoan.getLastSyncTimeFromDB();

    if (lastSyncTime == null) {
      System.out.println("No last sync time found. Syncing all records.");
      lastSyncTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(1);
    }

    SyncManager syncManager = new SyncManager(List.of(syncLoan, syncLoanCoborrower));

    syncManager.syncAll(lastSyncTime, connection);
  }

  public static LoadLoanSummary getLoanSummary(
      PersonalLoan loan, uoa.lavs.legacy.mainframe.Connection connection) {
    LoadLoanSummary loadLoanSummary = new LoadLoanSummary();

    System.out.println("loan id is: " + loan.getLoanId());
    loadLoanSummary.setLoanId(loan.getLoanId());
    Status status = loadLoanSummary.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Successfully received loan summary");
      AppState.setIsOnLoanSummary(true);
      return loadLoanSummary;
    } else {
      System.out.println("Loan summary failed");
      System.out.println(status.getErrorMessage());
      System.out.println(status.getErrorCode());
      return null;
    }
  }
}
