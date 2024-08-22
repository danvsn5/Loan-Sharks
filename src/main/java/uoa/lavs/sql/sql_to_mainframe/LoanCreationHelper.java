package uoa.lavs.sql.sql_to_mainframe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import uoa.lavs.AppState;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.loan.LoadLoanSummary;
import uoa.lavs.sql.oop_to_sql.loan.LoanCoborrowersDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanDurationDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanPaymentDAO;

public class LoanCreationHelper {
  public static void createLoan(PersonalLoan loan) throws IOException {
    LoanDAO loanDAO = new LoanDAO();
    LoanDurationDAO loanDurationDAO = new LoanDurationDAO();
    LoanPaymentDAO loanPaymentDAO = new LoanPaymentDAO();
    LoanCoborrowersDAO loanCoborrowersDAO = new LoanCoborrowersDAO();

    // Creates if loanid not in mainframe and updates if it is
    if (loanDAO.getLoan(loan.getLoanId()) == null) {
      System.out.println("loan not in sql database. adding");
      System.out.println("loan id is: " + loan.getLoanId());
      System.out.println("loan principal is: " + loan.getPrincipal());
      System.out.println("loan start date is: " + loan.getDuration().getStartDate());
      System.out.println("loan term is: " + loan.getDuration().getLoanTerm());
      System.out.println("loan rate type is: " + loan.getRateType());
      System.out.println("loan rate is: " + loan.getRate());
      System.out.println("loan compounding is: " + loan.getPayment().getCompounding());
      System.out.println("loan payment frequency is: " + loan.getPayment().getPaymentFrequency());
      System.out.println("loan payment amount is: " + loan.getPayment().getPaymentAmount());
      System.out.println("loan interest only is: " + loan.getPayment().getInterestOnly());

      loanDAO.addLoan(loan);
      loanDurationDAO.addLoanDuration(loan.getDuration());
      loanPaymentDAO.addLoanPayment(loan.getPayment());

      ArrayList<String> coborrowerIds = new ArrayList<>();
      boolean coborrowerExists = false;
      for (int i = 0; i < 3; i++) {
        if (loan.getCoborrowerIds().get(i) != "") {
          coborrowerIds.add(loan.getCoborrowerIds().get(i));
          coborrowerExists = true;
        }
      }
      if (coborrowerExists) {
        loanCoborrowersDAO.addCoborrowers(loan.getLoanId(), coborrowerIds);
      }
    } else {
      System.out.println("loan in sql database. updating");
      loanDAO.updateLoan(loan);
      loanDurationDAO.updateLoanDuration(loan.getDuration());
      loanPaymentDAO.updateLoanPayment(loan.getPayment());
      ArrayList<String> coborrowerIds = new ArrayList<>();
      boolean coborrowerExists = false;
      for (int i = 0; i < 3; i++) {
        if (loan.getCoborrowerIds().get(i) != "") {
          coborrowerIds.add(loan.getCoborrowerIds().get(i));
          coborrowerExists = true;
        }
      }
      if (coborrowerExists) {
        loanCoborrowersDAO.updateCoborrowers(loan.getLoanId(), coborrowerIds);
      }
    }

    SyncLoan syncLoan = new SyncLoan();
    LocalDateTime lastSyncTime = syncLoan.getLastSyncTimeFromDB();

    if (lastSyncTime == null) {
      System.out.println("No last sync time found. Syncing all records.");
      lastSyncTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(1);
    }

    SyncManager syncManager = new SyncManager(List.of(syncLoan));

    syncManager.syncAll(lastSyncTime);
  }

  public static LoadLoanSummary getLoanSummary(PersonalLoan loan) {
    uoa.lavs.mainframe.Connection connection = Instance.getConnection();
    LoadLoanSummary loadLoanSummary = new LoadLoanSummary();

    System.out.println("loan id is: " + loan.getLoanId());
    loadLoanSummary.setLoanId(loan.getLoanId());
    Status status = loadLoanSummary.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Successfully received loan summary");
      AppState.isOnLoanSummary = true;
      return loadLoanSummary;
    } else {
      System.out.println("Loan summary failed");
      System.out.println(status.getErrorMessage());
      System.out.println(status.getErrorCode());
      return null;
    }
  }
}
