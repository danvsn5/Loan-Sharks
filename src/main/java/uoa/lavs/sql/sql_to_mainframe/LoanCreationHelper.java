package uoa.lavs.sql.sql_to_mainframe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import uoa.lavs.AppState;
import uoa.lavs.loan.LoanDuration;
import uoa.lavs.loan.LoanPayment;
import uoa.lavs.loan.PersonalLoan;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.loan.LoadLoanSummary;
import uoa.lavs.sql.oop_to_sql.loan.LoanCoborrowersDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanDurationDAO;
import uoa.lavs.sql.oop_to_sql.loan.LoanPaymentDAO;

public class LoanCreationHelper {
  public static boolean validateLoan(PersonalLoan loan) {
    LoanDuration loanDuration = loan.getDuration();
    LoanPayment loanPayment = loan.getPayment();

    if (loan.getPrincipal() == 0
        || !Double.toString(loan.getPrincipal()).matches("^\\d+(\\.\\d+)?$")
        || Double.toString(loan.getPrincipal()).length() > 15
        || loan.getRate() == 0
        || !Double.toString(loan.getRate()).matches("^\\d+(\\.\\d+)?$")
        || Double.toString(loan.getRate()).length() > 5
        || loan.getRateType() == null
        || loan.getRateType().equals("")) {
      return false;
    }

    if (loanDuration.getStartDate() == null
        || loanDuration.getPeriod() == 0
        || !Integer.toString(loanDuration.getPeriod()).matches("[0-9]+")
        || Integer.toString(loanDuration.getPeriod()).length() > 5
        || loanDuration.getLoanTerm() == 0) {
      return false;
    }

    if (loanPayment.getCompounding().equals("")
        || loanPayment.getPaymentFrequency().equals("")
        || loanPayment.getPaymentAmount().equals("")
        || !loanPayment.getPaymentAmount().matches("^\\d+(\\.\\d+)?$")) {
      return false;
    }

    return true;
  }

  public static void createLoan(PersonalLoan loan) throws IOException {
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
