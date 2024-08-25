package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomerNote;
import uoa.lavs.legacy.mainframe.messages.customer.UpdateCustomerNote;

public class SyncNotes extends Sync {
  @Override
  protected Status syncMainframeData(
      ResultSet resultSet, uoa.lavs.legacy.mainframe.Connection connection, java.sql.Connection localConn)
      throws SQLException, IOException {
    String customer_id = resultSet.getString("customerId");
    // Integer note_id = resultSet.getInt("noteId");
    LoadCustomerNote loadCustomerNote = new LoadCustomerNote();
    loadCustomerNote.setCustomerId(customer_id);
    loadCustomerNote.send(connection);

    Integer pageCount = loadCustomerNote.getPageCountFromServer();

    if (pageCount == null) {
      System.out.println("Notes not found in mainframe. Creating new notes.");
    } else {
      System.out.println("Page count of notes from mainframe: " + pageCount);
    }

    UpdateCustomerNote updateCustomerNote = updateCustomerNote(resultSet, customer_id);

    // System.out.println("NoteID from mainframe: " + note_id);
    // updateCustomerNote.setNumber(note_id);

    Status status = updateCustomerNote.send(connection);

    if (status.getErrorCode() == 0) {
      System.out.println("Notes updated successfully.");
    } else {
      System.out.println("Error updating notes: " + status.getErrorMessage());
    }

    return status;
  }

  private UpdateCustomerNote updateCustomerNote(ResultSet resultSet, String customerId) {
    UpdateCustomerNote updateCustomerNote = new UpdateCustomerNote();
    try {
      updateCustomerNote.setCustomerId(customerId);

      while (resultSet.next()) {
        int noteId = resultSet.getInt("noteId");
        String note = resultSet.getString("note");

        updateCustomerNote.setNumber(noteId);

        String[] lines = note.split("::");
        for (int i = 1; i < lines.length; i++) {
          updateCustomerNote.setLine(i, lines[i]);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return updateCustomerNote;
  }

  @Override
  protected String getSqlQuery() {
    return "SELECT * FROM customer_notes WHERE lastModified > ?";
  }
}
