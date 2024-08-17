package uoa.lavs.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.customer.Note;
import uoa.lavs.sql.DatabaseConnection;

public class NotesDAO {
  public void addNotes(ArrayList<Note> notes) {
    String customerId = notes.get(0).getCustomerId();

    for (int i = 0; i < notes.size(); i++) {
      String sql = "INSERT INTO customer_notes (customerId, noteId, note) VALUES (?, ?, ?)";
      try (Connection conn = DatabaseConnection.connect();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, customerId);
        pstmt.setInt(2, i);

        String noteLines = String.join("::", notes.get(i).getLines());
        pstmt.setString(3, noteLines);

        pstmt.executeUpdate();

        notes.get(i).setNoteId(i);
        notes.get(i).setCustomerId(customerId);
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public void updateNotes(ArrayList<Note> notes) {
    String customerId = notes.get(0).getCustomerId();

    for (int i = 0; i < notes.size(); i++) {
      String sql = "UPDATE customer_notes SET note = ? WHERE customerId = ? AND noteId = ?";
      try (Connection conn = DatabaseConnection.connect();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, String.join("::", notes.get(i).getLines()));
        pstmt.setString(2, customerId);
        pstmt.setInt(3, i);

        pstmt.executeUpdate();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public void addOrUpdateNotes(ArrayList<Note> notes) {
    String customerId = notes.get(0).getCustomerId();

    String checkSql = "SELECT COUNT(*) FROM customer_notes WHERE customerId = ? AND noteId = ?";
    String insertSql = "INSERT INTO customer_notes (customerId, noteId, note) VALUES (?, ?, ?)";
    String updateSql = "UPDATE customer_notes SET note = ? WHERE customerId = ? AND noteId = ?";

    try (Connection conn = DatabaseConnection.connect()) {

      for (int i = 0; i < notes.size(); i++) {
        try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
          checkPstmt.setString(1, customerId);
          checkPstmt.setInt(2, i);
          ResultSet rs = checkPstmt.executeQuery();
          rs.next();
          boolean exists = rs.getInt(1) > 0;

          String noteLines = String.join("::", notes.get(i).getLines());

          if (exists) {
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
              updatePstmt.setString(1, noteLines);
              updatePstmt.setString(2, customerId);
              updatePstmt.setInt(3, i);
              updatePstmt.executeUpdate();
            }
          } else {
            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
              insertPstmt.setString(1, customerId);
              insertPstmt.setInt(2, i);
              insertPstmt.setString(3, noteLines);
              insertPstmt.executeUpdate();
            }
          }

          notes.get(i).setNoteId(i);
          notes.get(i).setCustomerId(customerId);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public ArrayList<Note> getNotes(String customerId) {
    ArrayList<Note> notes = new ArrayList<>();

    String sql = "SELECT * FROM customer_notes WHERE customerId = ? ORDER BY noteId";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        int noteId = rs.getInt("noteId");
        String note = rs.getString("note");
        String[] noteLines = note.split("::");

        Note retrievedNote = new Note(customerId, noteLines);
        retrievedNote.setNoteId(noteId);
        notes.add(retrievedNote);
      }

      return notes;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
