package uoa.lavs.customer;

public class IndividualCustomerSingleton {
  private static IndividualCustomer instance;

  private IndividualCustomerSingleton() {}

  public static IndividualCustomer getInstance() {
    if (instance == null) {
      // Initialize with placeholder values
      instance =
          new IndividualCustomer("", "", "", null, "", "", "", null, null, null, null);
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
