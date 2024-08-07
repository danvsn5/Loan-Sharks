package uoa.lavs.sql.loan;

import uoa.lavs.loan.ILoan;
import uoa.lavs.sql.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoanDAO {
    public void addLoan(ILoan loan) {
        String sql = "INSERT INTO loan (loanId, customerId, principal, rate, durationId, paymentId) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getLoanId());
            pstmt.setString(2, loan.getCustomerId());
            pstmt.setDouble(3, loan.getPrincipal());
            pstmt.setDouble(4, loan.getRate());
            pstmt.setInt(5, loan.getDuration().getDurationId()); 
            pstmt.setInt(6, loan.getPayment().getPaymentId());  

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
