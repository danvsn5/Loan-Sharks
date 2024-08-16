package uoa.lavs.customer;

public class IndividualCustomerSingleton {
  private static IndividualCustomer instance;

  private IndividualCustomerSingleton() {}

  public static IndividualCustomer getInstance() {
    if (instance == null) {
      // Initialize with placeholder values
      Address physicalAddress = new Address("", "", "", "", "", "", "");
      Address mailingAddress = new Address("", "", "", "", "", "", "");
      Address employerAddress = new Address("", "", "", "", "", "", "");

      Phone phoneOne = new Phone("", "");
      Phone phoneTwo = new Phone("", "");
      CustomerContact contact = new CustomerContact("", phoneOne, phoneTwo, "", "");
      CustomerEmployer employer = new CustomerEmployer("", employerAddress, "", "", "", false);

      instance =
          new IndividualCustomer(
              "",
              "",
              "",
              null,
              "",
              "",
              "",
              physicalAddress,
              mailingAddress,
              contact,
              employer);
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
