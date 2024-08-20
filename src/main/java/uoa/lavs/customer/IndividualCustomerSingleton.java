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

      ArrayList<Address> addresses = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        Address address = new Address("", "", "", "", "", "", "", "", false, false);
        addresses.add(address);
      }

      Phone phoneOne = new Phone("", "");
      Phone phoneTwo = new Phone("", "");
      CustomerContact contact = new CustomerContact("", phoneOne, phoneTwo, "", "");
      CustomerEmployer employer =
          new CustomerEmployer("", "", "", "", "", "", "", "", "", "", false);

      instance =
          new IndividualCustomer("", "", "", null, "", "", notes, addresses, contact, employer);
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
