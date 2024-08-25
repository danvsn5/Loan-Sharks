package uoa.lavs.legacy.mainframe;

import uoa.lavs.legacy.mainframe.simulator.HttpConnection;

// implements the singleton pattern for a mainframe connection
public class Instance {
  // the URL to the remote server
  private static final String BASE_URL = "http://localhost:5000";

  // private constructor so that this class can only be initialized internally
  private Instance() {}

  // return the underlying connection
  public static Connection getConnection() {
    return SingletonHelper.INSTANCE;
  }

  // internal class to initialize the singleton, this enables lazy-loading
  // for the singleton
  private static class SingletonHelper {
    private static final Connection INSTANCE = new HttpConnection(BASE_URL);
  }
}
