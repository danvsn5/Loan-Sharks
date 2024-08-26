package uoa.lavs.backend.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.backend.oop.customer.Note;
import uoa.lavs.backend.sql.DatabaseConnection;

public class NotesDAO {
  public void addNote(Note note) {
    String customerId = note.getCustomerId();

    String sql = "INSERT INTO customer_notes (customerId, noteId, note) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, customerId);
      pstmt.setInt(2, note.getNoteId());
      String noteLines = String.join("::", note.getLines());
      pstmt.setString(3, noteLines);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // private int getNextNoteIdForCustomer(String customerId) {
  //   String sql = "SELECT MAX(noteId) FROM customer_notes WHERE customerId = ?";
  //   try (Connection conn = DatabaseConnection.connect();
  //       PreparedStatement pstmt = conn.prepareStatement(sql)) {

  //     pstmt.setString(1, customerId);
  //     ResultSet rs = pstmt.executeQuery();

  //     if (rs.next()) {
  //       int maxNoteId = rs.getInt(1);
  //       return maxNoteId + 1;
  //     }
  //   } catch (SQLException e) {
  //     System.out.println(e.getMessage());
  //   }
  //   return 1;
  // }

  public void updateNote(Note note) {
    String customerId = note.getCustomerId();

    String sql = "UPDATE customer_notes SET note = ? WHERE customerId = ? AND noteId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, String.join("::", note.getLines()));
      pstmt.setString(2, customerId);
      pstmt.setInt(3, note.getNoteId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void deleteNotes(String customerId) {
    String sql = "DELETE FROM customer_notes WHERE customerId = ?";
    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customerId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public ArrayList<Note> getNotes(String customerId) {
    ArrayList<Note> notes = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      notes.add(new Note(customerId, new String[19]));
    }

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
        notes.set(noteId, retrievedNote);
      }

      return notes;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
