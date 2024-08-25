package uoa.lavs.sql.customer;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uoa.lavs.customer.Note;
import uoa.lavs.sql.DatabaseConnection;
import uoa.lavs.sql.DatabaseState;
import uoa.lavs.sql.InitialiseDatabase;
import uoa.lavs.sql.oop_to_sql.customer.NotesDAO;

public class NotesDAOTest {
  DatabaseConnection conn;
  NotesDAO notesDAO;
  static File dbFile;

  @BeforeEach
  public void setUp() {
    DatabaseState.setActiveDB(true);
    InitialiseDatabase.createDatabase();
    notesDAO = new NotesDAO();
    dbFile = DatabaseState.DB_TEST_FILE;
  }

  @Test
  public void testAddNote() {
    Note note = new Note("000001", new String[] {"Allergic to peanuts"});

    notesDAO.addNote(note);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement("SELECT * FROM customer_notes WHERE customerId = ?")) {
      stmt.setString(1, "000001");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Note should be added to the database");
        Assertions.assertEquals("000001", rs.getString("customerId"));
        Assertions.assertEquals("Allergic to peanuts", rs.getString("note"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testUpdateNote() {
    Note note = new Note("000001", new String[] {"Allergic to peanuts"});

    notesDAO.addNote(note);

    note.setLines(new String[] {"Allergic to peanuts", "Allergic to dairy"});
    notesDAO.updateNote(note);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement(
                "SELECT * FROM customer_notes WHERE customerId = ? AND note = ?")) {
      stmt.setString(1, "000001");
      stmt.setString(2, "Allergic to peanuts::Allergic to dairy");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Note should be updated in the database");
        Assertions.assertEquals("000001", rs.getString("customerId"));
        Assertions.assertEquals("Allergic to peanuts::Allergic to dairy", rs.getString("note"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testAddOrUpdateNotes() {
    Note note = new Note("000001", new String[] {"Allergic to peanuts"});
    ArrayList<Note> notes = new ArrayList<>();
    notes.add(note);

    notesDAO.addOrUpdateNotes(notes);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement(
                "SELECT * FROM customer_notes WHERE customerId = ? AND note = ?")) {
      stmt.setString(1, "000001");
      stmt.setString(2, "Allergic to peanuts");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Note should be added to the database");
        Assertions.assertEquals("000001", rs.getString("customerId"));
        Assertions.assertEquals("Allergic to peanuts", rs.getString("note"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }

    notes.get(0).setLines(new String[] {"Allergic to peanuts", "Allergic to dairy"});
    notesDAO.addOrUpdateNotes(notes);

    try (Connection conn = DatabaseConnection.connect();
        PreparedStatement stmt =
            conn.prepareStatement(
                "SELECT * FROM customer_notes WHERE customerId = ? AND note = ?")) {
      stmt.setString(1, "000001");
      stmt.setString(2, "Allergic to peanuts::Allergic to dairy");

      try (ResultSet rs = stmt.executeQuery()) {
        Assertions.assertTrue(rs.next(), "Note should be updated in the database");
        Assertions.assertEquals("000001", rs.getString("customerId"));
        Assertions.assertEquals("Allergic to peanuts::Allergic to dairy", rs.getString("note"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assertions.fail("Database query failed");
    } finally {
      DatabaseConnection.close(null);
    }
  }

  @Test
  public void testGetNotes() {
    Note note = new Note("000001", new String[] {"Allergic to peanuts"});
    ArrayList<Note> notes = new ArrayList<>();
    notes.add(note);

    notesDAO.addNote(note);

    ArrayList<Note> retrievedNotes = notesDAO.getNotes("000001");

    Assertions.assertEquals(notes.size(), retrievedNotes.size());
    Assertions.assertEquals(notes.get(0).getCustomerId(), retrievedNotes.get(0).getCustomerId());
    Assertions.assertArrayEquals(notes.get(0).getLines(), retrievedNotes.get(0).getLines());
  }

  @Test
  public void testGetNotesInvalidId() {
    ArrayList<Note> retrievedNotes = notesDAO.getNotes("002301");

    Assertions.assertEquals(0, retrievedNotes.size());
  }

  @AfterEach
  public void tearDown() {
    DatabaseState.setActiveDB(false);
    if (!dbFile.delete()) {
      throw new RuntimeException(
          "Failed to delete test database file: " + dbFile.getAbsolutePath());
    }
  }
}
