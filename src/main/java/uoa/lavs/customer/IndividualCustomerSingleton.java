package uoa.lavs.customer;

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

      Address physicalAddress = new Address("", "", "", "", "", "", "", "", true, false);
      Address mailingAddress = new Address("", "", "", "", "", "", "", "", false, true);

      Phone phoneOne = new Phone("", "");
      Phone phoneTwo = new Phone("", "");
      CustomerContact contact = new CustomerContact("", phoneOne, phoneTwo, "", "");
      CustomerEmployer employer =
          new CustomerEmployer("", "", "", "", "", "", "", "", "", "", false);

      instance =
          new IndividualCustomer(
              "", "", "", null, "", "", notes, physicalAddress, mailingAddress, contact, employer);
    }
    return instance;
  }

  public static void setInstance(IndividualCustomer customer) {
    instance = customer;
  }

  public static void resetInstance() {
    instance = null;
  }
}
