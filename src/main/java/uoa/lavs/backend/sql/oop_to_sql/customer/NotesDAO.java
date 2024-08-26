package uoa.lavs.backend.sql.oop_to_sql.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uoa.lavs.backend.oop.customer.Note;
import uoa.lavs.backend.sql.DatabaseConnection;

public class NotesDAO {

  // Adds a note to the database
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

  // Updates a note in the database with new details from the note object
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

  // Deletes a note from the database associated with a customer
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

  // Adds or updates a list of notes in the database with new details from the note list
  public void addOrUpdateNotes(ArrayList<Note> notes) {
    String customerId = notes.get(0).getCustomerId();

    String checkSql = "SELECT COUNT(*) FROM customer_notes WHERE customerId = ? AND noteId = ?";
    String insertSql = "INSERT INTO customer_notes (customerId, noteId, note) VALUES (?, ?, ?)";
    String updateSql = "UPDATE customer_notes SET note = ? WHERE customerId = ? AND noteId = ?";

    try (Connection conn = DatabaseConnection.connect()) {

      for (int i = 0; i < notes.size(); i++) {
        try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
          checkPstmt.setString(1, customerId);
          checkPstmt.setInt(2, i + 1);
          ResultSet rs = checkPstmt.executeQuery();
          rs.next();
          boolean exists = rs.getInt(1) > 0;

          String noteLines = String.join("::", notes.get(i).getLines());

          if (exists) {
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
              updatePstmt.setString(1, noteLines);
              updatePstmt.setString(2, customerId);
              updatePstmt.setInt(3, i + 1);
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

  // Retrieves all notes for a customer from the database
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
