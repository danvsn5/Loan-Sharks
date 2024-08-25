package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uoa.lavs.backend.oop.loan.PersonalLoan;
import uoa.lavs.backend.oop.loan.PersonalLoanSingleton;
import uoa.lavs.legacy.mainframe.Frequency;
import uoa.lavs.legacy.mainframe.RateType;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.loan.LoadLoan;
import uoa.lavs.legacy.mainframe.messages.loan.UpdateLoan;

public class SyncLoan extends Sync {

  PersonalLoan personalLoan = PersonalLoanSingleton.getInstance();

  @Override
  protected Status syncMainframeData(
      ResultSet resultSet, uoa.lavs.legacy.mainframe.Connection connection, java.sql.Connection localConn)
      throws SQLException, IOException {
    String loanId = resultSet.getString("loanId");
    LoadLoan loadLoan = new LoadLoan();
    loadLoan.setLoanId(loanId);

    Status loadStatus = loadLoan.send(connection);

    if (loadStatus.getErrorCode() == 0) {
      System.out.println("Loan loaded successfully.");
      UpdateLoan updateLoan = updateLoan(resultSet, loanId);
      Status status = updateLoan.send(connection);

      if (status.getErrorCode() == 0) {
        System.out.println("Loan updated successfully.");
      } else {
        System.out.println("Error updating loan: " + status.getErrorMessage());
      }
    } else {
      System.out.println("Error loading loan: " + loadStatus.getErrorMessage());
      System.out.println(loadStatus.getErrorCode());
      System.out.println("Loan not found in mainframe, creating new loan");
      UpdateLoan updateLoan = updateLoan(resultSet, null);
      Status status = updateLoan.send(connection);

      String newLoanId = updateLoan.getLoanIdFromServer();

      if (newLoanId != null) {
        updateLoanIdInLocalDB(resultSet.getString("loanId"), newLoanId, localConn);
        personalLoan.setLoanId(newLoanId);
        loanId = newLoanId;
      }

      if (status.getErrorCode() == 0) {
        System.out.println("Loan updated successfully.");
      } else {
        System.out.println(
            "Error updating loan: " + status.getErrorCode() + status.getErrorMessage());
      }
    }

    System.out.println("Loan id ting is: " + loanId);
    loadLoan.setLoanId(loanId);
    loadStatus = loadLoan.send(connection);
    return loadStatus;
  }

  @Override
  protected String getSqlQuery() {
    return "SELECT l.loanId, "
        + "l.customerId, "
        + "l.principal, "
        + "l.rate, "
        + "l.rateType, "
        + "l.lastModified AS loanLastModified, "
        + "ld.durationId, "
        + "ld.startDate, "
        + "ld.period, "
        + "ld.loanTerm, "
        + "ld.lastModified AS durationLastModified, "
        + "lp.paymentId, "
        + "lp.compounding, "
        + "lp.paymentFrequency, "
        + "lp.paymentAmount, "
        + "lp.interestOnly, "
        + "lp.lastModified AS paymentLastModified "
        + "FROM loan l "
        + "LEFT JOIN loan_duration ld ON l.loanId = ld.loanId "
        + "LEFT JOIN loan_payment lp ON l.loanId = lp.loanId "
        + "WHERE l.lastModified > ? "
        + "OR ld.lastModified > ? "
        + "OR lp.lastModified > ?";
  }

  private UpdateLoan updateLoan(ResultSet resultSet, String loanId) throws SQLException {
    UpdateLoan updateLoan = new UpdateLoan();
    updateLoan.setLoanId(loanId);
    updateLoan.setCustomerId(resultSet.getString("customerId"));

    String compoundingStr =
        (resultSet.getString("compounding")).substring(0, 1).toUpperCase()
            + (resultSet.getString("compounding")).substring(1);
    Frequency compounding;
    try {
      compounding = Frequency.valueOf(compoundingStr);
    } catch (IllegalArgumentException | NullPointerException e) {
      compounding = Frequency.Unknown;
    }
    updateLoan.setCompounding(compounding);

    updateLoan.setPeriod(resultSet.getInt("period"));
    updateLoan.setPaymentAmount(resultSet.getDouble("paymentAmount"));

    String paymentFrequencyStr =
        (resultSet.getString("paymentFrequency")).substring(0, 1).toUpperCase()
            + (resultSet.getString("paymentFrequency")).substring(1);
    Frequency paymentFrequency;
    try {
      paymentFrequency = Frequency.valueOf(paymentFrequencyStr);
    } catch (IllegalArgumentException | NullPointerException e) {
      paymentFrequency = Frequency.Unknown;
    }
    updateLoan.setPaymentFrequency(paymentFrequency);
    updateLoan.setPrincipal(resultSet.getDouble("principal"));

    String rateTypeStr =
        (resultSet.getString("rateType")).substring(0, 1).toUpperCase()
            + (resultSet.getString("rateType")).substring(1);
    RateType rateType;
    try {
      rateType = RateType.valueOf(rateTypeStr);
    } catch (IllegalArgumentException | NullPointerException e) {
      rateType = RateType.Unknown;
    }
    updateLoan.setRateType(rateType);

    updateLoan.setRateValue(resultSet.getDouble("rate"));
    updateLoan.setTerm(resultSet.getInt("loanTerm"));
    updateLoan.setStartDate(resultSet.getDate("startDate").toLocalDate());
    return updateLoan;
  }

  private void updateLoanIdInLocalDB(String oldLoanId, String newLoanId, Connection conn)
      throws SQLException {

    // Update loanId in loan table
    String updateLoanSql = "UPDATE loan SET loanId = ? WHERE loanId = ?";
    try (PreparedStatement updateLoanPstmt = conn.prepareStatement(updateLoanSql)) {
      updateLoanPstmt.setString(1, newLoanId);
      updateLoanPstmt.setString(2, oldLoanId);
      int loanRowsAffected = updateLoanPstmt.executeUpdate();
      if (loanRowsAffected > 0) {
        System.out.println("Loan table updated with new loan ID: " + newLoanId);
      } else {
        System.out.println("No records updated in loan table.");
      }
    }

    // Update loanId in loan_coborrower table
    String updateCoborrowerSql = "UPDATE loan_coborrower SET loanId = ? WHERE loanId = ?";
    try (PreparedStatement updateCoborrowerPstmt = conn.prepareStatement(updateCoborrowerSql)) {
      updateCoborrowerPstmt.setString(1, newLoanId);
      updateCoborrowerPstmt.setString(2, oldLoanId);
      int coborrowerRowsAffected = updateCoborrowerPstmt.executeUpdate();
      if (coborrowerRowsAffected > 0) {
        System.out.println("Loan coborrower table updated with new loan ID: " + newLoanId);
      } else {
        System.out.println("No records updated in loan coborrower table.");
      }
    }

    // Update loanId in loan_duration table
    String updateDurationSql = "UPDATE loan_duration SET loanId = ? WHERE loanId = ?";
    try (PreparedStatement updateDurationPstmt = conn.prepareStatement(updateDurationSql)) {
      updateDurationPstmt.setString(1, newLoanId);
      updateDurationPstmt.setString(2, oldLoanId);
      int durationRowsAffected = updateDurationPstmt.executeUpdate();
      if (durationRowsAffected > 0) {
        System.out.println("Loan duration table updated with new loan ID: " + newLoanId);
      } else {
        System.out.println("No records updated in loan duration table.");
      }
    }

    // Update loanId in loan_payment table
    String updatePaymentSql = "UPDATE loan_payment SET loanId = ? WHERE loanId = ?";
    try (PreparedStatement updatePaymentPstmt = conn.prepareStatement(updatePaymentSql)) {
      updatePaymentPstmt.setString(1, newLoanId);
      updatePaymentPstmt.setString(2, oldLoanId);
      int paymentRowsAffected = updatePaymentPstmt.executeUpdate();
      if (paymentRowsAffected > 0) {
        System.out.println("Loan payment table updated with new loan ID: " + newLoanId);
      } else {
        System.out.println("No records updated in loan payment table.");
      }
    }
  }
}
