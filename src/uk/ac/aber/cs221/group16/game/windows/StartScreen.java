/*
* @(#) StartScreen.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/

package uk.ac.aber.cs221.group16.game.windows;

import javafx.geometry.HPos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.aber.cs221.group16.game.GraphicLoader;

import java.util.ArrayList;

/**
 * This class handles the UI of the starting screen, takes user input for player names
 * checks for validity and holds an arraylist of those names accessible via
 * {@link #getNames()}
 *
 * @author Kamyab Sherafat
 */

public class StartScreen {
   private GridPane wrapper;
   private ArrayList<String> names;
   Stage stage;

   /**
    * Default constructor which sets the stage to a decorated stage. Also opens
    * the window.
    */
   public StartScreen() {
      stage = new Stage(StageStyle.DECORATED);
      names = new ArrayList<>();
      display();
   }

   /**
    * Gets the valid names from the Start Screen after the ok button has been
    * pressed.
    *
    * @return The list of accepted player names after the player has pressed the
    * ok button
    */
   public ArrayList<String> getNames() {
      return names;
   }

   /**
    * Displays the start screen window
    */
   private void display() {

      Button help = new Button("Help | F1");
      help.setOnAction(e -> {
         new Help(stage);
      });

      Label title = new Label("Buccaneer Board Game");
      title.setFont(Font.font("Arial", 24));

      Label pleaseEnterName = new Label("Please enter your names: ");

      TextField player1 = new TextField("Player 1");
      TextField player2 = new TextField("Player 2");
      TextField player3 = new TextField("Player 3");
      TextField player4 = new TextField("Player 4");

      Button submit = new Button("OK");
      /*
       * iteratively goes through all children of the parent, from those that are TextFields
       * selects the text inside and adds to the names ArrayList
       */
      submit.setOnAction(e -> {
         for (Node node : wrapper.getChildren()) {
            if (node instanceof TextField) {
               if (nameValidator(node)) {
                  names.add(((TextField) node).getText());
               }
            }
         }
         // Names are only added to the names array if they pass the checks,
         // if not all of the names pass you cannot continue.
         if (names.size() < 4) {
            new GenericMsg(stage, "Not all of the names entered were " +
                    "valid.\nNames must be less than 15 charactors in " +
                    "length.\nPlease enter your names:");
            // clear the array for the next check.
            names.clear();
         } else {
            stage.close();
         }
      });

      wrapper = new GridPane();
      //wrapper.setGridLinesVisible(true); // for debugging
      GridPane.setHalignment(help, HPos.RIGHT);
      GridPane.setHalignment(submit, HPos.CENTER);
      wrapper.getChildren().addAll(help, title, pleaseEnterName,
              player1, player2, player3, player4, submit);
      wrapper.setConstraints(help, 2, 0);
      wrapper.setConstraints(title, 1, 1);
      wrapper.setConstraints(pleaseEnterName, 0, 2);
      wrapper.setConstraints(player1, 0, 3);
      wrapper.setConstraints(player2, 2, 3);
      wrapper.setConstraints(player3, 0, 4);
      wrapper.setConstraints(player4, 2, 4);
      wrapper.setConstraints(submit, 1, 5);
      wrapper.setPrefSize(wrapper.USE_COMPUTED_SIZE, wrapper.USE_COMPUTED_SIZE);
      wrapper.getStylesheets().addAll("/css/generic_button.css", "/css/start_screen.css");


      // cursor event to buttons
      for (Node node : wrapper.getChildren()) {
         if (node instanceof Button) {
            node.setOnMouseEntered(e -> {
               changeCursor((Button) node, GraphicLoader.getInstance().getFocusCursor());
            });
            node.setOnMouseExited(e -> {
               changeCursor((Button) node, GraphicLoader.getInstance().getDefaultCursor());
            });
         }
      }

      // HANDLING KEYBOARD INPUT
      stage.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
         if (KeyCode.ESCAPE == event.getCode()) {
            displayExitPopup(stage);
         }
         if (KeyCode.F1 == event.getCode()) {
            new Help(stage);
         }
      });

      // on press of x in top corner.
      stage.setOnCloseRequest((event) -> {
         displayExitPopup(stage);

         // consume event so it doesn't propagate and close
         event.consume();
      });

//      stage.setScene(new Scene(wrapper, Screen.getPrimary().getVisualBounds().getWidth() * 0.4,
//              Screen.getPrimary().getVisualBounds().getHeight() * 0.6));
      Scene scene = new Scene(wrapper);
      scene.setCursor(new ImageCursor(GraphicLoader.getInstance().getDefaultCursor()));
      stage.setScene(scene);
      stage.setResizable(false);
      player1.positionCaret(player1.getText().length());
      submit.setDefaultButton(true);
      stage.showAndWait();
   }

   /**
    * Displays an exit popup and if yes is pressed then it closes the stage.
    *
    * @param stage The stage instance.
    */
   private void displayExitPopup(Stage stage) {
      if (new Choice(stage, "Would you like to exit?", new String[]{"No", "Yes"}).getAnswer() == false) {
         stage.close();
      }
   }

   /**
    * changes the cursor of a button
    *
    * @param button to change the cursor for
    * @param image  the image to change the cursor to
    */
   private void changeCursor(Button button, Image image) {
      button.setCursor(new ImageCursor(image));
   }

   /**
    * Checks that the names entered are valid to the rules set.
    *
    * @param node This is the entry for a particular player
    * @return true if they pass the checks, false otherwise
    */
   private boolean nameValidator(Node node) {
      boolean failedChecks = false;

      if (((TextField) node).getText().length() <= 0) {
         failedChecks = true;
      }

      if (((TextField) node).getText().equals(" ")) {
         failedChecks = true;
      }

      if (((TextField) node).getText().length() > 15) {
         failedChecks = true;
      }

      if (failedChecks) {
         return false;
      } else {
         return true;
      }
   }
}
