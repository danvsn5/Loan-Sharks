package uoa.lavs.backend.oop.customer;

// sets the note object with getters and setters
public class Note {
  private String customerId;
  private int noteId;
  private String[] lines;

  public Note(String customerId, String[] lines) {
    this.customerId = customerId;
    this.lines = lines;
  }

  public String getCustomerId() {
    return this.customerId;
  }

  public int getNoteId() {
    return this.noteId;
  }

  public String[] getLines() {
    return this.lines;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setNoteId(int noteId) {
    this.noteId = noteId;
  }

  public void setLines(String[] lines) {
    this.lines = lines;
  }

  public void setLine(int index, String line) {
    this.lines[index] = line;
  }
}
