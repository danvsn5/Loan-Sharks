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
