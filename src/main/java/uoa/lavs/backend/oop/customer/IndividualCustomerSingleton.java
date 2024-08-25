package uoa.lavs.backend.oop.customer;

import java.util.ArrayList;

public class IndividualCustomerSingleton {
  private static IndividualCustomer instance;

  private IndividualCustomerSingleton() {}

  public static IndividualCustomer getInstance() {
    if (instance == null) {
      // Initialize with placeholder values
      ArrayList<Note> notes = new ArrayList<>();
      Note note = new Note("", new String[19]);
      notes.add(note);

      ArrayList<Address> addresses = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        Address address = new Address("", "", "", "", "", "", "", "", false, false);
        addresses.add(address);
      }

      ArrayList<Phone> phones = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        Phone phone = new Phone("", "", "", "", false, false);
        phones.add(phone);
      }

      ArrayList<Email> emails = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        Email email = new Email("", "", false);
        emails.add(email);
      }

      CustomerEmployer employer =
          new CustomerEmployer("", "", "", "", "", "", "", "", "", "", "", false);

      instance =
          new IndividualCustomer(
              "", "", "", null, "", "", "", notes, addresses, phones, emails, employer);
    }
    return instance;
  }

  public static void setInstance(IndividualCustomer customer) {
    instance = customer;
  }

  public static void resetInstance() {
    instance = null;
  }

  public static void setInstanceCustomer(Customer selectedCustomer) {
    ArrayList<Note> notes = selectedCustomer.getNotes();
    ArrayList<Address> addresses = selectedCustomer.getAddresses();
    ArrayList<Phone> phones = selectedCustomer.getPhones();
    ArrayList<Email> emails = selectedCustomer.getEmails();
    CustomerEmployer employer = selectedCustomer.getEmployer();

    instance =
        new IndividualCustomer(
            selectedCustomer.getCustomerId(),
            selectedCustomer.getTitle(),
            selectedCustomer.getName(),
            selectedCustomer.getDateOfBirth(),
            selectedCustomer.getOccupation(),
            selectedCustomer.getVisa(),
            selectedCustomer.getCitizenship(),
            notes,
            addresses,
            phones,
            emails,
            employer);
  }
}
