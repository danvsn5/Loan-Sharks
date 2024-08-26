package uoa.lavs.backend.sql.oop_to_sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uoa.lavs.backend.sql.DatabaseConnection;

public abstract class AbstractDAO {
  public int getNextId(String customerId, String sql) {
    int maxId = 0;
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();
      System.out.println(rs.next());

      maxId = rs.getInt(1);
      
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return maxId + 1;
  }
}
