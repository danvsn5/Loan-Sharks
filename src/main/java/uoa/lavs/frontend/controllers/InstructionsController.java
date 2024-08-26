package uoa.lavs.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.frontend.SceneManager.AppUI;

public class InstructionsController {

  @FXML private ImageView staticReturnImageView;

  @FXML private TextArea info;

  String text =
      "NOTE: This application was created with Java. Frameworks, packages and technologies used in"
          + " the application include:\n"
          + "JavaFX for the GUI\n"
          + "JUnit for unit testing in the business logic\n"
          + "SQLite for the local machine database instantiation\n"
          + "Regex data to aid with detail validation was taken from"
          + " https://stackoverflow.com/questions/50330109/simple-regex-pattern-for-email\n"
          + "The Graphical User Interface was created with the help of Canva, alongside any visual"
          + " image assets\n"
          + "The font, Quicksand, was retrieved from Google Fonts, as per"
          + " https://fonts.google.com/specimen/Quicksand. \n\n"
          + "Welcome to Lone Sharks LAVS. Here, you will see a list of instructions and some"
          + " information as to how the application is to be used. \n"
          + " \n"
          + " * Customers are created first, where their details are inputted into the system"
          + " and stored in the local database, to be synced to the mainframe \n"
          + " * Customers can be searched for by their name or ID, and their details can be"
          + " viewed and edited \n"
          + " * Loans will only be created once all customers associated with that loan have been"
          + " created; if any co-borrowers are part of the loan that is to be created, then their"
          + " ID will be dotted down by the MMM to be inputted in the loan creation screen \n"
          + " * Each loan has a primary borrower, and is capped at a total of three coborrowers \n"
          + " * If connection is lost while an MMM is creating a loan or customer, then the entity"
          + " will still be created and stored in their local database; this database will send the"
          + " required information to the mainframe once connectivity is restored. \n"
          + " Once connection is restored, you are able to sync"
          + " any new data from the local database to the mainframe with the usage of the sync"
          + " button on the main menu. \n"
          + " \n";

  @FXML
  private void initialize() {
    info.setText(text);
  }

  // returns to main menu
  @FXML
  private void handleBackButtonAction() {
    Main.setUi(AppUI.MAIN_MENU);
  }
}
