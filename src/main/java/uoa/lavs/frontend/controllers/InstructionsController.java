package uoa.lavs.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import uoa.lavs.Main;
import uoa.lavs.frontend.SceneManager.AppUI;

public class InstructionsController {

    @FXML
    private ImageView staticReturnImageView;

    @FXML
    private TextArea info;

    String text = "NOTE: This application was created with Java. Frameworks, packages and technologies used in the application include:\nJavaFX for the GUI\nJUnit for unit testing in the business logic\nSQLite for the local machine database instantiation\nRegex data to aid with detail validation was taken from https://stackoverflow.com/questions/50330109/simple-regex-pattern-for-email\nThe Graphical User Interface was created with the help of Canva, alongside any visual image assets\nThe font, Quicksand, was retrieved from Google Fonts, as per https://fonts.google.com/specimen/Quicksand. \n\nWelcome to Lone Sharks LAVS. Here, you will see a list of instructions and some information as to how the application is to be used. \n * Loans will only be created once all customers associated with that loan have been created; if any co-borrowers are part of the loan that is to be created, then their ID will be dotted down by the MMM to be inputted in the loan creation screen \n * Each loan has a primary borrower, and is capped at a total of four borrowers \n * If connection is lost while an MMM is creating a loan or customer, then the entity will still be created and stored in their local database; this database will send the required information to the mainframe once connectivity is restored \n \n Input Field Information: \n Customer Details: \n Name: maximum of 60 characters across all fields \n Title: Mr, Mrs, Ms, Master \n Visa/Residency: NZ citizen, NZ permanent resident, AUS citizen, NZ work visa, other \n Date of Birth: dd-mm-yyyy \n Occupation: maximum 40 characters \n Citizenship: any country listed in available dropdown \n \n Addresses: ";

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
