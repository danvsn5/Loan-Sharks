package uoa.lavs.oop.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uoa.lavs.backend.oop.customer.Note;

public class NoteTest {
  private String customerId;
  private String[] lines;
  Note note;

  @BeforeEach
  public void setUp() {
    customerId = "000001";
    lines = new String[] {"Allergic to peanuts"};
    note = new Note(customerId, lines);
  }

  @Test
  public void testGetCustomerId() {
    assertEquals(customerId, note.getCustomerId());
  }

  @Test
  public void testGetLines() {
    assertEquals(lines, note.getLines());
  }

  @Test
  public void testSetCustomerId() {
    String newCustomerId = "000002";
    note.setCustomerId(newCustomerId);
    assertEquals(newCustomerId, note.getCustomerId());
  }

  @Test
  public void testGetAndSetNoteId() {
    int noteId = 1;
    note.setNoteId(noteId);
    assertEquals(noteId, note.getNoteId());
  }

  @Test
  public void testSetLines() {
    String[] newLines = new String[] {"Allergic to peanuts", "Allergic to dairy"};
    note.setLines(newLines);
    assertEquals(newLines, note.getLines());
  }

  @Test
  public void testSetLine() {
    String newLine = "Allergic to dairy";
    note.setLine(0, newLine);
    assertEquals(newLine, note.getLines()[0]);
  }
}
