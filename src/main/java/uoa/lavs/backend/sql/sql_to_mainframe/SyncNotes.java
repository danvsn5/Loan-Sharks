package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerNote;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomerNote;

public class SyncNotes extends Sync {
  // Syncs the notes data from the local database to the mainframe
  @Override
  protected Status syncMainframeData(
      ResultSet resultSet,
      uoa.lavs.legacy.mainframe.Connection connection,
      java.sql.Connection localConn)
      throws SQLException, IOException {
    String customer_id = resultSet.getString("customerId");
    Integer note_id = resultSet.getInt("noteId");
    LoadCustomerNote loadCustomerNote = new LoadCustomerNote();
    loadCustomerNote.setCustomerId(customer_id);
    loadCustomerNote.setNumber(note_id);
    loadCustomerNote.send(connection);

    Integer lineCount = loadCustomerNote.getLineCountFromServer();

    UpdateCustomerNote updateCustomerNote = updateCustomerNote(resultSet, customer_id);

    if (lineCount == null) {
      System.out.println("Note " + note_id + " not found in mainframe. Creating new notes.");
      updateCustomerNote.setNumber(null);
    } else {
      System.out.println("Note " + note_id + "found in mainframe. Updating notes.");
      updateCustomerNote.setNumber(note_id);
    }
    Status status = updateCustomerNote.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Notes updated successfully.");
    } else {
      System.out.println("Error updating notes: " + status.getErrorMessage());
    }

    return status;
  }

  // Update the notes data from the local database to the mainframe
  private UpdateCustomerNote updateCustomerNote(ResultSet resultSet, String customerId) {
    UpdateCustomerNote updateCustomerNote = new UpdateCustomerNote();
    try {
      updateCustomerNote.setCustomerId(customerId);

      int noteId = resultSet.getInt("noteId");
      String note = resultSet.getString("note");

      updateCustomerNote.setNumber(noteId);

      String[] lines = note.split("::");
      for (int i = 0; i < lines.length; i++) {
        updateCustomerNote.setLine(i + 1, lines[i]);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return updateCustomerNote;
  }

  // Override the SQL query to retrieve notes data from the local database
  @Override
  protected String getSqlQuery() {
    return "SELECT * FROM customer_notes WHERE lastModified > ?";
  }
}
