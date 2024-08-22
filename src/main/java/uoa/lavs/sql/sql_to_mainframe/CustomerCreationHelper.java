package uoa.lavs.sql.sql_to_mainframe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import uoa.lavs.customer.Address;
import uoa.lavs.customer.Email;
import uoa.lavs.customer.IndividualCustomer;
import uoa.lavs.customer.Note;
import uoa.lavs.customer.Phone;
import uoa.lavs.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.sql.oop_to_sql.customer.CustomerEmployerDAO;
import uoa.lavs.sql.oop_to_sql.customer.EmailDAO;
import uoa.lavs.sql.oop_to_sql.customer.NotesDAO;
import uoa.lavs.sql.oop_to_sql.customer.PhoneDAO;

public class CustomerCreationHelper {
  public static void createCustomer(IndividualCustomer customer) throws IOException {
    CustomerDAO customerdao = new CustomerDAO();
    customerdao.addCustomer(customer);

    NotesDAO notesdao = new NotesDAO();
    ArrayList<Note> notes = customer.getNotes();
    for (Note note : notes) {
      note.setCustomerId(customer.getCustomerId());
      notesdao.addNote(note);
    }

    AddressDAO addressdao = new AddressDAO();
    ArrayList<Address> addresses = customer.getAddresses();
    for (Address address : addresses) {
      if (address.getAddressType() == null
          || address.getAddressLineOne() == ""
          || address.getSuburb() == ""
          || address.getPostCode() == ""
          || address.getCity() == ""
          || address.getCountry() == "") {
        continue;
      }

      address.setCustomerId(customer.getCustomerId());
      addressdao.addAddress(address);
    }

    PhoneDAO phonedao = new PhoneDAO();
    ArrayList<Phone> phones = customer.getPhones();
    for (Phone phone : phones) {
      if (phone.getType() == null || phone.getPrefix() == "" || phone.getPhoneNumber() == "") {
        continue;
      }
      // System.out.println("not skipping phone");
      phone.setCustomerId(customer.getCustomerId());
      phonedao.addPhone(phone);
    }

    EmailDAO emaildao = new EmailDAO();
    ArrayList<Email> emails = customer.getEmails();
    for (Email email : emails) {
      if (email.getEmailAddress() == "") {
        continue;
      }

      email.setCustomerId(customer.getCustomerId());
      emaildao.addEmail(email);
    }

    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();
    customer.getEmployer().setCustomerId(customer.getCustomerId());
    employerdao.addCustomerEmployer(customer.getEmployer());

    SyncCustomer syncCustomer = new SyncCustomer();
    SyncAddress syncAddress = new SyncAddress();
    SyncEmployer syncEmployer = new SyncEmployer();
    SyncNotes syncNotes = new SyncNotes();
    SyncPhone syncPhone = new SyncPhone();
    SyncEmail syncEmail = new SyncEmail();
    LocalDateTime lastSyncTime = syncCustomer.getLastSyncTimeFromDB();

    if (lastSyncTime == null) {
      System.out.println("No last sync time found. Syncing all records.");
      lastSyncTime = LocalDateTime.now(ZoneOffset.UTC).minusDays(1);
    }

    SyncManager syncManager =
        new SyncManager(
            List.of(syncCustomer, syncAddress, syncEmployer, syncPhone, syncEmail, syncNotes));

    syncManager.syncAll(lastSyncTime);
  }
}
