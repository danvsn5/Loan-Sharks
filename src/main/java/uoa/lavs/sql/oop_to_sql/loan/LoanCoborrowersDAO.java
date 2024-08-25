package uoa.lavs.sql.oop_to_sql.loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.sql.DatabaseConnection;

public class LoanCoborrowersDAO {
  public void addCoborrowers(String loanId, ArrayList<String> coborrowerIds) {
    String sql = "INSERT INTO loan_coborrower (loanId, coborrowerId) VALUES (?, ?)";

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      for (String coborrowerId : coborrowerIds) {
        pstmt.setString(1, loanId);
        pstmt.setString(2, coborrowerId);
        pstmt.executeUpdate();
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateCoborrowers(String loanId, ArrayList<String> coborrowerIds) {
    String sql = "DELETE FROM loan_coborrower WHERE loanId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loanId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    addCoborrowers(loanId, coborrowerIds);
  }

  public ArrayList<String> getCoborrowers(String loanId) {
    String sql = "SELECT coborrowerId FROM loan_coborrower WHERE loanId = ?";
    ArrayList<String> coborrowerIds = new ArrayList<>();

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, loanId);
      var rs = pstmt.executeQuery();

      while (rs.next()) {
        coborrowerIds.add(rs.getString("coborrowerId"));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return coborrowerIds;
  }
}
