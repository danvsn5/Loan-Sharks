package uoa.lavs;

import java.io.IOException;
import javafx.application.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uoa.lavs.backend.sql.InitialiseDatabase;
import uoa.lavs.frontend.AppState;
import uoa.lavs.frontend.SceneManager;
import uoa.lavs.frontend.SceneManager.AppUI;
import uoa.lavs.legacy.mainframe.Connection;
import uoa.lavs.legacy.mainframe.Status;
import uoa.lavs.legacy.mainframe.messages.customer.LoadCustomer;

public class Main extends Application {
  public static Scene scene;

  public static void main(String[] args) {
    // the following shows two ways of using the mainframe interface
    // approach #1: use the singleton instance - this way is recommended as it provides a single
    // configuration
    // location (and is easy for the testers to change when needed).

    // approach #2: dynamically initialize the interface based on some parameters - this way allows
    // the connection
    // to change when needed (e.g., based on a command-line argument.) But it means that the
    // connection must be
    // passed around in the application.

    // Below is code that would have connected to the database.
    /*   String dataPath = args.length > 1 ? args[1] : "lavs-data.txt";
        if (args.length > 0 && args[0].equals("record")) {
          connection = new RecorderConnection(dataPath);
        } else {
          connection = new SimpleReplayConnection(dataPath);
        }
        executeTestMessage(connection);
    */
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
    // String fxmlPath = "/fxml/" + fxml + ".fxml";
    // System.out.println("Loading FXML from path: " + fxmlPath);
    return new FXMLLoader(Main.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  public static void setUi(AppUI newUi) {
    scene.setRoot(SceneManager.getScene(newUi));
  }

  @Override
  public void start(final Stage stage) throws IOException {
    // initialises database
    InitialiseDatabase.createDatabase();

    // activates antialiasing for text in application !IMPORTANT
    System.setProperty("prism.lcdtext", "false");

    // Add all Admin screens
    SceneManager.addScene(AppUI.MAIN_MENU, loadFxml("admin/welcome"));
    SceneManager.addScene(AppUI.LOGIN, loadFxml("admin/login"));
    SceneManager.addScene(AppUI.INFORMATION, loadFxml("admin/instructions"));

    // Add all static customer scenes
    SceneManager.addScene(AppUI.CUSTOMER_MENU, loadFxml("customer/customer_management"));

    SceneManager.addScene(AppUI.CUSTOMER_SEARCH, loadFxml("customer/customer_search"));

    // Add all static loan screens
    SceneManager.addScene(AppUI.LOAN_MENU, loadFxml("loan/loan_management"));

    // BYPASSING TO CUSTOMER MENU
    scene = new Scene(SceneManager.getScene(AppUI.MAIN_MENU), 1280, 720);
    // imports main index.css file
    String cssPath = getClass().getResource("/css/index.css").toExternalForm();
    scene.getStylesheets().add(cssPath);
    // imports fonts that are implemented through css
    Font.loadFont(getClass().getResourceAsStream("/font/Quicksand-Regular.ttf"), 14);
    Font.loadFont(getClass().getResourceAsStream("/font/Quicksand-Bold.ttf"), 14);
    Font.loadFont(getClass().getResourceAsStream("/font/Quicksand-Medium.ttf"), 14);

    stage.setScene(scene);
    stage.show();
    AppState.setCurrentUiName(AppUI.LOGIN);
    System.out.println(AppState.getCurrentUiName());
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
