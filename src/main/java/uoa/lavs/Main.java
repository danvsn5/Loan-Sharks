package uoa.lavs;

import java.io.IOException;
import javafx.application.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uoa.lavs.SceneManager.AppUI;
import uoa.lavs.mainframe.Connection;
import uoa.lavs.mainframe.Instance;
import uoa.lavs.mainframe.Status;
import uoa.lavs.mainframe.messages.customer.LoadCustomer;
import uoa.lavs.mainframe.simulator.RecorderConnection;
import uoa.lavs.mainframe.simulator.SimpleReplayConnection;

public class Main extends Application {
  public static Scene scene;

  public static void main(String[] args) {

    // the following shows two ways of using the mainframe interface
    // approach #1: use the singleton instance - this way is recommended as it provides a single
    // configuration
    // location (and is easy for the testers to change when needed).
    Connection connection = Instance.getConnection();
    executeTestMessage(connection);

    // approach #2: dynamically initialize the interface based on some parameters - this way allows
    // the connection
    // to change when needed (e.g., based on a command-line argument.) But it means that the
    // connection must be
    // passed around in the application.
    String dataPath = args.length > 1 ? args[1] : "lavs-data.txt";
    if (args.length > 0 && args[0].equals("record")) {
      connection = new RecorderConnection(dataPath);
    } else {
      connection = new SimpleReplayConnection(dataPath);
    }
    executeTestMessage(connection);

    // you can use another approach if desired, but make sure you document how the markers can
    // change the
    // connection implementation.
    launch();
  }

  private static void executeTestMessage(Connection connection) {
    LoadCustomer testMessage = new LoadCustomer();
    testMessage.setCustomerId("123456-789");
    Status status = testMessage.send(connection);
    try {
      connection.close();
    } catch (IOException e) {
      System.out.println(
          "Something went wrong - could not clos connection! The message is " + e.getMessage());
      return;
    }

    if (status.getWasSuccessful()) {
      System.out.println(
          "The send was successful: the customer name is " + testMessage.getNameFromServer());
    } else {
      System.out.println(
          "Something went wrong - the send failed! The code is " + status.getErrorCode());
    }
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  private static Parent loadFxml(final String fxml) throws IOException {
    String fxmlPath = "/fxml/" + fxml + ".fxml";
    System.out.println("Loading FXML from path: " + fxmlPath);
    return new FXMLLoader(Main.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  public static void setUi(AppUI newUi) {
    scene.setRoot(SceneManager.getScene(newUi));
  }

  @Override
  public void start(final Stage stage) throws IOException {
    SceneManager.addScene(AppUI.LOGIN, loadFxml("login"));
    System.out.println("Loading login scene");
    scene = new Scene(SceneManager.getScene(AppUI.LOGIN), 1000, 900);

    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(
        new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent event) {
            Platform.exit();
            System.exit(0);
          }
        });
  }
}
