package uoa.lavs.sql.sql_to_mainframe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
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
    loanDAO.addLoan(loan);
    LoanDurationDAO loanDurationDAO = new LoanDurationDAO();
    loanDurationDAO.addLoanDuration(loan.getDuration());
    LoanPaymentDAO loanPaymentDAO = new LoanPaymentDAO();
    loanPaymentDAO.addLoanPayment(loan.getPayment());

    LoanCoborrowersDAO loanCoborrowersDAO = new LoanCoborrowersDAO();
    ArrayList<String> coborrowerIds = loan.getCoborrowerIds();
    for (String id : coborrowerIds) {
      if (!(id == null || id == "")) {
        loanCoborrowersDAO.addCoborrowers(loan.getLoanId(), loan.getCoborrowerIds());
      }
    }

    SyncCustomer syncCustomer = new SyncCustomer();
    SyncLoan syncLoan = new SyncLoan();
    LocalDateTime lastSyncTime = syncCustomer.getLastSyncTimeFromDB();

    if (lastSyncTime == null) {
      System.out.println("No last sync time found. Syncing all records.");
      lastSyncTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(1);
    }

    SyncManager syncManager = new SyncManager(List.of(syncCustomer, syncLoan));

    syncManager.syncAll(lastSyncTime);
  }

  public static LoadLoanSummary getLoanSummary(PersonalLoan loan) {
    uoa.lavs.mainframe.Connection connection = Instance.getConnection();
    LoadLoanSummary loadLoanSummary = new LoadLoanSummary();

    loadLoanSummary.setLoanId(loan.getLoanId());
    Status status = loadLoanSummary.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Successfully received loan summary");
      return loadLoanSummary;
    } else {
      System.out.println("Loan summary failed");
      return null;
    }
  }
}
