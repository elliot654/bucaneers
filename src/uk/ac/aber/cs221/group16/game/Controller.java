/*
* @(#) Controller.java 1.0 2017/04/01
*
* Copyright (c) 2017 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group16.game;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.aber.cs221.group16.game.windows.Choice;
import uk.ac.aber.cs221.group16.game.windows.Help;
import uk.ac.aber.cs221.group16.game.windows.StartScreen;

import java.util.ArrayList;

/**
 * This class starts the JavaFX UI and makes calls to start scenes.
 *
 * @author Josh Smith
 * @author Alex Toop
 * @author Kamyab Sherafat
 * @version 1.0 JavaFX First Implementation.
 */
public class Controller extends Application {

   // TODO: Create popup helper singleton which handles popups and keeps track of the stage. (Priority : LOW)
   public static Stage stage;

   @Override
   public void start(Stage primaryStage) throws Exception {
      stage = primaryStage;
      //stage.setMaximized(true);
      /*
      // if running on linux, this line is needed.
      if (System.getProperty("os.name").equals("Linux"))
         stage.setFullScreen(true);
      */

      // set the stage bounds to be fullscreen
      stage.setX(Screen.getPrimary().getVisualBounds().getMinX());
      stage.setY(Screen.getPrimary().getVisualBounds().getMinY());
      stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
      stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());

      stage.initStyle(StageStyle.UNDECORATED);
      stage.setResizable(false);
      stage.setTitle("Buccaneer! | press ESC to exit or F1 for help");

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

      ArrayList<String> pNames = null;
      try {
         // Shows the start screen to get user names
         pNames = new StartScreen().getNames();
      } catch (RuntimeException error) {
         System.err.print(error.toString());
      }
      // This then triggers the game scene to start once the form is submitted.
      if (pNames != null && pNames.size() == Game.numPlayers)
         newGame(pNames);
   }

   // Private Methods

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
    * Starts a new game and sets the scene to start it.
    *
    * @param names        The names of the players to be put into the game
    * @param primaryStage The primary stage of the JavaFX application
    */
   private void newGame(ArrayList<String> names, Stage primaryStage) {
      Game game = new Game(names, primaryStage);
      game.start();
   }

   /**
    * calls parent method with a default stage if only names are passed in
    *
    * @param names The names of the players to be put into the game
    */
   private void newGame(ArrayList<String> names) {
      newGame(names, stage);
   }

   // Static Methods

   public static void main(String args[]) {
      launch(args);
   }
}
