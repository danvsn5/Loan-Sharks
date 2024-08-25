package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoanCoborrowers;
import uoa.lavs.legacy.mainframe.messages.loan.UpdateLoanCoborrower;

public class SyncLoanCoborrower extends Sync {
  @Override
  protected Status syncMainframeData(
      ResultSet resultSet, uoa.lavs.legacy.mainframe.Connection connection, java.sql.Connection localConn)
      throws SQLException, IOException {
    String loanId = resultSet.getString("loanId");

    LoadLoanCoborrowers loadLoanCoborrowers = new LoadLoanCoborrowers();
    loadLoanCoborrowers.setLoanId(loanId);
    loadLoanCoborrowers.setNumber(resultSet.getInt("number"));
    Status coborrowerLoadStatus = loadLoanCoborrowers.send(connection);

    Integer coborrowerNumber;

    if (coborrowerLoadStatus.getErrorCode() != 0) {
      System.out.println("Coborrower loaded successfully.");
      coborrowerNumber = resultSet.getInt("number");

    } else {
      System.out.println(
          "Error loading coborrower: "
              + coborrowerLoadStatus.getErrorCode()
              + " "
              + coborrowerLoadStatus.getErrorMessage());
      coborrowerNumber = null;
    }

    UpdateLoanCoborrower updateLoanCoborrower =
        updateLoanCoborrower(resultSet, loanId, coborrowerNumber);
    Status coborrowerStatus = updateLoanCoborrower.send(connection);
    System.out.println(
        "Coborrower status: "
            + coborrowerStatus.getErrorCode()
            + " "
            + coborrowerStatus.getErrorMessage());

    return coborrowerLoadStatus;
  }

  @Override
  protected String getSqlQuery() {
    return "SELECT * FROM loan_coborrower WHERE lastModified > ?";
  }

  private UpdateLoanCoborrower updateLoanCoborrower(
      ResultSet resultSet, String loanId, Integer coborrowerNumber) throws SQLException {
    UpdateLoanCoborrower updateLoanCoborrower = new UpdateLoanCoborrower();
    System.out.println("Coborrower ID: " + resultSet.getString("coborrowerId"));
    System.out.println("Loan ID: " + loanId);
    System.out.println("Number: " + coborrowerNumber);
    updateLoanCoborrower.setLoanId(loanId);
    updateLoanCoborrower.setNumber(coborrowerNumber);
    updateLoanCoborrower.setCoborrowerId(resultSet.getString("coborrowerId"));
    return updateLoanCoborrower;
  }
}
