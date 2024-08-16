package uoa.lavs.sql.oop_to_sql.loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import uoa.lavs.sql.DatabaseConnection;

public class LoanCoborrowersDAO {
    public void addCoborrowers(int loanId, ArrayList<String> coborrowerIds) {
        String sql = "INSERT INTO loan_coborrower (loanId, coborrowerId) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); 

            for (String coborrowerId : coborrowerIds) {
                pstmt.setInt(1, loanId);
                pstmt.setString(2, coborrowerId);
                pstmt.addBatch(); 
            }
            
            pstmt.executeBatch(); 
            conn.commit(); 

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try (Connection conn = DatabaseConnection.connect()) {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void updateCoborrowers(int loanId, ArrayList<String> coborrowerIds) {
        String sql = "DELETE FROM loan_coborrower WHERE loanId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        addCoborrowers(loanId, coborrowerIds);
    }

}
