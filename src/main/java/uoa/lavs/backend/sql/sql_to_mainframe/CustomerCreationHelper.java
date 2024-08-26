package uoa.lavs.backend.sql.sql_to_mainframe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import uoa.lavs.backend.oop.customer.Address;
import uoa.lavs.backend.oop.customer.CustomerEmployer;
import uoa.lavs.backend.oop.customer.Email;
import uoa.lavs.backend.oop.customer.IndividualCustomer;
import uoa.lavs.backend.oop.customer.Note;
import uoa.lavs.backend.oop.customer.Phone;
import uoa.lavs.backend.sql.oop_to_sql.customer.AddressDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.CustomerDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.CustomerEmployerDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.EmailDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.NotesDAO;
import uoa.lavs.backend.sql.oop_to_sql.customer.PhoneDAO;
import uoa.lavs.legacy.mainframe.Connection;

public class CustomerCreationHelper {

  // Validates the customer details
  public static boolean validateCustomer(IndividualCustomer customer) {
    ArrayList<Address> addresses = customer.getAddresses();
    ArrayList<Phone> phones = customer.getPhones();
    ArrayList<Email> emails = customer.getEmails();
    CustomerEmployer employer = customer.getEmployer();

    // CUSTOMER VALIDATION
    if (customer.getTitle().equals("")
        || customer.getName().equals("")
        || customer.getDateOfBirth() == null
        || customer.getOccupation().equals("")
        || customer.getOccupation().length() > 40
        || customer.getVisa().equals("")
        || customer.getCitizenship().equals("")) {
      return false;
    }

    for (int i = addresses.size(); i > 0; i--) {
      // cull empty addresses
      Address address = addresses.get(i - 1);
      if (address.getAddressType() == "" && address.getAddressLineOne() == "" && address.getAddressLineTwo() == ""
          && address.getSuburb() == "" && address.getPostCode() == "" && address.getCity() == ""
          && address.getCountry() == "" && address.getIsPrimary() == false && address.getIsMailing() == false) {
        addresses.remove(address);
      }
    }

    // ADDRESS VALIDATION
    if (addresses.size() == 0) {
      return false;
    } else {

      for (Address address : addresses) {
        if (address.getAddressType().equals("")
            || address.getAddressLineOne().equals("")
            || address.getAddressLineOne().length() > 60
            || (address.getAddressLineTwo().length() > 60 && !(address.getAddressLineTwo() == null))
            || address.getSuburb().equals("")
            || address.getSuburb().length() > 30
            || address.getPostCode().equals("")
            || address.getPostCode().length() > 10
            || !address.getPostCode().matches("[0-9]+")
            || address.getCity().equals("")
            || address.getCity().length() > 30
            || address.getCountry().equals("")) {
          return false;
        }

      }
    }

    // CHECK IF PRIMARY AND MAILING ADDRESS ARE SET IN ALL ADDRESSES
    boolean primaryAddressSet = false;
    boolean mailingAddressSet = false;
    for (Address address : addresses) {
      if (address.getIsPrimary()) {
        primaryAddressSet = true;
      }
      if (address.getIsMailing()) {
        mailingAddressSet = true;
      }
    }
    if (!primaryAddressSet || !mailingAddressSet) {
      return false;
    }

    // cull empty emails
    for (int i = emails.size(); i > 0; i--) {
      Email email = emails.get(i - 1);
      if (email.getEmailAddress() == "" && email.getIsPrimary() == false) {
        emails.remove(email);
      }
    }

    // EMAIL VALIDATION
    if (emails.size() == 0) {
      return false;
    } else {

      for (Email email : emails) {
        if (email.getEmailAddress().equals("")
            || email.getEmailAddress().length() > 60
            || !email.getEmailAddress().matches("^[^@]+@[^@]+\\.[^@]+$")) {
          return false;
        }
      }
    }

    boolean primaryEmailSet = false;
    for (Email email : emails) {
      if (email.getIsPrimary()) {
        primaryEmailSet = true;
        break;
      }
    }
    if (!primaryEmailSet) {
      return false;
    }

    // cull empty phones
    for (int i = phones.size(); i > 0; i--) {
      Phone phone = phones.get(i - 1);
      if (phone.getType() == "" && phone.getPrefix() == "" && phone.getPhoneNumber() == ""
          && phone.getIsPrimary() == false) {
        phones.remove(phone);
      }
    }

    // PHONE VALIDATION
    if (phones.size() == 0) {
      return false;
    } else {

      for (Phone phone : phones) {
        if (phone.getType().equals("")
            || phone.getPrefix().equals("")
            || phone.getPrefix().length() > 10
            || !phone.getPrefix().matches("[0-9\\+]+")
            || phone.getPhoneNumber().equals("")
            || phone.getPhoneNumber().length() > 20
            || !phone.getPhoneNumber().matches("[0-9\\-]+")) {
          return false;
        }
      }
    }

    // CHECK IF PRIMARY PHONE IS SET IN ALL PHONES
    boolean primaryPhoneSet = false;
    for (Phone phone : phones) {
      if (phone.getIsPrimary()) {
        primaryPhoneSet = true;
        break;
      }
    }
    if (!primaryPhoneSet) {
      return false;
    }

    // EMPLOYER VALIDATION
    if (employer.getEmployerName().equals("")
        || employer.getEmployerName().length() > 60
        || employer.getEmployerEmail().equals("")
        || employer.getEmployerEmail().length() > 60
        || !employer.getEmployerEmail().matches("^[^@]+@[^@]+\\.[^@]+$")
        || employer.getEmployerWebsite().equals("")
        || employer.getEmployerWebsite().length() > 60
        || !employer
            .getEmployerWebsite()
            .matches(
                "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")
        || employer.getEmployerPhone().equals("")
        || employer.getEmployerPhone().length() > 30
        || !employer.getEmployerPhone().matches("^\\+?[0-9\\-]{1,30}$")
        || employer.getLineOne().equals("")
        || employer.getLineOne().length() > 60
        || (employer.getLineTwo().length() > 60 && !(employer.getLineTwo() == null))
        || employer.getSuburb().equals("")
        || employer.getSuburb().length() > 30
        || employer.getCity().equals("")
        || employer.getCity().length() > 30
        || employer.getPostCode().equals("")
        || !employer.getPostCode().matches("[0-9]{1,10}")
        || employer.getCountry().equals("")) {
      return false;
    }

    return true;
  }

  public static void createCustomer(
      IndividualCustomer customer, boolean currentlyExists, Connection connection)
      throws IOException {

    CustomerDAO customerdao = new CustomerDAO();

    // if the customer does not currently exist, add the customer to the database,
    // otherwise update
    if (!currentlyExists) {
      customerdao.addCustomer(customer);
    } else {
      customerdao.updateCustomer(customer);
    }

    String customerId = customer.getCustomerId();

    // SETTING NOTES OF CUSTOMER
    NotesDAO notesdao = new NotesDAO();
    ArrayList<Note> notes = customer.getNotes();
    if (currentlyExists) {
      notesdao.deleteNotes(customerId);
    }

    for (int j = 0; j < notes.size(); j++) {
      Note note = notes.get(j);
      note.setCustomerId(customerId);
      note.setNoteId(j + 1);
      int nullCount = 0;
      for (int i = 0; i < 19; i++) {
        if (note.getLines()[i] == null || note.getLines()[i].equals("")) {
          nullCount++;
          note.getLines()[i] = "";
        }
      }
      if (nullCount == 19) {
        continue;
      }
      notesdao.addNote(note);
    }

    // SETTING ADDRESSES OF CUSTOMER
    AddressDAO addressdao = new AddressDAO();
    ArrayList<Address> addresses = customer.getAddresses();
    int numberOfDatabaseAddresses = addressdao.getAddresses(customerId).size();
    for (Address address : addresses) {
      if (address.getAddressType() == null
          || address.getAddressLineOne() == ""
          || address.getSuburb() == ""
          || address.getPostCode() == ""
          || address.getCity() == ""
          || address.getCountry() == "") {
        continue;
      }

      address.setCustomerId(customerId);
      if (!currentlyExists) {
        addressdao.addAddress(address);
      } else {

        // if the customer currently exists, but does not have an address with that ID,
        // then create an address instead of adding one
        if (address.getAddressId() > numberOfDatabaseAddresses) {
          addressdao.addAddress(address);
        } else {
          addressdao.updateAddress(address);
        }
      }
    }

    // SETTING PHONES OF CUSTOMER
    PhoneDAO phonedao = new PhoneDAO();
    ArrayList<Phone> phones = customer.getPhones();
    int numberOfDatabasePhones = phonedao.getPhones(customerId).size();
    for (Phone phone : phones) {
      if (phone.getType() == null || phone.getPrefix() == "" || phone.getPhoneNumber() == "") {
        continue;
      }
      // System.out.println("not skipping phone");
      phone.setCustomerId(customerId);
      if (!currentlyExists) {
        phonedao.addPhone(phone);
      } else {
        // if the customer currently exists, but does not have a phone with that ID,
        // then create a phone instead of adding one
        if (phone.getPhoneId() > numberOfDatabasePhones) {
          phonedao.addPhone(phone);
        } else {

          phonedao.updatePhone(phone);
        }
      }
    }

    // SETTING EMAILS OF CUSTOMER
    EmailDAO emaildao = new EmailDAO();
    ArrayList<Email> emails = customer.getEmails();
    int numberOfDatabaseEmails = emaildao.getEmails(customerId).size();
    for (Email email : emails) {
      if (email.getEmailAddress() == "") {
        continue;
      }

      email.setCustomerId(customerId);
      if (!currentlyExists) {
        emaildao.addEmail(email);
      } else {
        // if the customer currently exists, but does not have an email with that ID,
        // then create an email instead of adding one
        if (email.getEmailId() > numberOfDatabaseEmails) {
          emaildao.addEmail(email);
        } else
          emaildao.updateEmail(email);
      }
    }

    // SETTING EMPLOYER OF CUSTOMER
    CustomerEmployerDAO employerdao = new CustomerEmployerDAO();
    customer.getEmployer().setCustomerId(customerId);
    if (!currentlyExists) {
      employerdao.addCustomerEmployer(customer.getEmployer());
    } else {
      employerdao.updateCustomerEmployer(customer.getEmployer());
    }

    // Sync all records to mainframe
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

    SyncManager syncManager = new SyncManager(
        List.of(syncCustomer, syncAddress, syncEmployer, syncPhone, syncEmail, syncNotes));

    syncManager.syncAll(lastSyncTime, connection);
  }
}
